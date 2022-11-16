package com.romnan.kamusbatak.presentation.preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.model.ThemeMode
import com.romnan.kamusbatak.domain.util.Constants
import com.romnan.kamusbatak.domain.util.UIText
import com.romnan.kamusbatak.presentation.preferences.component.BasicPreference
import com.romnan.kamusbatak.presentation.preferences.component.PreferencesTopBar
import com.romnan.kamusbatak.presentation.preferences.component.SwitchPreference
import com.romnan.kamusbatak.presentation.preferences.component.TimePickerDialog
import com.romnan.kamusbatak.presentation.theme.spacing
import com.romnan.kamusbatak.presentation.util.UIEvent
import com.romnan.kamusbatak.presentation.util.asString
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Destination
@Composable
fun PreferencesScreen(
    viewModel: PreferencesViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    parentScaffoldState: ScaffoldState,
) {
    val state = viewModel.state.value
    val context = LocalContext.current

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

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

    Scaffold(scaffoldState = scaffoldState, topBar = {
        PreferencesTopBar(onOpenDrawer = { scope.launch { parentScaffoldState.drawerState.open() } })
    }) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(scaffoldPadding)
                .wrapContentHeight()
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
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
                    if (!dailyNotif.value.isActivated) TimePickerDialog(
                        context = context,
                        initHourOfDay = dailyNotif.value.calendar[Calendar.HOUR_OF_DAY],
                        initMinute = dailyNotif.value.calendar[Calendar.MINUTE],
                        onPicked = { hourOfDay: Int, minute: Int ->
                            viewModel.onDailyWordTimePicked(hourOfDay = hourOfDay, minute = minute)
                        },
                    ).show() else viewModel.onTurnOffDailyWord()
                },
                onCheckedChange = {
                    if (!dailyNotif.value.isActivated) TimePickerDialog(
                        context = context,
                        initHourOfDay = dailyNotif.value.calendar[Calendar.HOUR_OF_DAY],
                        initMinute = dailyNotif.value.calendar[Calendar.MINUTE],
                        onPicked = { hourOfDay: Int, minute: Int ->
                            viewModel.onDailyWordTimePicked(hourOfDay = hourOfDay, minute = minute)
                        },
                    ).show() else viewModel.onTurnOffDailyWord()
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
                        R.string.format_last_updated, when (state.localDbLastUpdatedAt) {
                            null -> UIText.StringResource(R.string.data_never_downloaded)
                            else -> {
                                val date = SimpleDateFormat(
                                    Constants.PATTERN_DATE, Locale.getDefault()
                                ).format(Date().apply {
                                    this.time = state.localDbLastUpdatedAt
                                })
                                UIText.DynamicString(date)
                            }
                        }.asString()
                    )
                ),
                onClick = { viewModel.onUpdateLocalDb() },
            )

            BasicPreference(
                imageVector = when (state.currentThemeMode) {
                    ThemeMode.System -> Icons.Default.BrightnessMedium
                    ThemeMode.Light -> Icons.Default.LightMode
                    ThemeMode.Dark -> Icons.Default.DarkMode
                },
                title = stringResource(id = R.string.theme_mode),
                description = state.currentThemeMode.readableName.asString(),
                onClick = { viewModel.onThemeModeDialogVisibilityChange(visible = true) },
            )

            if (state.isThemeModeDialogVisible) Dialog(onDismissRequest = {
                viewModel.onThemeModeDialogVisibilityChange(
                    visible = false
                )
            }) {
                Column(
                    Modifier
                        .background(
                            color = MaterialTheme.colors.surface,
                            shape = RoundedCornerShape(8.dp),
                        )
                        .padding(vertical = MaterialTheme.spacing.medium)
                ) {
                    Text(
                        text = stringResource(R.string.choose_theme),
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.spacing.medium),
                    )

                    Divider(modifier = Modifier.padding(vertical = MaterialTheme.spacing.medium))

                    ThemeMode.values().forEach { themeMode ->
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.onThemeModeChosen(themeMode = themeMode) }
                                .padding(horizontal = MaterialTheme.spacing.medium)) {
                            RadioButton(
                                selected = themeMode == state.currentThemeMode,
                                onClick = { viewModel.onThemeModeChosen(themeMode = themeMode) },
                            )
                            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                            Text(text = themeMode.readableName.asString())
                        }
                    }
                }
            }
        }
    }
}