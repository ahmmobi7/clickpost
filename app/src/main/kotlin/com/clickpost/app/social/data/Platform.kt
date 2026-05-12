package com.clickpost.app.social.data

enum class Platform {
    TIKTOK, FACEBOOK, INSTAGRAM, YOUTUBE;

    val displayName: String get() = when (this) {
        TIKTOK -> "TikTok"
        FACEBOOK -> "Facebook"
        INSTAGRAM -> "Instagram"
        YOUTUBE -> "YouTube"
    }
}
