package com.lmalecic.lovefinderzz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lmalecic.lovefinderzz.R
import com.lmalecic.lovefinderzz.formatter.toLocalizedString
import com.lmalecic.lovefinderzz.framework.datePickerMillisToLocalDate
import com.lmalecic.lovefinderzz.framework.toDatePickerMillis
import com.lmalecic.lovefinderzz.ui.ViewContext
import com.lmalecic.lovefinderzz.ui.theme.LovefinderzzTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.FormatStyle

data class ReminderDraft(
    val date: LocalDate,
    val time: LocalTime,
    val message: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderDialog(
    characterName: String,
    onDismiss: () -> Unit,
    onSave: (ReminderDraft) -> Unit,
    viewContext: ViewContext
) {
    val initialDateTime = remember {
        LocalDateTime.now()
            .plusMinutes(5)
            .withSecond(0)
            .withNano(0)
    }

    var date by remember { mutableStateOf(initialDateTime.toLocalDate()) }
    var time by remember { mutableStateOf(initialDateTime.toLocalTime()) }
    var message by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.new_reminder))
        },
        text = {
            ReminderDialogContent(
                characterName = characterName,
                date = date,
                time = time,
                message = message,
                onDateClick = { showDatePicker = true },
                onTimeClick = { showTimePicker = true },
                onMessageChange = { message = it }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        ReminderDraft(
                            date = date,
                            time = time,
                            message = message
                        )
                    )
                }
            ) {
                Text(stringResource(R.string.schedule))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = date.toDatePickerMillis())

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { date = it.datePickerMillisToLocalDate() }
                        showDatePicker = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(initialHour = time.hour, initialMinute = time.minute, is24Hour = true)

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            text = { TimeInput(state = timePickerState) },
            confirmButton = {
                TextButton(
                    onClick = {
                        time = LocalTime.of(timePickerState.hour, timePickerState.minute)
                        showTimePicker = false
                    }
                ) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showTimePicker = false }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun ReminderDialogContent(
    characterName: String,
    date: LocalDate,
    time: LocalTime,
    message: String,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
    onMessageChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.from))
            Text(
                text = characterName,
                style = MaterialTheme.typography.bodyMedium
                    .merge(fontWeight = FontWeight.Medium)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.date))

            OutlinedButton(onClick = onDateClick) {
                Text(date.toLocalizedString(FormatStyle.FULL))
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.time))

            OutlinedButton(onClick = onTimeClick) {
                Text(time.toLocalizedString(FormatStyle.SHORT))
            }
        }

        OutlinedTextField(
            value = message,
            onValueChange = onMessageChange,
            placeholder = { Text(stringResource(R.string.reminder_message_placeholder)) },
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 128.dp)
                .padding(top = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReminderDialogContentPreview() {
    LovefinderzzTheme {
        ReminderDialogContent(
            characterName = "Android",
            date = LocalDate.now(),
            time = LocalTime.now(),
            message = "",
            onDateClick = {},
            onTimeClick = {},
            onMessageChange = {}
        )
    }
}