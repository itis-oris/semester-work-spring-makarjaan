plugins {
    id("java")
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    id ("org.liquibase.gradle") version "2.2.0"
    kotlin("jvm")
}

group = "com.makarova"
version = "1.0-SNAPSHOT"

val springVersion: String by project
val springSecurityVersion: String by project
val jakartaVersion: String by project
val hibernateVersion: String by project
val postgresVersion: String by project
val lombokVersion: String by project

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    implementation("com.cloudinary:cloudinary-core:1.36.0")
    implementation("com.cloudinary:cloudinary-http44:1.36.0")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity5:3.1.1.RELEASE")

    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.webjars:stomp-websocket:2.3.4")
    implementation("org.webjars:sockjs-client:1.5.1")
    implementation("org.webjars:jquery:3.6.0")
    implementation("org.webjars:bootstrap:4.6.0")
    implementation("org.webjars:webjars-locator-core:0.46")


    implementation("jakarta.servlet:jakarta.servlet-api:$jakartaVersion")
    implementation("org.postgresql:postgresql:$postgresVersion")
    implementation("javax.mail:javax.mail-api:1.6.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")
    implementation("org.springframework.security:spring-security-taglibs:$springSecurityVersion")
    implementation("org.apache.tomcat:tomcat-jsp-api:10.1.20")
    implementation("javax.servlet.jsp:jsp-api:2.1")
    implementation(kotlin("stdlib-jdk8"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}