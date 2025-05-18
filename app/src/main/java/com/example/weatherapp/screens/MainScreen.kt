package com.example.weatherapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R

@Preview(showBackground = true)
@Composable
fun MainScreen() {
    Image(
        painter = painterResource(id = R.drawable.bg),
        contentDescription = "Background",
        modifier = Modifier
            .fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 3.dp, bottom = 3.dp, start = 10.dp, end = 10.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(50.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.plus),
                            contentDescription = "Plus",
                            modifier = Modifier.size(36.dp),
                            tint = Color.White,
                        )
                    }
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Text(
                        text = "Moscow",
                        style = TextStyle(fontSize = 42.sp),
                        color = Color.White
                    )
                    Text(
                        text = "23°C",
                        style = TextStyle(fontSize = 108.sp),
                        color = Color.White
                    )
                    Text(
                        text = "Clear 19°/8°",
                        style = TextStyle(fontSize = 32.sp),
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.width(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Last update: 18 May 15:23",
                            style = TextStyle(fontSize = 16.sp),
                            color = Color.White
                        )
                        IconButton(
                            onClick = {}
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