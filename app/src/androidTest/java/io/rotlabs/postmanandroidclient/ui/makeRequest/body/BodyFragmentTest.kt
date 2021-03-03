package io.rotlabs.postmanandroidclient.ui.makeRequest.body

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.rotlabs.postmanandroidclient.R
import io.rotlabs.postmanandroidclient.TestComponentRule
import io.rotlabs.postmanandroidclient.ui.makeRequest.auth.AuthInfoFragment
import org.hamcrest.CoreMatchers
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BodyFragmentTest {

    val testComponentRule =
        TestComponentRule(InstrumentationRegistry.getInstrumentation().targetContext)

    @get:Rule
    val rule = RuleChain.outerRule(testComponentRule)

    @Test
    fun on_BodyFragment_Launch_NoBody_Should_Be_Selected() {
        launchFragmentInContainer<BodyFragment>(themeResId = R.style.Theme_PostmanAndroidClient)
        Espresso.onView(ViewMatchers.withId(R.id.bodyTypeSpinner)).apply {
            check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            check(ViewAssertions.matches(ViewMatchers.withSpinnerText("NO_BODY")))
        }
        Espresso.onView(ViewMatchers.withId(R.id.tvNoBody))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun when_FormData_Type_Body_Selected_Should_Show_AddKeyValueConfigLayout() {
        launchFragmentInContainer<BodyFragment>(themeResId = R.style.Theme_PostmanAndroidClient)
        Espresso.onView(ViewMatchers.withId(R.id.bodyTypeSpinner)).apply {
            perform(ViewActions.click())
            Espresso.onData(
                CoreMatchers.allOf(
                    CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java)),
                    CoreMatchers.`is`("FORM_DATA")
                )
            ).perform(ViewActions.click())
            check(ViewAssertions.matches(ViewMatchers.withSpinnerText("FORM_DATA")))
        }
        Espresso.onView(ViewMatchers.withId(R.id.addKeyValueLayout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun when_Raw_Type_Body_Selected_Should_Show_EditTextRawBox() {
        launchFragmentInContainer<BodyFragment>(themeResId = R.style.Theme_PostmanAndroidClient)
        Espresso.onView(ViewMatchers.withId(R.id.bodyTypeSpinner)).apply {
            perform(ViewActions.click())
            Espresso.onData(
                CoreMatchers.allOf(
                    CoreMatchers.`is`(CoreMatchers.instanceOf(String::class.java)),
                    CoreMatchers.`is`("RAW")
                )
            ).perform(ViewActions.click())
            check(ViewAssertions.matches(ViewMatchers.withSpinnerText("RAW")))
        }
        Espresso.onView(ViewMatchers.withId(R.id.etRawBodyBox))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}