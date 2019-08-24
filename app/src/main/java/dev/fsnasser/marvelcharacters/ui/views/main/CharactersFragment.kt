package dev.fsnasser.marvelcharacters.ui.views.main


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import dev.fsnasser.marvelcharacters.R
import dev.fsnasser.marvelcharacters.ui.adapters.CharactersListAdapter
import dev.fsnasser.marvelcharacters.databinding.FragmentCharactersBinding
import dev.fsnasser.marvelcharacters.ui.entities.Character
import dev.fsnasser.marvelcharacters.utils.SpacesItemDecoration

class CharactersFragment : Fragment() {

    private lateinit var mBinding: FragmentCharactersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_characters, container,
            false)

        val charactersMock = ArrayList<Character>()
        charactersMock.add(Character(123, "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784", "3-D Man", true, ""))
        charactersMock.add(Character(456, "http://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16", "A-Bomb (HAS)", false, "Rick Jones has been Hulk's best bud since day one, but now he's more than a friend...he's a teammate! Transformed by a Gamma energy explosion, A-Bomb's thick, armored skin is just as strong and powerful as it is blue. And when he curls into action, he uses it like a giant bowling ball of destruction! "))
        charactersMock.add(Character(789, "http://i.annihil.us/u/prod/marvel/i/mg/6/20/52602f21f29ec", "A.I.M", false, "AIM is a terrorist organization bent on destroying the world."))
        charactersMock.add(Character(124, "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available", "Aaron Stack", true, ""))
        charactersMock.add(Character(235, "http://i.annihil.us/u/prod/marvel/i/mg/9/50/4ce18691cbf04", "Abomination (Emil Blonsky)", true, "Formerly known as Emil Blonsky, a spy of Soviet Yugoslavian origin working for the KGB, the Abomination gained his powers after receiving a dose of gamma radiation similar to that which transformed Bruce Banner into the incredible Hulk."))
        charactersMock.add(Character(346, "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available", "Abomination (Ultimate)", false, ""))
        charactersMock.add(Character(457, "http://i.annihil.us/u/prod/marvel/i/mg/1/b0/5269678709fb7", "Absorbing Man", false, ""))
        charactersMock.add(Character(568, "http://i.annihil.us/u/prod/marvel/i/mg/9/30/535feab462a64", "Abyss", true, ""))


        mBinding.apply {
            rvCharacters.adapter =
                CharactersListAdapter(charactersMock)

            rvCharacters.addItemDecoration(SpacesItemDecoration(2, 8, false))
        }

        return mBinding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = CharactersFragment()
    }

}
