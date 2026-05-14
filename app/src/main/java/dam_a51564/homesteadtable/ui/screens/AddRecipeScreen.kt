package dam_a51564.homesteadtable.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dam_a51564.homesteadtable.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(addRecipeViewModel: AddRecipeViewModel, onNavigateBack: () -> Unit
) {
    val addRecipeUIState by addRecipeViewModel.uiState.collectAsState()
    val unitOptions = listOf("g", "kg", "ml", "L", "tbsp", "tsp", "cup", "unit")

    Scaffold(
        containerColor = Cream,
        topBar = {
            TopAppBar(
                title = { Text("New Recipe", style = MaterialTheme.typography.headlineMedium) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Cream)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Recipe Title
            item {
                Text("Recipe information", style = MaterialTheme.typography.titleMedium, color = WarmTan)

                OutlinedTextField(
                    value = addRecipeUIState.title,
                    onValueChange = addRecipeViewModel::onTitleChange,
                    label = { Text("Recipe Title") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = White,
                        unfocusedContainerColor = White
                    )
                )
            }

            // Ingredients Section
            item {
                Text("Ingredients", style = MaterialTheme.typography.titleMedium, color = WarmTan)
            }

            itemsIndexed(addRecipeUIState.ingredients) { index, ingredient ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Cream),
                    border = BorderStroke(1.dp, ParchmentBorder)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Row 1: Name + Delete
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = ingredient.name,
                                onValueChange = { addRecipeViewModel.updateIngredient(index, ingredient.copy(name = it)) },
                                label = { Text("Ingredient Name") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = White, unfocusedContainerColor = White)
                            )
                            IconButton(onClick = { addRecipeViewModel.removeIngredient(index) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove", tint = BurntRed)
                            }
                        }
                        // Row 2: Quantity + Unit Dropdown
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = ingredient.quantity,
                                onValueChange = { addRecipeViewModel.updateIngredient(index, ingredient.copy(quantity = it)) },
                                label = { Text("Qty") },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = White, unfocusedContainerColor = White)
                            )

                            var expanded by remember { mutableStateOf(false) }
                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = !expanded },
                                modifier = Modifier.weight(1.2f)
                            ) {
                                OutlinedTextField(
                                    value = ingredient.unit,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Unit") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                    modifier = Modifier.menuAnchor(),
                                    colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = White, unfocusedContainerColor = White)
                                )
                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier.background(White)
                                ) {
                                    unitOptions.forEach { option ->
                                        DropdownMenuItem(
                                            text = { Text(text = option, color = Espresso) },
                                            onClick = {
                                                addRecipeViewModel.updateIngredient(index, ingredient.copy(unit = option))
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                TextButton(onClick = addRecipeViewModel::addIngredient, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Terracotta)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add new ingredient", color = Terracotta, style = MaterialTheme.typography.labelLarge)
                }
            }

            // Equipment Section
            item {
                Text("Equipment", style = MaterialTheme.typography.titleMedium, color = WarmTan)
            }

            itemsIndexed(addRecipeUIState.equipment) { index, item ->
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = item,
                        onValueChange = { addRecipeViewModel.updateEquipment(index, it) },
                        label = { Text("Equipment Name") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = White, unfocusedContainerColor = White)
                    )
                    IconButton(onClick = { addRecipeViewModel.removeEquipment(index) }) {
                        Icon(Icons.Default.Delete, tint = BurntRed, contentDescription = "Remove")
                    }
                }
            }

            item {
                TextButton(onClick = addRecipeViewModel::addEquipment, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Terracotta)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add new equipment", color = Terracotta, style = MaterialTheme.typography.labelLarge)
                }
            }

            // Instructions Section
            item {
                Text("Instructions", style = MaterialTheme.typography.titleMedium, color = WarmTan)
            }

            itemsIndexed(addRecipeUIState.steps) { index, step ->
                Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = step,
                        onValueChange = { addRecipeViewModel.updateStep(index, it) },
                        label = { Text("Step ${index + 1}") },
                        modifier = Modifier.weight(1f).heightIn(min = 100.dp),
                        maxLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = White, unfocusedContainerColor = White)
                    )
                    IconButton(onClick = { addRecipeViewModel.removeStep(index) }, modifier = Modifier.padding(top = 8.dp)) {
                        Icon(Icons.Default.Delete, tint = BurntRed, contentDescription = "Remove")
                    }
                }
            }

            item {
                TextButton(onClick = addRecipeViewModel::addStep, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Terracotta)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add new step", color = Terracotta, style = MaterialTheme.typography.labelLarge)
                }
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { /* TODO: Save logic */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save recipe", style = MaterialTheme.typography.labelLarge)
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}