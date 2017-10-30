package uk.co.jbrunton.droneforecast.models

/**
 * Created by jamie on 30/10/2017.
 */
class ForecastItemResponse (
       val time: Long,
       val summary: String,
       val icon: String,
       val nearestStormDistance: Int,
       val nearestStormBearing: Int,
       val precipIntensity: Float,
       val precipProbability: Float,
       val temperature: Float,
       val apparentTemperature: Float,
       val dewPoint: Float,
       val humidity: Float,
       val pressure: Float,
       val windSpeed: Float,
       val windGust: Float,
       val windBearing: Int,
       val cloudCover: Float,
       val uvIndex: Int,
       val visibility: Float,
       val ozone: Float
)