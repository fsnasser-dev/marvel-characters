package dev.fsnasser.marvelcharacters.ui.views.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerAppCompatActivity
import dev.fsnasser.marvelcharacters.R
import dev.fsnasser.marvelcharacters.databinding.ActivityCharacterDetailBinding
import dev.fsnasser.marvelcharacters.ui.adapters.ComicsSeriesAdapter
import dev.fsnasser.marvelcharacters.ui.entities.Character
import javax.inject.Inject

class CharacterDetailActivity : DaggerAppCompatActivity() {

    companion object {
        const val CHARACTER_OBJ = "CHARACTER_OBJ"
        private const val PAGE_LIMIT = 20
    }

    private lateinit var mViewModel: CharacterDetailViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mCharacterIntent: Character

    private lateinit var mBinding: ActivityCharacterDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CharacterDetailViewModel::class.java)

        mCharacterIntent = intent.getSerializableExtra(CHARACTER_OBJ) as Character

        title = mCharacterIntent.name

        mBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_character_detail
        )

        val comicsAdapter = ComicsSeriesAdapter(emptyList())

        mBinding.apply {
            if(!mCharacterIntent.thumbnail.isNullOrBlank()) {
                Picasso.with(this@CharacterDetailActivity)
                    .load("${mCharacterIntent.thumbnail}/landscape_amazing.${mCharacterIntent.thumbnailExt}")
                    .into(ivCharacterDetailImg)
            }

            tvCharacterDetailDesc.text =
                if(!mCharacterIntent.description.isNullOrBlank()) mCharacterIntent.description
                else getString(R.string.no_character_description)

            rvCharacterDetailComics.adapter = comicsAdapter
            rvCharacterDetailSeries.adapter = ComicsSeriesAdapter(emptyList())

            svCharacterDetail.smoothScrollTo(0, 0)
        }

        mViewModel.comics.observe(this, Observer { comics ->
            comicsAdapter.update(comics)
        })

        mViewModel.getAllFromCharacter(mCharacterIntent.id, PAGE_LIMIT)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.character_detail_menu, menu)
        val favoriteMenu = menu?.findItem(R.id.menu_favorite)
        if(mCharacterIntent.isFavorite) {
            favoriteMenu?.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_nm)
        } else {
            favoriteMenu?.icon = ContextCompat.getDrawable(this, R.drawable.ic_star_border_nm)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.menu_favorite -> {

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
