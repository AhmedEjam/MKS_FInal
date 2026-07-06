package com.ahmedyejam.mks.data.importer.xlsx

import com.ahmedyejam.mks.util.MksLogger

object PoiInitializer {
    private var isInitialized = false

    fun init() {
        if (isInitialized) return
        try {
            // Set StAX properties to use Aalto XML
            System.setProperty("javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl")
            System.setProperty("javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl")
            System.setProperty("javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl")
            
            // Set additional properties to ensure POI doesn't look for AWT
            System.setProperty("java.awt.headless", "true")
            System.setProperty("org.apache.poi.util.ignore_missing_fontmetrics", "true")
            
            isInitialized = true
        } catch (e: Exception) {
            MksLogger.e("PoiInitializer", "Failed to initialize POI environment", e)
        }
    }
}
