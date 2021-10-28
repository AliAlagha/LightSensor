package com.example.lightsensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private var textView: TextView? = null
    private var constraintLayout: ConstraintLayout? = null
    private var morningSoundMediaPlayer: MediaPlayer? = null
    private var nightSoundMediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        constraintLayout = findViewById(R.id.constraintLayout)

        morningSoundMediaPlayer = MediaPlayer.create(this, R.raw.morning_sound)
        nightSoundMediaPlayer = MediaPlayer.create(this, R.raw.night_sound)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
        for (sensor in deviceSensors) {
            Log.d("sensorsList", sensor.name)
        }

        if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
            Log.d("isSensorFound", "Sensor found")
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        } else {
            Log.d("isSensorFound", "Sensor not found")
        }

    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {

            val lightValue = event.values[0].toInt()
            val maxRange = event.sensor.maximumRange

            textView?.text = "$lightValue\n$maxRange"

            if (lightValue > 40) {
                constraintLayout?.setBackgroundResource(R.drawable.morning_image)

                if (nightSoundMediaPlayer?.isPlaying!!) {
                    nightSoundMediaPlayer?.pause()
                }
                morningSoundMediaPlayer?.start()
            } else {
                constraintLayout?.setBackgroundResource(R.drawable.night_sound)

                if (morningSoundMediaPlayer?.isPlaying!!) {
                    morningSoundMediaPlayer?.pause()
                }
                nightSoundMediaPlayer?.start()
            }

        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        // Do something here if sensor accuracy changes.
    }

}