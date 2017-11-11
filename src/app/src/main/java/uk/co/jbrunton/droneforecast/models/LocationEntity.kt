package uk.co.jbrunton.droneforecast.models

import android.location.Location
import io.realm.RealmObject

/**
 * Created by jjbrunton on 10/11/2017.
 */
open class LocationEntity : RealmObject() {
    var name: String? = null
}