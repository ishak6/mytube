
package com.mytube.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.isSystemInDarkTheme

private val Gold = Color(0xFFFFC629)
private val Green = Color(0xFF16C784)
private val Red = Color(0xFFFF3038)
private val Purple = Color(0xFF8B5CF6)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTubeApp()
        }
    }
}

@Composable
fun MyTubeApp() {
    val dark = isSystemInDarkTheme()

    val scheme = if (dark) {
        darkColorScheme(
            primary = Red,
            secondary = Gold,
            background = Color(0xFF090A0F),
            surface = Color(0xFF171923)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFFD92332),
            secondary = Color(0xFFB8860B),
            background = Color(0xFFF5F6FA),
            surface = Color.White
        )
    }

    var selectedTab by remember { mutableStateOf("Premium") }

    MaterialTheme(colorScheme = scheme) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                MyTubeBottomBar(selectedTab) {
                    selectedTab = it
                }
            }
        ) { padding ->
            when (selectedTab) {
                "Premium" -> PremiumDashboard(Modifier.padding(padding))
                "Home" -> SimpleScreen(
                    "Home",
                    "Discover videos and creators",
                    Icons.Default.Home,
                    Modifier.padding(padding)
                )
                "Shorts" -> SimpleScreen(
                    "Shorts",
                    "Discover short videos",
                    Icons.Default.SmartDisplay,
                    Modifier.padding(padding)
                )
                "Explore" -> SimpleScreen(
                    "Explore",
                    "Music • Gaming • News • Live",
                    Icons.Default.Explore,
                    Modifier.padding(padding)
                )
                else -> SimpleScreen(
                    "Profile",
                    "Account and app settings",
                    Icons.Default.Person,
                    Modifier.padding(padding)
                )
            }
        }
    }
}

@Composable
fun PremiumDashboard(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Header()
        PremiumHeroCard()
        BenefitsCard()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            UsageCard(Modifier.weight(1f))
            PremiumSettingsCard(Modifier.weight(1f))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionsCard(Modifier.weight(1f))
            PremiumStatusCard(Modifier.weight(1f))
        }

        Text(
            "mytube Premium • Version 1.0.0",
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            fontSize = 11.sp
        )
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(Red),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.PlayArrow,
                contentDescription = "mytube",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }

        Spacer(Modifier.width(10.dp))

        Text(
            "mytube",
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(Modifier.width(10.dp))

        Surface(
            color = Gold.copy(alpha = 0.14f),
            shape = RoundedCornerShape(30.dp)
        ) {
            Row(
                modifier = Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 7.dp
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.WorkspacePremium,
                    contentDescription = null,
                    tint = Gold,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    "Premium",
                    color = Gold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(Modifier.weight(1f))

        IconButton(onClick = {}) {
            Icon(Icons.Default.Notifications, contentDescription = "Notifications")
        }

        IconButton(onClick = {}) {
            Icon(Icons.Default.Settings, contentDescription = "Settings")
        }
    }
}

@Composable
fun PremiumHeroCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF29202A)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Gold.copy(alpha = 0.65f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Gold.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.WorkspacePremium,
                    contentDescription = null,
                    tint = Gold,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    "mytube Premium",
                    color = Color.White,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "All mytube features unlocked",
                    color = Gold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Enjoy your mytube experience.",
                    color = Color.LightGray,
                    fontSize = 10.sp
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Surface(
                    color = Color(0xFF075B35),
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Text(
                        "✓ Active",
                        modifier = Modifier.padding(
                            horizontal = 9.dp,
                            vertical = 6.dp
                        ),
                        color = Color.White,
                        fontSize = 11.sp
                    )
                }
                Spacer(Modifier.height(10.dp))
                Text("FREE", color = Gold, fontWeight = FontWeight.Bold)
            }
        }
    }
}

