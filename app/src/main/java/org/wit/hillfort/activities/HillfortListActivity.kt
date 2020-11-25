package org.wit.hillfort.activities

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_hillfort.*
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.*
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel
import java.text.SimpleDateFormat
import java.util.*

// Activity to show a list of hillforts
class HillfortListActivity : AppCompatActivity(), HillfortListener, AnkoLogger{

    lateinit var app: MainApp
    var hillfort = HillfortModel()
    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list)
        app = application as MainApp

        // Support for a toolbar
        toolbar.title = title
        setSupportActionBar(toolbar)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // retrieve user from the intent
        if(intent.hasExtra("user"))
        {
            user = intent.extras?.getParcelable<UserModel>("user")!!
            info("User: $user")
        }
        loadHillforts()
    }

    // Retrieve all of the users hillforts
    private fun loadHillforts() {
        showHillforts(app.hillforts.findAll(user.id))
    }

    // show hillforts in the recycler view
    fun showHillforts (hillforts: List<HillfortModel>) {
        recyclerView.adapter = HillfortAdapter(hillforts, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }

    // create the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // functions when an item from the menu is clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            R.id.item_add -> startActivityForResult(intentFor<HillfortActivity>().putExtra(
                "user",
                user
            ), 0)
            R.id.item_deleteAllHillforts -> {
                app.hillforts.deleteUserHillforts(user.id)
                loadHillforts()
            }
            R.id.item_logout ->startActivityForResult(intentFor<AuthenticationActivity>(),0)
            R.id.item_settings ->  startActivityForResult(intentFor<UserSettingsActivity>().putExtra(
                "user",
                user
            ), 0)
        }
        return super.onOptionsItemSelected(item)
    }

    // When a hillfort is clicked start the hillfortActivity for the clicked hillfort
    override fun onHillfortClick(hillfort: HillfortModel) {
        startActivityForResult(intentFor<HillfortActivity>().putExtra("hillfort_edit", hillfort).putExtra("user", user), 0)
    }

    // when the hillfort visited checkbox is clicked set the value to true or false in the json file
    override fun onVisitedCheckboxClick(hillfort: HillfortModel, isChecked: Boolean) {
        info("hillfort :"+hillfort+" isChecked:"+ isChecked)
        if(isChecked)
        {
            app.hillforts.visited(hillfort, true)
            val simpleDateFormat = SimpleDateFormat("yyy.MM.dd 'at' HH:mm:ss")
            val currentDateAndTime: String = simpleDateFormat.format(Date())
            hillfort.date = currentDateAndTime


        }
        else{
            app.hillforts.visited(hillfort, false)
            hillfort.date = ""
        }

    }
    // Refreshes the view when a hillfort is updated
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadHillforts()
        super.onActivityResult(requestCode, resultCode, data)
    }

    // Don't allow a user to go back from the authentication screen
    override fun onBackPressed() {
        longToast("Logout from the menu to go back")
    }
}
