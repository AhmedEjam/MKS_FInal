-keepnames class com.ahmedyejam.mks.data.local.entity.QuestionEntity
-if class com.ahmedyejam.mks.data.local.entity.QuestionEntity
-keep class com.ahmedyejam.mks.data.local.entity.QuestionEntityJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.ahmedyejam.mks.data.local.entity.QuestionEntity
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.ahmedyejam.mks.data.local.entity.QuestionEntity {
    public synthetic <init>(long,java.lang.String,long,java.lang.String,com.ahmedyejam.mks.data.local.entity.QuestionType,java.util.List,java.util.List,java.lang.String,java.lang.String,java.lang.String,int,java.lang.String,java.lang.String,java.lang.String,int,int,boolean,java.lang.Long,java.lang.String,boolean,java.lang.Long,java.lang.String,java.lang.Long,java.lang.String,java.util.List,java.util.List,java.lang.String,long,int,long,java.lang.String,java.lang.String,java.lang.String,java.lang.String,long,long,long,long,long,java.lang.Boolean,int,java.lang.Long,int,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
