package kodeinViewModelInjector

import android.arch.lifecycle.ViewModel
import com.github.salomonbrys.kodein.Kodein

object KodeinViewModelInjector {
    val baseContainer: Kodein by lazy {
        val container = internalBaseContainerProvider?.invoke()
                ?: throw kotlin.IllegalStateException(
                        "KodeinViewModelInjector missing base container provider")
        internalBaseContainerProvider = null
        container
    }

    private var initialized = false
    private var internalBaseContainerProvider: (() -> Kodein)? = null

    fun init(baseContainerProvider: () -> Kodein) {
        if (initialized) {
            throw kotlin.IllegalStateException(
                    "KodeinViewModelInjector already initialized")
        }
        initialized = true
        KodeinViewModelInjector.internalBaseContainerProvider = baseContainerProvider
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


