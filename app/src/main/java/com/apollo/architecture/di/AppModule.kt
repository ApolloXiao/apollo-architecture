package com.apollo.architecture.di

import com.apollo.architecture.model.api.SimpleRetrofitClient
import com.apollo.architecture.model.api.SimpleService
import com.apollo.architecture.model.repository.ArticleRepository
import com.apollo.architecture.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}

val repositoryModule = module {
    single { SimpleRetrofitClient.getService(SimpleService::class.java, SimpleService.BASE_URL) }
    single { ArticleRepository(get()) }
}

val appModule = listOf(viewModelModule, repositoryModule)