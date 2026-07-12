-keepnames class com.ahmedyejam.mks.data.local.entity.StudySessionEntity
-if class com.ahmedyejam.mks.data.local.entity.StudySessionEntity
-keep class com.ahmedyejam.mks.data.local.entity.StudySessionEntityJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.ahmedyejam.mks.data.local.entity.StudySessionEntity
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.ahmedyejam.mks.data.local.entity.StudySessionEntity {
    public synthetic <init>(long,java.lang.String,long,java.lang.String,java.lang.String,long,float,boolean,int,int,long,long,java.lang.Long,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
