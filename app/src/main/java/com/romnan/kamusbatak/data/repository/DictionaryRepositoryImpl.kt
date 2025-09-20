package com.romnan.kamusbatak.data.repository

import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.data.datastore.AppPreferencesManager
import com.romnan.kamusbatak.data.retrofit.EntryApi
import com.romnan.kamusbatak.data.room.EntryDao
import com.romnan.kamusbatak.data.room.entity.EntryEntity
import com.romnan.kamusbatak.domain.model.Entry
import com.romnan.kamusbatak.domain.model.Language
import com.romnan.kamusbatak.domain.model.QuizGame
import com.romnan.kamusbatak.domain.model.QuizItem
import com.romnan.kamusbatak.domain.repository.DictionaryRepository
import com.romnan.kamusbatak.domain.util.Constants.QUIZ_ITEM_OPTIONS_SIZE
import com.romnan.kamusbatak.domain.util.Resource
import com.romnan.kamusbatak.domain.util.SimpleResource
import com.romnan.kamusbatak.domain.util.UIText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import logcat.asLog
import logcat.logcat
import retrofit2.HttpException
import java.io.IOException
import kotlin.random.Random

class DictionaryRepositoryImpl(
    private val entryApi: EntryApi,
    private val entryDao: EntryDao,
    private val appPreferencesManager: AppPreferencesManager
) : DictionaryRepository {

    override val localDbLastUpdatedAt: Flow<Long>
        get() = appPreferencesManager.dataStore.data.map { it.localDbLastUpdatedAt }

    override fun getEntries(
        keyword: String,
        srcLang: Language,
    ): Flow<Resource<List<Entry>>> = flow {
        if (keyword.isBlank() || !keyword.all { it.isLetterOrDigit() }) {
            emit(Resource.Success(emptyList()))
            return@flow
        }

        val localEntries =
            entryDao.findByKeyword(keyword = keyword, srcLangCodeName = srcLang.codeName)
                .map { it.toEntry() }

        if (isFullOfflineSupported()) {
            emit(Resource.Success(data = localEntries))
            return@flow
        }

        try {
            emit(Resource.Loading(data = localEntries))

            val remoteEntries = entryApi.getEntries(
                params = mapOf(
                    "headword" to "like.%$keyword%".lowercase(),
                    "source_lang" to "eq.${srcLang.codeName}"
                )
            )

            entryDao.insert(remoteEntries.map { it.toEntryEntity() })
            val cachedEntries =
                entryDao.findByKeyword(keyword = keyword, srcLangCodeName = srcLang.codeName)
                    .map { it.toEntry() }

            emit(Resource.Success(cachedEntries))
        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> UIText.StringResource(R.string.em_http_exception)
                is IOException -> UIText.StringResource(R.string.em_io_exception)
                else -> UIText.StringResource(R.string.em_unknown)
            }.let { emit(Resource.Error(uiText = it, data = localEntries)) }
        }
    }

    override fun getEntry(
        id: Int,
    ): Flow<Resource<Entry>> = flow {
        emit(Resource.Loading())

        val entryEntity = entryDao.findById(id = id)

        emit(
            if (entryEntity != null) Resource.Success(entryEntity.toEntry())
            else Resource.Error(UIText.StringResource(R.string.em_unknown))
        )
    }

    override fun getBookmarkedEntries(
        srcLang: Language,
    ): Flow<Resource<List<Entry>>> = flow {
        emit(Resource.Loading())
        val entries =
            entryDao.findBookmarked(srcLangCodeName = srcLang.codeName).map { it.toEntry() }
        emit(Resource.Success(entries))
    }

    override fun getQuizItem(
        quizGameName: String,
    ): Flow<Resource<QuizItem>> = flow {
        emit(Resource.Loading())

        try {
            val quizGame = QuizGame.valueOf(quizGameName)
            val result: QuizItem = when (quizGame) {
                QuizGame.VocabMix -> entryDao.getRandomEntries(
                    count = QUIZ_ITEM_OPTIONS_SIZE,
                    srcLangCodeName = if (Random.nextBoolean()) Language.IND.codeName
                    else Language.BTK.codeName,
                ).let { createQuizItem(it) }

                QuizGame.VocabIndBtk -> entryDao.getRandomEntries(
                    count = QUIZ_ITEM_OPTIONS_SIZE,
                    srcLangCodeName = Language.IND.codeName,
                ).let { createQuizItem(it) }

                QuizGame.VocabBtkInd -> entryDao.getRandomEntries(
                    count = QUIZ_ITEM_OPTIONS_SIZE,
                    srcLangCodeName = Language.BTK.codeName,
                ).let { createQuizItem(it) }
            }

            emit(Resource.Success(result))
        } catch (e: Exception) {
            logcat { e.asLog() }
            emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
        }
    }

    override suspend fun getRandomEntry(): Entry? {
        return try {
            entryDao.getRandomEntries(
                count = 1,
                srcLangCodeName = Language.BTK.codeName,
            ).first().toEntry()
        } catch (e: Exception) {
            null
        }
    }

    override fun toggleBookmarkEntry(
        id: Int,
    ): Flow<Resource<Entry>> = flow {
        emit(Resource.Loading())

        val oldEntryEntity = entryDao.findById(id = id)
        entryDao.toggleBookmark(id = id)
        val newEntryEntity = entryDao.findById(id = id)

        when {
            newEntryEntity == null -> emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
            newEntryEntity.bookmarkedAt == oldEntryEntity?.bookmarkedAt -> {
                emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
            }

            else -> emit(Resource.Success(newEntryEntity.toEntry()))
        }
    }

    override fun updateLocalDb(): Flow<SimpleResource> = flow {
        emit(Resource.Loading())

        try {
            val latestEntryUpdatedAt = entryDao.getLatestEntryUpdatedAt()
            val params = when {
                isFullOfflineSupported() && !latestEntryUpdatedAt.isNullOrBlank() -> mapOf("updated_at" to "gt.$latestEntryUpdatedAt")
                else -> emptyMap()
            }
            val remoteEntries = entryApi.getEntries(params = params)
            entryDao.insert(remoteEntries.map { it.toEntryEntity() })
            setLocalDbLastUpdatedAt(System.currentTimeMillis())
            logcat { "downloadUpdate: ${remoteEntries.size} new entries inserted to local cache" }

            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            logcat { e.asLog() }
            when (e) {
                is HttpException -> UIText.StringResource(R.string.em_http_exception)
                is IOException -> UIText.StringResource(R.string.em_io_exception)
                else -> UIText.StringResource(R.string.em_unknown)
            }.let { emit(Resource.Error(uiText = it)) }
        }
    }

    private suspend fun setLocalDbLastUpdatedAt(currentTimeMillis: Long) {
        appPreferencesManager.dataStore.updateData {
            it.copy(localDbLastUpdatedAt = currentTimeMillis)
        }
    }

    private suspend fun isFullOfflineSupported(): Boolean {
        return localDbLastUpdatedAt.firstOrNull() != null
    }

    private fun createQuizItem(entries: List<EntryEntity>): QuizItem {
        val firstEntry = entries[0]

        var answerKeyIdx = 0

        val options: List<String> = mutableMapOf<String, Boolean>().apply {
            putAll(
                entries.slice(1..entries.lastIndex)
                    .map { it.definition.orEmpty() to false }) // Add false options
            put(firstEntry.definition.orEmpty(), true) // Add true option
        }.toList().shuffled().mapIndexed { index, pair ->
            if (pair.second) answerKeyIdx = index // Mark answerkey index
            pair.first // Map to its definition
        }.map {
            it.split(";".toRegex()).first() // Get the first definition of the word
        }

        return QuizItem(
            entryId = firstEntry.id,
            question = firstEntry.headword.orEmpty().orEmpty(),
            options = options,
            answerKeyIdx = answerKeyIdx,
        )
    }
}