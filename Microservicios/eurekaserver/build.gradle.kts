plugins {
  id("org.springframework.boot") version "3.5.6"
  id("io.spring.dependency-management") version "1.1.7"
  kotlin("jvm") version "1.9.25"
  kotlin("plugin.spring") version "1.9.25"
}

repositories { mavenCentral() }

val springCloudVersion = "2025.0.0"
dependencyManagement {
  imports {
    mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
  }
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
}

java {
  toolchain { languageVersion = JavaLanguageVersion.of(21) }
}
