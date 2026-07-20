package com.ahmedyejam.mks.data.importer.security

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail
import org.junit.Test
import java.io.File

/**
 * Proves the XXE and entity-expansion holes are actually closed.
 *
 * A hardening change that only compiles proves nothing — these feed the parser the same payloads an
 * attacker would put in a crafted .xlsx and assert it refuses them. Plain JVM tests: JAXP is a
 * platform API, so no Robolectric or device is required.
 */
class SecureXmlTest {

    private fun parse(xml: String) =
        SecureXml.newDocumentBuilderFactory()
            .newDocumentBuilder()
            .parse(xml.byteInputStream())

    @Test
    fun `benign document still parses`() {
        // Hardening must not break legitimate import content.
        val doc = parse("""<?xml version="1.0"?><root><cell>Hello</cell></root>""")
        assertNotNull(doc)
        assertEquals("root", doc.documentElement.tagName)
        assertEquals("Hello", doc.getElementsByTagName("cell").item(0).textContent)
    }

    @Test
    fun `namespaced document still parses`() {
        // OOXML is heavily namespaced; getElementsByTagNameNS lookups must keep working.
        val doc = parse(
            """<?xml version="1.0"?><a:root xmlns:a="http://example.com/a"><a:pic/></a:root>""",
        )
        assertEquals(1, doc.getElementsByTagNameNS("*", "pic").length)
    }

    @Test
    fun `file disclosure via external entity is refused`() {
        // The classic XXE: without hardening this resolves and leaks the file into the document.
        val secret = File.createTempFile("mks-xxe-canary", ".txt").apply {
            writeText("CANARY_SECRET_VALUE")
            deleteOnExit()
        }
        val payload = """
            <?xml version="1.0"?>
            <!DOCTYPE root [ <!ENTITY xxe SYSTEM "file://${secret.absolutePath}"> ]>
            <root>&xxe;</root>
        """.trimIndent()

        try {
            val doc = parse(payload)
            // Some parsers strip rather than throw; either is acceptable, leaking is not.
            val text = doc.documentElement.textContent.orEmpty()
            if (text.contains("CANARY_SECRET_VALUE")) {
                fail("XXE: external entity was resolved and local file contents leaked into the document")
            }
        } catch (expected: Exception) {
            // Refusing to parse a DOCTYPE at all is the intended outcome.
        }
    }

    @Test
    fun `parameter entity pointing at a local file is refused`() {
        val payload = """
            <?xml version="1.0"?>
            <!DOCTYPE root [ <!ENTITY % pe SYSTEM "file:///etc/passwd"> %pe; ]>
            <root/>
        """.trimIndent()

        try {
            val doc = parse(payload)
            val text = doc.documentElement.textContent.orEmpty()
            if (text.contains("root:")) {
                fail("XXE: parameter entity resolved and /etc/passwd contents reached the document")
            }
        } catch (expected: Exception) {
            // Expected.
        }
    }

    @Test
    fun `billion laughs entity expansion does not hang or exhaust memory`() {
        // Nested entities expanding to ~3^9 nodes. Unhardened this is a denial of service on a
        // file the user merely opened. The parser must refuse promptly rather than expand.
        val payload = """
            <?xml version="1.0"?>
            <!DOCTYPE lolz [
              <!ENTITY lol "lol">
              <!ENTITY lol1 "&lol;&lol;&lol;">
              <!ENTITY lol2 "&lol1;&lol1;&lol1;">
              <!ENTITY lol3 "&lol2;&lol2;&lol2;">
              <!ENTITY lol4 "&lol3;&lol3;&lol3;">
              <!ENTITY lol5 "&lol4;&lol4;&lol4;">
              <!ENTITY lol6 "&lol5;&lol5;&lol5;">
              <!ENTITY lol7 "&lol6;&lol6;&lol6;">
              <!ENTITY lol8 "&lol7;&lol7;&lol7;">
              <!ENTITY lol9 "&lol8;&lol8;&lol8;">
            ]>
            <lolz>&lol9;</lolz>
        """.trimIndent()

        val startedAt = System.currentTimeMillis()
        try {
            val doc = parse(payload)
            val expanded = doc.documentElement.textContent.orEmpty()
            if (expanded.length > 100_000) {
                fail("Entity expansion ran: produced ${expanded.length} chars from a 10-line document")
            }
        } catch (expected: Exception) {
            // Expected.
        }
        val elapsed = System.currentTimeMillis() - startedAt
        if (elapsed > 5_000) {
            fail("Parsing the entity bomb took ${elapsed}ms — expansion is not being refused promptly")
        }
    }

    @Test
    fun `plain doctype declaration is rejected`() {
        // No MKS import format uses a DTD, so DOCTYPE itself is the thing being refused. If this
        // ever starts passing, disallow-doctype-decl silently stopped applying and every case
        // above is resting on weaker fallbacks.
        try {
            parse("""<?xml version="1.0"?><!DOCTYPE root><root/>""")
            fail("DOCTYPE was accepted; disallow-doctype-decl is not in effect")
        } catch (expected: Exception) {
            // Expected.
        }
    }
}
