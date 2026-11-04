
package com.mytube.android

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.isSystemInDarkTheme
import org.json.JSONArray

private val BrandRed = Color(0xFFE62117)
private val PremiumGold = Color(0xFFFFC857)
private val AccentPurple = Color(0xFF8B5CF6)
private val SuccessGreen = Color(0xFF16C784)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTubeApp()
        }
    }
}

private enum class AppTab(
    val label: String,
    val icon: ImageVector
) {
    HOME("Home", Icons.Default.Home),
    SEARCH("Search", Icons.Default.Search),
    SHORTS("Shorts", Icons.Default.SmartDisplay),
    LIBRARY("Library", Icons.Default.VideoLibrary),
    PREMIUM("Premium", Icons.Default.WorkspacePremium)
}

private data class Category(
    val title: String,
    val icon: ImageVector,
    val query: String
)

private val categories = listOf(
    Category("Music", Icons.Default.MusicNote, "music"),
    Category("Gaming", Icons.Default.SportsEsports, "gaming"),
    Category("News", Icons.Default.Newspaper, "news"),
    Category("Live", Icons.Default.LiveTv, "live"),
    Category("Education", Icons.Default.School, "education"),
    Category("Sports", Icons.Default.SportsSoccer, "sports"),
    Category("Technology", Icons.Default.Devices, "technology"),
    Category("Movies", Icons.Default.Movie, "movies")
)

@Composable
fun MyTubeApp() {
    val dark = isSystemInDarkTheme()

    val scheme = if (dark) {
        darkColorScheme(
            primary = BrandRed,
            secondary = PremiumGold,
            tertiary = AccentPurple,
            background = Color(0xFF0D0F14),
            surface = Color(0xFF191C24)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFFD51E28),
            secondary = Color(0xFF9A6B00),
            tertiary = AccentPurple,
            background = Color(0xFFF5F6F8),
            surface = Color.White
        )
    }

    var selectedTab by remember {
        mutableStateOf(AppTab.HOME)
    }

    MaterialTheme(colorScheme = scheme) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                MyTubeTopBar()
            },
            bottomBar = {
                NavigationBar {
                    AppTab.values().forEach { tab ->
                        NavigationBarItem(
                            selected = selectedTab == tab,
                            onClick = {
                                selectedTab = tab
                            },
                            icon = {
                                Icon(
                                    tab.icon,
                                    contentDescription = tab.label
                                )
                            },
                            label = {
                                Text(
                                    tab.label,
                                    fontSize = 10.sp
                                )
                            }
                        )
                    }
                }
            }
        ) { padding ->

            when (selectedTab) {

                AppTab.HOME -> HomeScreen(
                    Modifier.padding(padding),
                    onSearch = {
                        selectedTab = AppTab.SEARCH
                    }
                )

                AppTab.SEARCH -> SearchScreen(
                    Modifier.padding(padding)
                )

                AppTab.SHORTS -> ShortsScreen(
                    Modifier.padding(padding)
                )

                AppTab.LIBRARY -> LibraryScreen(
                    Modifier.padding(padding)
                )

                AppTab.PREMIUM -> PremiumDashboard(
                    Modifier.padding(padding)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyTubeTopBar() {
    val context = LocalContext.current

    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(BrandRed),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White
                    )
                }

                Spacer(Modifier.width(9.dp))

                Text(
                    "mytube",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    openWeb(
                        context,
                        "https://www.youtube.com/feed/notifications"
                    )
                }
            ) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Notifications"
                )
            }

            IconButton(
                onClick = {
                    openWeb(
                        context,
                        "https://www.youtube.com/account"
                    )
                }
            ) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = "Account"
                )
            }
        }
    )
}

private fun openWeb(
    context: Context,
    url: String
) {
    runCatching {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
        )
    }
}

