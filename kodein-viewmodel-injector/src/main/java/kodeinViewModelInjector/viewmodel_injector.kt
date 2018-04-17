package kodeinViewModelInjector

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance

inline fun <reified ViewModelT : ViewModel> FragmentActivity.viewModelBinder(
        baseContainer: Kodein = KodeinViewModelInjector.container,
        crossinline binder: (Kodein.Builder.() -> Unit)) = lazy {
    ViewModelProviders
            .of(this, object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val testViewModel = KodeinViewModelInjector
                            .getTestViewModel(ViewModelT::class.java)
                    @Suppress("UNCHECKED_CAST")
                    return when (testViewModel) {
                        null ->
                            Kodein {
                                extend(baseContainer)
                                binder.invoke(this)
                            }.run { instance<ViewModelT>() }
                        else -> testViewModel
                    } as T
                }
            })
            .get(ViewModelT::class.java)
}

