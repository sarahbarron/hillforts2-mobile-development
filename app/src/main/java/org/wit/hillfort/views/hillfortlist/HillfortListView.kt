package org.wit.hillfort.views.hillfortlist

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.*
import org.wit.hillfort.R
import org.wit.hillfort.activities.HillfortAdapter
import org.wit.hillfort.activities.HillfortListener
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.views.base.BaseView

// Activity to show a list of hillforts
class HillfortListView : BaseView(), HillfortListener, AnkoLogger{

    lateinit var presenter: HillfortListPresenter
    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list)
        // Support for a toolbar
        toolbar.title = title
        setSupportActionBar(toolbar)

        presenter = initPresenter(HillfortListPresenter(this)) as HillfortListPresenter

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = HillfortAdapter(presenter.getHillforts(user.id), this)
        recyclerView.adapter?.notifyDataSetChanged()
        // retrieve user from the intent
        if(intent.hasExtra("user"))
        {
            user = intent.extras?.getParcelable<UserModel>("user")!!
            info("User: $user")
        }
        presenter.loadHillforts()
    }

    // show hillforts in the recycler view
    override fun showHillforts (hillforts: List<HillfortModel>) {
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
            R.id.item_add -> presenter.doAddHillfort()
            R.id.item_map -> presenter.doShowHillfortsMap()
            R.id.item_deleteAllHillforts -> presenter.deleteAllHillforts(user.id)
            R.id.nav_sign_out -> presenter.doLogout()
            R.id.item_settings -> presenter.doShowSettings(user)
        }
        return super.onOptionsItemSelected(item)
    }

    // When a hillfort is clicked start the hillfortActivity for the clicked hillfort
    override fun onHillfortClick(hillfort: HillfortModel) {
       presenter.doEditHillfort(hillfort)
    }

    // when the hillfort visited checkbox is clicked set the value to true or false in the json file
    override fun onVisitedCheckboxClick(hillfort: HillfortModel, isChecked: Boolean) {
        info("hillfort :"+hillfort+" isChecked:"+ isChecked)
        if(isChecked)
        {
            presenter.doSetVisted(hillfort, true)
        }
        else{
            presenter.doSetVisted(hillfort, false)
        }

    }
    // Refreshes the view when a hillfort is updated
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.loadHillforts()
        super.onActivityResult(requestCode, resultCode, data)
    }

    // Don't allow a user to go back from the authentication screen
    override fun onBackPressed() {
        longToast("Logout from the menu to go back")
    }
}
