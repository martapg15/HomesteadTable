package dam_a51564.homesteadtable.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dam_a51564.homesteadtable.ui.theme.*

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    onNavigateToHome: () -> Unit,
    onNavigateToFavourites: () -> Unit,
    onLogOutSuccess: () -> Unit
) {
    val profileUIState by profileViewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Cream,
                contentColor = Espresso
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToHome,
                    icon = { Icon(Icons.Default.Book, contentDescription = "CookBook") },
                    label = { Text("CookBook", style = MaterialTheme.typography.labelSmall) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Cream,
                        selectedTextColor = Terracotta,
                        indicatorColor = Terracotta,
                        unselectedIconColor = WarmTan,
                        unselectedTextColor = WarmTan
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToFavourites,
                    icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = "Favourites") },
                    label = { Text("Favourites", style = MaterialTheme.typography.labelSmall) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Cream,
                        selectedTextColor = Terracotta,
                        indicatorColor = Terracotta,
                        unselectedIconColor = WarmTan,
                        unselectedTextColor = WarmTan
                    )
                )
                NavigationBarItem(
                    selected = true, // Profile is selected
                    onClick = { /* Already here */ },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile", style = MaterialTheme.typography.labelSmall) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Cream,
                        selectedTextColor = Terracotta,
                        indicatorColor = Terracotta,
                        unselectedIconColor = WarmTan,
                        unselectedTextColor = WarmTan
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Cream)
                .padding(innerPadding) // Use scaffold padding
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Header
            Text(
                text = "My Profile",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 20.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Profile Picture Placeholder
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(ParchmentBorder),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = WarmTan
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // User Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "NAME",
                        style = MaterialTheme.typography.labelSmall,
                        color = WarmTan
                    )
                    Text(
                        text = profileUIState.userName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Espresso
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "EMAIL",
                        style = MaterialTheme.typography.labelSmall,
                        color = WarmTan
                    )
                    Text(
                        text = profileUIState.email,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Espresso
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Log Out Button
            OutlinedButton(
                onClick = {
                    profileViewModel.onLogOut()
                    onLogOutSuccess()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                    width = 1.5.dp,
                    brush = androidx.compose.ui.graphics.SolidColor(BurntRed)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = BurntRed
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "LOG OUT",
                    style = MaterialTheme.typography.labelLarge,
                    color = BurntRed
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}