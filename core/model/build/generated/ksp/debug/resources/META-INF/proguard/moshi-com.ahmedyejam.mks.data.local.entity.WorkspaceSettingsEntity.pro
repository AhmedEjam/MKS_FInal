-keepnames class com.ahmedyejam.mks.data.local.entity.WorkspaceSettingsEntity
-if class com.ahmedyejam.mks.data.local.entity.WorkspaceSettingsEntity
-keep class com.ahmedyejam.mks.data.local.entity.WorkspaceSettingsEntityJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.ahmedyejam.mks.data.local.entity.WorkspaceSettingsEntity
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.ahmedyejam.mks.data.local.entity.WorkspaceSettingsEntity {
    public synthetic <init>(long,long,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,long,long,java.lang.Long,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
