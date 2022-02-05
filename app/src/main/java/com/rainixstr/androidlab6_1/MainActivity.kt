package com.rainixstr.androidlab6_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private var secondsElapsed: Int = 0
    private var startTime: Long = 0
    private lateinit var textSecondsElapsed: TextView
    private lateinit var backThread : Thread

    private fun startThread(): Thread = Thread {
        try {
            while (!Thread.currentThread().isInterrupted) {
                startTime = System.currentTimeMillis()
                textSecondsElapsed.post {
                    textSecondsElapsed.text = getString(R.string.seconds_elapsed, secondsElapsed++)
                }
                Thread.sleep(1000 + startTime - System.currentTimeMillis())
                Log.d(
                    "Thread",
                    "$secondsElapsed Error Sleeping: ${System.currentTimeMillis() - startTime - 1000}"
                )
            }
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            Log.d(
                "Thread",
                "InterruptedException"
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed)
    }

    override fun onStart() {
        super.onStart()
        backThread = startThread()
        backThread.start()
        Log.d("Thread","Started thread")
    }

    override fun onStop() {
        super.onStop()
        backThread.interrupt()
        Log.d("Thread","Stopped thread")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.run {
            putInt(SECONDS_ELAPSED, secondsElapsed)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.run {
            secondsElapsed = getInt(SECONDS_ELAPSED)
        }
    }

    companion object {
        const val SECONDS_ELAPSED = "Seconds elapsed"
    }
}