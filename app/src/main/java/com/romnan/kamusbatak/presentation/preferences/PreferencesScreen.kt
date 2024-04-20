package com.romnan.kamusbatak.presentation.preferences

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.PermissionChecker
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.model.ThemeMode
import com.romnan.kamusbatak.domain.util.Constants
import com.romnan.kamusbatak.presentation.components.DefaultDialog
import com.romnan.kamusbatak.presentation.components.RoundedEndsButton
import com.romnan.kamusbatak.presentation.preferences.component.BasicPreference
import com.romnan.kamusbatak.presentation.preferences.component.PreferencesTopBar
import com.romnan.kamusbatak.presentation.preferences.component.SwitchPreference
import com.romnan.kamusbatak.presentation.preferences.component.TimePickerDialog
import com.romnan.kamusbatak.presentation.theme.Gray100
import com.romnan.kamusbatak.presentation.theme.Yellow600
import com.romnan.kamusbatak.presentation.theme.spacing
import com.romnan.kamusbatak.presentation.util.UIEvent
import com.romnan.kamusbatak.presentation.util.asString
import com.romnan.kamusbatak.presentation.util.launchSendSuggestionIntent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Destination
@Composable
fun PreferencesScreen(
    viewModel: PreferencesViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    parentScaffoldState: ScaffoldState,
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val notifPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    } else null


    val notifPermissionReqLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissionResults ->
            notifPermissions?.forEach { permission ->
                viewModel.onPermissionResult(
                    permission = permission,
                    isGranted = permissionResults[permission] == true,
                )
            }
        },
    )

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.uiText.asString(context)
                    )
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            PreferencesTopBar(
                onOpenDrawer = { scope.launch { parentScaffoldState.drawerState.open() } },
            )
        },
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(scaffoldPadding)
                .wrapContentHeight()
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            BasicPreference(
                imageVector = when (state.currentThemeMode) {
                    ThemeMode.System -> Icons.Default.BrightnessMedium
                    ThemeMode.Light -> Icons.Default.LightMode
                    ThemeMode.Dark -> Icons.Default.DarkMode
                },
                title = stringResource(R.string.theme),
                description = state.currentThemeMode.readableName.asString(),
                onClick = { viewModel.onThemeModeDialogVisibilityChange(visible = true) },
            )

            val dailyNotif = viewModel.dailyWordSettings.collectAsState()
            SwitchPreference(
                icon = {
                    if (dailyNotif.value.isActivated) Icons.Filled.NotificationsActive
                    else Icons.Filled.Notifications
                },
                title = { stringResource(R.string.pref_title_daily_notif) },
                description = { dailyNotif.value.readableTime?.asString() },
                checked = { dailyNotif.value.isActivated },
                onClick = {
                    when {
                        notifPermissions?.any {
                            PermissionChecker.checkSelfPermission(
                                context,
                                it,
                            ) == PermissionChecker.PERMISSION_DENIED
                        } == true -> {
                            notifPermissionReqLauncher.launch(notifPermissions)
                        }

                        !dailyNotif.value.isActivated -> {
                            TimePickerDialog(
                                context = context,
                                initHourOfDay = dailyNotif.value.calendar[Calendar.HOUR_OF_DAY],
                                initMinute = dailyNotif.value.calendar[Calendar.MINUTE],
                                onPicked = { hourOfDay: Int, minute: Int ->
                                    viewModel.onDailyWordTimePicked(
                                        hourOfDay = hourOfDay,
                                        minute = minute,
                                    )
                                },
                            ).show()
                        }

                        else -> viewModel.onTurnOffDailyWord()
                    }
                },
                onCheckedChange = {
                    when {
                        notifPermissions?.any {
                            PermissionChecker.checkSelfPermission(
                                context,
                                it,
                            ) == PermissionChecker.PERMISSION_DENIED
                        } == true -> {
                            notifPermissionReqLauncher.launch(notifPermissions)
                        }

                        !dailyNotif.value.isActivated -> {
                            TimePickerDialog(
                                context = context,
                                initHourOfDay = dailyNotif.value.calendar[Calendar.HOUR_OF_DAY],
                                initMinute = dailyNotif.value.calendar[Calendar.MINUTE],
                                onPicked = { hourOfDay: Int, minute: Int ->
                                    viewModel.onDailyWordTimePicked(
                                        hourOfDay = hourOfDay,
                                        minute = minute,
                                    )
                                },
                            ).show()
                        }

                        else -> viewModel.onTurnOffDailyWord()
                    }
                },
            )

            BasicPreference(
                imageVector = if (state.isUpdatingLocalDb) Icons.Default.Downloading
                else Icons.Default.Sync,

                title = when {
                    state.isUpdatingLocalDb -> stringResource(R.string.downloading_dictionary_data)
                    state.localDbLastUpdatedAt != null -> stringResource(R.string.update_dictionary_data)
                    else -> stringResource(R.string.download_dictionary_data)
                },
                description = stringResource(R.string.update_description).plus("\n").plus(
                    context.getString(
                        R.string.format_last_updated,
                        state.localDbLastUpdatedAt?.let { timeInMillis: Long ->
                            SimpleDateFormat(Constants.PATTERN_DATE, Locale.getDefault())
                                .format(Date().apply { this.time = timeInMillis })
                        } ?: stringResource(id = R.string.data_never_downloaded)
                    )
                ),
                onClick = { viewModel.onUpdateLocalDb() },
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            BasicPreference(
                imageVector = Icons.Default.ThumbUp,
                title = stringResource(R.string.rate_this_app),
                description = stringResource(R.string.tell_us_how_do_you_like_this_app),
                onClick = viewModel::onClickRateApp,
            )

            BasicPreference(
                imageVector = Icons.Default.Share,
                title = stringResource(R.string.share_this_app),
                description = stringResource(R.string.help_spread_the_word_to_your_friends),
                onClick = {
                    val shareText = context.getString(R.string.download_kamus_batak)
                    Intent(Intent.ACTION_SEND).let { intent ->
                        intent.type = Constants.INTENT_TYPE_PLAIN_TEXT
                        intent.putExtra(Intent.EXTRA_TEXT, shareText)
                        context.startActivity(intent)
                    }
                },
            )

            BasicPreference(
                imageVector = Icons.Default.Email,
                title = stringResource(R.string.contact_support),
                description = stringResource(R.string.report_a_problem_or_give_suggestions),
                onClick = { launchSendSuggestionIntent(context = context) },
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            BasicPreference(
                imageVector = Icons.Default.Code,
                title = stringResource(R.string.source_code),
                onClick = {
                    Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(context.getString(R.string.source_code_url))
                    }.let { context.startActivity(it) }
                },
            )

            BasicPreference(
                imageVector = Icons.Default.PrivacyTip,
                title = stringResource(R.string.privacy_policy),
                onClick = {
                    Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(context.getString(R.string.privacy_policy_url))
                    }.let { context.startActivity(it) }
                },
            )

            BasicPreference(
                imageVector = Icons.Default.Info,
                title = stringResource(R.string.app_version),
                description = state.appVersion,
                onClick = { viewModel.onClickAppVersion() },
            )
        }

        if (state.isAppRatingDialogVisible) {
            var rating by remember { mutableStateOf(0) }

            DefaultDialog(
                title = { stringResource(R.string.rate_this_app) },
                onDismissRequest = viewModel::onDismissRateAppDialog,
                properties = DialogProperties(usePlatformDefaultWidth = false),
                modifier = Modifier.padding(MaterialTheme.spacing.large),
            ) {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    (1..5).map { num: Int ->
                        Icon(
                            imageVector = when (num <= rating) {
                                true -> Icons.Filled.Star
                                false -> Icons.Outlined.Star
                            },
                            contentDescription = null,
                            modifier = Modifier
                                .padding(MaterialTheme.spacing.extraSmall)
                                .size(36.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = rememberRipple(bounded = false, radius = 30.dp),
                                    onClick = { rating = num },
                                ),
                            tint = when (num <= rating) {
                                true -> Yellow600
                                false -> Gray100
                            },
                        )
                    }
                }

                AnimatedContent(
                    targetState = rating,
                    modifier = Modifier.fillMaxWidth(),
                ) { selectedRating: Int ->
                    when (selectedRating) {
                        in 4..5 -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = MaterialTheme.spacing.medium),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                                Text(
                                    text = stringResource(R.string.thanks_for_your_rating),
                                    textAlign = TextAlign.Center,
                                )

                                Text(
                                    text = stringResource(R.string.please_leave_a_review_on_the_play_store),
                                    textAlign = TextAlign.Center,
                                )

                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                                RoundedEndsButton(
                                    onClick = {
                                        viewModel.onDismissRateAppDialog()
                                        Intent(Intent.ACTION_VIEW).apply {
                                            data =
                                                Uri.parse(context.getString(R.string.play_store_url))
                                        }.let { context.startActivity(it) }
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.StarRate,
                                        contentDescription = null,
                                    )

                                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))

                                    Text(
                                        text = stringResource(R.string.review_on_play_store),
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                }
                            }
                        }

                        in 1..3 -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = MaterialTheme.spacing.medium),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                                Text(
                                    text = stringResource(R.string.thanks_for_your_rating),
                                    textAlign = TextAlign.Center,
                                )

                                Text(
                                    text = stringResource(R.string.please_let_us_know_how_we_can_improve),
                                    textAlign = TextAlign.Center,
                                )

                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                                RoundedEndsButton(
                                    onClick = {
                                        viewModel.onDismissRateAppDialog()
                                        launchSendSuggestionIntent(context = context)
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = null,
                                    )

                                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))

                                    Text(
                                        text = stringResource(R.string.send_feedback),
                                        fontWeight = FontWeight.SemiBold,
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            }

            if (rating == 5) {
                KonfettiView(
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


        if (state.isThemeModeDialogVisible) {
            DefaultDialog(
                title = { stringResource(R.string.choose_theme) },
                onDismissRequest = { viewModel.onThemeModeDialogVisibilityChange(visible = false) },
            ) {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                ThemeMode.values().forEach { themeMode ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.onThemeModeChosen(themeMode = themeMode) }
                            .padding(horizontal = MaterialTheme.spacing.medium),
                    ) {
                        RadioButton(
                            selected = themeMode == state.currentThemeMode,
                            onClick = { viewModel.onThemeModeChosen(themeMode = themeMode) },
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                        Text(text = themeMode.readableName.asString())
                    }
                }
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            }
        }

        state.visiblePermissionDialogQueue.reversed().forEach { permission ->
            DefaultDialog(
                title = { stringResource(R.string.permission_request) },
                onDismissRequest = viewModel::onDismissPermissionDialog,
                contentPadding = PaddingValues(MaterialTheme.spacing.medium),
            ) {
                Text(
                    text = if (permission == Manifest.permission.POST_NOTIFICATIONS) stringResource(
                        R.string.rationale_word_of_the_day_notification
                    ) else stringResource(R.string.rationale_default),
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                RoundedEndsButton(
                    onClick = {
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        ).also { context.startActivity(it) }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(R.string.go_to_settings),
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colors.onSecondary,
                    )
                }
            }
        }
    }
}