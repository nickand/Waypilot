package com.ddn.waypilot.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ddn.waypilot.data.TripStyle
import com.ddn.waypilot.ui.screens.components.NumberStepper
import com.ddn.waypilot.ui.theme.components.TripCoverPickerPreview
import com.ddn.waypilot.ui.trips.AddTripViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTripScreen(
    onDone: () -> Unit,
    vm: AddTripViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var start by remember { mutableStateOf(LocalDate.now().plusDays(7)) }
    var end by remember { mutableStateOf(start.plusDays(4)) }
    var travelers by remember { mutableStateOf(2) }
    var style by remember { mutableStateOf(TripStyle.SOLO) }
    var currency by remember { mutableStateOf("USD") }
    var budget by remember { mutableStateOf(0.0) }
    var coverUri by remember { mutableStateOf<String?>(null) }
    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        coverUri = uri?.toString()
    }

    var styleMenuExpanded by remember { mutableStateOf(false) }
    val tripStyleOptions = TripStyle.entries.toTypedArray()

    val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE // Or your preferred format

    var showStartDateDialog by remember { mutableStateOf(false) }
    val startDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = start.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    )

    var showEndDateDialog by remember { mutableStateOf(false) }
    val endDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = end.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    )

    if (showStartDateDialog) {
        DatePickerDialog(
            onDismissRequest = { showStartDateDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    startDatePickerState.selectedDateMillis?.let {
                        start = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate()
                    }
                    showStartDateDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartDateDialog = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = startDatePickerState)
        }
    }

    if (showEndDateDialog) {
        DatePickerDialog(
            onDismissRequest = { showEndDateDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    endDatePickerState.selectedDateMillis?.let {
                        end = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate()
                    }
                    showEndDateDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndDateDialog = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = endDatePickerState)
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Add Trip") }) },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Button(
                    onClick = {
                        vm.addBasicTrip(
                            title, city, country, start, end, travelers,
                            style, currency, budget, coverUri
                        ) { onDone() }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) { Text("Save") }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = innerPadding.calculateTopPadding() + 16.dp,
                bottom = 16.dp + 80.dp // Ensure space for the button
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                TripCoverPickerPreview(
                    cover = coverUri,
                    onPick = { pickImage.launch("image/*") }
                )
            }
            item {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("City") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = country,
                    onValueChange = { country = it },
                    label = { Text("Country") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = start.format(dateFormatter),
                    onValueChange = {},
                    label = { Text("Start Date") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            awaitEachGesture {
                                awaitFirstDown(pass = PointerEventPass.Initial)
                                val up = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                if (up != null) {
                                    showStartDateDialog = true
                                }
                            }
                        },
                    trailingIcon = { Icon(Icons.Filled.DateRange, "Select start date") }
                )
            }
            item {
                OutlinedTextField(
                    value = end.format(dateFormatter),
                    onValueChange = {},
                    label = { Text("End Date") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            awaitEachGesture {
                                awaitFirstDown(pass = PointerEventPass.Initial)
                                val up = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                if (up != null) {
                                    showEndDateDialog = true
                                }
                            }
                        },
                    trailingIcon = { Icon(Icons.Filled.DateRange, "Select end date") }
                )
            }

            item {
                NumberStepper(
                    value = travelers,
                    onValueChange = { travelers = it },
                    label = "Travelers",
                    minValue = 1
                )
            }

            item {
                ExposedDropdownMenuBox(
                    expanded = styleMenuExpanded,
                    onExpandedChange = { styleMenuExpanded = !styleMenuExpanded }
                ) {
                    OutlinedTextField(
                        value = style.name,
                        onValueChange = {},
                        label = { Text("Style") },
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = styleMenuExpanded) }
                    )
                    ExposedDropdownMenu(
                        expanded = styleMenuExpanded,
                        onDismissRequest = { styleMenuExpanded = false }
                    ) {
                        tripStyleOptions.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption.name) },
                                onClick = {
                                    style = selectionOption
                                    styleMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = currency,
                        onValueChange = { currency = it },
                        label = { Text("Currency") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = budget.toString(),
                        onValueChange = { v -> v.toDoubleOrNull()?.let { budget = it } },
                        label = { Text("Budget") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
