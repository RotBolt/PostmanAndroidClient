package io.rotlabs.postmanandroidclient.ui.makeRequest.addKeyValue

import android.os.Bundle
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
class AddKeyValueBottomSheetTest {

    val component = TestComponentRule(InstrumentationRegistry.getInstrumentation().targetContext)

    @get:Rule
    val rule = RuleChain.outerRule(component)

    @Test
    fun on_AddKeyValueBottomSheet_with_KeyValueType_QueryParam_launch_Key_Value_Description_Button_Should_Visible() {
        launchFragmentInContainer<AddKeyValueBottomSheet>(Bundle().apply {
            putString(
                AddKeyValueBottomSheet.ARG_KEY_VALUE_TYPE,
                KeyValueType.QUERY_PARAM
            )
        }, themeResId = R.style.Theme_PostmanAndroidClient)

        onView(withId(R.id.tvAddConfigHeading)).apply {
            check(matches(isDisplayed()))
            check(matches(withText("${KeyValueType.QUERY_PARAM}")))
        }
        onView(withId(R.id.etKey)).check(matches(isDisplayed()))
        onView(withId(R.id.etValue)).check(matches(isDisplayed()))
        onView(withId(R.id.etDescription)).check(matches(isDisplayed()))
        onView(withId(R.id.btnSaveKeyValue)).check(matches(isDisplayed()))

    }

    @Test
    fun on_AddKeyValueBottomSheet_with_KeyValueType_Header_launch_Key_Value_Description_Button_Should_Visible() {
        launchFragmentInContainer<AddKeyValueBottomSheet>(Bundle().apply {
            putString(
                AddKeyValueBottomSheet.ARG_KEY_VALUE_TYPE,
                KeyValueType.HEADER
            )
        }, themeResId = R.style.Theme_PostmanAndroidClient)

        onView(withId(R.id.tvAddConfigHeading)).apply {
            check(matches(isDisplayed()))
            check(matches(withText("${KeyValueType.HEADER}")))
        }
        onView(withId(R.id.etKey)).check(matches(isDisplayed()))
        onView(withId(R.id.etValue)).check(matches(isDisplayed()))
        onView(withId(R.id.etDescription)).check(matches(isDisplayed()))
        onView(withId(R.id.btnSaveKeyValue)).check(matches(isDisplayed()))

    }

    @Test
    fun on_AddKeyValueBottomSheet_with_KeyValueType_FormData_launch_Key_Value_Description_Button_Should_Visible() {
        launchFragmentInContainer<AddKeyValueBottomSheet>(Bundle().apply {
            putString(
                AddKeyValueBottomSheet.ARG_KEY_VALUE_TYPE,
                KeyValueType.FORM_DATA
            )
        }, themeResId = R.style.Theme_PostmanAndroidClient)

        onView(withId(R.id.tvAddConfigHeading)).apply {
            check(matches(isDisplayed()))
            check(matches(withText("${KeyValueType.FORM_DATA}")))
        }
        onView(withId(R.id.etKey)).check(matches(isDisplayed()))
        onView(withId(R.id.etValue)).check(matches(isDisplayed()))
        onView(withId(R.id.etDescription)).check(matches(isDisplayed()))
        onView(withId(R.id.btnSaveKeyValue)).check(matches(isDisplayed()))

    }
}