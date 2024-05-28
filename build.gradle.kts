import java.util.Properties

private val localProperties = "local.properties"
private val mapApiKey = "MAPKIT_API_KEY"
private val mapKitKey by extra(getMapkitApiKey())
private val kotlinCompileExtensionVersion: String by extra("1.5.13")

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}

fun getMapkitApiKey(): String {
    val inputStreamProperties = project.file(localProperties).inputStream()
    val properties = Properties().apply { load(inputStreamProperties) }
    return properties.getProperty(mapApiKey)
}
