package dam_a51564.homesteadtable.ui.screens.recipe_management

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dam_a51564.homesteadtable.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecipeScreen(
    editRecipeViewModel: EditRecipeViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by editRecipeViewModel.uiState.collectAsState()
    val recipe = uiState.recipe
    val unitOptions = listOf("g", "kg", "ml", "L", "tbsp", "tsp", "cup", "unit")

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onNavigateBack()
        }
    }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Terracotta)
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Edit Recipe", fontWeight = FontWeight.Bold, color = Espresso) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Espresso)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Cream)
                )
            },
            containerColor = Cream
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Basic Info Section
                item {
                    Text("Basic Information", style = MaterialTheme.typography.titleMedium, color = WarmTan)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = recipe.title,
                        onValueChange = { editRecipeViewModel.onTitleChange(it) },
                        label = { Text("Recipe Title") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = White, unfocusedContainerColor = White)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Category Dropdown
                    ExposedDropdownMenuBox(
                        expanded = uiState.isCategoryExpanded,
                        onExpandedChange = { editRecipeViewModel.onCategoryExpandedChange(it) }
                    ) {
                        OutlinedTextField(
                            value = recipe.category,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            modifier = Modifier.fillMaxWidth().menuAnchor(),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.isCategoryExpanded) },
                            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = White, unfocusedContainerColor = White)
                        )
                        ExposedDropdownMenu(
                            expanded = uiState.isCategoryExpanded,
                            onDismissRequest = { editRecipeViewModel.onCategoryExpandedChange(false) },
                            modifier = Modifier.background(White)
                        ) {
                            uiState.categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(text = category, color = Espresso) },
                                    onClick = { editRecipeViewModel.onCategorySelect(category) }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Portions / Base Servings Input
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Base Portions (Servings)", style = MaterialTheme.typography.labelLarge, color = Espresso)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            FilledIconButton(
                                onClick = { if (recipe.baseServings > 1) editRecipeViewModel.onServingsChange(recipe.baseServings - 1) },
                                modifier = Modifier.size(36.dp),
                                shape = CircleShape,
                                colors = IconButtonDefaults.filledIconButtonColors(containerColor = White, contentColor = Terracotta)
                            ) { Icon(Icons.Default.Remove, "Decrease", Modifier.size(18.dp)) }

                            Text("${recipe.baseServings}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Terracotta)

                            FilledIconButton(
                                onClick = { editRecipeViewModel.onServingsChange(recipe.baseServings + 1) },
                                modifier = Modifier.size(36.dp),
                                shape = CircleShape,
                                colors = IconButtonDefaults.filledIconButtonColors(containerColor = White, contentColor = Terracotta)
                            ) { Icon(Icons.Default.Add, "Increase", Modifier.size(18.dp)) }
                        }
                    }
                }

                // Ingredients Section
                item { Text("Ingredients", style = MaterialTheme.typography.titleMedium, color = WarmTan) }

                itemsIndexed(recipe.ingredients) { index, ingredient ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Cream),
                        border = BorderStroke(1.dp, ParchmentBorder)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OutlinedTextField(
                                    value = ingredient.name,
                                    onValueChange = { editRecipeViewModel.updateIngredient(index, ingredient.copy(name = it)) },
                                    label = { Text("Ingredient Name") },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = White, unfocusedContainerColor = White)
                                )
                                IconButton(onClick = { editRecipeViewModel.removeIngredient(index) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove", tint = BurntRed)
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = ingredient.quantity,
                                    onValueChange = { editRecipeViewModel.updateIngredient(index, ingredient.copy(quantity = it)) },
                                    label = { Text("Quantity") },
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
                                                    editRecipeViewModel.updateIngredient(index, ingredient.copy(unit = option))
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
                    TextButton(onClick = { editRecipeViewModel.addIngredient() }, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.Add, null, tint = Terracotta)
                        Spacer(Modifier.width(8.dp))
                        Text("Add new ingredient", color = Terracotta, style = MaterialTheme.typography.labelLarge)
                    }
                }

                // Equipment Section
                item { Text("Equipment", style = MaterialTheme.typography.titleMedium, color = WarmTan) }

                itemsIndexed(recipe.equipment) { index, item ->
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = item,
                            onValueChange = { editRecipeViewModel.updateEquipment(index, it) },
                            label = { Text("Equipment Name") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = White, unfocusedContainerColor = White)
                        )
                        IconButton(onClick = { editRecipeViewModel.removeEquipment(index) }) {
                            Icon(Icons.Default.Delete, tint = BurntRed, contentDescription = "Remove")
                        }
                    }
                }

                item {
                    TextButton(onClick = { editRecipeViewModel.addEquipment() }, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.Add, null, tint = Terracotta)
                        Spacer(Modifier.width(8.dp))
                        Text("Add new equipment", color = Terracotta, style = MaterialTheme.typography.labelLarge)
                    }
                }

                // Instructions Section
                item { Text("Instructions", style = MaterialTheme.typography.titleMedium, color = WarmTan) }

                itemsIndexed(recipe.instructions) { index, step ->
                    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = step,
                            onValueChange = { editRecipeViewModel.updateInstruction(index, it) },
                            label = { Text("Step ${index + 1}") },
                            modifier = Modifier.weight(1f).heightIn(min = 100.dp),
                            maxLines = 5,
                            colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = White, unfocusedContainerColor = White)
                        )
                        IconButton(onClick = { editRecipeViewModel.removeInstruction(index) }, modifier = Modifier.padding(top = 8.dp)) {
                            Icon(Icons.Default.Delete, tint = BurntRed, contentDescription = "Remove")
                        }
                    }
                }

                item {
                    TextButton(onClick = { editRecipeViewModel.addInstruction() }, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.Add, null, tint = Terracotta)
                        Spacer(Modifier.width(8.dp))
                        Text("Add new step", color = Terracotta, style = MaterialTheme.typography.labelLarge)
                    }
                }

                // Save Action
                item {
                    if (uiState.errorMessage != null) {
                        Text(text = uiState.errorMessage!!, color = BurntRed, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(bottom = 8.dp))
                    }
                    Button(
                        onClick = { editRecipeViewModel.onUpdateRecipe() },
                        enabled = !uiState.isSaving,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Terracotta)
                    ) {
                        if (uiState.isSaving) {
                            CircularProgressIndicator(color = Cream, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                        } else {
                            Text("SAVE CHANGES", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}