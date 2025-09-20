package com.romnan.kamusbatak.presentation.quizGame

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.model.QuizGame
import com.romnan.kamusbatak.domain.util.Constants
import com.romnan.kamusbatak.presentation.components.RoundedEndsButton
import com.romnan.kamusbatak.presentation.theme.spacing
import com.romnan.kamusbatak.presentation.util.asString
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

@Composable
@Destination
fun QuizResultScreen(
    quizGameName: String?,
    totalItems: Int,
    correctItems: Int,
    navigator: DestinationsNavigator,
) {
    val score = (correctItems * 100) / totalItems
    val quizGameTitle = try {
        quizGameName?.let { QuizGame.valueOf(it).title }
    } catch (e: Exception) {
        null
    }?.asString()

    val context = LocalContext.current

    Scaffold(
        scaffoldState = rememberScaffoldState(),
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.surface,
                modifier = Modifier.fillMaxWidth(),
                elevation = 0.dp,
            ) {
                IconButton(onClick = { navigator.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.cd_nav_back),
                        tint = contentColorFor(backgroundColor = MaterialTheme.colors.surface),
                    )
                }

                Text(
                    text = stringResource(R.string.quiz_results),
                    style = MaterialTheme.typography.h6,
                    color = contentColorFor(backgroundColor = MaterialTheme.colors.surface),
                )

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = {
                    val shareText = context.getString(
                        R.string.format_share_quiz_results,
                        score,
                        quizGameTitle,
                    )

                    Intent(Intent.ACTION_SEND).let { intent ->
                        intent.type = Constants.INTENT_TYPE_PLAIN_TEXT
                        intent.putExtra(Intent.EXTRA_TEXT, shareText)
                        context.startActivity(intent)
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = stringResource(R.string.cd_share),
                        tint = MaterialTheme.colors.onPrimary,
                    )
                }
            }
        },
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
        ) {
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .padding(MaterialTheme.spacing.medium)
                    .fillMaxWidth(),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(MaterialTheme.spacing.medium),
                ) {
                    quizGameTitle?.let {
                        Text(
                            text = it,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                    Image(
                        painter = painterResource(
                            if (score > 69) R.drawable.partying_face
                            else R.drawable.thumbs_up
                        ),
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                    Text(
                        text = stringResource(R.string.congrats_finishing_quiz),
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                    Text(
                        text = "${score}%",
                        style = MaterialTheme.typography.h4,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            RoundedEndsButton(
                onClick = { navigator.navigateUp() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium),
            ) {
                Text(
                    text = stringResource(R.string.quit_game),
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
        }

        if (score == 100) KonfettiView(
            parties = listOf(
                Party(
                    emitter = Emitter(duration = 10, TimeUnit.SECONDS).perSecond(300),
                    position = Position.Relative(0.0, 1.0),
                    spread = 45,
                    angle = 271,
                    speed = 100f,
                ),
                Party(
                    emitter = Emitter(duration = 10, TimeUnit.SECONDS).perSecond(300),
                    position = Position.Relative(1.0, 1.0),
                    spread = 45,
                    angle = 269,
                    speed = 100f,
                ),
            ),
            modifier = Modifier.fillMaxSize(),
        )
    }
}