package com.clickpost.app.social.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.clickpost.app.social.data.CredentialStatus

@Composable
fun CredentialStatusDot(
    status: CredentialStatus,
    onTap: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val color = when (status) {
        CredentialStatus.VALID -> Color(0xFF4CAF50)
        CredentialStatus.EXPIRED -> Color(0xFFFFC107)
        CredentialStatus.ERROR -> Color(0xFFF44336)
        CredentialStatus.UNVERIFIED -> Color(0xFF9E9E9E)
    }

    Box(
        modifier = modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(color)
            .border(1.dp, Color.White, CircleShape)
            .then(if (onTap != null) Modifier.clickable(onClick = onTap) else Modifier)
    )
}
