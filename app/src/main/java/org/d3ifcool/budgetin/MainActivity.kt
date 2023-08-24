package org.d3ifcool.budgetin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import org.d3ifcool.budgetin.databinding.ActivityMainBinding
import org.d3ifcool.budgetin.notify.FcmService
import org.d3ifcool.budgetin.notify.createChannel
import org.d3ifcool.budgetin.ui.ProfileFragment
import org.d3ifcool.budgetin.ui.StatistikFragment
import org.d3ifcool.budgetin.ui.catatan.TransaksiFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        @Suppress("DEPRECATION")
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.catatan -> {
                    loadFragment(TransaksiFragment())
                }

                R.id.statistika -> {
                    loadFragment(StatistikFragment())
                }

                R.id.profile -> {
                    loadFragment(ProfileFragment())
                }
            }
            true
        }

        binding.bottomNavigationView.selectedItemId = R.id.catatan

        createChannel(
            this,
            R.string.news_channel_id,
            R.string.news_channel_name,
            R.string.news_channel_desc
        )

        createChannel(
            this,
            R.string.notif_channel_id,
            R.string.notif_channel_name,
            R.string.notif_channel_desc
        )
        tanganiPengumuman(intent)
    }

    private  fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_fragment, fragment)
            addToBackStack(null)
            commit()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            tanganiPengumuman(intent)
        }
    }

    private fun tanganiPengumuman(intent: Intent) {
        if (!intent.hasExtra(FcmService.KEY_URL)) return
        val url = intent.getStringExtra(FcmService.KEY_URL) ?: return
        val tabsIntent = CustomTabsIntent.Builder().build()
        tabsIntent.launchUrl(this, Uri.parse(url))
    }
}