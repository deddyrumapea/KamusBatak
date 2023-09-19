package com.romnan.kamusbatak.presentation.preferences

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.PermissionChecker
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.romnan.kamusbatak.R
import com.romnan.kamusbatak.domain.model.ThemeMode
import com.romnan.kamusbatak.domain.util.Constants
import com.romnan.kamusbatak.domain.util.UIText
import com.romnan.kamusbatak.presentation.components.DefaultDialog
import com.romnan.kamusbatak.presentation.components.RoundedEndsButton
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
import java.util.Calendar
import java.util.Date
import java.util.Locale

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

            BasicPreference(
                imageVector = Icons.Default.Info,
                title = stringResource(R.string.app_version),
                description = state.appVersion,
                onClick = { viewModel.onClickAppVersion() },
            )

            if (state.isThemeModeDialogVisible) Dialog(
                onDismissRequest = { viewModel.onThemeModeDialogVisibilityChange(visible = false) },
            ) {
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