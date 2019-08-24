package dev.fsnasser.marvelcharacters.ui.views.main

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.fsnasser.marvelcharacters.utils.di.ViewModelKey

@Module
internal abstract class MainActivityModule {

    @ContributesAndroidInjector
    internal abstract fun mainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun charactersFrament(): CharactersFragment

    @ContributesAndroidInjector
    internal abstract fun favoritesFragment(): FavoritesFragment

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

}