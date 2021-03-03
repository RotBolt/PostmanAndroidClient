package io.rotlabs.postmanandroidclient.ui.makeRequest.auth

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.rotlabs.postmanandroidclient.R
import io.rotlabs.postmanandroidclient.TestComponentRule
import org.hamcrest.CoreMatchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthInfoFragmentTest {

    val testComponentRule =
        TestComponentRule(InstrumentationRegistry.getInstrumentation().targetContext)

    @get:Rule
    val rule = RuleChain.outerRule(testComponentRule)

    @Test
    fun on_AuthInfoFragment_Launch_NoAuth_Should_Be_Selected() {
        launchFragmentInContainer<AuthInfoFragment>(themeResId = R.style.Theme_PostmanAndroidClient)
        onView(withId(R.id.authTypeSpinner)).apply {
            check(matches(isDisplayed()))
            check(matches(withSpinnerText("NO_AUTH")))
        }
        onView(withId(R.id.tvNoAuth)).check(matches(isDisplayed()))
    }

    @Test
    fun when_ApiKey_Type_Auth_Selected_Should_Show_ApiKey_Container() {
        launchFragmentInContainer<AuthInfoFragment>(themeResId = R.style.Theme_PostmanAndroidClient)
        onView(withId(R.id.authTypeSpinner)).apply {
            perform(click())
            onData(allOf(`is`(instanceOf(String::class.java)), `is`("API_KEY"))).perform(click())
            check(matches(withSpinnerText("API_KEY")))
        }
        onView(withId(R.id.authInfoApiKeyContainer)).check(matches(isDisplayed()))
        onView(withId(R.id.etAuthApiKeyKey)).check(matches(isDisplayed()))
        onView(withId(R.id.etAuthApiKeyValue)).check(matches(isDisplayed()))
        onView(withId(R.id.authApiKeySpinner)).apply {
            check(matches(isDisplayed()))
            check(matches(withSpinnerText("Header")))
        }
    }

    @Test
    fun when_BearerToken_Type_Auth_Selected_Should_Show_Token_Container() {
        launchFragmentInContainer<AuthInfoFragment>(themeResId = R.style.Theme_PostmanAndroidClient)
        onView(withId(R.id.authTypeSpinner)).apply {
            perform(click())
            onData(
                allOf(
                    `is`(instanceOf(String::class.java)),
                    `is`("BEARER_TOKEN")
                )
            ).perform(click())
            check(matches(withSpinnerText("BEARER_TOKEN")))
        }
        onView(withId(R.id.authInfoTokenContainer)).check(matches(isDisplayed()))
        onView(withId(R.id.etAuthToken)).check(matches(isDisplayed()))
    }

    @Test
    fun when_Basic_Type_Auth_Selected_Should_Show_Basic_Container() {
        launchFragmentInContainer<AuthInfoFragment>(themeResId = R.style.Theme_PostmanAndroidClient)
        onView(withId(R.id.authTypeSpinner)).apply {
            perform(click())
            onData(allOf(`is`(instanceOf(String::class.java)), `is`("BASIC_AUTH"))).perform(click())
            check(matches(withSpinnerText("BASIC_AUTH")))
        }
        onView(withId(R.id.authInfoBasicContainer)).check(matches(isDisplayed()))
        onView(withId(R.id.etAuthBasicUsername)).check(matches(isDisplayed()))
        onView(withId(R.id.etAuthBasicPassword)).check(matches(isDisplayed()))
        onView(withId(R.id.showPasswordCheckBox)).apply {
            check(matches(isDisplayed()))
            check(matches(isNotChecked()))
        }
    }
}