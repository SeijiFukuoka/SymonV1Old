package br.com.symon.injection.modules

import android.content.Context
import br.com.symon.CustomApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val customApplication: CustomApplication) {

    @Provides
    @Singleton
    fun provideContext(): Context = customApplication

    @Provides
    @Singleton
    fun provideApplication(): CustomApplication = customApplication
}