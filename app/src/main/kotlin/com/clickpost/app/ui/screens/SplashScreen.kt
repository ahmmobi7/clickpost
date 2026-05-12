package com.clickpost.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clickpost.app.ui.theme.Teal400
import com.clickpost.app.ui.theme.Teal500
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onAnimationFinished: () -> Unit) {
    val scale = remember { Animatable(0.4f) }
    val alpha = remember { Animatable(0f) }
    val taglineAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Logo entrance
        scale.animateTo(1f, animationSpec = spring(dampingRatio = 0.6f, stiffness = 300f))
        alpha.animateTo(1f, animationSpec = tween(400))
        delay(200)
        taglineAlpha.animateTo(1f, animationSpec = tween(500))
        delay(1200)
        onAnimationFinished()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(colors = listOf(Teal400.copy(alpha = 0.15f), Color.White))
            )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Logo icon in a teal circle
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(96.dp)
                    .scale(scale.value)
                    .alpha(alpha.value)
                    .background(
                        Brush.linearGradient(listOf(Teal400, Teal500)),
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.VideoLibrary,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "ClickPost",
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = Teal500,
                modifier = Modifier.alpha(alpha.value)
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Auto brand your videos",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.alpha(taglineAlpha.value)
            )
        }
    }
}
