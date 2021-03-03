package io.rotlabs.postmanandroidclient.ui.makeRequest.params

import androidx.fragment.app.testing.launchFragmentInContainer
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
class ParamsFragmentTest {

    val testComponentRule =
        TestComponentRule(InstrumentationRegistry.getInstrumentation().targetContext)

    @get:Rule
    val rule = RuleChain.outerRule(testComponentRule)

    @Test
    fun on_ParamsFragment_Launch_Key_Value_Header_Add_Param_Button_Should_Display() {
        launchFragmentInContainer<ParamsFragment>(themeResId = R.style.Theme_PostmanAndroidClient)
        onView(withId(R.id.tvKey)).apply {
            check(matches(isDisplayed()))
            check(matches(withText("Key")))
        }

        onView(withId(R.id.tvValue)).apply {
            check(matches(isDisplayed()))
            check(matches(withText("Value")))
        }


        onView(withId(R.id.toIncludeCheckBox)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))
        onView(withId(R.id.ivDelete)).check(matches(withEffectiveVisibility(Visibility.INVISIBLE)))

        onView(withId(R.id.btnAddKeyValueConfig)).apply {
            check(matches(isDisplayed()))
            check(matches(withText("Add Param")))
        }

    }
}