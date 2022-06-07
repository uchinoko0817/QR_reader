package com.example.qrreader

import android.Manifest
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import java.lang.Exception
import java.time.ZonedDateTime

class MainActivity : AppCompatActivity() {

    // Constants
    companion object{
        const val PERMISSION_CODE_CAMERA = 0
    }

    // DB service
    private val historyService = HistoryService(this)

    // Flag prevent scan repetition
    private var isBusy = false

    // Use original menu layout
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Switch screen by selected option
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.history -> {
                // Transition to history screen
                val intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.about_app -> {
                // Transition to AboutApp screen
                val intent = Intent(this, AboutAppActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check camera permission
        if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_CODE_CAMERA)
        }

        // Barcode reader settings
        val formats = listOf(BarcodeFormat.QR_CODE)
        val barcodeView :DecoratedBarcodeView = findViewById(R.id.barcodeView)
        barcodeView.barcodeView.decoderFactory = DefaultDecoderFactory(formats)
        barcodeView.decodeContinuous(barcodeCallback)
    }

    // Show dialog to notify permission matter
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE_CAMERA ->
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Please allow use of camera")
                        .setPositiveButton("OK", null)
                        .show()
                }
        }
    }

    // Callback when barcode scanned
    private val barcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult?) {
            // Validation
            if (isBusy || result?.text == null) {
                return
            }
            // Enable busy flg
            isBusy = true
            // Write DB
            try{
                historyService.saveData(result.text, ZonedDateTime.now())
            }
            catch(e :Exception){
                Log.e("saveData", e.toString())
            }
            // Show Dialog
            showResultDialog(result.text)
        }
    }

    // Show dialog to chose action after scan
    private fun showResultDialog(data :String) {
        var isSuccess = true
        AlertDialog.Builder(this)
            .setTitle("Scanned!")
            .setMessage(data)
            .setPositiveButton("Launch"){_, _ ->
                try{
                    // Publish intent
                    val uri = Uri.parse(data)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
                catch(e :Exception){
                    isSuccess = false
                }
            }
            .setNegativeButton("Copy"){ _, _ ->
                // copy to clipboard
                val item = ClipData.Item(data)
                val mimeType = arrayOf(ClipDescription.MIMETYPE_TEXT_URILIST)
                val cd = ClipData(ClipDescription("scanned_data", mimeType),item)
                val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                clipboardManager.setPrimaryClip(cd)
            }
            .setOnDismissListener {
                if (!isSuccess) {
                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Launch Failed")
                        .setPositiveButton("OK", null)
                        .setOnDismissListener {
                            isBusy = false
                        }
                        .show()
                } else {
                    isBusy = false
                }
            }
            .show()
    }

    // Utility to find barcode view
    private fun findBarcodeView() : DecoratedBarcodeView {
        return findViewById(R.id.barcodeView)
    }

    override fun onDestroy() {
        historyService.close()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        val barcodeView = findBarcodeView()
        barcodeView.resume()
    }

    override fun onPause() {
        super.onPause()
        val barcodeView = findBarcodeView()
        barcodeView.pause()
    }
}