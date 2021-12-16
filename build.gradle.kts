plugins {
    kotlin("jvm") version "1.6.0"
    java
}

//group = "evanrr"
version = "2021.08"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.tinylog:tinylog-api-kotlin:2.4.1")
    implementation("org.jetbrains.kotlin:kotlin-script-runtime:1.6.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}