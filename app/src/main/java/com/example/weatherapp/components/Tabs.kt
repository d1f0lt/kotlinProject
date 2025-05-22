package com.example.weatherapp.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapp.data.ResponseStorage
import com.example.weatherapp.data.WeatherCardInfo
import com.example.weatherapp.data.WeatherCardInfoPerDay
import com.example.weatherapp.data.WeatherMainInfo
import com.example.weatherapp.data.getMainScreenInfo
import kotlinx.coroutines.launch

// @Preview
@RequiresApi(Build.VERSION_CODES.O)
@Suppress("ktlint:standard:function-naming")
@Composable
fun Tabs(
    mainCardInfo: MutableState<WeatherMainInfo>,
    daysList: List<WeatherCardInfo>,
) {
    val tabList = listOf<String>("Days", "Hours")
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { tabList.size })
    val coroutineScope = rememberCoroutineScope()
    val hoursList = mainCardInfo.value.hours

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                )
            },
            containerColor = Color.Transparent,
            contentColor = Color.White,
        ) {
            tabList.forEachIndexed { index, tab ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(text = tab)
                    },
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1.0f),
        ) { pageIndex ->
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (tabList[pageIndex] == "Days") {
                    items(daysList.size) { cardIndex ->
                        ListItem(mainCardInfo, daysList[cardIndex], cardIndex)
                    }
                } else if (tabList[pageIndex] == "Hours") {
                    items(hoursList.size) { cardIndex ->
                        ListItem(mainCardInfo, hoursList[cardIndex], cardIndex)
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Suppress("ktlint:standard:function-naming")
@Composable
fun ListItem(
    mainCardInfo: MutableState<WeatherMainInfo>,
    itemInfo: WeatherCardInfo,
    currentCardIndex: Int,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 3.dp)
                .clickable {
                    if (itemInfo is WeatherCardInfoPerDay) {
                        ResponseStorage.doWithResponse { json ->
                            mainCardInfo.value = getMainScreenInfo(json, currentCardIndex)
                        }
                    }
                }.drawBehind {
                    val borderWidth = 1.dp.toPx()
                    drawLine(
                        color = Color.Gray,
                        start = Offset(0f, size.height - borderWidth / 2),
                        end = Offset(size.width, size.height - borderWidth / 2),
                        strokeWidth = borderWidth,
                    )
                },
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 0.dp,
            ),
        colors =
            CardDefaults.cardColors(
                containerColor = Color.Transparent,
            ),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 3.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val textStyle = TextStyle(fontSize = 20.sp)
            Text(
                text = itemInfo.date,
                color = Color.White,
                style = textStyle,
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AsyncImage(
                    model = "https:${itemInfo.condition.imageUrl}",
                    contentDescription = "weather picture",
                    modifier = Modifier.size(32.dp),
                )
                Text(
                    text = itemInfo.condition.condition,
                    color = Color.White.copy(alpha = 0.6f),
                    style = TextStyle(fontSize = textStyle.fontSize * 0.7),
                )
            }
            Text(
                text = itemInfo.temperature,
                color = Color.White,
                style = textStyle,
            )
        }
    }
}
