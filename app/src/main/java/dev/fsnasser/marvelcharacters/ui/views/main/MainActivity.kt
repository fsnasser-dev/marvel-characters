package dev.fsnasser.marvelcharacters.ui.views.main

import android.app.Activity
import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.DaggerAppCompatActivity
import dev.fsnasser.marvelcharacters.R
import dev.fsnasser.marvelcharacters.ui.adapters.MainFragmentAdapter
import dev.fsnasser.marvelcharacters.databinding.ActivityMainBinding
import dev.fsnasser.marvelcharacters.ui.views.detail.CharacterDetailActivity
import dev.fsnasser.marvelcharacters.utils.helpers.FetcherListener
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    companion object {
        const val DETAIL_ACTIVITY_REQ = 8392
    }

    private lateinit var mViewModel: MainViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mBinding: ActivityMainBinding

    var fetcherListener: FetcherListener? = null //Needed for tests

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ViewModelProviders.of(this, viewModelFactory)
            .get(MainViewModel::class.java)

        supportActionBar?.elevation = 0.0f

        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        mViewModel.fetcherListener = fetcherListener

        mBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )

        val fragmentAdapter = MainFragmentAdapter(supportFragmentManager, this)

        mBinding.apply {
            vpCharactersMain.adapter = fragmentAdapter
            tlCharactersMain.setupWithViewPager(vpCharactersMain)
        }

        mViewModel.errorMessage.observe(this, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (Intent.ACTION_SEARCH == intent?.action) {
            mBinding.tlCharactersMain.requestFocus()
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                mViewModel.search.value = query
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == DETAIL_ACTIVITY_REQ && resultCode == Activity.RESULT_OK) {
            val characterId = data?.getLongExtra(CharacterDetailActivity.CHARACTER_ID, 0)
            if(characterId != null) mViewModel.updateFavoriteItemId.value = characterId
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val menuItem = menu?.findItem(R.id.menu_search)
        val searchView = menuItem?.actionView as SearchView
        searchView.queryHint = getString(R.string.search)
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnCloseListener {
            mViewModel.search.value = null
            false
        }

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val componentName = ComponentName(this@MainActivity, MainActivity::class.java)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        return true
    }
}
