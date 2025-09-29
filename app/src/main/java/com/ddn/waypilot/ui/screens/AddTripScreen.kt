package com.ddn.waypilot.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ddn.waypilot.data.TripStyle
import com.ddn.waypilot.ui.screens.components.NumberStepper
import com.ddn.waypilot.ui.theme.components.TripCoverPickerPreview
import com.ddn.waypilot.ui.trips.AddTripViewModel
import java.time.LocalDate

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
    val tripStyleOptions = TripStyle.values()

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
                bottom = 16.dp + 80.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp) // Ajustado para mejor espaciado
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
                    value = start.toString(),
                    onValueChange = {},
                    label = { Text("Start (YYYY-MM-DD)") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = end.toString(),
                    onValueChange = {},
                    label = { Text("End (YYYY-MM-DD)") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
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
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = styleMenuExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
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
