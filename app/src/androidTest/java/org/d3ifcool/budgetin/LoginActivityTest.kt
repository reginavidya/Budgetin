package org.d3ifcool.budgetin

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.database.FirebaseDatabase
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest{

    @Test
    fun testInsert() {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("TestLogin")
        val email = databaseReference.child("email")
        val password = databaseReference.child("password")
        val activityScenario = ActivityScenario.launch( LoginActivity::class.java )


        onView(withId(R.id.edt_email_login)).perform(
            typeText(email.toString()))
        onView(withId(R.id.edt_password_login)).perform(
            typeText(password.toString()), closeSoftKeyboard())
        onView(withId(R.id.btn_login)).perform(click())
        activityScenario.close()
    }
}