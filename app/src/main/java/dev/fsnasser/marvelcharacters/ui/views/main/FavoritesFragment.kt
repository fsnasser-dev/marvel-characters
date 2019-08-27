package dev.fsnasser.marvelcharacters.ui.views.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.DaggerFragment
import dev.fsnasser.marvelcharacters.R
import dev.fsnasser.marvelcharacters.databinding.FragmentFavoritesBinding
import dev.fsnasser.marvelcharacters.ui.adapters.CharactersListAdapter
import dev.fsnasser.marvelcharacters.ui.entities.Character
import dev.fsnasser.marvelcharacters.utils.views.SpacesItemDecoration
import javax.inject.Inject

class FavoritesFragment : DaggerFragment() {

    private lateinit var mViewModel: MainViewModel

    private lateinit var mBinding: FragmentFavoritesBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            mViewModel = ViewModelProviders.of(it, viewModelFactory).get(MainViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container,
            false)

        val adapter = CharactersListAdapter(ArrayList(), mViewModel)

        mBinding.apply {
            viewModel = mViewModel
            executePendingBindings()

            rvFavoriteCharacters.adapter = adapter

            rvFavoriteCharacters.addItemDecoration(
                SpacesItemDecoration(
                    2,
                    8,
                    false
                )
            )
        }

        mViewModel.favoriteCharacters.observe(this, Observer { favoriteCharactersResult ->
            val characters = ArrayList<Character>()
            for(favoriteCharacter in favoriteCharactersResult) {
                characters.add(
                    Character(favoriteCharacter.id, favoriteCharacter.thumbnail,
                    favoriteCharacter.thumbnailExt, favoriteCharacter.name,
                    favoriteCharacter.isFavorite, favoriteCharacter.description)
                )
            }
            adapter.updateCharactersList(characters)
        })

        mViewModel.getAllFavorites()

        return mBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavoritesFragment()
    }

}
