package dam_a51564.homesteadtable.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SessionManager {
    private const val PREFS_NAME = "homestead_prefs"
    private const val KEY_REMEMBER_ME = "remember_me"
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setRememberMe(value: Boolean) {
        prefs.edit { putBoolean(KEY_REMEMBER_ME, value) }
    }

    fun isRememberMe(): Boolean {
        return prefs.getBoolean(KEY_REMEMBER_ME, false)
    }
}