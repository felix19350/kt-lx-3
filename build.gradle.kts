plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin on the JVM.
    id("org.jetbrains.kotlin.jvm").version("1.3.31")
    id("org.jlleitschuh.gradle.ktlint").version("8.2.0")

    // Apply the application plugin to add support for building a CLI application.
    application
}

val isCiBuild = System.getenv("CI_ENV") == "true"
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    withType<Test> {
        useJUnitPlatform {
            if (!isCiBuild) {
                excludeTags("Slow")
            }
        }
    }
}

repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    mavenCentral()
    jcenter()
}

dependencies {
    // Use the Kotlin JDK 8 standard library + co-routines
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.2")

    // Ktor
    implementation("io.ktor:ktor-server-core:1.2.0")
    implementation("io.ktor:ktor-server-netty:1.2.0")
    implementation("io.ktor:ktor-jackson:1.2.0")

    // Cassandra
    implementation("com.datastax.oss:java-driver-core:4.0.0")
    implementation("com.datastax.oss:java-driver-query-builder:4.0.0")

    // Import junit 5
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.4.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.4.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.4.1")

    // Kluent
    testImplementation("org.amshove.kluent:kluent:1.49")

    // Mockk
    testImplementation("io.mockk:mockk:1.9.3")

    // Ktor test server
    testImplementation("io.ktor:ktor-server-test-host:1.2.0")

    // Test containers +  cassandra
    testImplementation("org.testcontainers:junit-jupiter:1.11.3")
    testImplementation("org.testcontainers:cassandra:1.11.3")
}

application {
    // Define the main class for the application
    mainClassName = "lx.kotlin.AppKt"
}
