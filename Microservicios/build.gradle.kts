
plugins {
  id("org.springframework.boot") version "3.5.6" apply false
  id("io.spring.dependency-management") version "1.1.7" apply false
  kotlin("jvm") version "1.9.25" apply false
  kotlin("plugin.spring") version "1.9.25" apply false
}

val springCloudVersion by extra("2025.0.0")

subprojects {
  group = "com.example"
  version = "0.0.1-SNAPSHOT"

  repositories { mavenCentral() }

  // aplicar dependency-management a cada submódulo
  pluginManager.apply("io.spring.dependency-management")
  dependencyManagement {
    imports { mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion") }
  }
}
