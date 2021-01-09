package org.wit.hillfort.views.hillfortlist

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import kotlinx.android.synthetic.main.activity_hillfort_list.bottom_navigation
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.card_hillfort.*
import org.jetbrains.anko.*
import org.wit.hillfort.R
import org.wit.hillfort.activities.HillfortAdapter
import org.wit.hillfort.activities.HillfortListener
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.views.BaseView

// Activity to show a list of hillforts
class HillfortListView : BaseView(), HillfortListener, AnkoLogger{

    lateinit var presenter: HillfortListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list)
        super.init(toolbar,false)

        presenter = initPresenter(HillfortListPresenter(this)) as HillfortListPresenter

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = HillfortAdapter(presenter.getHillforts(), this)
        recyclerView.adapter?.notifyDataSetChanged()
        presenter.loadHillforts()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                if(text.isNullOrBlank())
                    presenter.loadHillforts()
                else
                    presenter.loadSearchedHillforts(text)

                return false
            }
            override fun onQueryTextChange(text: String?): Boolean {
                if(text.isNullOrBlank())
                    presenter.loadHillforts()
                else
                    presenter.loadSearchedHillforts(text)
                return false
            }
        })


        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottomMain -> {presenter.doViewHillforts()
                    true}
                R.id.bottomFavourites -> {presenter.doViewFavourites()
                    true}
                R.id.bottomMap -> {presenter.doViewHillfortsMap()
                    true}
                R.id.bottomSettings -> {presenter.doViewSettings()
                true}
                else -> false
            }
        }
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
            R.id.nav_sign_out -> presenter.doLogout()
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

    override fun onFavouriteButtonClick(hillfort: HillfortModel) {
        if(hillfort.favourite) {
            btnFavourite.setColorFilter(Color.LTGRAY);
            presenter.doSetFavourite(hillfort, false)
            presenter.loadHillforts()

        }
        else{
           btnFavourite.setColorFilter(Color.RED)
            presenter.doSetFavourite(hillfort,true)
            presenter.loadHillforts()
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
