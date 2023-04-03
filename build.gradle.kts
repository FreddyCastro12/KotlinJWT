import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.0.0-SNAPSHOT"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.0"
	kotlin("plugin.spring") version "1.6.0"
	kotlin("plugin.jpa") version "1.6.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	implementation("org.jboss.logging:jboss-logging:3.5.0.Final")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("io.jsonwebtoken:jjwt:0.9.1")
	implementation("net.sf.jasperreports:jasperreports:6.19.1")
	implementation("hu.blackbelt.bundles.itext:com.lowagie.itext:2.1.7-1")
	compileOnly("net.sf.jasperreports:jasperreports:6.20.0")
	implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")
	implementation("com.google.zxing:core:3.5.1")
	implementation("com.google.zxing:javase:3.5.1")
	implementation("mysql:mysql-connector-java")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
