package io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.rotlabs.postmanandroidclient.R
import io.rotlabs.postmanandroidclient.TestComponentRule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddKeyValueBottomSheetTest {

    val component = TestComponentRule(InstrumentationRegistry.getInstrumentation().targetContext)

    @get:Rule
    val rule = RuleChain.outerRule(component)

    @Test
    fun on_AddKeyValueBottomSheet_launch_Key_Value_Description_Button_Should_Visible() {
        launchFragmentInContainer<AddKeyValueBottomSheet>(themeResId = R.style.Theme_PostmanAndroidClient)

        onView(withId(R.id.etKey)).check(matches(isDisplayed()))
        onView(withId(R.id.etValue)).check(matches(isDisplayed()))
        onView(withId(R.id.etDescription)).check(matches(isDisplayed()))
        onView(withId(R.id.btnSaveKeyValue)).check(matches(isDisplayed()))


    }
}