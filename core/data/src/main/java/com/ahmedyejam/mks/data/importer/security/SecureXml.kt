package com.ahmedyejam.mks.data.importer.security

import com.ahmedyejam.mks.util.MksLogger
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Hardened XML factories for the import pipeline.
 *
 * Every XML document MKS parses arrives from a file the user picked — an .xlsx, .pptx or .html of
 * unknown provenance. A stock [DocumentBuilderFactory] will happily resolve a `<!DOCTYPE>` that
 * points at `file:///etc/passwd` or an internal URL (XXE), or expand a nested-entity bomb until the
 * process dies (billion laughs). Neither needs the user to do anything beyond opening the file.
 *
 * Nothing in MKS's import formats legitimately uses a DTD, so the primary defence is simply to
 * refuse DOCTYPE outright: with `disallow-doctype-decl` set, a document declaring one throws
 * instead of parsing, which forecloses every entity-based attack in one step. The remaining
 * features are defence in depth for parsers that ignore or partially implement it.
 *
 * Use [newDocumentBuilderFactory] instead of calling `DocumentBuilderFactory.newInstance()`
 * directly anywhere that touches imported content.
 *
 * The streaming (StAX) side is handled separately in `PoiInitializer`: Android's platform API has
 * no `javax.xml.stream`, so POI supplies its own via Aalto and is constrained there through POI's
 * own `ZipSecureFile` limits rather than a factory built here.
 */
object SecureXml {

    private const val DISALLOW_DOCTYPE = "http://apache.org/xml/features/disallow-doctype-decl"
    private const val EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities"
    private const val EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities"
    private const val LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd"

    // Spelled out rather than taken from XMLConstants: these were added in JAXP 1.5 and are absent
    // from the XMLConstants that Android's platform API ships, so referencing the constants fails
    // to compile even though the underlying parser honours the attribute names at runtime.
    private const val ACCESS_EXTERNAL_DTD = "http://javax.xml.XMLConstants/property/accessExternalDTD"
    private const val ACCESS_EXTERNAL_SCHEMA = "http://javax.xml.XMLConstants/property/accessExternalSchema"

    /**
     * A [DocumentBuilderFactory] that cannot be induced to touch the filesystem or the network.
     *
     * @param namespaceAware mirrors the caller's existing requirement; OOXML parsing needs it.
     */
    fun newDocumentBuilderFactory(namespaceAware: Boolean = true): DocumentBuilderFactory {
        val factory = DocumentBuilderFactory.newInstance()

        // The decisive one. No MKS import format uses a DTD, so refusing DOCTYPE entirely blocks
        // XXE and entity expansion together rather than trying to enumerate every entity type.
        factory.setFeatureQuietly(DISALLOW_DOCTYPE, true)

        // Belt and braces for parser implementations that tolerate DOCTYPE anyway.
        factory.setFeatureQuietly(EXTERNAL_GENERAL_ENTITIES, false)
        factory.setFeatureQuietly(EXTERNAL_PARAMETER_ENTITIES, false)
        factory.setFeatureQuietly(LOAD_EXTERNAL_DTD, false)
        factory.setFeatureQuietly(XMLConstants.FEATURE_SECURE_PROCESSING, true)

        factory.isXIncludeAware = false
        factory.isExpandEntityReferences = false
        factory.isNamespaceAware = namespaceAware

        // Empty strings mean "no protocol is permitted" for external access, so even a parser that
        // somehow reached entity resolution has nowhere to go.
        factory.setAttributeQuietly(ACCESS_EXTERNAL_DTD, "")
        factory.setAttributeQuietly(ACCESS_EXTERNAL_SCHEMA, "")

        return factory
    }

    /**
     * Feature support varies across the JAXP implementation Android ships and the one POI pulls in;
     * an unsupported feature throws rather than returning false. A failure to *enable* a hardening
     * feature is not fatal on its own — the layers above still hold — but it is logged, because a
     * silent gap here is exactly the kind of thing that makes an audit finding look fixed when it
     * is not.
     */
    private fun DocumentBuilderFactory.setFeatureQuietly(name: String, value: Boolean) {
        try {
            setFeature(name, value)
        } catch (e: Exception) {
            MksLogger.w("SecureXml", "XML hardening feature unsupported: $name", e)
        }
    }

    private fun DocumentBuilderFactory.setAttributeQuietly(name: String, value: Any) {
        try {
            setAttribute(name, value)
        } catch (e: Exception) {
            MksLogger.w("SecureXml", "XML hardening attribute unsupported: $name", e)
        }
    }

}
