package kodeinViewModelInjector

import android.arch.lifecycle.ViewModel
import java.lang.ref.WeakReference

internal object TestViewModelsHolder {
    private val testViewModels = mutableMapOf<String, WeakReference<ViewModel>>()

    fun <ViewModelT : ViewModel> getTestViewModel(type: Class<ViewModelT>): ViewModelT? =
            synchronized(testViewModels) {
                testViewModels[key(type)]?.get()?.let {
                    @Suppress("UNCHECKED_CAST")
                    it as ViewModelT
                }
            }

    fun <ViewModelT : ViewModel> overrideInjectionRuleForTesting(
            type: Class<ViewModelT>, viewModel: ViewModel): Unit = synchronized(testViewModels) {
        testViewModels[key(type)] = WeakReference(viewModel)
    }

    fun <ViewModelT : ViewModel> clearInjectionRuleForTesting(
            type: Class<ViewModelT>): Unit = synchronized(testViewModels) {
        testViewModels.remove(key(type))
    }

    fun reset() = testViewModels.clear()

    private fun <ViewModelT : ViewModel> key(type: Class<ViewModelT>) = typeName(type)
    private fun <T> typeName(type: Class<T>): String = type.name
}
