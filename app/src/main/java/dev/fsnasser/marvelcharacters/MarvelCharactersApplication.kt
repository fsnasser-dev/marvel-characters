package dev.fsnasser.marvelcharacters

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class MarvelCharactersApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }

}