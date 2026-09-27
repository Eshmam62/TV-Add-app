package com.example.smarttvadplayer.media.player

import android.content.Context
import android.net.Uri
import com.example.smarttvadplayer.R

/**
 * Provides access to the bundled company advertisement video.
 *
 * The company advertisement is bundled as res/raw/company_intro.mp4.
 * To replace the advertisement:
 *   1. Replace app/src/main/res/raw/company_intro.mp4 with the new video.
 *   2. Keep the filename as company_intro.mp4.
 *   3. Rebuild the APK.
 *   4. No backend/API/database changes needed.
 */
object CompanyAdProvider {

    /**
     * Get the URI for the bundled company advertisement video.
     * Uses android.resource:// scheme which works on all API levels.
     */
    fun getCompanyAdUri(context: Context): Uri {
        return Uri.parse("android.resource://${context.packageName}/${R.raw.company_intro}")
    }

    /**
     * Check if the company advertisement resource exists.
     */
    fun isCompanyAdAvailable(context: Context): Boolean {
        return try {
            context.resources.openRawResourceFd(R.raw.company_intro)?.close()
            true
        } catch (e: Exception) {
            false
        }
    }
}
