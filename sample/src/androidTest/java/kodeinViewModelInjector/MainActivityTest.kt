package kodeinViewModelInjector

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.provider
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.kodeinViewModelInjector.R
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var activityRule =
            ActivityTestRule(MainActivity::class.java,
                    true, false /* Do not launch activity! */)

    @After
    fun resetAll() {
        KodeinViewModelInjector.clearAllInjectionRulesForTesting()

        kodeinBase.clear()
        kodeinBase.addExtend(kodeinApp)
    }

    @Test
    fun test_with_real_viewmodel() {
        val expectedUrl = InMemCatService.aCatUrl

        launchActivity()

        onView(withId(R.id.catPicUrlTv)).check(matches(withText(expectedUrl)))
    }

    @Test
    fun test_with_mocked_viewmodel() {
        val expectedUrl = "http://24.media.tumblr.com/tumblr_lh3yotLGx71qgnva2o1_500.jpg"

        // Create mock
        val viewModel = mock<MainViewModel>()
        whenever(viewModel.fetchCatPicUrl()).thenReturn(expectedUrl)
        // Override injection rule
        viewModel.overrideInjectionRuleForTesting()

        // Activity will receive the mock instead of the "real" ViewModel
        launchActivity()

        onView(withId(R.id.catPicUrlTv)).check(matches(withText(expectedUrl)))
    }

    @Test
    fun test_with_mocked_service() {
        val expectedUrl = "https://78.media.tumblr.com/qgIb8tERimy88hbpqEWqCFOno1_500.jpg"

        // We override the "base container" passed to the "KodeinViewModelInjector::init" method
        kodeinBase.addImport(Kodein.Module {
            bind<CatService>(overrides = true) with provider { InMemCatService(expectedUrl) }
        }, allowOverride = true)

        launchActivity()

        onView(withId(R.id.catPicUrlTv)).check(matches(withText(expectedUrl)))
    }

    @Test
    fun test_with_real_viewmodel_2() {
        val expectedUrl = InMemCatService.aCatUrl

        launchActivity()

        onView(withId(R.id.catPicUrlTv)).check(matches(withText(expectedUrl)))
    }

    private fun launchActivity() {
        activityRule.launchActivity(Intent())
    }
}
