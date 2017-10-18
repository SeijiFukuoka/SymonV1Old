package br.com.symon.di

import android.app.Application
import android.content.Context
import br.com.symon.SymonApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 *
 * @author juancho.
 */
@Module
class AppModule(val app: SymonApp) {

    @Provides
    @Singleton
    fun provideContext(): Context = app

    @Provides
    @Singleton
    fun provideApplication() : Application = app

}
