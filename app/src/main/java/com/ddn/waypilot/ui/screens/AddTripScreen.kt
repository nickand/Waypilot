package com.ddn.waypilot.ui.screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ddn.waypilot.data.Destination
import com.ddn.waypilot.data.TripStyle
import com.ddn.waypilot.ui.screens.components.NumberStepper
import com.ddn.waypilot.ui.theme.components.TripCoverPickerPreview
import com.ddn.waypilot.ui.trips.AddTripViewModel
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
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
    var destinations by remember { mutableStateOf<List<Destination>>(emptyList()) }
    var start by remember { mutableStateOf(LocalDate.now().plusDays(7)) }
    var end by remember { mutableStateOf(start.plusDays(4)) }
    var travelers by remember { mutableStateOf(2) }
    var style by remember { mutableStateOf(TripStyle.SOLO) }
    var currency by remember { mutableStateOf("USD") }
    var budget by remember { mutableStateOf(0.0) }
    var coverUri by remember { mutableStateOf<String?>(null) }

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> coverUri = uri?.toString() }

    var styleMenuExpanded by remember { mutableStateOf(false) }
    val tripStyleOptions = TripStyle.entries.toTypedArray()

    val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    var showStartDateDialog by remember { mutableStateOf(false) }
    val startDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = start.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    )

    var showEndDateDialog by remember { mutableStateOf(false) }
    val endDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = end.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    )

    // === Autocomplete launcher ===
    val context = LocalContext.current
    val placeLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data = result.data
        if (result.resultCode == Activity.RESULT_OK && data != null) {
            val place = Autocomplete.getPlaceFromIntent(data)
            val d = Destination(
                placeId = place.id ?: "",
                name = place.name ?: "",
                address = place.address,
                lat = place.latLng?.latitude,
                lng = place.latLng?.longitude
            )
            destinations = destinations + d
        } else if (data != null) {
            // status opcional
            // val status = Autocomplete.getStatusFromIntent(data)
        }
    }

    fun openPlaces() {
        val fields = listOf(
            Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG
        )
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields).build(context)
        placeLauncher.launch(intent)
    }

    if (showStartDateDialog) {
        DatePickerDialog(
            onDismissRequest = { showStartDateDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    startDatePickerState.selectedDateMillis?.let {
                        start = Instant.ofEpochMilli(it).atZone(ZoneOffset.UTC).toLocalDate()
                    }
                    showStartDateDialog = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showStartDateDialog = false }) { Text("Cancel") } }
        ) { DatePicker(state = startDatePickerState) }
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
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showEndDateDialog = false }) { Text("Cancel") } }
        ) { DatePicker(state = endDatePickerState) }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Add Trip") }) },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Button(
                    onClick = {
                        vm.addBasicTrip(
                            title = title,
                            destinations = destinations,
                            start = start,
                            end = end,
                            travelers = travelers,
                            style = style,
                            budgetCurrency = currency,
                            budgetTotal = budget,
                            coverImageUrl = coverUri,
                            onDone = onDone
                        )
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

            // ===== NUEVO: Destinations picker =====
            item {
                DestinationsPicker(
                    destinations = destinations,
                    onAdd = { openPlaces() },
                    onRemove = { idx ->
                        destinations = destinations.toMutableList().also { it.removeAt(idx) }
                    },
                    onMoveUp = { idx ->
                        if (idx > 0) destinations = destinations.toMutableList().also {
                            val tmp = it[idx - 1]; it[idx - 1] = it[idx]; it[idx] = tmp
                        }
                    },
                    onMoveDown = { idx ->
                        if (idx < destinations.lastIndex) destinations = destinations.toMutableList().also {
                            val tmp = it[idx + 1]; it[idx + 1] = it[idx]; it[idx] = tmp
                        }
                    }
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
                                if (up != null) showStartDateDialog = true
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
                                if (up != null) showEndDateDialog = true
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
                var styleMenuExpanded by remember { mutableStateOf(false) }
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

@Composable
private fun DestinationsPicker(
    destinations: List<Destination>,
    onAdd: () -> Unit,
    onRemove: (Int) -> Unit,
    onMoveUp: (Int) -> Unit,
    onMoveDown: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Destinations", style = MaterialTheme.typography.titleMedium)
        destinations.forEachIndexed { index, d ->
            ElevatedCard {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("${index + 1}. ${d.name}", style = MaterialTheme.typography.titleSmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        d.address?.let {
                            Text(it, style = MaterialTheme.typography.bodySmall, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                    Row {
                        IconButton(onClick = { onMoveUp(index) }) { Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Up") }
                        IconButton(onClick = { onMoveDown(index) }) { Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Down") }
                        IconButton(onClick = { onRemove(index) }) { Icon(Icons.Filled.Delete, contentDescription = "Remove") }
                    }
                }
            }
        }
        Button(onClick = onAdd, modifier = Modifier.fillMaxWidth()) {
            Text(if (destinations.isEmpty()) "Add first destination" else "Add another stop")
        }
    }
}
