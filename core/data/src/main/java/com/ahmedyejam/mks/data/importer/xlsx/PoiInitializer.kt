package com.ahmedyejam.mks.data.importer.xlsx

import com.ahmedyejam.mks.util.MksLogger

object PoiInitializer {
    private var isInitialized = false

    /**
     * Reject an entry whose compressed:uncompressed ratio is more extreme than this.
     *
     * POI's own default is 0.01 (100:1). Legitimate spreadsheet XML compresses well but not
     * absurdly; 0.005 (200:1) keeps headroom for genuinely repetitive sheets while still refusing
     * the pathological ratios a zip bomb depends on.
     */
    private const val MIN_INFLATE_RATIO = 0.005

    /** Hard ceiling on a single decompressed entry (100 MB), independent of its declared size. */
    private const val MAX_ENTRY_SIZE_BYTES = 100L * 1024 * 1024

    /** Hard ceiling on extracted text (20M chars) so a single huge cell cannot exhaust memory. */
    private const val MAX_TEXT_SIZE_CHARS = 20L * 1024 * 1024

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

            // POI parses the XML parts inside .xlsx/.pptx, which are untrusted user files. Cap
            // entity expansion so a nested-entity bomb cannot exhaust memory during import, and
            // bound the decompression ratio so a zip bomb is rejected on ratio rather than only on
            // the declared entry size, which an attacker controls.
            System.setProperty("org.apache.poi.openxml4j.strict.ooxml.zip.entity.expansion.limit", "1")
            org.apache.poi.openxml4j.util.ZipSecureFile.setMinInflateRatio(MIN_INFLATE_RATIO)
            org.apache.poi.openxml4j.util.ZipSecureFile.setMaxEntrySize(MAX_ENTRY_SIZE_BYTES)
            org.apache.poi.openxml4j.util.ZipSecureFile.setMaxTextSize(MAX_TEXT_SIZE_CHARS)

            isInitialized = true
        } catch (e: Exception) {
            MksLogger.e("PoiInitializer", "Failed to initialize POI environment", e)
        }
    }
}
