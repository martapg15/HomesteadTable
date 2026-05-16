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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dam_a51564.homesteadtable.R
import dam_a51564.homesteadtable.model.RecipeUnits
import dam_a51564.homesteadtable.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeScreen(
    addRecipeViewModel: AddRecipeViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by addRecipeViewModel.uiState.collectAsState()
    val recipe = uiState.recipe

    val unitOptions = RecipeUnits.list

    // Navigate back when recipe is saved successfully
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.new_recipe), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go Back", tint = Espresso)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Cream)
            )
        },
        containerColor = Cream
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // Basic Info Section
            item {
                Text(stringResource(R.string.basic_information_section), style = MaterialTheme.typography.titleMedium, color = WarmTan)
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = recipe.title,
                    onValueChange = { addRecipeViewModel.onTitleChange(it) },
                    label = { Text(stringResource(R.string.recipe_title)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = White, unfocusedContainerColor = White)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Category Dropdown
                ExposedDropdownMenuBox(
                    expanded = uiState.isCategoryExpanded,
                    onExpandedChange = { addRecipeViewModel.onCategoryExpandedChange(it) }
                ) {
                    OutlinedTextField(
                        value = recipe.category,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.category)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.isCategoryExpanded) },
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = White, unfocusedContainerColor = White)
                    )
                    ExposedDropdownMenu(
                        expanded = uiState.isCategoryExpanded,
                        onDismissRequest = { addRecipeViewModel.onCategoryExpandedChange(false) },
                        modifier = Modifier.background(White)
                    ) {
                        uiState.categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(text = category, color = Espresso) },
                                onClick = { addRecipeViewModel.onCategorySelect(category) }
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
                    Text(stringResource(R.string.base_portions), style = MaterialTheme.typography.labelLarge, color = Espresso)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        FilledIconButton(
                            onClick = { if (recipe.baseServings > 1) addRecipeViewModel.onServingsChange(recipe.baseServings - 1) },
                            modifier = Modifier.size(36.dp),
                            shape = CircleShape,
                            colors = IconButtonDefaults.filledIconButtonColors(containerColor = White, contentColor = Terracotta)
                        ) { Icon(Icons.Default.Remove, "Decrease", Modifier.size(18.dp)) }

                        Text("${recipe.baseServings}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Terracotta)

                        FilledIconButton(
                            onClick = { addRecipeViewModel.onServingsChange(recipe.baseServings + 1) },
                            modifier = Modifier.size(36.dp),
                            shape = CircleShape,
                            colors = IconButtonDefaults.filledIconButtonColors(containerColor = White, contentColor = Terracotta)
                        ) { Icon(Icons.Default.Add, "Increase", Modifier.size(18.dp)) }
                    }
                }
            }

            // Ingredients Section
            item {
                HorizontalDivider(color = ParchmentBorder, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.ingredients), style = MaterialTheme.typography.titleMedium, color = WarmTan)
            }

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
                                onValueChange = { addRecipeViewModel.updateIngredient(index, ingredient.copy(name = it)) },
                                label = { Text(stringResource(R.string.ingredient_name)) },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = White, unfocusedContainerColor = White)
                            )
                            IconButton(onClick = { addRecipeViewModel.removeIngredient(index) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove", tint = BurntRed)
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = ingredient.quantity,
                                onValueChange = { addRecipeViewModel.updateIngredient(index, ingredient.copy(quantity = it)) },
                                label = { Text(stringResource(R.string.quantity)) },
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
                                    label = { Text(stringResource(R.string.unit)) },
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
                TextButton(onClick = { addRecipeViewModel.addIngredient() }, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Add, null, tint = Terracotta)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.btn_add_new_ingredient), color = Terracotta, style = MaterialTheme.typography.labelLarge)
                }
            }

            // Equipment Section
            item {
                HorizontalDivider(color = ParchmentBorder, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.equipment), style = MaterialTheme.typography.titleMedium, color = WarmTan)
            }

            itemsIndexed(recipe.equipment) { index, item ->
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = item,
                        onValueChange = { addRecipeViewModel.updateEquipment(index, it) },
                        label = { Text(stringResource(R.string.equipment_name)) },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = White, unfocusedContainerColor = White)
                    )
                    IconButton(onClick = { addRecipeViewModel.removeEquipment(index) }) {
                        Icon(Icons.Default.Delete, tint = BurntRed, contentDescription = "Remove")
                    }
                }
            }

            item {
                TextButton(onClick = { addRecipeViewModel.addEquipment() }, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Add, null, tint = Terracotta)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.btn_add_new_equipment), color = Terracotta, style = MaterialTheme.typography.labelLarge)
                }
            }

            // Instructions Section
            item {
                HorizontalDivider(color = ParchmentBorder, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(stringResource(R.string.instructions), style = MaterialTheme.typography.titleMedium, color = WarmTan)
            }

            itemsIndexed(recipe.instructions) { index, step ->
                Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = step,
                        onValueChange = { addRecipeViewModel.updateInstruction(index, it) },
                        label = { Text(stringResource(R.string.recipe_step, index + 1)) },
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 100.dp),
                        maxLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = White, unfocusedContainerColor = White)
                    )
                    IconButton(onClick = { addRecipeViewModel.removeInstruction(index) }, modifier = Modifier.padding(top = 8.dp)) {
                        Icon(Icons.Default.Delete, tint = BurntRed, contentDescription = "Remove")
                    }
                }
            }

            item {
                TextButton(onClick = { addRecipeViewModel.addInstruction() }, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Add, null, tint = Terracotta)
                    Spacer(Modifier.width(8.dp))
                    Text(stringResource(R.string.btn_add_new_step), color = Terracotta, style = MaterialTheme.typography.labelLarge)
                }
            }

            // Save Button and Error
            item {
                if (uiState.errorMessage != null) {
                    Text(text = uiState.errorMessage!!, color = BurntRed, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(bottom = 8.dp))
                }

                Button(
                    onClick = { addRecipeViewModel.onSaveRecipe() },
                    enabled = !uiState.isSaving,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Terracotta)
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(color = Cream, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    } else {
                        Text(stringResource(R.string.btn_save_recipe), style = MaterialTheme.typography.labelLarge)
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}