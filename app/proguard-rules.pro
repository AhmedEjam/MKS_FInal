# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# --- General Attributes ---
# Retain metadata for reflection and generic type resolution
-keepattributes RuntimeVisibleAnnotations,RuntimeInvisibleAnnotations,Signature,InnerClasses,EnclosingMethod

# --- AndroidX / Safety ---
# Keep classes and members annotated with @Keep
-keep @androidx.annotation.Keep class * {*;}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <fields>;
}

# --- Data Layer (MKS Specific) ---
# Protect the entire data layer as requested (Broad rule for safety)
# This prevents obfuscation issues with Room entities, DTOs, and repositories.
-keep class com.ahmedyejam.mks.data.** { *; }

# --- Kotlin Serialization ---
# Keep serializable classes and their companion objects to ensure stable JSON parsing
-keep @kotlinx.serialization.Serializable class * { *; }
-keepclassmembers class * {
    @kotlinx.serialization.Serializable *;
}

# --- Room & Moshi ---
# While libraries publish consumer rules, these ensure stability in complex R8 configurations
-keep class **JsonAdapter { *; }
-keep class **JsonAdapterKt { *; }
-keep class * extends androidx.room.RoomDatabase
-keep class * extends androidx.room.Entity
-keep class * implements androidx.room.Dao

# --- Third Party Fixes / Dontwarns ---
# Apache POI may reference optional XML/security backends depending on workbook contents.
-dontwarn org.apache.xmlbeans.**
-dontwarn org.bouncycastle.**
-dontwarn org.apache.commons.compress.**

# Fix for missing classes in log4j, findbugs, and poi
-dontwarn aQute.bnd.annotation.**
-dontwarn edu.umd.cs.findbugs.annotations.**
-dontwarn java.awt.**
-dontwarn javax.xml.stream.**
-dontwarn org.osgi.framework.**

# Apache POI & Aalto XML
-keep class com.fasterxml.aalto.** { *; }
-keep class javax.xml.stream.** { *; }
-keep class org.apache.poi.** { *; }
-keep interface org.apache.poi.** { *; }
-keep class org.apache.xmlbeans.** { *; }
-keep interface org.apache.xmlbeans.** { *; }
-keep class schemaorg_apache_xmlbeans.** { *; }

# Preserve ServiceLoader files for POI providers
-keepclassmembers class * {
    *** *Service(...);
}

