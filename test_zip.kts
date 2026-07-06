import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.AesKeyStrength
import net.lingala.zip4j.model.enums.EncryptionMethod
import java.io.File

val tempDir = File.createTempFile("testdir", "")
tempDir.delete()
tempDir.mkdir()
File(tempDir, "test.txt").writeText("hello world")
File(tempDir, "nested").mkdir()
File(tempDir, "nested/test2.txt").writeText("hello nested")

val zipParameters = ZipParameters().apply {
    isEncryptFiles = true
    encryptionMethod = EncryptionMethod.AES
    aesKeyStrength = AesKeyStrength.KEY_STRENGTH_256
}
val tempZipFile = File.createTempFile("test_enc_", ".zip")
ZipFile(tempZipFile, "password".toCharArray()).use { zip ->
    tempDir.walkTopDown()
        .filter { it.isFile }
        .sortedBy { it.relativeTo(tempDir).invariantSeparatorsPath }
        .forEach { file ->
            val relativePath = file.relativeTo(tempDir).invariantSeparatorsPath
            zipParameters.fileNameInZip = relativePath
            zip.addFile(file, zipParameters)
        }
}
println("Zip size: " + tempZipFile.length())
