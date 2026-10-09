package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.CombinedVibration
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.speech.tts.TextToSpeech
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.sin

class AudioHelper(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    init {
        tts = TextToSpeech(context.applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("ar"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.w("AudioHelper", "Arabic language data missing or not supported on this device TTS")
            } else {
                isTtsReady = true
            }
        }
    }

    fun speakArabic(text: String) {
        if (isTtsReady) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ArabicUtterance_${System.currentTimeMillis()}")
        }
    }

    fun playCorrectSound(scope: CoroutineScope) {
        vibrateSuccess()
        scope.launch(Dispatchers.Default) {
            playTone(880.0, 100) // A5
            playTone(1174.66, 180) // D6
        }
    }

    fun playWrongSound(scope: CoroutineScope) {
        vibrateError()
        scope.launch(Dispatchers.Default) {
            playTone(330.0, 150)
            playTone(260.0, 220)
        }
    }

    private fun playTone(freqOfTone: Double, durationMs: Int) {
        try {
            val sampleRate = 8000
            val numSamples = durationMs * sampleRate / 1000
            val generatedSnd = ByteArray(2 * numSamples)

            for (i in 0 until numSamples) {
                val dVal = sin(2.0 * Math.PI * i.toDouble() / (sampleRate / freqOfTone))
                val valShort = (dVal * 32767).toInt().toShort()
                val idx = 2 * i
                generatedSnd[idx] = (valShort.toInt() and 0x00ff).toByte()
                generatedSnd[idx + 1] = (valShort.toInt() shr 8 and 0x00ff).toByte()
            }

            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
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
                .setBufferSizeInBytes(generatedSnd.size)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(generatedSnd, 0, generatedSnd.size)
            audioTrack.play()
        } catch (e: Exception) {
            Log.e("AudioHelper", "Failed to play synthesis tone", e)
        }
    }

    private fun vibrateSuccess() {
        try {
            val vibrator = getVibrator()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(70, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(70)
            }
        } catch (_: Exception) {}
    }

    private fun vibrateError() {
        try {
            val vibrator = getVibrator()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val timings = longArrayOf(0, 80, 80, 100)
                val amplitudes = intArrayOf(0, 180, 0, 220)
                vibrator?.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(200)
            }
        } catch (_: Exception) {}
    }

    private fun getVibrator(): Vibrator? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            manager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