# --- R8 Missing Classes Auto-generated Rules ---
-dontwarn de.rototor.pdfbox.graphics2d.IPdfBoxGraphics2DFontTextDrawer$IFontTextDrawerEnv
-dontwarn de.rototor.pdfbox.graphics2d.IPdfBoxGraphics2DFontTextDrawer
-dontwarn de.rototor.pdfbox.graphics2d.PdfBoxGraphics2D
-dontwarn de.rototor.pdfbox.graphics2d.PdfBoxGraphics2DFontTextDrawer
-dontwarn javax.imageio.ImageIO
-dontwarn javax.imageio.ImageReadParam
-dontwarn javax.imageio.ImageReader
-dontwarn javax.imageio.ImageTypeSpecifier
-dontwarn javax.imageio.metadata.IIOMetadata
-dontwarn javax.imageio.stream.ImageInputStream
-dontwarn javax.imageio.stream.MemoryCacheImageInputStream
-dontwarn javax.swing.JLabel
-dontwarn javax.xml.crypto.AlgorithmMethod
-dontwarn javax.xml.crypto.Data
-dontwarn javax.xml.crypto.KeySelector$Purpose
-dontwarn javax.xml.crypto.KeySelector
-dontwarn javax.xml.crypto.KeySelectorException
-dontwarn javax.xml.crypto.KeySelectorResult
-dontwarn javax.xml.crypto.MarshalException
-dontwarn javax.xml.crypto.OctetStreamData
-dontwarn javax.xml.crypto.URIDereferencer
-dontwarn javax.xml.crypto.URIReference
-dontwarn javax.xml.crypto.URIReferenceException
-dontwarn javax.xml.crypto.XMLCryptoContext
-dontwarn javax.xml.crypto.XMLStructure
-dontwarn javax.xml.crypto.dom.DOMStructure
-dontwarn javax.xml.crypto.dsig.CanonicalizationMethod
-dontwarn javax.xml.crypto.dsig.DigestMethod
-dontwarn javax.xml.crypto.dsig.Manifest
-dontwarn javax.xml.crypto.dsig.Reference
-dontwarn javax.xml.crypto.dsig.SignatureMethod
-dontwarn javax.xml.crypto.dsig.SignatureProperties
-dontwarn javax.xml.crypto.dsig.SignatureProperty
-dontwarn javax.xml.crypto.dsig.SignedInfo
-dontwarn javax.xml.crypto.dsig.Transform
-dontwarn javax.xml.crypto.dsig.TransformException
-dontwarn javax.xml.crypto.dsig.TransformService
-dontwarn javax.xml.crypto.dsig.XMLObject
-dontwarn javax.xml.crypto.dsig.XMLSignContext
-dontwarn javax.xml.crypto.dsig.XMLSignature
-dontwarn javax.xml.crypto.dsig.XMLSignatureException
-dontwarn javax.xml.crypto.dsig.XMLSignatureFactory
-dontwarn javax.xml.crypto.dsig.XMLValidateContext
-dontwarn javax.xml.crypto.dsig.dom.DOMSignContext
-dontwarn javax.xml.crypto.dsig.dom.DOMValidateContext
-dontwarn javax.xml.crypto.dsig.keyinfo.KeyInfo
-dontwarn javax.xml.crypto.dsig.keyinfo.KeyInfoFactory
-dontwarn javax.xml.crypto.dsig.keyinfo.KeyValue
-dontwarn javax.xml.crypto.dsig.keyinfo.X509Data
-dontwarn javax.xml.crypto.dsig.keyinfo.X509IssuerSerial
-dontwarn javax.xml.crypto.dsig.spec.C14NMethodParameterSpec
-dontwarn javax.xml.crypto.dsig.spec.DigestMethodParameterSpec
-dontwarn javax.xml.crypto.dsig.spec.SignatureMethodParameterSpec
-dontwarn javax.xml.crypto.dsig.spec.TransformParameterSpec
-dontwarn org.apache.batik.anim.dom.SAXSVGDocumentFactory
-dontwarn org.apache.batik.bridge.BridgeContext
-dontwarn org.apache.batik.bridge.DocumentLoader
-dontwarn org.apache.batik.bridge.GVTBuilder
-dontwarn org.apache.batik.bridge.UserAgent
-dontwarn org.apache.batik.bridge.UserAgentAdapter
-dontwarn org.apache.batik.bridge.ViewBox
-dontwarn org.apache.batik.dom.GenericDOMImplementation
-dontwarn org.apache.batik.ext.awt.RenderingHintsKeyExt
-dontwarn org.apache.batik.ext.awt.image.renderable.ClipRable8Bit
-dontwarn org.apache.batik.ext.awt.image.renderable.ClipRable
-dontwarn org.apache.batik.ext.awt.image.renderable.Filter
-dontwarn org.apache.batik.gvt.GraphicsNode
-dontwarn org.apache.batik.parser.DefaultLengthHandler
-dontwarn org.apache.batik.parser.LengthHandler
-dontwarn org.apache.batik.parser.LengthParser
-dontwarn org.apache.batik.svggen.DOMTreeManager
-dontwarn org.apache.batik.svggen.DefaultExtensionHandler
-dontwarn org.apache.batik.svggen.ExtensionHandler
-dontwarn org.apache.batik.svggen.SVGColor
-dontwarn org.apache.batik.svggen.SVGGeneratorContext$GraphicContextDefaults
-dontwarn org.apache.batik.svggen.SVGGeneratorContext
-dontwarn org.apache.batik.svggen.SVGGraphics2D
-dontwarn org.apache.batik.svggen.SVGIDGenerator
-dontwarn org.apache.batik.svggen.SVGPaintDescriptor
-dontwarn org.apache.batik.svggen.SVGTexturePaint
-dontwarn org.apache.batik.util.XMLResourceDescriptor
-dontwarn org.apache.jcp.xml.dsig.internal.dom.ApacheNodeSetData
-dontwarn org.apache.jcp.xml.dsig.internal.dom.DOMKeyInfo
-dontwarn org.apache.jcp.xml.dsig.internal.dom.DOMReference
-dontwarn org.apache.jcp.xml.dsig.internal.dom.DOMSignedInfo
-dontwarn org.apache.jcp.xml.dsig.internal.dom.DOMSubTreeData
-dontwarn org.apache.pdfbox.pdmodel.PDDocument
-dontwarn org.apache.pdfbox.pdmodel.PDPage
-dontwarn org.apache.pdfbox.pdmodel.PDPageContentStream
-dontwarn org.apache.pdfbox.pdmodel.common.PDRectangle
-dontwarn org.apache.pdfbox.pdmodel.font.PDFont
-dontwarn org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject
-dontwarn org.apache.xml.security.Init
-dontwarn org.apache.xml.security.c14n.Canonicalizer
-dontwarn org.apache.xml.security.signature.XMLSignatureInput
-dontwarn org.apache.xml.security.utils.XMLUtils
-dontwarn org.ietf.jgss.GSSException
-dontwarn org.ietf.jgss.Oid
-dontwarn org.w3c.dom.events.Event
-dontwarn org.w3c.dom.events.EventListener
-dontwarn org.w3c.dom.events.EventTarget
-dontwarn org.w3c.dom.events.MutationEvent
-dontwarn org.w3c.dom.svg.SVGDocument
-dontwarn org.w3c.dom.svg.SVGSVGElement
-dontwarn org.w3c.dom.traversal.DocumentTraversal
-dontwarn org.w3c.dom.traversal.NodeFilter
-dontwarn org.w3c.dom.traversal.NodeIterator
