package kodeinViewModelInjector

import org.kodein.di.Kodein
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

val kodeinApp = Kodein {
    bind<CatService>() with singleton { InMemCatService(null) }
}

val kodeinBase = ConfigurableKodein(mutable = true)
        .apply {
            addExtend(kodeinApp)
        }
