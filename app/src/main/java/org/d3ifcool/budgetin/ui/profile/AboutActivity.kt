package org.d3ifcool.budgetin.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.d3ifcool.budgetin.databinding.AboutActivityBinding

class AboutActivity : AppCompatActivity()  {

    private lateinit var binding : AboutActivityBinding

    override fun onCreate (savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = AboutActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        @Suppress("DEPRECATION")
        binding.btnBackAbout.setBackgroundColor(resources.getColor(android.R.color.transparent))
        binding.btnBackAbout.setOnClickListener{
            @Suppress("DEPRECATION")
            onBackPressed()
        }
    }
}
