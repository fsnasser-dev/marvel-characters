package dev.fsnasser.marvelcharacters.ui.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import dev.fsnasser.marvelcharacters.R
import dev.fsnasser.marvelcharacters.ui.adapters.CharactersListAdapter
import dev.fsnasser.marvelcharacters.databinding.FragmentFavoritesBinding
import dev.fsnasser.marvelcharacters.ui.entities.Character

class FavoritesFragment : Fragment() {

    private lateinit var mBinding: FragmentFavoritesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container,
            false)

        val charactersMock = ArrayList<Character>()
        charactersMock.add(Character(123, "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784/standard_xlarge.jpg", "3-D Man", true))
        charactersMock.add(Character(124, "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available/standard_xlarge.jpg", "Aaron Stack", true))
        charactersMock.add(Character(235, "http://i.annihil.us/u/prod/marvel/i/mg/9/50/4ce18691cbf04/standard_xlarge.jpg", "Abomination (Emil Blonsky)", true))
        charactersMock.add(Character(568, "http://i.annihil.us/u/prod/marvel/i/mg/9/30/535feab462a64/standard_xlarge.jpg", "Abyss", true))


        mBinding.apply {
            rvFavoriteCharacters.adapter =
                CharactersListAdapter(charactersMock)
        }

        return mBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavoritesFragment()
    }

}
