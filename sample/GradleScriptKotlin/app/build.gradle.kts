import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.internal.dsl.SigningConfig
import com.android.builder.core.DefaultApiVersion
import com.android.builder.core.DefaultProductFlavor
import com.android.builder.model.ApiVersion
import org.gradle.api.Project
import org.gradle.script.lang.kotlin.*
import org.jetbrains.kotlin.gradle.plugin.KotlinAndroidPluginWrapper
import java.io.File
import java.text.SimpleDateFormat

//Extension functions to allow comfortable references
fun Project.android(configuration: AppExtension.() -> Unit) = configure(configuration)
fun DefaultProductFlavor.setMinSdkVersion(value: Int) = setMinSdkVersion(value.asApiVersion())
fun DefaultProductFlavor.setTargetSdkVersion(value: Int) = setTargetSdkVersion(value.asApiVersion())
fun Int.asApiVersion(): ApiVersion = DefaultApiVersion.create(this)

apply {
    plugin<AppPlugin>()
    plugin<KotlinAndroidPluginWrapper>()
    plugin("kotlin-android-extensions")
}

android {
    compileSdkVersion(25)
    buildToolsVersion("23.0.3")

    var config: SigningConfig? = null
    signingConfigs {
        config = SigningConfig("MyConfig").apply {
            storeFile = File("./app/Depth.jks")
            keyAlias = "ice"
            keyPassword = "123456"
            storePassword = "123456"
        }
    }

    defaultConfig {
        applicationId = "com.ice.gradlescriptkotlin"
        minSdkVersion(14)
        targetSdkVersion(25)
        versionCode = 1
        versionName = "0.0.1"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isZipAlignEnabled = false
            isShrinkResources = false
            versionNameSuffix = "-debug"
            signingConfig = config
        }
        getByName("release") {
            isMinifyEnabled = true
            isZipAlignEnabled = true
            isShrinkResources = true
            proguardFiles("proguard-rules.pro")
            signingConfig = config
        }
    }

    /* // invalid play
    applicationVariants.forEach { variant ->
        variant.outputs.forEach { outPut ->
            val outPutFile = outPut.outputFile
            var fileName = ""
            if (outPutFile.name.endsWith(".apk")) {
                if (variant.buildType.name == "release") {
                    fileName = "${project.name}-${variant.buildType.name}-${defaultConfig.versionName}-${releaseTime()}.apk"
                } else if (variant.buildType.name == "debug") {
                    fileName = "${project.name}-${variant.buildType.name}-${defaultConfig.versionName}.apk"
                }
                outPut.outputFile = File(outPutFile.parent, fileName)
            }
        }
    }
    */

    lintOptions {
        isAbortOnError = false
    }
}

fun releaseTime(): String {
    return SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())
}

val kotlin_version = "1.1.1"
val anko_version = "0.9.1"

dependencies {
    compile(kotlinModule("stdlib"))
    compile("com.android.support:appcompat-v7:23.4.0")
    compile("com.android.support:design:25.1.1")
    compile("com.android.support:recyclerview-v7:25.1.1")

    compile("com.android.support.constraint:constraint-layout:1.0.0-alpha8")

    compile("org.jetbrains.kotlin:kotlin-stdlib-jre7:$kotlin_version")
    compile("org.jetbrains.anko:anko-sdk15:$anko_version")

    compile("com.github.bumptech.glide:glide:3.7.0")
    compile("com.google.code.gson:gson:2.6.2")
    compile("com.loopj.android:android-async-http:1.4.9")
}