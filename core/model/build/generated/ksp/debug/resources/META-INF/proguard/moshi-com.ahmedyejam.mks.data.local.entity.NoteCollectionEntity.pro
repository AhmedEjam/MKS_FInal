-keepnames class com.ahmedyejam.mks.data.local.entity.NoteCollectionEntity
-if class com.ahmedyejam.mks.data.local.entity.NoteCollectionEntity
-keep class com.ahmedyejam.mks.data.local.entity.NoteCollectionEntityJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.ahmedyejam.mks.data.local.entity.NoteCollectionEntity
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.ahmedyejam.mks.data.local.entity.NoteCollectionEntity {
    public synthetic <init>(long,java.lang.String,long,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.util.List,int,boolean,boolean,long,long,long,long,java.lang.Long,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
