package org.d3ifcool.budgetin.ui.catatan

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.d3ifcool.budgetin.R
import org.hamcrest.CoreMatchers.containsString
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito


@RunWith(AndroidJUnit4::class)
class TransaksiFragmentTest {

    @Test
    fun insertDataPengeluaran() {

        val mockNavController = Mockito.mock(NavController::class.java)

        val titleScenario =
            launchFragmentInContainer<TransaksiFragment>(themeResId = R.style.Theme_Budgetin)

        titleScenario.onFragment { fragment ->
            fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    mockNavController.setGraph(R.navigation.nav_graph)
                    Navigation.setViewNavController(fragment.requireView(), mockNavController)
                }
            }
        }

        onView(withId(R.id.btn_add)).perform(click())
        onView(withId(R.id.amount)).perform(
            typeText("50000")).perform()
        onView(withId(R.id.title)).perform(
            typeText("Test Pengeluaran")).perform(closeSoftKeyboard())
        onView(withId(R.id.category)).perform(click())
        onView(withText(containsString("Makanan/Minuman"))).inRoot(isPlatformPopup()).perform(click())
        onView(withId(R.id.category)).check(matches(withText("Makanan/Minuman")))
        onView(withId(R.id.saveButton)).perform(click())
    }

    @Test
    fun insertDataPemasukan() {

        val mockNavController = Mockito.mock(NavController::class.java)

        val titleScenario =
            launchFragmentInContainer<TransaksiFragment>(themeResId = R.style.Theme_Budgetin)

        titleScenario.onFragment { fragment ->
            fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    mockNavController.setGraph(R.navigation.nav_graph)
                    Navigation.setViewNavController(fragment.requireView(), mockNavController)
                }
            }
        }

        onView(withId(R.id.btn_add)).perform(click())
        onView(withId(R.id.rbIncome)).perform(click())
        onView(withId(R.id.amount)).perform(
            typeText("50000")).perform()
        onView(withId(R.id.title)).perform(
            typeText("Test Pemasukan")).perform(closeSoftKeyboard())
        onView(withId(R.id.category)).perform(click())
        onView(withText(containsString("Gaji"))).inRoot(isPlatformPopup()).perform(click())
        onView(withId(R.id.category)).check(matches(withText("Gaji")))
        onView(withId(R.id.saveButton)).perform(click())
    }
}