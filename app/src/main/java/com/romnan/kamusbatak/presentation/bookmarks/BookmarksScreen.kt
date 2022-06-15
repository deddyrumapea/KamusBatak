package com.romnan.kamusbatak.presentation.bookmarks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.romnan.kamusbatak.domain.model.Language
import com.romnan.kamusbatak.presentation.bookmarks.component.BookmarksTabContent
import com.romnan.kamusbatak.presentation.bookmarks.component.BookmarksTopBar
import com.romnan.kamusbatak.presentation.bookmarks.model.TabItem
import com.romnan.kamusbatak.presentation.destinations.EntryDetailScreenDestination
import com.romnan.kamusbatak.presentation.util.OnLifecycleEvent
import com.romnan.kamusbatak.presentation.util.UIEvent
import com.romnan.kamusbatak.presentation.util.asString
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Destination
@Composable
fun BookmarksScreen(
    viewModel: BookmarksViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    parentScaffoldState: ScaffoldState,
) {
    val state = viewModel.state.value
    val context = LocalContext.current

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val tabItems = listOf(
        TabItem(title = Language.Btk.displayName.asString()) {
            BookmarksTabContent(
                entries = state.btkEntries,
                onItemClick = { navigator.navigate(EntryDetailScreenDestination(entryId = it.id)) }
            )
        },
        TabItem(title = Language.Ind.displayName.asString()) {
            BookmarksTabContent(
                entries = state.indEntries,
                onItemClick = { navigator.navigate(EntryDetailScreenDestination(entryId = it.id)) }
            )
        }
    )

    val pagerState = rememberPagerState(pageCount = tabItems.size)

    OnLifecycleEvent { event ->
        if (event == Lifecycle.Event.ON_RESUME) viewModel.getBookmarkedEntries()
    }

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
            BookmarksTopBar(
                onOpenDrawer = { scope.launch { parentScaffoldState.drawerState.open() } }
            )
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                backgroundColor = MaterialTheme.colors.surface,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        height = 4.dp,
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier
                            .pagerTabIndicatorOffset(pagerState, tabPositions)
                            .clip(RoundedCornerShape(topStartPercent = 100, topEndPercent = 100))
                    )
                }
            ) {
                tabItems.forEachIndexed { index: Int, tabItem: TabItem ->
                    val isSelected = pagerState.currentPage == index
                    Tab(
                        text = {
                            Text(
                                text = tabItem.title,
                                style = MaterialTheme.typography.subtitle1,
                                color =
                                if (isSelected) MaterialTheme.colors.primaryVariant
                                else Color.Unspecified,
                                fontWeight =
                                if (isSelected) FontWeight.SemiBold
                                else FontWeight.Normal,
                            )
                        },
                        selected = isSelected,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Top,
            ) { page -> tabItems[page].content.invoke() }
        }
    }
}