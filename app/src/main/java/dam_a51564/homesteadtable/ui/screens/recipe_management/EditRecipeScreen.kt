package dam_a51564.homesteadtable.ui.screens.recipe_management

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dam_a51564.homesteadtable.R
import dam_a51564.homesteadtable.model.RecipeUnits
import dam_a51564.homesteadtable.ui.theme.*

/**
 * Displays the screen for editing an existing recipe.
 *
 * Pre-populates the form fields with the current recipe's data and allows the user to modify the photo,
 * title, category, base servings, ingredients, equipment, and step-by-step instructions.
 *
 * @param editRecipeViewModel The [EditRecipeViewModel] managing the pre-filled form state, media selection, and update actions.
 * @param onNavigateBack Callback invoked to return to the previous screen or triggered automatically upon a successful update.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecipeScreen(
    editRecipeViewModel: EditRecipeViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by editRecipeViewModel.uiState.collectAsState()
    val recipe = uiState.recipe

    val unitOptions = RecipeUnits.list

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onNavigateBack()
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                editRecipeViewModel.onImageSelected(uri)
            }
        }
    )

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Terracotta)
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.edit_recipe), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Image Section
                item {
                    // Decide which image to show: The newly picked one, or the existing one from Firebase
                    val imageToShow = uiState.imageUri ?: recipe.imageUrl

                    if (imageToShow.toString().isNotBlank()) {
                        AsyncImage(
                            model = imageToShow,
                            contentDescription = "Recipe Photo",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    OutlinedButton(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Terracotta),
                        border = BorderStroke(1.dp, Terracotta)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.change_photo))
                    }
                }

                // Basic Info Section
                item {
                    Text(stringResource(R.string.basic_information_section), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = recipe.title,
                        onValueChange = { editRecipeViewModel.onTitleChange(it) },
                        label = { Text(stringResource(R.string.recipe_title), color = MaterialTheme.colorScheme.onSurfaceVariant) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedTextColor = MaterialTheme.colorScheme.onBackground,
                            unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                        )
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
                            label = { Text(stringResource(R.string.category), color = MaterialTheme.colorScheme.onSurfaceVariant) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.isCategoryExpanded) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = uiState.isCategoryExpanded,
                            onDismissRequest = { editRecipeViewModel.onCategoryExpandedChange(false) },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                        ) {
                            uiState.categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(text = category, color = MaterialTheme.colorScheme.onBackground) },
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
                        Text(stringResource(R.string.base_portions), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onBackground)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            FilledIconButton(
                                onClick = { if (recipe.baseServings > 1) editRecipeViewModel.onServingsChange(recipe.baseServings - 1) },
                                modifier = Modifier.size(36.dp),
                                shape = CircleShape,
                                colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.surface, contentColor = Terracotta)
                            ) { Icon(Icons.Default.Remove, "Decrease", Modifier.size(18.dp)) }

                            Text("${recipe.baseServings}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Terracotta)

                            FilledIconButton(
                                onClick = { editRecipeViewModel.onServingsChange(recipe.baseServings + 1) },
                                modifier = Modifier.size(36.dp),
                                shape = CircleShape,
                                colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.surface, contentColor = Terracotta)
                            ) { Icon(Icons.Default.Add, "Increase", Modifier.size(18.dp)) }
                        }
                    }
                }

                // Ingredients Section
                item {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(stringResource(R.string.ingredients), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                itemsIndexed(recipe.ingredients) { index, ingredient ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OutlinedTextField(
                                    value = ingredient.name,
                                    onValueChange = { editRecipeViewModel.updateIngredient(index, ingredient.copy(name = it)) },
                                    label = { Text(stringResource(R.string.ingredient_name), color = MaterialTheme.colorScheme.onSurfaceVariant) },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                                    )
                                )
                                IconButton(onClick = { editRecipeViewModel.removeIngredient(index) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Remove", tint = BurntRed)
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = ingredient.quantity,
                                    onValueChange = { editRecipeViewModel.updateIngredient(index, ingredient.copy(quantity = it)) },
                                    label = { Text(stringResource(R.string.quantity), color = MaterialTheme.colorScheme.onSurfaceVariant) },
                                    modifier = Modifier.weight(1f),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                                    )
                                )

                                var expanded by remember { mutableStateOf(false) }
                                ExposedDropdownMenuBox(
                                    expanded = expanded,
                                    onExpandedChange = { expanded = !expanded },
                                    modifier = Modifier.weight(1.2f)
                                ) {
                                    OutlinedTextField(
                                        value = ingredient.displayUnit,
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text(stringResource(R.string.unit), color = MaterialTheme.colorScheme.onSurfaceVariant) },
                                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                        modifier = Modifier.menuAnchor(),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                            focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                            unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                                        )
                                    )
                                    ExposedDropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false },
                                        modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                                    ) {
                                        unitOptions.forEach { option ->
                                            DropdownMenuItem(
                                                text = { Text(text = option, color = MaterialTheme.colorScheme.onBackground) },
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
                        Text(stringResource(R.string.btn_add_new_ingredient), color = Terracotta, style = MaterialTheme.typography.labelLarge)
                    }
                }

                // Equipment Section
                item {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(stringResource(R.string.equipment), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                itemsIndexed(recipe.equipment) { index, item ->
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = item,
                            onValueChange = { editRecipeViewModel.updateEquipment(index, it) },
                            label = { Text(stringResource(R.string.equipment_name), color = MaterialTheme.colorScheme.onSurfaceVariant) },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                            )
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
                        Text(stringResource(R.string.btn_add_new_equipment), color = Terracotta, style = MaterialTheme.typography.labelLarge)
                    }
                }

                // Instructions Section
                item {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(stringResource(R.string.instructions), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                itemsIndexed(recipe.instructions) { index, step ->
                    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = step,
                            onValueChange = { editRecipeViewModel.updateInstruction(index, it) },
                            label = { Text(stringResource(R.string.recipe_step, index + 1), color = MaterialTheme.colorScheme.onSurfaceVariant) },
                            modifier = Modifier
                                .weight(1f)
                                .heightIn(min = 100.dp),
                            maxLines = 5,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                            )
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
                        Text(stringResource(R.string.btn_add_new_step), color = Terracotta, style = MaterialTheme.typography.labelLarge)
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Terracotta)
                    ) {
                        if (uiState.isSaving) {
                            CircularProgressIndicator(color = Cream, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                        } else {
                            Text(stringResource(R.string.btn_save_changes), style = MaterialTheme.typography.labelLarge)
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}