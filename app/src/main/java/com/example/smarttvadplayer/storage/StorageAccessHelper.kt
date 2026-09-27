package com.example.smarttvadplayer.storage

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Utility for Android Storage Access Framework (SAF) operations.
 *
 * Uses ACTION_OPEN_DOCUMENT for file picking which works on API 19+.
 * Handles URI permission persistence for continued access after reboot.
 */
object StorageAccessHelper {

    /**
     * Create an intent to pick a video file via SAF.
     */
    fun createVideoPickerIntent(): Intent {
        return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "video/*"
            // Allow persistent URI permission
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        }
    }

    /**
     * Create an intent to pick an image file via SAF.
     */
    fun createImagePickerIntent(): Intent {
        return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        }
    }

    /**
     * Persist a URI permission so the app can access the file after reboot.
     * This is critical for SAF URIs — without persistence, access is lost.
     */
    fun persistUriPermission(context: Context, uri: Uri) {
        try {
            val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            context.contentResolver.takePersistableUriPermission(uri, takeFlags)
        } catch (e: SecurityException) {
            // Permission may not be persistable — this is acceptable,
            // user will need to re-select the file if access is lost.
        }
    }

    /**
     * Check if a content URI is still accessible.
     */
    fun isUriAccessible(context: Context, uriString: String?): Boolean {
        if (uriString.isNullOrBlank()) return false
        return try {
            val uri = Uri.parse(uriString)
            val resolver: ContentResolver = context.contentResolver
            resolver.openInputStream(uri)?.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Release a previously persisted URI permission.
     */
    fun releaseUriPermission(context: Context, uri: Uri) {
        try {
            val releaseFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            context.contentResolver.releasePersistableUriPermission(uri, releaseFlags)
        } catch (e: Exception) {
            // Ignore — permission may already have been released
        }
    }
}
