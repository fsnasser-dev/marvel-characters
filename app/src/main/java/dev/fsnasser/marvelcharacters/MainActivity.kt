package dev.fsnasser.marvelcharacters

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import dev.fsnasser.marvelcharacters.adapters.MainFragmentAdapter
import dev.fsnasser.marvelcharacters.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        mBinding.apply {
            vpCharactersMain.adapter = MainFragmentAdapter(supportFragmentManager)
            tlCharactersMain.setupWithViewPager(vpCharactersMain)
        }
    }
}
