package uk.co.jbrunton.droneforecast.models

/**
 * Created by jamie on 30/10/2017.
 */
class ForecastResponse (
    val latitude: Float,
    val longitude: Float,
    val currently: ForecastItemResponse
)