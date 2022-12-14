import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.guru.composecookbook.ui.demoui.spotify.data.Album
import com.guru.composecookbook.ui.demoui.spotify.data.SpotifyDataProvider
import ui.SpotifyHome
import ui.SpotifyLibrary
import ui.SpotifyNavType
import ui.SpotifySearchScreen
import ui.detail.SpotifyDetailScreen


enum class state{
    EXPANDED, COLLAPSED
}
fun main() = application() {
    Window(onCloseRequest = ::exitApplication, title = "Spotify Clone") {
        val darkTheme = remember { mutableStateOf(false) }
        MaterialTheme(colors = if (darkTheme.value) DarkGreenColorPalette else LightGreenColorPalette) {
            SpotifyApp(darkTheme)
        }
    }
}

@Composable
fun SpotifyApp(darkTheme: MutableState<Boolean>) {
    val spotifyNavItemState = remember { mutableStateOf(SpotifyNavType.HOME) }

    val showAlbumDetailState = remember { mutableStateOf<Album?>(null) }
    Box {
        Row {
            SpotifySideBar(spotifyNavItemState, showAlbumDetailState, darkTheme)
            SpotifyBodyContent(spotifyNavItemState.value, showAlbumDetailState)
        }
        PlayerBottomBar(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun SpotifyBodyContent(spotifyNavType: SpotifyNavType, album: MutableState<Album?>) {
    if (album.value != null) {
        SpotifyDetailScreen(album.value!!) {
            album.value = null
        }
    } else {
        Crossfade(targetState = spotifyNavType, animationSpec = tween(1000)) { state ->
            when (state) {
                SpotifyNavType.HOME -> SpotifyHome { onAlbumSelected ->
                    album.value = onAlbumSelected
                }

                SpotifyNavType.SEARCH -> SpotifySearchScreen { onAlbumSelected ->
                    album.value = onAlbumSelected
                }

                SpotifyNavType.LIBRARY -> SpotifyLibrary()
            }
        }
    }
}

@Composable
fun SpotifySideBar(
    spotifyNavItemState: MutableState<SpotifyNavType>,
    showAlbumDetailState: MutableState<Album?>,
    darkTheme: MutableState<Boolean>
) {
    val selectedIndex = remember { mutableStateOf(-1) }
    val sideBarState = remember { mutableStateOf(state.COLLAPSED) }
    val sideBarWidth : Dp  by animateDpAsState(if(sideBarState.value == state.COLLAPSED) 50.dp else 250.dp )
    Column(
        modifier = Modifier.fillMaxHeight().width(sideBarWidth).background(MaterialTheme.colors.surface)
            .padding(8.dp).focusable(true).onFocusChanged {
                if (it.isFocused) {
                    sideBarState.value = state.EXPANDED
                } else {
                    sideBarState.value = state.COLLAPSED
                }
            }, horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Image(
            painterResource(if (darkTheme.value) "spotify.png" else "spotifydark.png"),
            modifier = Modifier.padding(end = 16.dp, top = 16.dp, bottom = 16.dp),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                .clickable { darkTheme.value = !darkTheme.value }.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Toggle Theme", style = typography.h6.copy(fontSize = 14.sp), color = MaterialTheme.colors.onSurface)
            if (darkTheme.value) {
                Icon(imageVector = Icons.Default.Star, tint = Color.Yellow, contentDescription = "")
            } else {
                Icon(imageVector = Icons.Default.Star, tint = MaterialTheme.colors.onSurface, contentDescription = "")
            }

        }
        Spacer(modifier = Modifier.height(20.dp))
        SideBarNavItem(
            "Home", Icons.Default.Home, spotifyNavItemState.value == SpotifyNavType.HOME, sideBarWidth
        ) {
            spotifyNavItemState.value = SpotifyNavType.HOME
            showAlbumDetailState.value = null
            selectedIndex.value = -1
        }
        Spacer(modifier = Modifier.height(12.dp))
        SideBarNavItem(
            "Search", Icons.Default.Search, spotifyNavItemState.value == SpotifyNavType.SEARCH, sideBarWidth
        ) {
            spotifyNavItemState.value = SpotifyNavType.SEARCH
            showAlbumDetailState.value = null
            selectedIndex.value = -1
        }
        Spacer(modifier = Modifier.height(12.dp))
        SideBarNavItem(
            "Your Library", Icons.Default.List, spotifyNavItemState.value == SpotifyNavType.LIBRARY, sideBarWidth
        ) {
            spotifyNavItemState.value = SpotifyNavType.LIBRARY
            showAlbumDetailState.value = null
            selectedIndex.value = -1
        }
        Spacer(modifier = Modifier.height(20.dp))
        PlayListsSideBar(selectedIndex.value) {
            showAlbumDetailState.value = SpotifyDataProvider.albums[it]
            selectedIndex.value = it
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun PlayerBottomBar(modifier: Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().background(color = MaterialTheme.colors.secondary).padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource("adele.jpeg"),
            modifier = Modifier.size(75.dp).padding(8.dp),
            contentScale = ContentScale.Crop,
            contentDescription = ""
        )
        Text(
            text = "Someone Like you by Adele",
            style = typography.h6.copy(fontSize = 14.sp),
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(16.dp),
        )
        Icon(
            imageVector = Icons.Default.AddCircle,
            modifier = Modifier.padding(8.dp),
            contentDescription = "",
            tint = MaterialTheme.colors.onSurface
        )
        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
            Icon(
                imageVector = Icons.Default.Refresh,
                modifier = Modifier.padding(8.dp),
                tint = MaterialTheme.colors.onSurface,
                contentDescription = ""
            )
            Icon(
                imageVector = Icons.Default.PlayArrow,
                modifier = Modifier.padding(8.dp),
                contentDescription = "",
                tint = MaterialTheme.colors.onSurface
            )
            Icon(
                imageVector = Icons.Default.Favorite,
                modifier = Modifier.padding(8.dp),
                contentDescription = "",
                tint = spotifyGreen
            )
        }

        Icon(
            imageVector = Icons.Default.List,
            modifier = Modifier.padding(8.dp),
            tint = MaterialTheme.colors.onSurface,
            contentDescription = "",
        )
        Icon(
            imageVector = Icons.Default.Share,
            modifier = Modifier.padding(8.dp),
            tint = MaterialTheme.colors.onSurface,
            contentDescription = "",
        )
    }
}

@Composable
fun PlayListsSideBar(selectedIndex: Int, onPlayListSelected: (Int) -> Unit) {

    Text(
        "PLAYLISTS",
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
        color = MaterialTheme.colors.onSurface
    )

    LazyColumn {

        itemsIndexed(SpotifyDataProvider.playLits) { index, playlist ->
            val color = animateColorAsState(
                if (index == selectedIndex) MaterialTheme.colors.onSurface else MaterialTheme.colors.onSecondary.copy(
                    alpha = 0.7f
                )
            )
            Text(
                playlist,
                modifier = Modifier.padding(8.dp).clickable { onPlayListSelected.invoke(index) },
                color = color.value,
                style = if (index == selectedIndex) typography.h6 else typography.body1
            )
        }
    }

}


@Composable
fun SideBarNavItem(title: String, icon: ImageVector, selected: Boolean, width: Dp, onClick: () -> Unit) {
    val animatedBackgroundColor =
        animateColorAsState(if (selected) MaterialTheme.colors.secondary else MaterialTheme.colors.surface)
    val animatedContentColor =
        animateColorAsState(if (selected) MaterialTheme.colors.onSurface else MaterialTheme.colors.onSecondary)
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(animatedBackgroundColor.value)
            .clickable {
                onClick.invoke()
            }.padding(8.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            tint = animatedContentColor.value,
            contentDescription = "",
        )
        if (width > 80.dp) {
            Text(
                title,
                style = typography.body1,
                color = animatedContentColor.value,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}
