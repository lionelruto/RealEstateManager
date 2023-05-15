package com.openclassrooms.realestatemanager.views

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityFirstBinding
import com.openclassrooms.realestatemanager.databinding.RealestateItemsBinding
import com.openclassrooms.realestatemanager.views.fragments.ListContainerFragment
import com.openclassrooms.realestatemanager.views.fragments.ListItemsFragment
import com.openclassrooms.realestatemanager.views.fragments.MapsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class First_Activity : AppCompatActivity(), View.OnClickListener, ListItemListener
     {
    private lateinit var estatrecycler: ActivityFirstBinding
    private lateinit var realestateItemsBinding: RealestateItemsBinding
    private val bundle = Bundle()
    var isAllFabsVisible = false;//permet de gerer la presence des icones floating
    val estates: ArrayList<Estate> = ArrayList()
    private lateinit var map: FloatingActionButton
    private lateinit var edit: FloatingActionButton
    private lateinit var fab: FloatingActionButton
    private val CONTENT_VIEW_ID = 10101010
    private lateinit var sharedPreference: SharedPreferences
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        estatrecycler= ActivityFirstBinding.inflate(layoutInflater)
        val view= estatrecycler.root
        setContentView(view)
        estatrecycler.toolbar?.title="Real Manager"
        estatrecycler.toolbar?.setNavigationIcon(R.drawable.ic_stat_menu)
        estatrecycler.toolbar?.setTitleTextColor(Color.WHITE)
        setSupportActionBar(estatrecycler.toolbar)
        val listItemListener= ListContainerFragment()
         map= estatrecycler.mapFab!!
        fab = estatrecycler.fab!!
        edit= estatrecycler.fab1!!
        map!!.visibility= View.GONE
        edit!!.visibility= View.GONE
        //estatrecycler.fab.setImageURI(ContextCompat.getDrawable(this, R.drawable.ic_stat_edit))
        fab.setOnClickListener( this )
        edit.setOnClickListener( this )
        map.setOnClickListener( this )
        //check if the intent passed with data add add to a bundle used by fragment
        if (intent.getSerializableExtra("estates")!= null){
            val estate:ArrayList<Estate> ? = intent.getSerializableExtra("estates") as ArrayList<Estate> ?
            if (estate != null) {
                val estate1= estate.get(0)
                bundle.putSerializable("estates", estate1)
                bundle.putString("chaine", "is a string")
                listItemListener.arguments= bundle

            }
        }
        //sharepreference for floating action buttons
        sharedPreference =  getSharedPreferences("isAllFabsVisible", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putBoolean("isAllFabsVisible",false)
        editor.commit()

        val getfloat =sharedPreference.getBoolean("isAllFabsVisible",false)
        if(getfloat){
            map.hide();
            map.setVisibility(View.GONE);
            edit.hide();
            edit.setVisibility(View.GONE)
            fab.show()
            fab.visibility= View.VISIBLE
            // isAllFabsVisible= false
            sharedPreference =  getSharedPreferences("isAllFabsVisible", Context.MODE_PRIVATE)
            val editor = sharedPreference.edit()
            editor.putBoolean("isAllFabsVisible",false)
            editor.commit()
        }

        if(savedInstanceState != null) {
            val frag1= supportFragmentManager.getFragment(savedInstanceState,"frag1")
            val frag2= supportFragmentManager.getFragment(savedInstanceState, "frag2")

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // landscape
                frag1?.let { showFragment().replace(R.id.fragment_containerlist_view, it).addToBackStack(null).commit() }
                //frag2?.let { showFragment2().replace(R.id.fragment_itemcontainer_view, it).commit() }

                realestateItemsBinding= RealestateItemsBinding.inflate(layoutInflater)
                realestateItemsBinding.estateName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                // portrait
                frag2?.let { showFragment2().replace(R.id.fragment_itemcontainer_view, it).addToBackStack(null).commit() }

            }


        }


    }

         override fun onResume() {
        super.onResume()
    }

    override fun onClick(view: View?) {
        val getfloat =sharedPreference.getBoolean("isAllFabsVisible",false)

        when(view?.id){
            R.id.fab ->{
                if(! getfloat){
                     map.show();
                     map.setVisibility(View.VISIBLE)
                     edit.show();
                     edit.setVisibility(View.VISIBLE)
                     //fab.hide()
                     //fab.visibility= View.GONE
                     //isAllFabsVisible = true;
                    sharedPreference =  getSharedPreferences("isAllFabsVisible", Context.MODE_PRIVATE)
                    val editor = sharedPreference.edit()
                    editor.putBoolean("isAllFabsVisible",true)
                    editor.commit()
                 }
                 else{
                     map.hide();
                     map.setVisibility(View.GONE);
                     edit.hide();
                     edit.setVisibility(View.GONE)
                     fab.show()
                     fab.visibility= View.VISIBLE
                    // isAllFabsVisible= false
                    sharedPreference =  getSharedPreferences("isAllFabsVisible", Context.MODE_PRIVATE)
                    val editor = sharedPreference.edit()
                    editor.putBoolean("isAllFabsVisible",false)
                    editor.commit()
                     }
            }

            R.id.fab1 ->{
                val inten= Intent(this, MainActivity::class.java).apply { }
                startActivity(inten)
            }

            R.id.map_fab ->{
                val fragment: Fragment= MapsFragment()
                showMapFragment().add(R.id.fragment_containerlist_view,fragment ).addToBackStack(null).commit()
            }
        }
    }

    fun showFragment(): FragmentTransaction {
        val fram = supportFragmentManager.beginTransaction()

        return fram

    }

    fun showMapFragment(): FragmentTransaction{
        val frag= supportFragmentManager
            .beginTransaction()

        return frag

    }

    fun showFragment2(): FragmentTransaction{
        val frag=  supportFragmentManager.beginTransaction()
        return frag
    }

    override fun onItemclik(index: Int) {
        val intent= Intent(this, MainActivity::class.java).apply {  }
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        supportFragmentManager.putFragment(outState, "frag1", ListContainerFragment())
        supportFragmentManager.putFragment(outState, "frag2",ListItemsFragment())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.estateitems, menu)
        val menuItem: MenuItem= menu!!.findItem(R.id.menusearch)
        val editItem: MenuItem= menu!!.findItem(R.id.menuedit)
        val searchView= menuItem.actionView as SearchView
        searchView.queryHint= "Rechercher une place"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        searchView.setOnSearchClickListener {view ->
            editItem.setVisible(false)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.getItemId()

        if (id == R.id.menuedit) {
            Toast.makeText(this, "Item Two Clicked", Toast.LENGTH_LONG).show()
            return true
        }
        if (id == R.id.menusearch) {
            Toast.makeText(this, "Item Three Clicked", Toast.LENGTH_LONG).show()
            return true
        }
        return true
    }


}