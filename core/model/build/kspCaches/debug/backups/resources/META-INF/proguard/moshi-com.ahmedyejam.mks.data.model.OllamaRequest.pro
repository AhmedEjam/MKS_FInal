-keepnames class com.ahmedyejam.mks.data.model.OllamaRequest
-if class com.ahmedyejam.mks.data.model.OllamaRequest
-keep class com.ahmedyejam.mks.data.model.OllamaRequestJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.ahmedyejam.mks.data.model.OllamaRequest
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.ahmedyejam.mks.data.model.OllamaRequest {
    public synthetic <init>(java.lang.String,java.lang.String,java.lang.String,boolean,java.util.Map,java.util.List,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
