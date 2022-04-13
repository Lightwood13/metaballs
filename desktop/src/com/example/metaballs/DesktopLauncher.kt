package com.example.metaballs

import kotlin.jvm.JvmStatic
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = Lwjgl3ApplicationConfiguration()
        val width = 800
        val height = 600
        config.setWindowedMode(width, height)
        config.setTitle("Metaballs")
        config.setForegroundFPS(60)
        Lwjgl3Application(Application(width, height), config)
    }
}