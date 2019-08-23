package dev.fsnasser.marvelcharacters.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import dev.fsnasser.marvelcharacters.R
import dev.fsnasser.marvelcharacters.adapters.CharactersListAdapter
import dev.fsnasser.marvelcharacters.databinding.FragmentCharactersBinding
import dev.fsnasser.marvelcharacters.views.entities.Character

class CharactersFragment : Fragment() {

    private lateinit var mBinding: FragmentCharactersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_characters, container,
            false)

        val charactersMock = ArrayList<Character>()
        charactersMock.add(Character(123, "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784/standard_xlarge.jpg", "3-D Man", true))
        charactersMock.add(Character(456, "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16/standard_xlarge.jpg", "A-Bomb (HAS)", false))
        charactersMock.add(Character(789, "http://i.annihil.us/u/prod/marvel/i/mg/6/20/52602f21f29ec/standard_xlarge.jpg", "A.I.M", false))
        charactersMock.add(Character(124, "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available/standard_xlarge.jpg", "Aaron Stack", true))
        charactersMock.add(Character(235, "http://i.annihil.us/u/prod/marvel/i/mg/9/50/4ce18691cbf04/standard_xlarge.jpg", "Abomination (Emil Blonsky)", true))
        charactersMock.add(Character(346, "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available/standard_xlarge.jpg", "Abomination (Ultimate)", false))
        charactersMock.add(Character(457, "http://i.annihil.us/u/prod/marvel/i/mg/1/b0/5269678709fb7/standard_xlarge.jpg", "Absorbing Man", false))
        charactersMock.add(Character(568, "http://i.annihil.us/u/prod/marvel/i/mg/9/30/535feab462a64/standard_xlarge.jpg", "Abyss", true))


        mBinding.apply {
            rvCharacters.adapter = CharactersListAdapter(charactersMock)
        }

        return mBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = CharactersFragment()
    }

}
