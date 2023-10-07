package com.gami.can_i_skip


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gami.can_i_skip.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


const val AD_UNIT_ID =
    "ca-app-pub-3940256099942544/1033173712"   //"ca-app-pub-5672265312556699/8971875646"
const val TIME_BETWEEN_AD = 1000*60*60*0.25 // 15 minutes
const val SETTINGS = "SETTINGS"
const val SUBJECTS = "SUBJECTS"
const val DAYS = "DAYS"
const val LOGIN = "LOGIN"

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    lateinit var mAdView: AdView
    private var mInterstitialAd: InterstitialAd? = null
    private var adIsLoading = false
    private var TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        var currentFragment = ""
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(R.layout.activity_main)

/*        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder().setTestDeviceIds(listOf("ABCDEF012345")).build()
        )
        MobileAds.initialize(this) { }
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
        loadAd()*/




        App.loadEverythingFromFile()
        Log.d("Timetable", App.timetable.toString())

        val navMenu = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        val settingsFragment = SettingsFragment();
        val loginFragment = LoginFragment();
        val subjectFragment = SubjectFragment();
        val dayFragment = DayFragment();




        if (App.credentials.email != "") {
            GlobalScope.launch {
                App.refresh()
                val currentFrag =
                    supportFragmentManager.findFragmentByTag(currentFragment)
                if(currentFrag!=null){
                    val ft = supportFragmentManager.beginTransaction()
                    ft.detach(currentFrag)

                    ft.commit()
                    val ft2 = supportFragmentManager.beginTransaction()
                    ft2.attach(currentFrag)

                    ft2.commit()

                }
            }


            replaceFragment(subjectFragment, getString(R.string.topbar_by_subject), SUBJECTS)
            currentFragment = SUBJECTS

        } else {

            replaceFragment(loginFragment, getString(R.string.topbar_login), LOGIN)
            currentFragment = LOGIN
        }


        val topBar =
            findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.topAppBar)

        topBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.refresh -> {
                    GlobalScope.launch {

                        App.refresh()
                        //RELOAD CURRENT FRAGMENT
                        val currentFrag =
                            supportFragmentManager.findFragmentByTag(currentFragment)
                        if(currentFrag!=null){
                            val ft = supportFragmentManager.beginTransaction()
                            ft.detach(currentFrag)

                            ft.commit()
                            val ft2 = supportFragmentManager.beginTransaction()
                            ft2.attach(currentFrag)

                            ft2.commit()

                        }
                    }
                    true
                }

                else -> false
            }
        }
        App.topBar = topBar


        navMenu.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.nav_day -> {
       /*             showInterstitial()*/

                    replaceFragment(dayFragment, getString(R.string.topbar_by_day), DAYS);
                    currentFragment = DAYS
                    true

                }

                R.id.nav_subject -> {
               /*     showInterstitial()*/

                    replaceFragment(subjectFragment, getString(R.string.topbar_by_subject), SUBJECTS)
                    currentFragment = SUBJECTS


                    true
                }

                R.id.nav_settings -> {
              /*      showInterstitial()*/
                    replaceFragment(settingsFragment, getString(R.string.topbar_settings), SETTINGS)
                    currentFragment = SETTINGS
                    true
                }


                else -> false

            }


        }


    }

    private fun replaceFragment(fragment: Fragment, title: String = "Can I Skip?", tag: String = "") {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentHost, fragment, tag).commit()
        val TopBar =
            findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.topAppBar)
        App.topBar?.setTitle(title)

    }

    private fun loadAd() {
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this, AD_UNIT_ID, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.message)
                mInterstitialAd = null
                adIsLoading = false
                val error =
                    "domain: ${adError.domain}, code: ${adError.code}, " + "message: ${adError.message}"
                Log.d(TAG, "onAdFailedToLoad() with error $error")
            }

            override fun onAdLoaded(ad: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = ad
                adIsLoading = false
                Log.d(TAG, "onAdLoaded()")
            }
        })
    }


    // Show the ad if it's ready. Otherwise toast and restart the game.
    private fun showInterstitial() {
        if (App.preferences.lastAdTimestamp + TIME_BETWEEN_AD > System.currentTimeMillis()) return
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Ad was dismissed.")
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    mInterstitialAd = null
                    loadAd()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.d(TAG, "Ad failed to show.")
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    mInterstitialAd = null
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Ad showed fullscreen content.")
                    // Called when ad is dismissed.
                }
            }
            mInterstitialAd?.show(this)
            App.preferences.lastAdTimestamp = System.currentTimeMillis()
        } else {
            Log.d(TAG, "The interstitial ad wasn't ready yet.")

        }
    }


}


