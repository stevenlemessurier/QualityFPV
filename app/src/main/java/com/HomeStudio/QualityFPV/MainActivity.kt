package com.HomeStudio.QualityFPV

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Filter
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.HomeStudio.QualityFPV.data.SiteSelectorViewModel
import com.HomeStudio.QualityFPV.data.product.Product
import com.HomeStudio.QualityFPV.data.product.ProductViewModel
import com.HomeStudio.QualityFPV.nested_fragments.ProductFragment
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*


class MainActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var mDrawerToggle: ActionBarDrawerToggle
    private var mToolBarNavigationListenerIsRegister: Boolean = false
    private lateinit var mProductViewModel: ProductViewModel
    private lateinit var mSiteSelectorViewModel: SiteSelectorViewModel
    private lateinit var product: Product
    var currentSite = "Pyro Drone"
    var productOpen = false
    private var savedState: Bundle? = null
    lateinit var appInfo: ApplicationInfo


    // Sets up dropdown of websites in drawer
    class MyAdapter(context: Context, items: List<String>)
        : ArrayAdapter<String>(context, R.layout.dropdown_website_item, items) {

        private val noOpFilter = object : Filter() {
            private val noOpResult = FilterResults()
            override fun performFiltering(constraint: CharSequence?) = noOpResult
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {}
        }

        override fun getFilter() = noOpFilter
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedState = savedInstanceState

        setContentView(R.layout.activity_main)

        mProductViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        mSiteSelectorViewModel = ViewModelProvider(this).get(SiteSelectorViewModel::class.java)

        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        drawerLayout = findViewById(R.id.drawer_layout)

        // Set list of websites for data to be pulled from in the app
        val siteList = listOf("Pyro Drone", "GetFpv", "RaceDayQuads")
        navView.getHeaderView(0).autoCompleteTextView.apply {
            setAdapter(MyAdapter(applicationContext, siteList))
            setText("Pyro Drone", false)
        }

        // Set website badge and background in drawer header depending on site selected
        when(mSiteSelectorViewModel.website.value){
            "Pyro Drone" -> {
                navView.getHeaderView(0).imageView.setImageResource(R.drawable.pyrodrone)
                navView.getHeaderView(0).headerBackground.setBackgroundResource(R.drawable.background_nav_bar_pyro)
                mSiteSelectorViewModel.setWebsite("Pyro Drone")
            }
            "GetFpv" -> {
                navView.getHeaderView(0).imageView.setImageResource(R.drawable.getfpv_logo_original)
                navView.getHeaderView(0).headerBackground.setBackgroundResource(R.drawable.background_nav_bar_getfpv)
                mSiteSelectorViewModel.setWebsite("GetFpv")
            }
            "RaceDayQuads" -> {
                navView.getHeaderView(0).imageView.setImageResource(R.drawable.racedayquads_logo)
                navView.getHeaderView(0).headerBackground.setBackgroundResource(R.drawable.background_nav_bar_rdq)
                mSiteSelectorViewModel.setWebsite("RaceDayQuads")
            }
        }

        // Set website badge and background in drawer header depending on site selected
        navView.getHeaderView(0).autoCompleteTextView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                currentSite = siteList[position]
                when (currentSite) {
                    "Pyro Drone" -> {
                        navView.getHeaderView(0).imageView.setImageResource(R.drawable.pyrodrone)
                        navView.getHeaderView(0).headerBackground.setBackgroundResource(R.drawable.background_nav_bar_pyro)
                        mSiteSelectorViewModel.setWebsite("Pyro Drone")
                    }
                    "GetFpv" -> {
                        navView.getHeaderView(0).imageView.setImageResource(R.drawable.getfpv_logo_original)
                        navView.getHeaderView(0).headerBackground.setBackgroundResource(R.drawable.background_nav_bar_getfpv)
                        mSiteSelectorViewModel.setWebsite("GetFpv")
                    }
                    "RaceDayQuads" -> {
                        navView.getHeaderView(0).imageView.setImageResource(R.drawable.racedayquads_logo)
                        navView.getHeaderView(0).headerBackground.setBackgroundResource(R.drawable.background_nav_bar_rdq)
                        mSiteSelectorViewModel.setWebsite("RaceDayQuads")
                    }
                }
            }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_app_bar_open_drawer_description, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(mDrawerToggle)
        mDrawerToggle.syncState()

        supportFragmentManager.addOnBackStackChangedListener(this)
        toggleHamburgerUpButton()

        // Get the api key for youtube
        appInfo = applicationContext.packageManager.getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_flight_controller, R.id.nav_esc, R.id.nav_motor, R.id.nav_frame,
            R.id.nav_camera, R.id.nav_vtx, R.id.nav_antenna, R.id.nav_prop, R.id.nav_drone), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


    // Open last fragment saved when device is re orientated or activity resumes
    override fun onResume() {
        super.onResume()

        if (savedState?.getParcelable<Product>("productData") != null){
            val productFragment = ProductFragment()
            val bundle = Bundle()
            bundle.putParcelable("productData", savedState!!.getParcelable("productData"))
            productFragment.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, productFragment,"tag").addToBackStack("product").commit()
        }
    }


    // Closes fragments to be reopened on resume
    override fun onPause() {
        super.onPause()

        if (supportFragmentManager.backStackEntryCount > 0){
            val frag = supportFragmentManager.findFragmentByTag("tag") as ProductFragment
            product = frag.product
            productOpen = true
        }

        supportFragmentManager.popBackStack("product", 1)
    }


    // Saves the product fragment when orientation changes
    override fun onSaveInstanceState(outState: Bundle) {
        if(productOpen)
            outState.putParcelable("productData", product)

        savedState = outState

        super.onSaveInstanceState(outState)
    }


    // Sets up logic for hamburger button or back button depending on which is currently displayed
    private fun toggleHamburgerUpButton() {
        val upBtn : Boolean = supportFragmentManager.backStackEntryCount > 0

        if(upBtn){
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

            mDrawerToggle.isDrawerIndicatorEnabled = false

            if(!mToolBarNavigationListenerIsRegister){
                mDrawerToggle.setToolbarNavigationClickListener {
                    supportFragmentManager.popBackStack("product", 1)
                }

                mToolBarNavigationListenerIsRegister = true
            }
        }

        else{
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            mDrawerToggle.isDrawerIndicatorEnabled = true

            mDrawerToggle.setToolbarNavigationClickListener { null }
            mToolBarNavigationListenerIsRegister = false
        }
    }


    // Toggles visibility of progress bar when loading various fragments
    fun toggleProgressBar(makeVisible: Boolean){
        if(makeVisible) {
            progress_card.visibility = View.VISIBLE
        }
        else {
            progress_card.visibility = View.GONE
        }
    }


    // Sets text of progress dialog
    fun setProgressText(text: String){
        runOnUiThread {
            progress_text.text = text
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 0)
            supportFragmentManager.popBackStack("product", 1)
        else
            super.onBackPressed()
    }


    override fun onBackStackChanged() {
        toggleHamburgerUpButton()
    }
}