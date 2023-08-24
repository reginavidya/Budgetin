package org.d3ifcool.budgetin

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RegisterActivityTest {

    private lateinit var databaseReference: DatabaseReference

    @Before
    fun database(){
        databaseReference = FirebaseDatabase.getInstance().reference.child("TestLogin")
    }

    @Test
    fun testInsert() {
        val activityScenario = ActivityScenario.launch(RegisterActivity::class.java)

        activityScenario.close()
    }

}