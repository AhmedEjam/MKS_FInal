-keepnames class com.ahmedyejam.mks.data.local.entity.SourceDocumentEntity
-if class com.ahmedyejam.mks.data.local.entity.SourceDocumentEntity
-keep class com.ahmedyejam.mks.data.local.entity.SourceDocumentEntityJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.ahmedyejam.mks.data.local.entity.SourceDocumentEntity
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.ahmedyejam.mks.data.local.entity.SourceDocumentEntity {
    public synthetic <init>(long,java.lang.Long,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,long,long,java.lang.Long,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
