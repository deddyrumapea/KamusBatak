package com.romnan.kamusbatak.domain.model

import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.util.UIText

enum class QuizGame(val title: UIText) {
    VocabMix(title = UIText.StringResource(R.string.quiz_mix)),

    VocabIndBtk(title = UIText.StringResource(R.string.quiz_ind_btk)),

    VocabBtkInd(title = UIText.StringResource(R.string.quiz_btk_ind)),
}