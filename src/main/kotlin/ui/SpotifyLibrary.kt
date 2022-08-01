package ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp
import utils.horizontalGradientBackground

@Composable
fun SpotifyLibrary() {

    Column(
        modifier = Modifier.fillMaxSize()
            .horizontalGradientBackground(listOf(MaterialTheme.colors.secondary, MaterialTheme.colors.surface))
            .padding(vertical = 50.dp).verticalScroll(ScrollState(0)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var draw2 by remember { mutableStateOf(false) }
        var draw4 by remember { mutableStateOf(false) }
        val imageSize = 300.dp

        SpotifyTitle("My Library")
        Spacer(modifier = Modifier.padding(30.dp))
        Box(modifier = Modifier.clickable(onClick = {
            draw2 = !draw2
            draw4 = false
        })) {
            val elevationState by animateFloatAsState(if (draw2) 30f else 5f)
            val translationState by animateFloatAsState(if (draw2) 520f else 0f)
            Image(
                painterResource("bp.jpg"), contentDescription = "", modifier = Modifier.size(imageSize).graphicsLayer(
                    shadowElevation = elevationState,
                    translationX = translationState,
                    translationY = 0f,
                )
            )
            Image(
                painterResource("dualipa.jpeg"),
                contentDescription = "",
                modifier = Modifier.size(imageSize).graphicsLayer(
                    shadowElevation = animateFloatAsState(if (draw2) 30f else 10f).value,
                    translationX = animateFloatAsState(if (draw2) -520f else 0f).value,
                    translationY = animateFloatAsState(if (draw2) 0f else 30f).value
                ).clickable(onClick = { draw2 = !draw2 })
            )
            Image(
                painterResource("tylor.jpeg"),
                contentDescription = "",
                modifier = Modifier.size(imageSize).graphicsLayer(
                    shadowElevation = animateFloatAsState(if (draw2) 30f else 5f).value,
                    translationY = animateFloatAsState(if (draw2) 0f else 50f).value
                ).clickable(onClick = { draw2 = !draw2 })
            )
        }

        Spacer(modifier = Modifier.padding(50.dp))

        Box(modifier = Modifier.clickable {
            draw4 = !draw4
            draw2 = false
        }) {
            Image(
                painterResource("katy.jpg"), contentDescription = "", modifier = Modifier.size(imageSize).graphicsLayer(
                    shadowElevation = animateFloatAsState(if (draw4) 30f else 5f).value,
                    translationX = animateFloatAsState(if (draw4) 320f else 0f).value,
                    rotationZ = animateFloatAsState(if (draw4) 45f else 0f).value,
                    translationY = 0f
                )
            )
            Image(
                painterResource("ed2.jpg"), contentDescription = "", modifier = Modifier.size(imageSize).graphicsLayer(
                    shadowElevation = animateFloatAsState(if (draw4) 30f else 10f).value,
                    translationX = animateFloatAsState(if (draw4) -320f else 0f).value,
                    rotationZ = animateFloatAsState(if (draw4) 45f else 0f).value,
                    translationY = animateFloatAsState(if (draw4) 0f else 30f).value
                )
            )
            Image(
                painterResource("camelia.jpeg"),
                contentDescription = "",
                modifier = Modifier.size(imageSize).graphicsLayer(
                    shadowElevation = animateFloatAsState(if (draw4) 30f else 5f).value,
                    translationY = animateFloatAsState(if (draw4) 0f else 50f).value,
                    rotationZ = animateFloatAsState( if (draw4) 45f else 0f).value
                )
            )
        }
    }
}