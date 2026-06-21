package mapitgis.jalnigamk

import android.content.Context
import androidx.core.content.edit

enum class UserType(val displayName: String) {
    CITIZEN("Citizen"),
    DEPARTMENT("Department");

    override fun toString(): String = displayName

    companion object {
        fun fromDisplayName(name: String?): UserType? {
            if (name.isNullOrBlank()) return null
            return UserType.entries.find { it.displayName.equals(name, ignoreCase = true) }
        }
    }
}



object UserTypeManager {
    private const val PREF_NAME = "app_prefs"
    private const val KEY_USER_TYPE = "user_type"

    fun saveUserType(context: Context, userType: UserType) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putString(KEY_USER_TYPE, userType.name) // store enum name, not displayName
        }
    }

    fun getUserType(context: Context): UserType? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val name = prefs.getString(KEY_USER_TYPE, null)
        return name?.let { runCatching { UserType.valueOf(it) }.getOrNull() }
    }

    fun clearUserType(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit { remove(KEY_USER_TYPE) }
    }
}
