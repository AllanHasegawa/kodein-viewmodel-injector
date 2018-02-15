package kodeinViewModelInjector

import android.arch.lifecycle.ViewModel

inline fun <reified ViewModelT : ViewModel> ViewModelT.overrideInjectionRuleForTesting() =
        KodeinViewModelInjector.overrideInjectionRuleForTesting(
                ViewModelT::class.java, this)

@Suppress("unused")
inline fun <reified ViewModelT : ViewModel> ViewModelT.clearInjectionRuleForTesting() =
        KodeinViewModelInjector.clearInjectionRuleForTesting(ViewModelT::class.java)
