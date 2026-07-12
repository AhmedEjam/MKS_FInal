-keepnames class com.ahmedyejam.mks.data.model.LearningSessionState
-if class com.ahmedyejam.mks.data.model.LearningSessionState
-keep class com.ahmedyejam.mks.data.model.LearningSessionStateJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.ahmedyejam.mks.data.model.LearningSessionState
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.ahmedyejam.mks.data.model.LearningSessionState {
    public synthetic <init>(java.lang.String,long,java.lang.Long,java.lang.Long,java.util.Set,java.util.Map,int,java.util.Map,long,java.lang.Long,int,int,java.lang.String,java.util.Map,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
