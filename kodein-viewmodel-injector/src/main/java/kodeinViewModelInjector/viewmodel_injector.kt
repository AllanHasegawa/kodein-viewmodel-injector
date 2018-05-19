package kodeinViewModelInjector

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

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
                            }.run {
                                val viewModel by instance<ViewModelT>()
                                viewModel
                            }
                        else -> testViewModel
                    } as T
                }
            })
            .get(ViewModelT::class.java)
}

