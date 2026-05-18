package com.example.camscannerallinone.util

import android.app.Activity
import android.content.Intent
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult

class DocumentScanner(private val activity: Activity) {

    private val options = GmsDocumentScannerOptions.Builder()
        .setGalleryImportAllowed(true)
        .setPageLimit(20)
        .setResultFormats(RESULT_FORMAT_JPEG, RESULT_FORMAT_PDF)
        .setScannerMode(SCANNER_MODE_FULL)
        .build()

    private val scanner = GmsDocumentScanning.getClient(options)

    fun startScan(onResult: (GmsDocumentScanningResult) -> Unit, onError: (Exception) -> Unit) {
        scanner.getStartScanIntent(activity)
            .addOnSuccessListener { intentSender ->
                activity.startIntentSenderForResult(
                    intentSender,
                    SCAN_REQUEST_CODE,
                    null,
                    0,
                    0,
                    0
                )
            }
            .addOnFailureListener {
                onError(it)
            }
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?): GmsDocumentScanningResult? {
        if (requestCode == SCAN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            return GmsDocumentScanningResult.fromActivityResultIntent(data)
        }
        return null
    }

    companion object {
        const val SCAN_REQUEST_CODE = 1001
    }
}
