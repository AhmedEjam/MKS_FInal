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

# --- Enum Keep Rules (Finding ①) ---
# Prevent R8 from removing or renaming enum members used with valueOf() or name()
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
# Explicitly keep QuestionType and its constants
-keep enum com.ahmedyejam.mks.data.local.entity.QuestionType { *; }

# --- Moshi Keep Rules (Finding ② & ⑤) ---
# Keep all generated JsonAdapters and ensure reflection works for generic types
-keep class com.ahmedyejam.mks.data.local.entity.**JsonAdapter { *; }
-keep class com.ahmedyejam.mks.data.model.**JsonAdapter { *; }
-keep class com.squareup.moshi.** { *; }
-keepattributes *Annotation*, Signature, EnclosingMethod, InnerClasses

# Prevent Moshi from stripping reflection-based adapter logic for generic types
-keepclassmembers class com.squareup.moshi.Types {
    public static *** newParameterizedType(...);
}

# Preserve names and fields of classes annotated with @JsonClass for adapter lookup
-keep @com.squareup.moshi.JsonClass class * { *; }
-keepclassmembers class * {
    @com.squareup.moshi.JsonClass <init>(...);
}

# Preservation of generic signatures is vital for Moshi's reflection on List<T>
-keep class java.util.List { *; }
-keep class java.util.Map { *; }
-keep class java.util.ArrayList { *; }
-keep class java.util.HashMap { *; }
-keep class java.util.Set { *; }
-keep class java.util.HashSet { *; }

# --- Room / Data Access (Finding ③) ---
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Dao interface *
-keep class * implements androidx.room.Dao
-keep @androidx.room.Entity class *
-keep class com.ahmedyejam.mks.data.local.MksDatabase { *; }
-keep class com.ahmedyejam.mks.data.local.dao.** { *; }
-keep class com.ahmedyejam.mks.data.repository.** { *; }
-keep class com.ahmedyejam.mks.data.local.Converters { *; }

# Keep Room's generated implementation classes
-keep class *_*_Impl { *; }
-keep class com.ahmedyejam.mks.data.local.MksDatabase_Impl { *; }
-keep class com.ahmedyejam.mks.data.local.dao.**_Impl { *; }

# Prevent Room entities from being stripped of their fields (crucial for reflection binding)
-keepclassmembers @androidx.room.Entity class * {
    <fields>;
    <init>(...);
}

# --- Hilt / Dagger (Finding ④) ---
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep @dagger.hilt.android.AndroidEntryPoint class *
-keep @com.google.dagger.** class *
-keep class * {
    @javax.inject.Inject <fields>;
    @javax.inject.Inject <init>(...);
}

# --- Data Seeding (R8 Safety) ---
# Ensure the seeder is preserved
-keep class com.ahmedyejam.mks.data.seeder.** { *; }

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
-dontwarn org.osgi.annotation.**
-dontwarn org.codehaus.stax2.**
-dontwarn org.apache.poi.xslf.draw.**
-dontwarn org.apache.poi.sl.draw.**

# Apache POI & Aalto XML
-keep class com.fasterxml.aalto.** { *; }
-keep class javax.xml.stream.** { *; }
-keep class org.apache.poi.** { *; }
-keep interface org.apache.poi.** { *; }
-keep class org.apache.xmlbeans.** { *; }
-keep interface org.apache.xmlbeans.** { *; }
-keep class schemaorg_apache_xmlbeans.** { *; }
-keep class org.openxmlformats.** { *; }
-keep interface org.openxmlformats.** { *; }
-keep class com.microsoft.schemas.** { *; }
-keep interface com.microsoft.schemas.** { *; }

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

# --- Network / Image Loading ---
-keep class com.ahmedyejam.mks.data.network.** { *; }
-keep class okhttp3.** { *; }
-dontwarn okhttp3.**

# --- Apache POI / OpenXML Schemas (Missing Rules) ---
-dontwarn org.openxmlformats.schemas.**
-dontwarn com.microsoft.schemas.**

# --- PDFBox Missing Classes (JP2Decoder / JP2Encoder) ---
-dontwarn com.gemalto.jp2.**
-dontwarn com.tom_roush.pdfbox.**
