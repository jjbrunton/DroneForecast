package uk.co.jbrunton.droneforecast.application

import android.app.Application
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.squareup.leakcanary.LeakCanary
import io.realm.Realm
import javax.inject.Inject
import io.realm.Realm.setDefaultConfiguration
import io.realm.RealmConfiguration



/**
 * Created by jamie on 30/10/2017.
 */
class DFApplication : Application() {
    companion object {
        //platformStatic allow access it from java code
        @JvmStatic lateinit var graph: ApplicationComponent
    }

    @Inject
    lateinit var analytics: FirebaseAnalytics

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        Realm.init(this)
        val config = RealmConfiguration.Builder()
        config.deleteRealmIfMigrationNeeded()
        Realm.setDefaultConfiguration(config.build())
        LeakCanary.install(this)
        graph = DaggerApplicationComponent.builder().androidModule(AndroidModule(this)).build()
        graph.inject(this)
        this.analytics.logEvent("App_Launched", Bundle())
    }
}