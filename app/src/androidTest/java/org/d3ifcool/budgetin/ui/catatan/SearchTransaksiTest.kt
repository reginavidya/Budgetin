package org.d3ifcool.budgetin.ui.catatan

import android.os.SystemClock
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.d3ifcool.budgetin.MainActivity
import org.d3ifcool.budgetin.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchTransaksiTest {

    @get:Rule
    val activityScenario = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun init() {
        activityScenario.scenario.onActivity {
            it.supportFragmentManager.beginTransaction()
        }
    }

    @Test
    fun searchingJudulTransaksi() {
        onView(withId(R.id.search_data)).perform(click())
        onView(withId(androidx.appcompat.R.id.search_src_text)).perform(typeText("gaji"))
        SystemClock.sleep(1500)
    }
}
