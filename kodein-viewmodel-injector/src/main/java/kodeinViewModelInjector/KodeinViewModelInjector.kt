package kodeinViewModelInjector

import android.arch.lifecycle.ViewModel
import com.github.salomonbrys.kodein.Kodein

object KodeinViewModelInjector {
    val container: Kodein
        get() = internalContainerProvider?.invoke()
                ?: throw IllegalStateException("Container provider not set yet")

    private var internalContainerProvider: (() -> Kodein)? = null

    fun setContainerProvider(containerProvider: () -> Kodein) {
        KodeinViewModelInjector.internalContainerProvider = containerProvider
    }

    fun <ViewModelT : ViewModel> getTestViewModel(type: Class<ViewModelT>): ViewModelT? =
            TestViewModelsHolder.getTestViewModel(type)

    fun <ViewModelT : ViewModel> overrideInjectionRuleForTesting(
        type: Class<ViewModelT>, viewModel: ViewModel) =
            TestViewModelsHolder.overrideInjectionRuleForTesting(type, viewModel)

    fun <ViewModelT : ViewModel> clearInjectionRuleForTesting(type: Class<ViewModelT>) =
            TestViewModelsHolder.clearInjectionRuleForTesting(type)

    fun clearAllInjectionRulesForTesting() = TestViewModelsHolder.reset()
}


