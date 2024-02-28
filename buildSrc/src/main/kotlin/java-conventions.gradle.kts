plugins {
    java
    jacoco
}

java {
    targetCompatibility = JavaVersion.VERSION_17
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

jacoco {
    toolVersion = "0.8.9"
}
tasks.getByName<Test>("test") {
    useJUnitPlatform()
}