package com.example.weatherapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.data.WeatherMainInfo

@Suppress("ktlint:standard:function-naming")
@Composable
fun Background() {
    Image(
        painter = painterResource(id = R.drawable.bg),
        contentDescription = "Background",
        modifier =
            Modifier
                .fillMaxSize(),
        contentScale = ContentScale.Crop,
    )
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun MainCard(cardInfo: MutableState<WeatherMainInfo>) {
    Column(
        modifier =
            Modifier
                .padding(5.dp),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors =
                CardDefaults.cardColors(
                    containerColor = Color.Transparent,
                ),
            elevation =
                CardDefaults.cardElevation(
                    defaultElevation = 0.dp,
                ),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 3.dp, bottom = 3.dp, start = 10.dp, end = 10.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    IconButton(
                        onClick = {},
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.plus),
                            contentDescription = "Plus",
                            modifier = Modifier.size(36.dp),
                            tint = Color.White,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Text(
                        text = cardInfo.value.date,
                        color = Color.White.copy(alpha = 0.7f),
                        style = TextStyle(fontSize = 15.sp),
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Text(
                        text = cardInfo.value.city,
                        style = TextStyle(fontSize = 42.sp),
                        color = Color.White,
                    )
                    Text(
                        text = cardInfo.value.curTemp,
                        style = TextStyle(fontSize = 108.sp),
                        color = Color.White,
                    )
                    Text(
                        text = "${cardInfo.value.condition.condition} ${cardInfo.value.maxTemp}/${cardInfo.value.minTemp}",
                        style = TextStyle(fontSize = 26.sp),
                        color = Color.White,
                    )

                    Spacer(modifier = Modifier.width(10.dp))
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Last update: ${cardInfo.value.lastUpdate}",
                            style = TextStyle(fontSize = 16.sp),
                            color = Color.White.copy(alpha = 0.5f),
                        )
                        IconButton(
                            onClick = {},
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.sync),
                                contentDescription = "Sync",
                                modifier = Modifier.size(36.dp),
                                tint = Color.White,
                            )
                        }
                    }
                }
            }
        }
    }
}
