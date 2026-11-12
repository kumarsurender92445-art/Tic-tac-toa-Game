package com.example.sound

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

class SoundManager {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private val sampleRate = 44100

    private val moveXSamples: ShortArray by lazy { generateSweep(startFreq = 520.0, endFreq = 880.0, durationMs = 120) }
    private val moveOSamples: ShortArray by lazy { generateSweep(startFreq = 700.0, endFreq = 440.0, durationMs = 130) }
    private val winSamples: ShortArray by lazy { generateArpeggio(listOf(523.25, 659.25, 783.99, 1046.50), noteDurationMs = 110) }
    private val drawSamples: ShortArray by lazy { generateSweep(startFreq = 440.0, endFreq = 330.0, durationMs = 260) }
    private val clickSamples: ShortArray by lazy { generateSweep(startFreq = 900.0, endFreq = 1200.0, durationMs = 30) }

    fun playMoveX(enabled: Boolean) {
        if (!enabled) return
        playPcm(moveXSamples)
    }

    fun playMoveO(enabled: Boolean) {
        if (!enabled) return
        playPcm(moveOSamples)
    }

    fun playWin(enabled: Boolean) {
        if (!enabled) return
        playPcm(winSamples)
    }

    fun playDraw(enabled: Boolean) {
        if (!enabled) return
        playPcm(drawSamples)
    }

    fun playClick(enabled: Boolean) {
        if (!enabled) return
        playPcm(clickSamples)
    }

    private fun playPcm(samples: ShortArray) {
        scope.launch {
            try {
                val bufferSize = samples.size * 2
                val audioTrack = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                audioTrack.write(samples, 0, samples.size)
                audioTrack.play()

                // Release track after playback finished
                val durationMs = (samples.size * 1000L) / sampleRate
                kotlinx.coroutines.delay(durationMs + 60L)
                audioTrack.stop()
                audioTrack.release()
            } catch (_: Exception) {
                // Audio failure graceful fallback
            }
        }
    }

    private fun generateSweep(startFreq: Double, endFreq: Double, durationMs: Int): ShortArray {
        val totalSamples = (sampleRate * (durationMs / 1000.0)).toInt()
        val buffer = ShortArray(totalSamples)
        var phase = 0.0

        for (i in 0 until totalSamples) {
            val t = i.toDouble() / totalSamples
            val currentFreq = startFreq + (endFreq - startFreq) * t
            phase += 2.0 * PI * currentFreq / sampleRate
            // ADSR: smooth attack (first 10%) and exponential release
            val envelope = when {
                t < 0.10 -> t / 0.10
                else -> exp(-3.5 * (t - 0.10))
            }
            val sample = sin(phase) * envelope * 0.75
            buffer[i] = (sample * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
        return buffer
    }

    private fun generateArpeggio(notes: List<Double>, noteDurationMs: Int): ShortArray {
        val samplesPerNote = (sampleRate * (noteDurationMs / 1000.0)).toInt()
        val totalSamples = samplesPerNote * notes.size
        val buffer = ShortArray(totalSamples)

        for ((index, freq) in notes.withIndex()) {
            var phase = 0.0
            val offset = index * samplesPerNote
            for (i in 0 until samplesPerNote) {
                val t = i.toDouble() / samplesPerNote
                phase += 2.0 * PI * freq / sampleRate
                val envelope = when {
                    t < 0.08 -> t / 0.08
                    else -> exp(-2.5 * (t - 0.08))
                }
                val sample = sin(phase) * envelope * 0.70
                val sampleIndex = offset + i
                if (sampleIndex < totalSamples) {
                    buffer[sampleIndex] = (sample * Short.MAX_VALUE).toInt()
                        .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
                }
            }
        }
        return buffer
    }
}
