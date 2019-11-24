package com.tosh.poolandroid.injection.component

import com.tosh.poolandroid.injection.module.NetworkModule
import com.tosh.poolandroid.viewmodel.UserViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {


    fun inject(userViewModel: UserViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }

}