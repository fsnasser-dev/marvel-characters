package dev.fsnasser.marvelcharacters.ui.views.detail

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class CharacterDetailActivityModule {

    @ContributesAndroidInjector
    internal abstract fun mainActivity(): CharacterDetailActivity

}