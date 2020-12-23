package org.wit.hillfort.views.usersettings

import android.graphics.Color.RED
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.*
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp



class UserSettingsActivity: AppCompatActivity(), AnkoLogger {

    lateinit var app : MainApp

    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        toolbarSettings.title=title
        setSupportActionBar(toolbarSettings)

        app = application as MainApp

        // Retrieve the user from the intent and set the input text with the information
        if(intent.hasExtra("user"))
        {

//            //  statistics
//            //  User statistics
//            val userViewed = app.hillforts.totalHillforts(user.id)
//            statisticsHillfortsTotal.setText("Total: $userViewed")
//            statisticsHillfortsViewed.setText("Viewed: "+app.hillforts.viewedHillforts(user.id))
//            statisticsHillfortsUnseen.setText("Unseen: "+app.hillforts.unseenHillforts(user.id))

//            // Class Average Statistics
//            val classViewed = app.hillforts.classAverageViewed(app.users.findAll().size)
//            statisticsClassTotal.setText("Average Total: "+app.hillforts.classAverageTotal(app.users.findAll().size))
//            statisticsClassViewed.setText("Average Viewed: $classViewed")
//            statisticsClassUnseen.setText("Average Unseen: "+app.hillforts.classAverageUnseen(app.users.findAll().size))

//            // if the user is equal to or above class average set a green message on the layout
//            if(userViewed>=classViewed){ statisticsUserPosition.setText("Keep up the good work!!")}
//            // if the user is below average set a red message on the layout
//            else {
//                statisticsUserPosition.setText("You are below average! \n It might be time to start catching up")
//                statisticsUserPosition.setTextColor(RED)
//            }
        }

        // Update user details
        btnUpdateSettings.setOnClickListener(){
            var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

            // Check for text in both input boxes and check the email is a valid email regex
            // if they are then update the info in the json file
            if(settingsUsername != null && settingsPassword !=null) {

            }
            else{
                toast("Enter a username and password")
            }
        }

        //  Delete User and return to the authentication screen
        btnDeleteUser.setOnClickListener(){
            app.hillforts.deleteUserHillforts()
//            app.users.delete(user.copy())
//            startActivityForResult(intentFor<AuthenticationView>(),0)
        }

    }

    // Create a menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // functions for options on the menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            // cancel - finish and return
            R.id.item_settingsCancel -> {
                finish()
            }

            // delete user - delete all users hillforts and then delete the user from json file.
            // return to the user to the authenticationActivity
            R.id.item_deleteUser -> {

                app.hillforts.deleteUserHillforts()
//                app.users.delete(user.copy())
//                startActivityForResult(intentFor<AuthenticationActivity>(),0)
            }

            // Logout - return the user to the AuthenticationActivity
            R.id.nav_sign_out -> {
//                startActivityForResult(intentFor<AuthenticationActivity>(),0)
            }

        }
        return super.onOptionsItemSelected(item)
    }
    //    retrieve the current user from the JSON file
    fun loadUser(){
//        user = app.users.findOne(user.copy())

    }
}