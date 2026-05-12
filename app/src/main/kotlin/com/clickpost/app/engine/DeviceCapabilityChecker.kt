package com.clickpost.app.engine

import android.content.Context
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.transformer.EncoderUtil
import com.clickpost.app.data.model.DeviceCapabilities
import com.clickpost.app.data.model.Resolution
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@UnstableApi
@Singleton
class DeviceCapabilityChecker @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun query(): DeviceCapabilities {
        val h265Encoders = EncoderUtil.getSupportedEncoders(MimeTypes.VIDEO_H265)
        val h264Encoders = EncoderUtil.getSupportedEncoders(MimeTypes.VIDEO_H264)
        val supportsH265 = h265Encoders.isNotEmpty()
        val allEncoders = h265Encoders + h264Encoders

        val supportedResolutions = Resolution.entries.filter { resolution ->
            allEncoders.any { encoder ->
                getSupportedResolutions(encoder, resolution.height).isNotEmpty()
            }
        }.ifEmpty {
            // Fallback: all devices can do at least 720p and 1080p
            listOf(Resolution.HD_720, Resolution.FHD_1080)
        }

        return DeviceCapabilities(
            supportedResolutions = supportedResolutions,
            supportsH265 = supportsH265,
            hardwareEncoderAvailable = allEncoders.isNotEmpty()
        )
    }
}

// Helper to get supported resolutions for a given encoder info
@UnstableApi
private fun getSupportedResolutions(
    encoderInfo: android.media.MediaCodecInfo,
    targetHeight: Int
): List<Int> {
    return runCatching {
        val caps = encoderInfo.getCapabilitiesForType(
            if (encoderInfo.supportedTypes.contains(MimeTypes.VIDEO_H265)) MimeTypes.VIDEO_H265
            else MimeTypes.VIDEO_H264
        )
        val videoCaps = caps.videoCapabilities
        if (videoCaps.isSizeSupported(targetHeight * 16 / 9, targetHeight)) {
            listOf(targetHeight)
        } else emptyList()
    }.getOrDefault(emptyList())
}
