-keepnames class com.ahmedyejam.mks.data.local.entity.FlashcardDeckEntity
-if class com.ahmedyejam.mks.data.local.entity.FlashcardDeckEntity
-keep class com.ahmedyejam.mks.data.local.entity.FlashcardDeckEntityJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.ahmedyejam.mks.data.local.entity.FlashcardDeckEntity
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.ahmedyejam.mks.data.local.entity.FlashcardDeckEntity {
    public synthetic <init>(long,java.lang.String,long,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.util.List,int,int,float,boolean,boolean,long,long,long,long,java.lang.Long,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