private fun openYouTubeSearch(
    context: Context,
    query: String
) {
    val url =
        "https://www.youtube.com/results?search_query=" +
            Uri.encode(query)

    openWeb(context, url)
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    onSearch: () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 20.dp)
    ) {

        OutlinedTextField(
            value = "",
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 10.dp
                )
                .clickable {
                    onSearch()
                },
            placeholder = {
                Text("Search videos, channels and more")
            },
            leadingIcon = {
                Icon(Icons.Default.Search, null)
            },
            trailingIcon = {
                Icon(Icons.Default.Mic, null)
            },
            shape = RoundedCornerShape(28.dp)
        )

        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = true,
                onClick = {},
                label = {
                    Text("For you")
                }
            )

            categories.forEach { category ->
                AssistChip(
                    onClick = {
                        openYouTubeSearch(
                            context,
                            category.query
                        )
                    },
                    label = {
                        Text(category.title)
                    },
                    leadingIcon = {
                        Icon(
                            category.icon,
                            null,
                            Modifier.size(16.dp)
                        )
                    }
                )
            }
        }

        SectionTitle(
            "Explore mytube",
            "Discover"
        )

        categories.chunked(3).forEach { rowItems ->

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                rowItems.forEach { category ->

                    Card(
                        Modifier
                            .weight(1f)
                            .padding(vertical = 5.dp)
                            .clickable {
                                openYouTubeSearch(
                                    context,
                                    category.query
                                )
                            },
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 18.dp),
                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {
                            Icon(
                                category.icon,
                                null,
                                tint =
                                    MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(27.dp)
                            )

                            Spacer(Modifier.height(6.dp))

                            Text(
                                category.title,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                repeat(3 - rowItems.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }

        SectionTitle(
            "Popular on YouTube",
            "Explore"
        )

        VideoLinkCard(
            "Trending videos",
            "Explore popular videos and current trends",
            Icons.Default.Whatshot
        ) {
            openWeb(
                context,
                "https://www.youtube.com/feed/trending"
            )
        }

        VideoLinkCard(
            "Music discovery",
            "Songs, artists, mixes and music videos",
            Icons.Default.MusicNote
        ) {
            openWeb(
                context,
                "https://music.youtube.com"
            )
        }

        VideoLinkCard(
            "Live now",
            "Find live streams and premieres",
            Icons.Default.LiveTv
        ) {
            openYouTubeSearch(context, "live")
        }

        VideoLinkCard(
            "Gaming",
            "Gameplay, creators and live gaming",
            Icons.Default.SportsEsports
        ) {
            openYouTubeSearch(context, "gaming")
        }

        VideoLinkCard(
            "Technology",
            "Discover technology and gadgets",
            Icons.Default.Devices
        ) {
            openYouTubeSearch(context, "technology")
        }

        Text(
            "Content opens through official YouTube services. " +
                "Personalized recommendations require authorized API integration.",
            Modifier.padding(
                horizontal = 18.dp,
                vertical = 12.dp
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun SearchScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var query by remember {
        mutableStateOf("")
    }

    var recent by remember {
        mutableStateOf(loadSearchHistory(context))
    }

    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            "Search YouTube",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = {
                Text("Videos, channels, playlists...")
            },
            leadingIcon = {
                Icon(Icons.Default.Search, null)
            },
            trailingIcon = {
                if (query.isNotBlank()) {
                    IconButton(
                        onClick = {
                            query = ""
                        }
                    ) {
                        Icon(
                            Icons.Default.Close,
                            "Clear"
                        )
                    }
                }
            },
            shape = RoundedCornerShape(18.dp)
        )

        Spacer(Modifier.height(10.dp))

        Button(
            onClick = {
                val value = query.trim()

                if (value.isNotEmpty()) {

                    recent =
                        (
                            listOf(value) +
                                recent.filterNot {
                                    it.equals(value, true)
                                }
                            ).take(12)

                    saveSearchHistory(
                        context,
                        recent
                    )

                    openYouTubeSearch(
                        context,
                        value
                    )
                }
            },
            enabled = query.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Search, null)
            Spacer(Modifier.width(8.dp))
            Text("Search")
        }

        Spacer(Modifier.height(22.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Recent searches",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            TextButton(
                onClick = {
                    recent = emptyList()
                    saveSearchHistory(context, recent)
                }
            ) {
                Text("Clear")
            }
        }

        if (recent.isEmpty()) {

            Text(
                "Your recent searches will appear here.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        } else {

            recent.forEach { item ->

                ListItem(
                    headlineContent = {
                        Text(item)
                    },
                    leadingContent = {
                        Icon(
                            Icons.Default.History,
                            null
                        )
                    },
                    trailingContent = {
                        Icon(
                            Icons.Default.NorthWest,
                            null
                        )
                    },
                    modifier = Modifier.clickable {
                        query = item
                        openYouTubeSearch(context, item)
                    }
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        Text(
            "Search currently uses YouTube's official website. " +
                "In-app API search will be added with secure API configuration.",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ShortsScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            Modifier
                .size(92.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(BrandRed),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.SmartDisplay,
                null,
                tint = Color.White,
                modifier = Modifier.size(54.dp)
            )
        }

        Spacer(Modifier.height(18.dp))

        Text(
            "Shorts",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold
        )

        Text(
            "Discover short-form videos on YouTube.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                openWeb(
                    context,
                    "https://www.youtube.com/shorts"
                )
            }
        ) {
            Icon(Icons.Default.PlayArrow, null)
            Spacer(Modifier.width(8.dp))
            Text("Open YouTube Shorts")
        }

        Text(
            "Shorts playback is handled by official YouTube services.",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun LibraryScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val entries = listOf(
        Triple(
            "History",
            "Videos you've watched on YouTube",
            Icons.Default.History
        ),
        Triple(
            "Playlists",
            "Your saved and created playlists",
            Icons.Default.PlaylistPlay
        ),
        Triple(
            "Watch Later",
            "Videos saved for later",
            Icons.Default.WatchLater
        ),
        Triple(
            "Subscriptions",
            "Channels you follow",
            Icons.Default.Subscriptions
        ),
        Triple(
            "Your videos",
            "Manage your own uploaded videos",
            Icons.Default.VideoLibrary
        )
    )

    LazyColumn(
        modifier.fillMaxSize()
    ) {

        item {
            Column(
                Modifier.padding(18.dp)
            ) {
                Text(
                    "Your Library",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Open your YouTube library and account data.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(entries) { entry ->

            ListItem(
                headlineContent = {
                    Text(
                        entry.first,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                supportingContent = {
                    Text(entry.second)
                },
                leadingContent = {
                    Icon(
                        entry.third,
                        null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingContent = {
                    Icon(
                        Icons.Default.OpenInNew,
                        null
                    )
                },
                modifier = Modifier.clickable {

                    val url = when (entry.first) {

                        "History" ->
                            "https://www.youtube.com/feed/history"

                        "Playlists" ->
                            "https://www.youtube.com/feed/playlists"

                        "Watch Later" ->
                            "https://www.youtube.com/playlist?list=WL"

                        "Subscriptions" ->
                            "https://www.youtube.com/feed/channels"

                        else ->
                            "https://studio.youtube.com"
                    }

                    openWeb(context, url)
                }
            )

            HorizontalDivider()
        }

        item {
            Text(
                "Library content is linked to your official YouTube account. " +
                    "Private account data requires authorization.",
                Modifier.padding(18.dp),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PremiumDashboard(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val prefs = remember {
        context.getSharedPreferences("mytube_premium", Context.MODE_PRIVATE)
    }

    var pipEnabled by remember {
        mutableStateOf(prefs.getBoolean("pip", false))
    }

    var analyticsEnabled by remember {
        mutableStateOf(prefs.getBoolean("analytics", true))
    }

    var customThemeEnabled by remember {
        mutableStateOf(prefs.getBoolean("custom_theme", true))
    }

    var backupEnabled by remember {
        mutableStateOf(prefs.getBoolean("backup", false))
    }

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier.weight(1f)
            ) {
                Text(
                    "Premium Dashboard",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    "Every mytube-owned feature is free",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }

            Surface(
                color = PremiumGold.copy(alpha = 0.18f),
                shape = CircleShape
            ) {
                Row(
                    Modifier.padding(
                        horizontal = 10.dp,
                        vertical = 7.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.WorkspacePremium,
                        null,
                        tint = PremiumGold,
                        modifier = Modifier.size(17.dp)
                    )

                    Spacer(Modifier.width(5.dp))

                    Text(
                        "FREE",
                        color = PremiumGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF29202A)
            ),
            shape = RoundedCornerShape(22.dp)
        ) {

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.WorkspacePremium,
                    null,
                    tint = PremiumGold,
                    modifier = Modifier.size(48.dp)
                )

                Spacer(Modifier.width(14.dp))

                Column(
                    Modifier.weight(1f)
                ) {
                    Text(
                        "mytube Premium",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "All app-owned features unlocked",
                        color = PremiumGold,
                        fontSize = 12.sp
                    )

                    Text(
                        "No mytube subscription or paywall.",
                        color = Color.LightGray,
                        fontSize = 11.sp
                    )
                }

                Icon(
                    Icons.Default.CheckCircle,
                    null,
                    tint = SuccessGreen
                )
            }
        }

        PremiumPanel(
            "Included features",
            Icons.Default.Star
        ) {

            val features = listOf(
                "Ad-free experience for your own media",
                "Theme and interface customization",
                "Local library and backup tools",
                "Usage dashboard and diagnostics",
                "PiP/background playback for permitted own media"
            )

            features.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        null,
                        tint = SuccessGreen,
                        modifier = Modifier.size(17.dp)
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        it,
                        fontSize = 12.sp
                    )
                }
            }
        }

        PremiumPanel(
            "Feature controls",
            Icons.Default.Tune
        ) {

            FeatureToggle(
                "Picture-in-Picture preference",
                pipEnabled
            ) {
                pipEnabled = it
                prefs.edit().putBoolean("pip", it).apply()
            }

            FeatureToggle(
                "Usage analytics",
                analyticsEnabled
            ) {
                analyticsEnabled = it
                prefs.edit().putBoolean("analytics", it).apply()
            }

            FeatureToggle(
                "Custom interface options",
                customThemeEnabled
            ) {
                customThemeEnabled = it
                prefs.edit().putBoolean("custom_theme", it).apply()
            }

            FeatureToggle(
                "Local backup reminders",
                backupEnabled
            ) {
                backupEnabled = it
                prefs.edit().putBoolean("backup", it).apply()
            }

            Text(
                "These switches currently save preferences. " +
                    "Actual PiP, analytics collection and backup execution " +
                    "require their dedicated implementation.",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        PremiumPanel(
            "Platform restrictions",
            Icons.Default.Info
        ) {
            Text(
                "mytube cannot unlock YouTube Premium, remove YouTube ads, " +
                    "bypass DRM, or override YouTube playback restrictions. " +
                    "YouTube content uses official YouTube services.",
                fontSize = 12.sp
            )
        }

        PremiumPanel(
            "App information",
            Icons.Default.Info
        ) {
            Text("Application: mytube")
            Text("Version: 1.0.0")
            Text("Package: com.mytube.android")
            Text("Minimum Android: 10")
        }
    }
}

@Composable
private fun PremiumPanel(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(11.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    icon,
                    null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.width(8.dp))

                Text(
                    title,
                    fontWeight = FontWeight.Bold
                )
            }

            content()
        }
    }
}

@Composable
private fun FeatureToggle(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            label,
            Modifier.weight(1f),
            fontSize = 12.sp
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun SectionTitle(
    title: String,
    action: String
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 22.dp,
                bottom = 9.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            title,
            Modifier.weight(1f),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Text(
            action,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun VideoLinkCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 5.dp
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp)
    ) {

        Row(
            Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.12f
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(
                Modifier.weight(1f)
            ) {
                Text(
                    title,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    subtitle,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                Icons.Default.ChevronRight,
                null
            )
        }
    }
}

private fun loadSearchHistory(
    context: Context
): List<String> {
    return runCatching {

        val raw = context
            .getSharedPreferences("mytube_local", 0)
            .getString("search_history", "[]") ?: "[]"

        val array = JSONArray(raw)

        (0 until array.length()).map {
            array.getString(it)
        }

    }.getOrDefault(emptyList())
}

private fun saveSearchHistory(
    context: Context,
    values: List<String>
) {
    val array = JSONArray()

    values.forEach {
        array.put(it)
    }

    context
        .getSharedPreferences("mytube_local", 0)
        .edit()
        .putString("search_history", array.toString())
        .apply()
}
