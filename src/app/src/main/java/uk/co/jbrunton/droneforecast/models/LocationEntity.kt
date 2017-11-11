package uk.co.jbrunton.droneforecast.models

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import io.realm.RealmObject
import java.util.UUID.randomUUID
import io.realm.annotations.PrimaryKey
import java.util.*


/**
 * Created by jjbrunton on 10/11/2017.
 */
open class LocationEntity : RealmObject() {
    @PrimaryKey
    var locationId: String? = UUID.randomUUID().toString()
    var name: String? = null
    var lat: Double? = null
    var lng: Double? = null
    var createdAt: Date = Date()
}