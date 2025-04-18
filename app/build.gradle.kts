import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
	application
	jacoco
	alias(libs.plugins.spring.boot)
	alias(libs.plugins.spring.dependency.management)
	alias(libs.plugins.sonarqube)
	alias(libs.plugins.lombok)
	alias(libs.plugins.sentry.jvm)
}

sonar {
	properties {
		property("sonar.projectKey", "danikirillov_java-project-99")
		property("sonar.organization", "danikirillov")
		property("sonar.host.url", "https://sonarcloud.io")
	}
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

application { mainClass.set("hexlet.code.app.AppApplication") }

repositories {
	mavenCentral()
}

buildscript {
	repositories {
		mavenCentral()
	}
}

sentry {
	// Generates a JVM (Java, Kotlin, etc.) source bundle and uploads your source code to Sentry.
	// This enables source context, allowing you to see your source
	// code as part of your stack traces in Sentry.
	includeSourceContext = true

	org = "dddddd-7d"
	projectName = "java-spring-boot"
	authToken = System.getenv("SENTRY_AUTH_TOKEN")
}

dependencies {
	implementation(libs.spring.boot.starter.web)
	implementation(libs.spring.boot.starter.data.jpa)
	implementation(libs.spring.boot.starter.validation)
	implementation(libs.spring.boot.starter.security)
	implementation(libs.spring.boot.starter.oauth2.resource.server)
	implementation(libs.spring.boot.starter.jdbc)
	implementation(libs.jackson.databind.nullable)

	// OpenAPI (Swagger)
	implementation(libs.springdoc.openapi.ui)

	// MapStruct
	implementation(libs.mapstruct)
	annotationProcessor(libs.mapstruct.processor)
	
	runtimeOnly(libs.h2)
	runtimeOnly(libs.postgresql)
//	developmentOnly("org.springframework.boot:spring-boot-devtools")
	
	// Test dependencies
	testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.security.test)
    testImplementation(libs.instancio.junit)
    testImplementation(libs.json.unit.assertj)
    testImplementation(libs.datafaker)
	testImplementation(platform(libs.junit.bom))
	testImplementation(libs.junit.jupiter)
}

tasks.jacocoTestReport { reports { xml.required.set(true) } }

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	useJUnitPlatform()
	testLogging {
		exceptionFormat = TestExceptionFormat.FULL
		events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
		showStandardStreams = true
	}
}