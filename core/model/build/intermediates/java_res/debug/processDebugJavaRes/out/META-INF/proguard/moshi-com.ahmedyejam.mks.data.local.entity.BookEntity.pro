-keepnames class com.ahmedyejam.mks.data.local.entity.BookEntity
-if class com.ahmedyejam.mks.data.local.entity.BookEntity
-keep class com.ahmedyejam.mks.data.local.entity.BookEntityJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.ahmedyejam.mks.data.local.entity.BookEntity
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.ahmedyejam.mks.data.local.entity.BookEntity {
    public synthetic <init>(long,long,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.lang.String,long,long,long,long,long,boolean,boolean,java.util.List,int,int,int,float,float,java.lang.Long,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
