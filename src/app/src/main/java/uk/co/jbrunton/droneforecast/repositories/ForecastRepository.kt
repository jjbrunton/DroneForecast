package uk.co.jbrunton.droneforecast.repositories

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import uk.co.jbrunton.droneforecast.models.ForecastResponse

/**
 * Created by jamie on 30/10/2017.
 */
interface ForecastRepository {
    @GET("forecast/{key}/{lat},{lon}")
    fun getForecastForLocation(@Path("key") key: String, @Path("lat") lat: Float,
               @Path("lon") lon: Float)
            : Call<ForecastResponse>
}