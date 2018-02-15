package kodeinViewModelInjector

import com.github.salomonbrys.kodein.Kodein

object KodeinViewModelInjector {
    val baseContainerProvider: (() -> Kodein)
        get() =
            internalBaseContainerProvider
                    ?: throw kotlin.IllegalStateException(
                            "KoideinViewModelInjector missing base container provider")

    fun init(baseContainerProvider: () -> Kodein) {
        KodeinViewModelInjector.internalBaseContainerProvider = baseContainerProvider
    }

    private var internalBaseContainerProvider: (() -> Kodein)? = null
}