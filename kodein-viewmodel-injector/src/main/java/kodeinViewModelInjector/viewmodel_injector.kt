package kodeinViewModelInjector

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance

inline fun <ActivityT : FragmentActivity, reified ViewModelT : ViewModel> ActivityT.viewModelBinder(
        baseContainer: Kodein = KodeinViewModelInjector.baseContainer,
        crossinline binder: (Kodein.Builder.(ActivityT) -> Unit)) = lazy {
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
                                binder.invoke(this, this@viewModelBinder)
                            }.run { instance<ViewModelT>() }
                        else -> testViewModel
                    } as T
                }
            })
            .get(ViewModelT::class.java)
}

