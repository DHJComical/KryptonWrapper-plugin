plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.fcl.plugin.mobileglues"
    compileSdk = 35


    defaultConfig {
        applicationId = "com.fcl.plugin.kryptonwrapper"
        minSdk = 26
        targetSdk = 35
        versionCode = 2
        versionName = "Release 0.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("../keystore.jks")
            storePassword = "666666"
            keyAlias = "KWkey"
            keyPassword = "666666"
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        configureEach {
            resValue("string","app_name","Krypton Wrapper")

            manifestPlaceholders["des"] = "Krypton Wrapper (OpenGL 3.0)"
            manifestPlaceholders["renderer"] = "NGGL4ES:libng_gl4es.so:libEGL_angle.so"
            
            manifestPlaceholders["minMCVer"] = "1.6.4"
            manifestPlaceholders["maxMCVer"] = "1.21.6" 

            manifestPlaceholders["boatEnv"] = mutableMapOf<String,String>().apply {
                put("LIBGL_EGL","libEGL_angle.so")
                put("LIBGL_GLES","libGLESv2_angle.so")
                put("LIBGL_USE_MC_COLOR","1")
                put("DLOPEN","libspirv-cross-c-shared.so")
                put("LIBGL_GL","30")
                put("LIBGL_ES","3")
                put("LIBGL_MIPMAP","3")
                put("LIBGL_NORMALIZE","1")
                put("LIBGL_NOINTOVLHACK","1")
                put("LIBGL_NOERROR","1")
            }.run {
                var env = ""
                forEach { (key, value) ->
                    env += "$key=$value:"
                }
                env.dropLast(1)
            }
            manifestPlaceholders["pojavEnv"] = manifestPlaceholders["boatEnv"] as String + (mutableMapOf<String,String>().apply {
                put("POJAV_RENDERER","opengles3")
            }.run {
                var env = ":"
                forEach { (key, value) ->
                    env += "$key=$value:"
                }
                env.dropLast(1)
            })
            buildConfigField("boolean", "useANGLE", "true")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.documentfile)
    implementation(libs.gson)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.google.material)
    implementation(project(":NG-GL4ES"))
}
