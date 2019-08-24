package dev.fsnasser.marvelcharacters.ui.views.main

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import dagger.android.support.DaggerAppCompatActivity
import dev.fsnasser.marvelcharacters.R
import dev.fsnasser.marvelcharacters.ui.adapters.MainFragmentAdapter
import dev.fsnasser.marvelcharacters.databinding.ActivityMainBinding

class MainActivity : DaggerAppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.elevation = 0.0f

        mBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )

        mBinding.apply {
            vpCharactersMain.adapter =
                MainFragmentAdapter(supportFragmentManager, this@MainActivity)
            tlCharactersMain.setupWithViewPager(vpCharactersMain)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (Intent.ACTION_SEARCH == intent?.action) {
            mBinding.tlCharactersMain.requestFocus()
            intent.getStringExtra(SearchManager.QUERY)?.also { _ ->
                // TODO: Search by query
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val menuItem = menu?.findItem(R.id.menu_search)
        val searchView = menuItem?.actionView as SearchView
        searchView.queryHint = getString(R.string.search)
        searchView.maxWidth = Integer.MAX_VALUE

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val componentName = ComponentName(this@MainActivity, MainActivity::class.java)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        return true
    }
}
