package uk.co.jbrunton.droneforecast.repositories

import com.google.android.gms.maps.model.LatLng
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.realm.Realm
import uk.co.jbrunton.droneforecast.models.LocationEntity
import java.util.*

/**
 * Created by jamie on 11/11/2017.
 */
class LocationRepository(private val realm: Realm) {
    private val locationSubject: BehaviorSubject<List<LocationEntity>> = BehaviorSubject.create()
    val locations: Observable<List<LocationEntity>>
        get() = this.locationSubject

    init {
        var elements = this.realm.where(LocationEntity::class.java).findAll()

        this.locationSubject.onNext(elements)
    }

    fun getLocationByLatLng(lat:Double, lng: Double): LocationEntity? {
        return this.realm.where(LocationEntity::class.java).equalTo("lat", lat).equalTo("lng", lng).findFirst()
    }

    fun getLocationById(locationId: String): LocationEntity? {
        return this.realm.where(LocationEntity::class.java).equalTo("locationId", locationId).findFirst()
    }

    fun saveLocation(lat:Double, lng: Double, name: String) {
        this.realm.executeTransaction {
            var location = this.realm.createObject(LocationEntity::class.java, UUID.randomUUID().toString())
            location.name = name
            location.lat = lat
            location.lng = lng
        }

        var elements = this.realm.where(LocationEntity::class.java).findAll()

        this.locationSubject.onNext(elements)
    }

    fun removeLocation(lat: Double, lng: Double) {
        this.realm.executeTransaction {
            var location = this.realm.where(LocationEntity::class.java).equalTo("lat", lat).equalTo("lng", lng).findAll()
            if (location != null) {
                location.deleteAllFromRealm()
                var elements = this.realm.where(LocationEntity::class.java).findAll()

                this.locationSubject.onNext(elements)
            }
        }
    }
}