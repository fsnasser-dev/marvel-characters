package dev.fsnasser.marvelcharacters

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun providesContext(application: MarvelCharactersApplication): Context {
        return application.applicationContext
    }

}