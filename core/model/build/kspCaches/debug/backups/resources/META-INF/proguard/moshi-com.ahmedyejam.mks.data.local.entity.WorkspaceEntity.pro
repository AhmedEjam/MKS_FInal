-keepnames class com.ahmedyejam.mks.data.local.entity.WorkspaceEntity
-if class com.ahmedyejam.mks.data.local.entity.WorkspaceEntity
-keep class com.ahmedyejam.mks.data.local.entity.WorkspaceEntityJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.ahmedyejam.mks.data.local.entity.WorkspaceEntity
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.ahmedyejam.mks.data.local.entity.WorkspaceEntity {
    public synthetic <init>(long,java.lang.String,java.lang.String,java.lang.String,boolean,long,long,java.lang.Long,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
