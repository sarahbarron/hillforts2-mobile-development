package org.wit.hillfort.views.usersettings

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_authentication.view.*
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.R
import org.wit.hillfort.views.BaseView

class UserSettingsView: BaseView(), AnkoLogger {
    lateinit var presenter: UserSettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        super.init(toolbarSettings, true)

        presenter = initPresenter (UserSettingsPresenter(this)) as UserSettingsPresenter

        val totalHillforts = presenter.totalHillforts()
        val totalViewed = presenter.totalViewedHillforts()
        val totalToView = totalHillforts - totalViewed

        settingsUsername.setText(presenter.user.email)
        statisticsHillfortsTotal.setText("Total Hillforts   : "+totalHillforts)
        statisticsHillfortsViewed.setText("Hillforts Visited : "+totalViewed)
        statisticsHillfortsUnseen.setText("Hillfort Unseen    : "+totalToView)

        btnUpdateSettings.setOnClickListener(){
          presenter.updateSettings(settingsUsername.text.toString(), settingsPassword.text.toString())
        }

        btnDeleteUser.setOnClickListener(){
            presenter.doDelete()
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
                presenter.doCancel()
            }

            // delete user - delete all users hillforts and then delete the user from json file.
            // return to the user to the authenticationActivity
            R.id.item_deleteUser -> {
               presenter.doDelete()
            }

            // Logout - return the user to the AuthenticationActivity
            R.id.nav_sign_out -> {
                presenter.doLogout()
            }

        }
        return super.onOptionsItemSelected(item)
    }
}


