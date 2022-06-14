package com.romnan.kamusbatak.data.repository

import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.data.local.CoreDao
import com.romnan.kamusbatak.domain.model.Entry
import com.romnan.kamusbatak.domain.util.Resource
import com.romnan.kamusbatak.domain.util.UIText
import com.romnan.kamusbatak.domain.repository.EntryDetailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EntryDetailRepositoryImpl(
    private val coreDao: CoreDao
) : EntryDetailRepository {

    override fun getEntry(
        id: Int
    ): Flow<Resource<Entry>> = flow {
        emit(Resource.Loading())

        val entryEntity = coreDao.getEntry(id = id)

        emit(
            if (entryEntity != null) Resource.Success(entryEntity.toEntry())
            else Resource.Error(UIText.StringResource(R.string.em_unknown))
        )
    }

    override fun toggleBookmarkEntry(
        id: Int
    ): Flow<Resource<Entry>> = flow {
        emit(Resource.Loading())

        val oldEntryEntity = coreDao.getEntry(id = id)
        coreDao.toggleBookmarkEntry(id = id)
        val newEntryEntity = coreDao.getEntry(id = id)

        when {
            newEntryEntity == null -> emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
            newEntryEntity.isBookmarked == oldEntryEntity?.isBookmarked -> {
                emit(Resource.Error(UIText.StringResource(R.string.em_unknown)))
            }
            else -> emit(Resource.Success(newEntryEntity.toEntry()))
        }
    }
}