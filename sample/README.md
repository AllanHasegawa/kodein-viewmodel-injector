# kodein-viewmodel-injector Sample

This sample simply shows a cat pic on screen.

## CatService

The URL is "fetched" by the `CatService`:

```kotlin
interface CatService {
    fun getCatPic(): String
}
```

A in-memory implementation is provided:

```kotlin
class InMemCatService(private val urlOverride: String?) : CatService {
    companion object {
        const val aCatUrl = "http://24.media.tumblr.com/tumblr_m1mlrfFqvk1qze0hyo1_500.jpg"
    }

    override fun getCatPic(): String = urlOverride ?: aCatUrl
}
```

## ViewModel

Our ViewModel uses the `CatService` to provide the cat pic URL:

```kotlin
open class MainViewModel(private val catService: CatService) : ViewModel() {
    private var lastCatPic: String? = null

    open fun fetchCatPicUrl(): String =
            lastCatPic ?: run {
                lastCatPic = catService.getCatPic()
                lastCatPic!!
            }
}
```

_Note_: Our ViewModel is "open" to make it easier to mock later.

## MainActivity

The MainActivity will simply inject the ViewModel.

```kotlin
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModelBinder<MainViewModel> {
        bind() from provider { MainViewModel(instance()) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.fetchCatPicUrl().let { url ->
            Log.i("MainActivity", "Loading $url")
            Picasso.with(this@MainActivity).load(url).into(catPicIv)
            catPicUrlTv.text = url
        }
    }
}
```

## Preparing the dependencies

We create two kodein containers to hold our dependencies. Singletons are placed in the `kodeinApp`
container; this includes our in-memory cat service implementation.

A second container is used as the "base container"; this container is used as the base for our
ViewModel injector.

```kotlin
val kodeinApp = Kodein {
    bind<CatService>() with singleton { InMemCatService(null) }
}

val kodeinBase = ConfigurableKodein(mutable = true)
        .apply {
            addExtend(kodeinApp)
        }
```

The `kodeinBase` container is a configurable kodein container so make it easier to override
some of our instances later on in our tests.

The `MainApplication` initializes the ViewModel injector:

```kotlin
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KodeinViewModelInjector.init { kodeinBase }
    }
}
```

And this is enough to get our app running!

## Testing

For testing and overriding our instances, check the `MainActivityTest` class. There you'll find
sample code on how to mock our ViewModel, or how to mock our service (or any other instance).

