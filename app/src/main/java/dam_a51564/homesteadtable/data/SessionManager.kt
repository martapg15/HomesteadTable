package dam_a51564.homesteadtable.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Utility object that handles local persistent preferences for session configurations,
 * such as remembering user login choices.
 */
object SessionManager {
    private const val PREFS_NAME = "homestead_prefs"
    private const val KEY_REMEMBER_ME = "remember_me"
    private lateinit var prefs: SharedPreferences

    /**
     * Initializes the [SharedPreferences] instance using the application context.
     * Must be invoked before any session properties are accessed or modified.
     *
     * @param context The Android context used to retrieve shared preferences.
     */
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Sets whether the user chose to remain logged into the application.
     *
     * @param value True to enable "Remember Me", false to disable.
     */
    fun setRememberMe(value: Boolean) {
        prefs.edit { putBoolean(KEY_REMEMBER_ME, value) }
    }

    /**
     * Checks if the "Remember Me" flag is locally saved as true.
     *
     * @return True if "Remember Me" is enabled, false otherwise.
     */
    fun isRememberMe(): Boolean {
        return prefs.getBoolean(KEY_REMEMBER_ME, false)
    }
}