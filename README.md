# kodein-viewmodel-injector

Simple way to inject Google AAC's ViewModel into an Activity; with support for overriding the
injection during tests.

## Installation

Add it in your root `build.gradle`:

```groovy
allprojects {
    repositories {
        //...
        maven { url 'https://jitpack.io' }
	}
}
```

Add the dependency:

```groovy
dependencies {
    implementation 'com.github.AllanHasegawa:kodein-viewmodel-injector:<version>'
}
```

Where `<version>` should be replaced with the latest version.

[![Release](https://jitpack.io/v/AllanHasegawa/kodein-viewmodel-injector.svg)](https://jitpack.io/#AllanHasegawa/kodein-viewmodel-injector)

## Usage

Create your kodein container as you usually do, for example:

```kotlin
val kodeinApp = Kodein {
    bind<Service>() with singleton { Service() }
    // Others bindings
}
```

Then, initialize this lib with the container containing all of the dependencies required by the ViewModels:

```kotlin
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KodeinViewModelInjector.init { kodeinApp }
    }
}
```

In your activity, inject the ViewModel with the `viewModelBinder` delegate:

```kotlin
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModelBinder<MainActivity, MainViewModel> {
        bind() from provider { MainViewModel(instance()) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Use your ViewModel here
        viewModel.getSomething()
    }
}
```

And that's it!

## Overriding the ViewModel during tests

If you want to override the ViewModel injection during tests in order to use a mock instead,
then you can call the extension function `overrideInjectionRuleForTesting()` on your
VieModel:

```kotlin
    @Test
    fun test_with_mocked_viewmodel() {
        // Create mock
        val viewModel = mock<MainViewModel>()
        whenever(viewModel.someMethod()).thenReturn(something)
	
        // Override injection rule
        viewModel.overrideInjectionRuleForTesting()

        // Activity will receive the mock instead of the "real" ViewModel
        launchActivity()

        onView(withId(R.id.text)).check(matches(withText(expectedText)))
    }
```

Check the full sample in the `sample` module.

## FAQ

### Should I use this?

"No" -- Jake Wharton

## LICENSE

```
Copyright 2018 Allan Yoshio Hasegawa

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
