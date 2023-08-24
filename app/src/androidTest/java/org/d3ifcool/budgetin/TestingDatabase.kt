package org.d3ifcool.budgetin

import android.os.SystemClock
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.d3ifcool.budgetin.model.TransactionModel
import org.hamcrest.Matchers.containsString
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestingDatabase {
    companion object{
        private val Data1 = TransactionModel("","",0,"","Makanan/Minuman",20000,0,0)
        private val Data2 = TransactionModel("","",0,"","",20000,0,0)
        private val Data3 = TransactionModel("","",0,"","",null,0,0)
        private val Data4 = TransactionModel("","",0,"Galon Air Mineral","Makanan/Minuman",null,0,0)
        private val Data5 = TransactionModel("","",0,"Kopi Kenangan Mantan Boba","",27000,0,0)

        private val Data6 = TransactionModel("","",0,"12345","Transportasi",500000,0,0)
        private val Data7 = TransactionModel("","",0,"87101","Belanja",20000,0,0)
        private val Data8 = TransactionModel("","",0,"","",null,0,0)
        private val Data9 = TransactionModel("","",0,"Tiket Pesawat Air Asia","",2500000,0,0)
        private val Data10 = TransactionModel("","",0,"Tiket Pesawat Citilink","Transportasi",null,0,0)

        private val Data11 = TransactionModel("","",0,"&^*","Investasi",989000,0,0)
        private val Data12 = TransactionModel("","",0,"%$#","",989000,0,0)
        private val Data13 = TransactionModel("","",0,"","",null,0,0)
        private val Data14 = TransactionModel("","",0,"Bahan Pokok Harian","", 250000,0,0)
        private val Data15 = TransactionModel("","",0,"Sayuran","Belanja",null,0,0)

        private val Data16 = TransactionModel("","",0,"Galon Air Mineral","Makanan/Minuman",20000,0,0)
        private val Data17 = TransactionModel("","",0,"Tiket Kereta Api Surabaya Bandung","Transportasi",1000000,0,0)
        private val Data18 = TransactionModel("","",0,"Uang Gedung Kampus","Pendidikan",3000000,0,0)

    }

    @Before
    fun setUp(){
        InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun testTransaction() {
        ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.btn_add)).perform(click())
        onView(withId(R.id.amount)).perform(
            ViewActions.typeText(Data1.amount.toString())
        )
        onView(withId(R.id.title)).perform(
            ViewActions.typeText(Data1.title)
        )

        onView(withId(R.id.category)).perform(click())
        onView(withText(containsString(Data1.category))).inRoot(isPlatformPopup()).perform(click())
        onView(withId(R.id.category)).check(matches(withText(Data1.category)))
        onView(withId(R.id.category)).perform(ViewActions.closeSoftKeyboard())

        onView(withId(R.id.saveButton)).perform(click())
        SystemClock.sleep(2000)
        pressBack()

        onView(withId(R.id.btn_add)).perform(click())
        onView(withId(R.id.amount)).perform(
            ViewActions.typeText(Data2.amount.toString())
        )
        onView(withId(R.id.title)).perform(
            ViewActions.typeText(Data2.title)
        )
        onView(withId(R.id.category)).perform(click())
        pressBack()
        onView(withId(R.id.category)).perform(ViewActions.closeSoftKeyboard())

        onView(withId(R.id.saveButton)).perform(click())
        SystemClock.sleep(2000)
        pressBack()

        onView(withId(R.id.btn_add)).perform(click())
        onView(withId(R.id.amount)).perform(
            ViewActions.typeText(Data3.amount.toString())
        )
        onView(withId(R.id.title)).perform(
            ViewActions.typeText(Data3.title)
        )
        onView(withId(R.id.category)).perform(click())
        pressBack()
        onView(withId(R.id.category)).perform(ViewActions.closeSoftKeyboard())

        onView(withId(R.id.saveButton)).perform(click())
        SystemClock.sleep(2000)
        pressBack()

        onView(withId(R.id.btn_add)).perform(click())
        onView(withId(R.id.amount)).perform(
            ViewActions.typeText(Data4.amount.toString())
        )
        onView(withId(R.id.title)).perform(
            ViewActions.typeText(Data4.title)
        )
        onView(withId(R.id.category)).perform(click())
        onView(withText(containsString(Data4.category))).inRoot(isPlatformPopup()).perform(click())
        onView(withId(R.id.category)).check(matches(withText(Data4.category)))
        onView(withId(R.id.category)).perform(ViewActions.closeSoftKeyboard())

        onView(withId(R.id.saveButton)).perform(click())
        SystemClock.sleep(2000)
        pressBack()

        onView(withId(R.id.btn_add)).perform(click())
        onView(withId(R.id.amount)).perform(
            ViewActions.typeText(Data5.amount.toString())
        )
        onView(withId(R.id.title)).perform(
            ViewActions.typeText(Data5.title)
        )
        onView(withId(R.id.category)).perform(click())
        pressBack()
        onView(withId(R.id.category)).perform(ViewActions.closeSoftKeyboard())

        onView(withId(R.id.saveButton)).perform(click())
        SystemClock.sleep(2000)
        pressBack()

        onView(withId(R.id.btn_add)).perform(click())
        onView(withId(R.id.amount)).perform(
            ViewActions.typeText(Data6.amount.toString())
        )
        onView(withId(R.id.title)).perform(
            ViewActions.typeText(Data6.title)
        )
        onView(withId(R.id.category)).perform(click())
        onView(withText(containsString(Data6.category))).inRoot(isPlatformPopup()).perform(click())
        onView(withId(R.id.category)).check(matches(withText(Data6.category)))
        onView(withId(R.id.category)).perform(ViewActions.closeSoftKeyboard())

        onView(withId(R.id.saveButton)).perform(click())
        SystemClock.sleep(2000)
        pressBack()

        onView(withId(R.id.btn_add)).perform(click())
        onView(withId(R.id.amount)).perform(
            ViewActions.typeText(Data7.amount.toString())
        )
        onView(withId(R.id.title)).perform(
            ViewActions.typeText(Data7.title)
        )
        onView(withId(R.id.category)).perform(click())
        onView(withText(containsString(Data7.category))).inRoot(isPlatformPopup()).perform(click())
        onView(withId(R.id.category)).check(matches(withText(Data7.category)))
        onView(withId(R.id.category)).perform(ViewActions.closeSoftKeyboard())

        onView(withId(R.id.saveButton)).perform(click())
        SystemClock.sleep(2000)
        pressBack()

        onView(withId(R.id.btn_add)).perform(click())
        onView(withId(R.id.amount)).perform(
            ViewActions.typeText(Data8.amount.toString())
        )
        onView(withId(R.id.title)).perform(
            ViewActions.typeText(Data8.title)
        )
        onView(withId(R.id.category)).perform(click())
        pressBack()
        onView(withId(R.id.category)).perform(ViewActions.closeSoftKeyboard())

        onView(withId(R.id.saveButton)).perform(click())
        SystemClock.sleep(2000)
        pressBack()

        onView(withId(R.id.btn_add)).perform(click())
        onView(withId(R.id.amount)).perform(
            ViewActions.typeText(Data9.amount.toString())
        )
        onView(withId(R.id.title)).perform(
            ViewActions.typeText(Data9.title)
        )
        onView(withId(R.id.category)).perform(click())
        pressBack()
        onView(withId(R.id.category)).perform(ViewActions.closeSoftKeyboard())

        onView(withId(R.id.saveButton)).perform(click())
        SystemClock.sleep(2000)
        pressBack()

        onView(withId(R.id.btn_add)).perform(click())
        onView(withId(R.id.amount)).perform(
            ViewActions.typeText(Data10.amount.toString())
        )
        onView(withId(R.id.title)).perform(
            ViewActions.typeText(Data10.title)
        )
        onView(withId(R.id.category)).perform(click())
        onView(withText(containsString(Data10.category))).inRoot(isPlatformPopup()).perform(click())
        onView(withId(R.id.category)).check(matches(withText(Data10.category)))
        onView(withId(R.id.category)).perform(ViewActions.closeSoftKeyboard())

        onView(withId(R.id.saveButton)).perform(click())
        SystemClock.sleep(2000)
        pressBack()

        onView(withId(R.id.btn_add)).perform(click())
        onView(withId(R.id.amount)).perform(
            ViewActions.typeText(Data11.amount.toString())
        )
        onView(withId(R.id.title)).perform(
            ViewActions.typeText(Data11.title)
        )
        onView(withId(R.id.category)).perform(click())
        onView(withText(containsString(Data11.category))).inRoot(isPlatformPopup()).perform(click())
        onView(withId(R.id.category)).check(matches(withText(Data11.category)))
        onView(withId(R.id.category)).perform(ViewActions.closeSoftKeyboard())

        onView(withId(R.id.saveButton)).perform(click())
        SystemClock.sleep(2000)
        pressBack()

        onView(withId(R.id.btn_add)).perform(click())
        onView(withId(R.id.amount)).perform(
            ViewActions.typeText(Data12.amount.toString())
        )
        onView(withId(R.id.title)).perform(
            ViewActions.typeText(Data12.title)
        )
        onView(withId(R.id.category)).perform(click())
        pressBack()
        onView(withId(R.id.category)).perform(ViewActions.closeSoftKeyboard())

        onView(withId(R.id.saveButton)).perform(click())
        SystemClock.sleep(2000)
        pressBack()

        onView(withId(R.id.btn_add)).perform(click())
        onView(withId(R.id.amount)).perform(
            ViewActions.typeText(Data13.amount.toString())
        )
        onView(withId(R.id.title)).perform(
            ViewActions.typeText(Data13.title)
        )
        onView(withId(R.id.category)).perform(click())
        pressBack()
        onView(withId(R.id.category)).perform(ViewActions.closeSoftKeyboard())

        onView(withId(R.id.saveButton)).perform(click())
        SystemClock.sleep(2000)
        pressBack()

        onView(withId(R.id.btn_add)).perform(click())
        onView(withId(R.id.amount)).perform(
            ViewActions.typeText(Data14.amount.toString())
        )
        onView(withId(R.id.title)).perform(
            ViewActions.typeText(Data14.title)
        )
        onView(withId(R.id.category)).perform(click())
        pressBack()
        onView(withId(R.id.category)).perform(ViewActions.closeSoftKeyboard())

        onView(withId(R.id.saveButton)).perform(click())
        SystemClock.sleep(2000)
        pressBack()

        onView(withId(R.id.btn_add)).perform(click())
        onView(withId(R.id.amount)).perform(
            ViewActions.typeText(Data15.amount.toString())
        )
        onView(withId(R.id.title)).perform(
            ViewActions.typeText(Data15.title)
        )
        onView(withId(R.id.category)).perform(click())
        onView(withText(containsString(Data15.category))).inRoot(isPlatformPopup()).perform(click())
        onView(withId(R.id.category)).check(matches(withText(Data15.category)))
        onView(withId(R.id.category)).perform(ViewActions.closeSoftKeyboard())

        onView(withId(R.id.saveButton)).perform(click())
        SystemClock.sleep(2000)
        pressBack()

        onView(withId(R.id.btn_add)).perform(click())
        onView(withId(R.id.amount)).perform(
            ViewActions.typeText(Data16.amount.toString())
        )
        onView(withId(R.id.title)).perform(
            ViewActions.typeText(Data16.title)
        )
        onView(withId(R.id.category)).perform(click())
        onView(withText(containsString(Data16.category))).inRoot(isPlatformPopup()).perform(click())
        onView(withId(R.id.category)).check(matches(withText(Data16.category)))
        onView(withId(R.id.category)).perform(ViewActions.closeSoftKeyboard())

        onView(withId(R.id.saveButton)).perform(click())
        SystemClock.sleep(2000)

        onView(withId(R.id.btn_add)).perform(click())
        onView(withId(R.id.amount)).perform(
            ViewActions.typeText(Data17.amount.toString())
        )
        onView(withId(R.id.title)).perform(
            ViewActions.typeText(Data17.title)
        )
        onView(withId(R.id.category)).perform(click())
        onView(withText(containsString(Data17.category))).inRoot(isPlatformPopup()).perform(click())
        onView(withId(R.id.category)).check(matches(withText(Data17.category)))
        onView(withId(R.id.category)).perform(ViewActions.closeSoftKeyboard())

        onView(withId(R.id.saveButton)).perform(click())
        SystemClock.sleep(2000)

        onView(withId(R.id.btn_add)).perform(click())
        onView(withId(R.id.amount)).perform(
            ViewActions.typeText(Data18.amount.toString())
        )
        onView(withId(R.id.title)).perform(
            ViewActions.typeText(Data18.title)
        )
        onView(withId(R.id.category)).perform(click())
        onView(withText(containsString(Data18.category))).inRoot(isPlatformPopup()).perform(click())
        onView(withId(R.id.category)).check(matches(withText(Data18.category)))
        onView(withId(R.id.category)).perform(ViewActions.closeSoftKeyboard())

        onView(withId(R.id.saveButton)).perform(click())
        SystemClock.sleep(2000)
    }
}
