-keepnames class com.ahmedyejam.mks.data.local.entity.QuizEntity
-if class com.ahmedyejam.mks.data.local.entity.QuizEntity
-keep class com.ahmedyejam.mks.data.local.entity.QuizEntityJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.ahmedyejam.mks.data.local.entity.QuizEntity
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.ahmedyejam.mks.data.local.entity.QuizEntity {
    public synthetic <init>(long,java.lang.String,long,java.lang.String,java.lang.String,java.lang.String,java.util.List,java.lang.String,java.lang.String,long,long,long,long,long,boolean,boolean,int,int,int,float,float,java.lang.Long,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
