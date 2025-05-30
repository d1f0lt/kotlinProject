package com.example.weatherapp.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue

@Suppress("ktlint:standard:function-naming")
@Composable
fun DialogSearch(
    searchDialogFlag: MutableState<Boolean>,
    onSubmit: (String) -> Unit,
) {
    var textFieldState by remember { mutableStateOf(TextFieldValue("")) }
    AlertDialog(
        onDismissRequest = {
            searchDialogFlag.value = false
        },
        confirmButton = {
            TextButton(onClick = {
                searchDialogFlag.value = false
                onSubmit(textFieldState.text)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                searchDialogFlag.value = false
            }) {
                Text("Cancel")
            }
        },
        title = {
            Text("Enter the city name:")
        },
        text = {
            TextField(
                value = textFieldState,
                onValueChange = { text ->
                    textFieldState = text
                },
            )
        },
    )
}
