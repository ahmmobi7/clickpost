package com.clickpost.app.social.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clickpost.app.social.data.CredentialStatus
import com.clickpost.app.social.data.Platform

@Composable
fun PlatformBadge(
    platform: Platform,
    accountName: String,
    status: CredentialStatus,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (platform) {
        Platform.TIKTOK -> Color(0xFF010101)
        Platform.FACEBOOK -> Color(0xFF1877F2)
        Platform.INSTAGRAM -> Color(0xFFE1306C)
        Platform.YOUTUBE -> Color(0xFFFF0000)
    }

    Box(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(backgroundColor)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = accountName.take(14),
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        
        CredentialStatusDot(
            status = status,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 4.dp, y = (-4).dp)
        )
    }
}
