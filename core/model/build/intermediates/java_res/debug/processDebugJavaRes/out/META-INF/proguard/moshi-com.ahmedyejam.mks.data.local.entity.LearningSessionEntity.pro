-keepnames class com.ahmedyejam.mks.data.local.entity.LearningSessionEntity
-if class com.ahmedyejam.mks.data.local.entity.LearningSessionEntity
-keep class com.ahmedyejam.mks.data.local.entity.LearningSessionEntityJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.ahmedyejam.mks.data.local.entity.LearningSessionEntity
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.ahmedyejam.mks.data.local.entity.LearningSessionEntity {
    public synthetic <init>(long,long,java.lang.String,java.lang.String,boolean,long,long,java.lang.Long,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
