plugins {
	id("org.springframework.boot")
	kotlin("jvm")
	kotlin("plugin.spring")
}

tasks {
	named("bootJar") {
		enabled = false
	}
	named("jar") {
		enabled = true
	}
}

dependencies {
    implementation(project(":base"))
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	implementation("org.springframework.boot:spring-boot-starter-hateoas")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("com.razorpay:razorpay-java:1.3.9")
	implementation("javax.xml.bind:jaxb-api:2.3.0")
}

configurations {
	all {
		exclude(module = "spring-boot-starter-tomcat")
	}
}