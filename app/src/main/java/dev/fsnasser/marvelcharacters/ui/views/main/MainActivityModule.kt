package dev.fsnasser.marvelcharacters.ui.views.main

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class MainActivityModule {

    @ContributesAndroidInjector
    internal abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun charactersFrament(): CharactersFragment

    @ContributesAndroidInjector
    internal abstract fun favoritesFragment(): FavoritesFragment

}