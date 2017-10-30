package uk.co.jbrunton.droneforecast.application

import android.app.Application

/**
 * Created by jamie on 30/10/2017.
 */
class DFApplication : Application() {
    companion object {
        //platformStatic allow access it from java code
        @JvmStatic lateinit var graph: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        graph = DaggerApplicationComponent.builder().androidModule(AndroidModule(this)).build()
        graph.inject(this)
    }
}