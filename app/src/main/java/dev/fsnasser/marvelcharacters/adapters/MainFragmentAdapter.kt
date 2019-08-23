package dev.fsnasser.marvelcharacters.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import dev.fsnasser.marvelcharacters.views.CharactersFragment
import dev.fsnasser.marvelcharacters.views.FavoritesFragment

class MainFragmentAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        return if (position == 0) {
            CharactersFragment.newInstance()
        } else {
            FavoritesFragment.newInstance()
        }
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0) {
            "Personagens"
        } else {
            "Favoritos"
        }
    }

}