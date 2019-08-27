package dev.fsnasser.marvelcharacters.data.db

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import androidx.room.Room

@Module
class CharactersDbModule {

    @Singleton
    @Provides
    fun providesMarvelCharactersDatabase(context: Context) : MarvelCharactersDatabase {
        return Room.databaseBuilder(context,
            MarvelCharactersDatabase::class.java, MarvelCharactersDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Singleton
    @Provides
    fun provideCharactersDao(marvelCharactersDatabase: MarvelCharactersDatabase): CharactersDao {
        return marvelCharactersDatabase.charactersDao()
    }

}