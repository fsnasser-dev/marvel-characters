package dev.fsnasser.marvelcharacters.ui.views.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.squareup.picasso.Picasso
import dagger.android.support.DaggerAppCompatActivity
import dev.fsnasser.marvelcharacters.R
import dev.fsnasser.marvelcharacters.databinding.ActivityCharacterDetailBinding
import dev.fsnasser.marvelcharacters.ui.adapters.ComicsSeriesAdapter
import dev.fsnasser.marvelcharacters.ui.entities.Character

class CharacterDetailActivity : DaggerAppCompatActivity() {

    companion object {
        const val CHARACTER_OBJ = "CHARACTER_OBJ"
    }

    private lateinit var mCharacterIntent: Character

    private lateinit var mBinding: ActivityCharacterDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mCharacterIntent = intent.getSerializableExtra(CHARACTER_OBJ) as Character

        title = mCharacterIntent.name

        mBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_character_detail
        )

        val comics = ArrayList<Character.ComicSerie>()
        comics.add(Character.ComicSerie("Avengers: The Initiative (2007) #19", "http://i.annihil.us/u/prod/marvel/i/mg/d/03/58dd080719806"))
        comics.add(Character.ComicSerie("Avengers: The Initiative (2007) #18", "http://i.annihil.us/u/prod/marvel/i/mg/6/20/58dd057d304d1"))
        comics.add(Character.ComicSerie("Avengers: The Initiative (2007) #17", "http://i.annihil.us/u/prod/marvel/i/mg/b/a0/58dd03dc2ec00"))
        comics.add(Character.ComicSerie("Avengers: The Initiative (2007) #16", "http://i.annihil.us/u/prod/marvel/i/mg/c/10/58dd01dbc6e51"))

        val series = ArrayList<Character.ComicSerie>()
        series.add(Character.ComicSerie("Avengers: The Initiative (2007 - 2010)", "http://i.annihil.us/u/prod/marvel/i/mg/5/a0/514a2ed3302f5"))
        series.add(Character.ComicSerie("Deadpool (1997 - 2002)", "http://i.annihil.us/u/prod/marvel/i/mg/7/03/5130f646465e3"))
        series.add(Character.ComicSerie("Marvel Premiere (1972 - 1981)", "http://i.annihil.us/u/prod/marvel/i/mg/4/40/5a98437953d4e"))

        mBinding.apply {
            if(!mCharacterIntent.thumbnail.isNullOrBlank()) {
                Picasso.with(this@CharacterDetailActivity)
                    .load("${mCharacterIntent.thumbnail}/landscape_amazing.${mCharacterIntent.thumbnailExt}")
                    .into(ivCharacterDetailImg)
            }

            tvCharacterDetailDesc.text =
                if(!mCharacterIntent.description.isNullOrBlank()) mCharacterIntent.description
                else getString(R.string.no_character_description)

            rvCharacterDetailComics.adapter = ComicsSeriesAdapter(comics)
            rvCharacterDetailSeries.adapter = ComicsSeriesAdapter(series)

            svCharacterDetail.smoothScrollTo(0, 0)
        }
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
