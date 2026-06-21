package mapitgis.jalnigamk.nirmal.collection


import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    companion object {
        private const val PREF_NAME = "user_session"
        private const val KEY_NAME = "key_name"
        private const val KEY_SCHEME = "key_scheme"
        private const val KEY_SCHEME_ID = "key_scheme_id"
        private const val KEY_PIU = "key_piu"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveNirmalContractorDetail(name: String, scheme: String,schemeId: String, piu: String) {
        prefs.edit().apply {
            putString(KEY_NAME, name)
            putString(KEY_SCHEME, scheme)
            putString(KEY_PIU, piu)
            putString(KEY_SCHEME_ID, schemeId)
            apply()
        }
    }

    fun getName(): String? = prefs.getString(KEY_NAME, null)

    fun getScheme(): String? = prefs.getString(KEY_SCHEME, null)

    fun getPIU(): String? = prefs.getString(KEY_PIU, null)

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
