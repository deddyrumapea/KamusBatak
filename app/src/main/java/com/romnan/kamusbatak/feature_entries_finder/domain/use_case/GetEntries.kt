package com.romnan.kamusbatak.feature_entries_finder.domain.use_case

import com.romnan.kamusbatak.core.domain.model.Entry
import com.romnan.kamusbatak.core.util.Language
import com.romnan.kamusbatak.core.util.Resource
import com.romnan.kamusbatak.feature_entries_finder.domain.repository.EntriesFinderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetEntries(
    private val repository: EntriesFinderRepository
) {
    operator fun invoke(
        keyword: String,
        srcLang: Language
    ): Flow<Resource<List<Entry>>> {

        if (keyword.isBlank()) return flow { }

        val formattedKeyword = when (keyword.length) {
            in 0..3 -> "$keyword%"
            else -> "%$keyword%"
        }.lowercase()

        return repository.getEntries(keyword = formattedKeyword, srcLang = srcLang)
    }
}