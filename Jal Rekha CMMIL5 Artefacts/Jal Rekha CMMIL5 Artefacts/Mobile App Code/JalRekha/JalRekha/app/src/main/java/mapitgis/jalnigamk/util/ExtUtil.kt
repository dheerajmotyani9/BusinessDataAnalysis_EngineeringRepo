package mapitgis.jalnigamk.util

import com.google.gson.JsonObject

fun JsonObject.getSafeDouble(key: String, default: Double = 0.0): Double {
    return if (has(key) && !get(key).isJsonNull) get(key).asDouble else default
}