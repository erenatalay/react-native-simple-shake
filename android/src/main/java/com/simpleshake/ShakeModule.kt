package com.simpleshake;

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.modules.core.DeviceEventManagerModule

class ShakeModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext), SensorEventListener {
    private var sensorManager: SensorManager? = null
    private val SHAKE_EVENT = "ShakeDetected"
    private var lastUpdate: Long = 0
    private var lastX = 0f
    private var lastY = 0f
    private var lastZ = 0f
    private var listenerCount = 0
    
    companion object {
        private const val SHAKE_THRESHOLD = 800
        private const val UPDATE_INTERVAL = 100
    }

    override fun getName(): String = "ShakeModule"

    @ReactMethod
    fun addListener(eventName: String) {
        listenerCount++
        if (listenerCount == 1) {
            startObserving()
        }
    }

    @ReactMethod
    fun removeListeners(count: Int) {
        listenerCount = maxOf(0, listenerCount - count)
        if (listenerCount == 0) {
            stopObserving()
        }
    }

    private fun startObserving() {
        sensorManager = reactApplicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager?.registerListener(
            this,
            sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    private fun stopObserving() {
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val currentTime = System.currentTimeMillis()
        
        if ((currentTime - lastUpdate) > UPDATE_INTERVAL) {
            val diffTime = currentTime - lastUpdate
            lastUpdate = currentTime

            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val speed = kotlin.math.sqrt(
                ((x - lastX) * (x - lastX) +
                (y - lastY) * (y - lastY) +
                (z - lastZ) * (z - lastZ)).toDouble()
            ).toFloat() / diffTime * 10000

            if (speed > SHAKE_THRESHOLD) {
                sendShakeEvent()
            }

            lastX = x
            lastY = y
            lastZ = z
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun sendShakeEvent() {
        reactApplicationContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(SHAKE_EVENT, null)
    }
}