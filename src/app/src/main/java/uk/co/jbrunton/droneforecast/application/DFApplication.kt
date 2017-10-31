package uk.co.jbrunton.droneforecast.application

import android.app.Application
import com.squareup.leakcanary.LeakCanary

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
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
        graph = DaggerApplicationComponent.builder().androidModule(AndroidModule(this)).build()
        graph.inject(this)
    }
}