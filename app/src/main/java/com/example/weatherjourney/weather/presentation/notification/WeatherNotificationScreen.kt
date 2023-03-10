package com.example.weatherjourney.weather.presentation.notification

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherjourney.R
import com.example.weatherjourney.presentation.component.BasicTopBar
import com.example.weatherjourney.presentation.component.LoadingContent
import com.example.weatherjourney.weather.presentation.notification.component.AqiNotificationItem
import com.example.weatherjourney.weather.presentation.notification.component.UvNotificationItem

@Composable
fun WeatherNotificationScreen(
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WeatherNotificationViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = { BasicTopBar(stringResource(R.string.notification), onBackClick) }
    ) { paddingValues ->

        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        WeatherNotificationScreenContent(
            uiState = uiState,
            modifier = Modifier.padding(paddingValues),
            onRefresh = viewModel::onRefresh
        )

        // Check for user messages to display on the screen
        uiState.userMessage?.let { userMessage ->
            val snackbarText = userMessage.message.asString()
            LaunchedEffect(snackbarHostState, viewModel, userMessage, snackbarText) {
                snackbarHostState.showSnackbar(snackbarText)
                viewModel.onSnackbarMessageShown()
            }
        }
    }
}

@Composable
fun WeatherNotificationScreenContent(
    uiState: WeatherNotificationUiState,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val screenPadding = PaddingValues(
        vertical = dimensionResource(R.dimen.vertical_margin),
        horizontal = 16.dp
    )

    LoadingContent(isLoading = uiState.isLoading, onRefresh = onRefresh, modifier) {
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(screenPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            uiState.notifications?.uvNotification?.let {
                item {
                    UvNotificationItem(
                        title = stringResource(R.string.uv_notification),
                        firstTimeLine = it.firstTimeLine.asString(),
                        secondTimeLine = it.secondTimeLine.asString(),
                        info = stringResource(it.infoRes),
                        adviceRes = stringResource(it.adviceRes)
                    )
                }
            }
            uiState.notifications?.aqiNotification?.let {
                item {
                    AqiNotificationItem(
                        firstTimeLine = it.firstTimeLine.asString(),
                        secondTimeLine = it.secondTimeLine.asString(),
                        info = stringResource(it.infoRes),
                        adviceRes1 = stringResource(it.adviceRes),
                        adviceRes2 = it.adviceRes2.asString()
                    )
                }
            }
        }
    }
}
