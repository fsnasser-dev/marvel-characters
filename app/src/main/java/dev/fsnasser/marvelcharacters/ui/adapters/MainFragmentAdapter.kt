package dev.fsnasser.marvelcharacters.ui.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import dev.fsnasser.marvelcharacters.R
import dev.fsnasser.marvelcharacters.ui.views.main.CharactersFragment
import dev.fsnasser.marvelcharacters.ui.views.main.FavoritesFragment

class MainFragmentAdapter(fm: FragmentManager, private val mContext: Context)
    : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        return if (position == 0) {
            CharactersFragment.newInstance()
        } else {
            FavoritesFragment.newInstance()
        }
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0) mContext.getString(R.string.characters)
        else mContext.getString(R.string.favorites)
    }

}