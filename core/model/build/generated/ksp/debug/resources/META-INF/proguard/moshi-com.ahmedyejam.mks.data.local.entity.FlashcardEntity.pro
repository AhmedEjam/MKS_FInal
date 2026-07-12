-keepnames class com.ahmedyejam.mks.data.local.entity.FlashcardEntity
-if class com.ahmedyejam.mks.data.local.entity.FlashcardEntity
-keep class com.ahmedyejam.mks.data.local.entity.FlashcardEntityJsonAdapter {
    public <init>(com.squareup.moshi.Moshi);
}
-if class com.ahmedyejam.mks.data.local.entity.FlashcardEntity
-keepnames class kotlin.jvm.internal.DefaultConstructorMarker
-keepclassmembers class com.ahmedyejam.mks.data.local.entity.FlashcardEntity {
    public synthetic <init>(long,java.lang.String,long,java.lang.String,java.lang.String,java.lang.String,java.lang.String,java.util.List,int,int,int,java.lang.String,long,int,long,long,long,java.lang.Long,java.util.Map,java.lang.Long,int,kotlin.jvm.internal.DefaultConstructorMarker);
}
