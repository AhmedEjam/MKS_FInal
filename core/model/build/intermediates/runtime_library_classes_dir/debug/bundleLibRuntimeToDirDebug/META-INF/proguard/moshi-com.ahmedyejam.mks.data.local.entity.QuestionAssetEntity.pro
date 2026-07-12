-keepnames class com.ahmedyejam.mks.data.local.entity.QuestionAssetEntity
-if class com.ahmedyejam.mks.data.local.entity.QuestionAssetEntity
-keep class com.ahmedyejam.mks.data.local.entity.QuestionAssetEntityJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.ahmedyejam.mks.data.local.entity.QuestionAssetEntity
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.ahmedyejam.mks.data.local.entity.QuestionAssetEntity {
    public synthetic <init>(long,long,long,long,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.Long,java.lang.String,java.lang.Long,java.lang.String,java.lang.String,int,boolean,boolean,long,long,java.lang.Long,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
