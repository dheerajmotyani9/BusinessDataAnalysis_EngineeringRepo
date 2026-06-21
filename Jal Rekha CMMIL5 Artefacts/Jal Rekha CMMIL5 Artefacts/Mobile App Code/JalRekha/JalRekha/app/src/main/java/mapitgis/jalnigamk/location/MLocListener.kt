package mapitgis.jalnigamk.location

import android.location.Location

/**
 * Callback that can be implemented in order to listen for events
 */
interface MLocListener {
    fun locationOn()
    fun currentLocation(location: Location)
    fun locationCancelled()
}