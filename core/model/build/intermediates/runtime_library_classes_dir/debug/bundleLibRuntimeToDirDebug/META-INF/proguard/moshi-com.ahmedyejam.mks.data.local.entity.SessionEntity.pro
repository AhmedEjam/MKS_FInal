-keepnames class com.ahmedyejam.mks.data.local.entity.SessionEntity
-if class com.ahmedyejam.mks.data.local.entity.SessionEntity
-keep class com.ahmedyejam.mks.data.local.entity.SessionEntityJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.ahmedyejam.mks.data.local.entity.SessionEntity
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.ahmedyejam.mks.data.local.entity.SessionEntity {
    public synthetic <init>(long,long,java.lang.String,int,int,int,java.util.Map,java.util.Map,boolean,long,long,long,long,long,java.util.List,int,boolean,boolean,boolean,boolean,int,int,int,int,java.util.List,java.util.Map,java.util.Map,java.util.Map,java.util.Map,int,int,java.lang.Long,java.util.Map,int,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
