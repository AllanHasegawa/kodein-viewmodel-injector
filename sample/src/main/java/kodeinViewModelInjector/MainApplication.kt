package kodeinViewModelInjector

import android.app.Application

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KodeinViewModelInjector.setContainerProvider { kodeinBase }
    }
}