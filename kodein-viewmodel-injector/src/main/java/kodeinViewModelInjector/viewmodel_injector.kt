package kodeinViewModelInjector

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import java.lang.ref.WeakReference

inline fun <ActivityT : FragmentActivity, reified ViewModelT : ViewModel> ActivityT.viewModelBinder(
        baseContainer: Kodein? = null,
        crossinline binder: (Kodein.Builder.(ActivityT) -> Unit)) = lazy {
    ViewModelProviders
            .of(this, object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val testViewModel = KodeinViewModelTestOverrider
                            .getTestViewModel(ViewModelT::class.java)
                    @Suppress("UNCHECKED_CAST")
                    return when (testViewModel) {
                        null ->
                            Kodein {
                                val container = baseContainer
                                        ?: KodeinViewModelInjector.baseContainerProvider()
                                extend(container)
                                binder.invoke(this, this@viewModelBinder)
                            }.run { instance<ViewModelT>() }
                        else -> testViewModel
                    } as T
                }
            })
            .get(ViewModelT::class.java)
}

object KodeinViewModelTestOverrider {
    private val testViewModels = mutableMapOf<String, WeakReference<ViewModel>>()

    fun <ViewModelT : ViewModel> getTestViewModel(type: Class<ViewModelT>): ViewModelT? =
            synchronized(testViewModels) {
                testViewModels[key(type)]?.get()?.let {
                    @Suppress("UNCHECKED_CAST")
                    it as ViewModelT
                }
            }

    fun <ViewModelT : ViewModel> overrideInjectionRuleForTesting(
            type: Class<ViewModelT>, viewModel: ViewModel) = synchronized(testViewModels) {
        testViewModels[key(type)] = WeakReference(viewModel)
    }

    fun reset() = testViewModels.clear()

    private fun <ViewModelT : ViewModel> key(type: Class<ViewModelT>) = typeName(type)
}

inline fun <reified ViewModelT : ViewModel> ViewModelT.overrideInjectionRuleForTesting() {
    KodeinViewModelTestOverrider.overrideInjectionRuleForTesting(
            ViewModelT::class.java, this)
}

private fun <T> typeName(type: Class<T>): String = type.name
