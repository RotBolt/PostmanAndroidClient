package io.rotlabs.postmanandroidclient.ui.makeRequest

import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.rotlabs.postmanandroidclient.R
import io.rotlabs.postmanandroidclient.TestComponentRule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MakeRequestActivityTest {

    val component = TestComponentRule(InstrumentationRegistry.getInstrumentation().targetContext)

    @get:Rule
    val rule = RuleChain.outerRule(component)


    @Test
    fun when_screen_launch_UrlEditText_RequestTypeSpinner_ButtonSend_ButtonResponse_should_display() {
        launch(MakeRequestActivity::class.java)
        onView(withId(R.id.etRequestUrl)).check(matches(isDisplayed()))
        onView(withId(R.id.requestTypeSpinner)).apply {
            check(matches(isDisplayed()))
            check(matches(withSpinnerText("GET")))
        }
        onView(withId(R.id.btnSend)).apply {
            check(matches(isDisplayed()))
            check(matches(withText("Send")))
        }
        onView(withId(R.id.btnResponse)).apply {
            check(matches(isDisplayed()))
            check(matches(withText("Response")))
        }
    }
}