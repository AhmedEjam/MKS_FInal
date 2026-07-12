-keepnames class com.ahmedyejam.mks.data.local.entity.AnnotationEntity
-if class com.ahmedyejam.mks.data.local.entity.AnnotationEntity
-keep class com.ahmedyejam.mks.data.local.entity.AnnotationEntityJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.ahmedyejam.mks.data.local.entity.AnnotationEntity
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.ahmedyejam.mks.data.local.entity.AnnotationEntity {
    public synthetic <init>(long,long,long,java.lang.String,long,java.lang.String,java.lang.String,java.lang.String,java.lang.String,long,long,java.lang.Long,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
