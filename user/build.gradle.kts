plugins {
	id("org.springframework.boot")
	kotlin("jvm")
	kotlin("plugin.spring")
}

dependencies {
	implementation(project(":base"))
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	implementation("org.springframework.boot:spring-boot-starter-hateoas")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("com.google.firebase:firebase-admin:7.1.1")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
}

tasks {
	named("bootJar") {
		enabled = false
	}
	named("jar") {
		enabled = true
	}
}

configurations {
	all {
		exclude(module = "spring-boot-starter-tomcat")
	}
}