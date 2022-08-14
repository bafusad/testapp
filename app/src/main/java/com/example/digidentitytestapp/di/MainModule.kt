package com.example.digidentitytestapp.di

import android.content.Context
import com.example.digidentitytestapp.data.mapper.ItemMapper
import com.example.digidentitytestapp.data.repository.CacheRepositoryImpl
import com.example.digidentitytestapp.data.repository.CatalogRepositoryImpl
import com.example.digidentitytestapp.data.security.SecurityUtil
import com.example.digidentitytestapp.domain.repository.CacheRepository
import com.example.digidentitytestapp.domain.repository.CatalogRepository
import com.example.digidentitytestapp.domain.usecase.CachingUseCase
import com.example.digidentitytestapp.domain.usecase.CachingUseCaseImpl
import com.example.digidentitytestapp.domain.usecase.MainScreenUseCase
import com.example.digidentitytestapp.domain.usecase.MainScreenUseCaseImpl
import com.example.digidentitytestapp.presentation.screen.details.DetailsViewModel
import com.example.digidentitytestapp.presentation.screen.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

fun mainModule(appContext: Context): Module = module {
    factory { appContext }
    factory { get<Context>().getSharedPreferences("app_pref", Context.MODE_PRIVATE) }
    single { SecurityUtil }
    factory { ItemMapper() }

    factory<CatalogRepository> { CatalogRepositoryImpl(api = get(), mapper = get()) }
    factory<MainScreenUseCase> { MainScreenUseCaseImpl(cachingUseCase = get(), repository = get()) }

    factory<CacheRepository> {
        CacheRepositoryImpl(
            gson = get(),
            mapper = get(),
            securityUtil = get(),
            sharedPreferences = get()
        )
    }
    factory<CachingUseCase> { CachingUseCaseImpl(cacheRepository = get()) }

    viewModel { MainViewModel(get()) }
    viewModel { DetailsViewModel() }
}