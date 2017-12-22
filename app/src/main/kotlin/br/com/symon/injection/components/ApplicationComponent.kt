package br.com.symon.injection.components

import android.content.Context
import br.com.symon.CustomApplication
import br.com.symon.data.repository.*
import br.com.symon.injection.modules.ApplicationModule
import br.com.symon.injection.modules.CacheModule
import br.com.symon.injection.modules.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(ApplicationModule::class), (NetworkModule::class), (CacheModule::class)])
interface ApplicationComponent {
    fun inject(customApplication: CustomApplication)
    fun context(): Context
    fun userRepository(): UserRepository
    fun fileRepository(): FileRepository
    fun settingsRepository(): SettingsRepository
    fun saleRepository(): SaleRepository
    fun commentRepository(): CommentRepository
    fun blockedUsersRepository(): BlockedUsersRepository
}