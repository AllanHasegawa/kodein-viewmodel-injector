package kodeinViewModelInjector

import android.arch.lifecycle.ViewModel
import java.lang.ref.WeakReference

internal object TestViewModelsHolder {
    private val VIEW_MODEL_NAME = typeName(ViewModel::class.java)

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

    private fun <T> key(type: Class<T>): String =
            typeNamesForThisAndParents(type).last { !it.contains("mock", ignoreCase = true) }

    private fun <T> typeNamesForThisAndParents(source: Class<T>): List<String> {
        val parent = source.superclass
        return listOf(typeName(source)) +
                when {
                    parent == null || typeName(parent) == VIEW_MODEL_NAME -> emptyList()
                    else -> typeNamesForThisAndParents(parent)
                }
    }

    private fun <T> typeName(type: Class<T>): String = type.name
}
