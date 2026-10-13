package io.github.dekabrsky.androidMidSemester.di

import io.github.dekabrsky.androidMidSemester.data.cache.MatchesInMemoryCache
import io.github.dekabrsky.androidMidSemester.data.repository.MatchRepositoryImpl
import io.github.dekabrsky.androidMidSemester.domain.repository.MatchRepository
import org.koin.dsl.module

val repositoryModule = module {
    // Теперь передаем два аргумента: api (get()) и cache (get())
    single<MatchRepository> { MatchRepositoryImpl(get(), get()) }
}

val cacheModule = module {
    single { MatchesInMemoryCache() }
}
