package io.rotlabs.postmanandroidclient.ui.makeRequest.header

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.rotlabs.postmanandroidclient.R
import io.rotlabs.postmanandroidclient.TestComponentRule
import io.rotlabs.postmanandroidclient.ui.makeRequest.params.ParamsFragment
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HeaderFragmentTest {

    val testComponentRule =
        TestComponentRule(InstrumentationRegistry.getInstrumentation().targetContext)

    @get:Rule
    val rule = RuleChain.outerRule(testComponentRule)

    @Test
    fun on_HeaderFragment_Launch_Key_Value_Header_Add_Param_Button_Should_Display() {
        launchFragmentInContainer<HeaderFragment>(themeResId = R.style.Theme_PostmanAndroidClient)
        Espresso.onView(ViewMatchers.withId(R.id.tvKey)).apply {
            check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            check(ViewAssertions.matches(ViewMatchers.withText("Key")))
        }

        Espresso.onView(ViewMatchers.withId(R.id.tvValue)).apply {
            check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            check(ViewAssertions.matches(ViewMatchers.withText("Value")))
        }


        Espresso.onView(ViewMatchers.withId(R.id.toIncludeCheckBox))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))
        Espresso.onView(ViewMatchers.withId(R.id.ivDelete))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)))

        Espresso.onView(ViewMatchers.withId(R.id.btnAddKeyValueConfig)).apply {
            check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            check(ViewAssertions.matches(ViewMatchers.withText("Add Header")))
        }

    }
}