package uk.co.jbrunton.droneforecast.extensions

/**
 * Created by jamie on 09/11/2017.
 */
fun Int.toHeadingString() : String {
    var directions = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
        var index = Math.round( ( this.toDouble()%360 ) / 45) %8
    return directions[index.toInt()]
}