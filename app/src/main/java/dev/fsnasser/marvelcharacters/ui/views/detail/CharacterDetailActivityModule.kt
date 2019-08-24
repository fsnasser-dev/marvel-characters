package dev.fsnasser.marvelcharacters.ui.views.detail

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import dev.fsnasser.marvelcharacters.utils.di.ViewModelKey

@Module
internal abstract class CharacterDetailActivityModule {

    @ContributesAndroidInjector
    internal abstract fun characterDetailActivity(): CharacterDetailActivity

    @Binds
    @IntoMap
    @ViewModelKey(CharacterDetailViewModel::class)
    abstract fun bindCharacterDetailViewModel(viewModel: CharacterDetailViewModel): ViewModel

}