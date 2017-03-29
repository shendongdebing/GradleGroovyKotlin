plugins {
    base
    idea
}

buildscript {
    //Temporary hack until Android plugin has proper support
    System.setProperty("com.android.build.gradle.overrideVersionCheck", "true")

    repositories {
        jcenter()
        gradleScriptKotlin()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:2.2.0")
        classpath(kotlinModule("gradle-plugin"))
    }
}

allprojects {
    repositories {
        jcenter()
        gradleScriptKotlin()
    }
}

dependencies {
    // Make the root project archives configuration depend on every subproject
    subprojects.forEach {
        archives(it)
    }
}
