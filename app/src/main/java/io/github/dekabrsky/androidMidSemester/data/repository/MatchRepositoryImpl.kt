package io.github.dekabrsky.androidMidSemester.data.repository

import android.util.Log
import io.github.dekabrsky.androidMidSemester.data.api.SStatsApi
import io.github.dekabrsky.androidMidSemester.data.cache.MatchesInMemoryCache
import io.github.dekabrsky.androidMidSemester.data.mapper.toMatch
import io.github.dekabrsky.androidMidSemester.domain.model.Match
import io.github.dekabrsky.androidMidSemester.domain.repository.MatchRepository
import java.time.LocalDate

class MatchRepositoryImpl(
    private val api: SStatsApi,
    private val cache: MatchesInMemoryCache,
) : MatchRepository {

    // Тег для удобной фильтрации в Logcat
    private val TAG = "MatchRepository"

    override suspend fun matchesOn(date: LocalDate): List<Match> {
        // 1. Проверяем кэш
        val cachedMatches = cache.get(date)
        if (cachedMatches != null) {
            Log.d(TAG, "✅ CACHE HIT: Загружено ${cachedMatches.size} матчей на дату $date из памяти (без сетевого запроса)")
            return cachedMatches
        }
        // 2. Если в кэше нет, делаем сетевой запрос
        Log.d(TAG, "❌ CACHE MISS: Данные на дату $date не найдены в кэше. Выполняется сетевой запрос...")
        val matches = api.gamesOn(date.toString()).data.map { it.toMatch() }

        // 3. Сохраняем результат в кэш
        cache.put(date, matches)
        Log.d(TAG, "💾 CACHE SAVED: Успешно получено и сохранено в кэш ${matches.size} матчей на дату $date")

        return matches
    }

    override suspend fun match(id: Long): Match =
        api.game(id).data?.toMatch() ?: throw NoSuchElementException("Матч $id не найден")
}
