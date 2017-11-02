package uk.co.jbrunton.droneforecast.services

import android.content.Context
import javax.inject.Singleton
import android.preference.PreferenceManager
import uk.co.jbrunton.droneforecast.R
import javax.measure.quantity.Length
import javax.measure.quantity.Temperature
import javax.measure.quantity.Velocity
import javax.measure.unit.NonSI
import javax.measure.unit.SI


/**
 * Created by jamie on 31/10/2017.
 */
@Singleton
class SettingsService(private val context: Context) {
    val settings = PreferenceManager.getDefaultSharedPreferences(this.context)

    fun getString(key: String): String {
        return settings.getString(key, "")
    }

    fun getStringValue(resId: Int): String {
        return this.context.getString(resId)
    }

    fun getWindUnit(): javax.measure.unit.Unit<Velocity> {
        var unit = this.getString("wind_speed_unit")
        return when (unit) {
            "mph" -> NonSI.MILES_PER_HOUR
            "kmh" -> NonSI.KILOMETERS_PER_HOUR
            "knots" -> NonSI.KNOT
            "ms" -> SI.METRES_PER_SECOND
            else -> NonSI.MILES_PER_HOUR
        }
    }

    fun getTemperatureUnit(): javax.measure.unit.Unit<Temperature> {
        var unit = this.getString("temperature_unit")
        return when (unit) {
            "c" -> SI.CELSIUS
            "f" -> NonSI.FAHRENHEIT
            else -> SI.CELSIUS
        }
    }

    fun getDistanceUnit(): javax.measure.unit.Unit<Length> {
        var unit = this.getString("distance_unit")
        return when (unit) {
            "kilometer" -> SI.KILOMETER
            "miles" -> NonSI.MILE
            else -> NonSI.MILE
        }
    }

    fun saveWidgets(widgets: List<String>) {
        this.settings.edit().putString("widgets", widgets.joinToString()).commit()
    }

    fun getWidgets(): List<String> {
        var keys = this.settings.getString("widgets", "")
        return if (keys != "") {
            keys.split(",").map { it.trim() }
        } else {
            ArrayList()
        }

    }

    fun getMinTemperature(): Int {
        return this.settings.getString("temperature_min_threshold", this.getStringValue(R.string.default_min_temperature)).toInt()
    }

    fun getMaxTemperature(): Int {
        return this.settings.getString("temperature_max_threshold", this.getStringValue(R.string.default_max_temperature)).toInt()
    }

    fun getMaxWindSpeed(): Int {
        return this.settings.getString("wind_speed_max_threshold", this.getStringValue(R.string.default_max_wind_speed)).toInt()
    }

    fun getMinVisibility(): Int {
        return this.settings.getString("visibility_min_threshold", this.getStringValue(R.string.default_min_visibility)).toInt()
    }
}