data class Benefit(
    val name: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun BenefitsCard() {
    val benefits = listOf(
        Benefit("Ad-Free\nOwn Media", Icons.Default.Block, Red),
        Benefit("PiP Mode", Icons.Default.PictureInPictureAlt, Purple),
        Benefit("Offline\nOwn Media", Icons.Default.Download, Color(0xFF2699F5)),
        Benefit("Background\nOwn Media", Icons.Default.PlayCircle, Purple),
        Benefit("Advanced\nCustomization", Icons.Default.Palette, Green),
        Benefit("Advanced\nAnalytics", Icons.Default.BarChart, Gold)
    )

    DashboardCard("Premium Benefits", Icons.Default.Star) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(benefits) { benefit ->
                Column(
                    modifier = Modifier.width(76.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(benefit.color.copy(alpha = 0.18f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            benefit.icon,
                            contentDescription = null,
                            tint = benefit.color,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(Modifier.height(7.dp))

                    Text(
                        benefit.name,
                        fontSize = 10.sp,
                        lineHeight = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun UsageCard(modifier: Modifier = Modifier) {
    DashboardCard(
        "Your Usage",
        Icons.Default.BarChart,
        modifier
    ) {
        Text(
            "Playback tracking will appear here.",
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
        )
        Spacer(Modifier.height(8.dp))
        Text("0h 00m", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Watch time", fontSize = 11.sp)
        Spacer(Modifier.height(8.dp))
        HorizontalDivider()
        Spacer(Modifier.height(8.dp))
        Text("0 videos watched", fontSize = 11.sp)
    }
}

@Composable
fun PremiumSettingsCard(modifier: Modifier = Modifier) {
    DashboardCard(
        "Premium Settings",
        Icons.Default.Settings,
        modifier
    ) {
        SettingRow("Feature Control", Icons.Default.ToggleOn)
        SettingRow("Theme & UI", Icons.Default.Palette)
        SettingRow("Media Manager", Icons.Default.Folder)
        SettingRow("Backup & Restore", Icons.Default.Backup)
    }
}

@Composable
fun SettingRow(title: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(7.dp))
        Text(title, modifier = Modifier.weight(1f), fontSize = 10.sp)
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            modifier = Modifier.size(17.dp)
        )
    }
}

@Composable
fun QuickActionsCard(modifier: Modifier = Modifier) {
    DashboardCard(
        "Quick Actions",
        Icons.Default.Bolt,
        modifier
    ) {
        QuickAction(Icons.Default.PlayArrow, "Own Media")
        QuickAction(Icons.Default.PictureInPictureAlt, "PiP")
        QuickAction(Icons.Default.Palette, "Theme")
        QuickAction(Icons.Default.Backup, "Backup")
    }
}

@Composable
fun QuickAction(icon: ImageVector, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(19.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(title, fontSize = 11.sp)
    }
}

@Composable
fun PremiumStatusCard(modifier: Modifier = Modifier) {
    DashboardCard(
        "Premium Status",
        Icons.Default.VerifiedUser,
        modifier
    ) {
        Text(
            "All own features unlocked",
            color = Green,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )

        listOf(
            "Premium Features",
            "Feature Controls",
            "Theme Customization",
            "Backup Tools"
        ).forEach { status ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Green,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(5.dp))
                Text(status, fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun DashboardCard(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(19.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            content()
        }
    }
}

@Composable
fun MyTubeBottomBar(
    selected: String,
    onSelect: (String) -> Unit
) {
    val items = listOf(
        Triple("Home", Icons.Default.Home, "Home"),
        Triple("Shorts", Icons.Default.SmartDisplay, "Shorts"),
        Triple("Explore", Icons.Default.Explore, "Explore"),
        Triple("Premium", Icons.Default.WorkspacePremium, "Premium"),
        Triple("Profile", Icons.Default.Person, "Profile")
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = selected == item.first,
                onClick = { onSelect(item.first) },
                icon = {
                    Icon(item.second, contentDescription = item.third)
                },
                label = {
                    Text(item.third, fontSize = 10.sp)
                }
            )
        }
    }
}

@Composable
fun SimpleScreen(
    title: String,
    subtitle: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(60.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(15.dp))

        Text(title, fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(7.dp))
        Text(subtitle)

        Spacer(Modifier.height(12.dp))

        Text(
            "This module will be implemented in the next development stage.",
            fontSize = 12.sp
        )
    }
}
