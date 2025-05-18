package com.example.weatherapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.components.Background
import com.example.weatherapp.components.MainCard
import com.example.weatherapp.components.Tabs

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun MainScreen() {
    Background()
    Column(
        verticalArrangement = Arrangement.spacedBy(40.dp),
    ) {
        MainCard()
        Tabs()
    }
}
