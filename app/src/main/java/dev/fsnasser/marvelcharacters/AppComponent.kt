package dev.fsnasser.marvelcharacters

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dev.fsnasser.marvelcharacters.ui.views.detail.CharacterDetailActivityModule
import dev.fsnasser.marvelcharacters.ui.views.main.MainActivityModule
import dev.fsnasser.marvelcharacters.utils.di.ViewModelBuilder
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ViewModelBuilder::class,
    MainActivityModule::class,
    CharacterDetailActivityModule::class
])

interface AppComponent : AndroidInjector<MarvelCharactersApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MarvelCharactersApplication>()

}