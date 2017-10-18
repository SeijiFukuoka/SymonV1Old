package br.com.symon.di.news

import br.com.symon.di.AppModule
import br.com.symon.di.NetworkModule
import br.com.symon.features.news.NewsFragment
import dagger.Component
import javax.inject.Singleton

/**
 *
 * @author juancho.
 */
@Singleton
@Component(modules = arrayOf(
        AppModule::class,
        NewsModule::class,
        NetworkModule::class)
)
interface NewsComponent {

    fun inject(newsFragment: NewsFragment)

}