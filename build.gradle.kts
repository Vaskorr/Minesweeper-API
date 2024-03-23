
plugins {
	java
	application
	id("org.springframework.boot") version "3.2.4"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.6.0"
	kotlin("plugin.spring") version "1.6.0"
}

apply(plugin = "io.spring.dependency-management")

group = "com.vaskorr"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

application {
	mainModule.set("minesweeper_api.main")
	mainClass.set("com.vaskorr.minesweeper_api.MinesweeperApiApplication")
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

springBoot{
	mainClass.set("com.vaskorr.minesweeper_api.MinesweeperApiApplication")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks {
	val fatJar = register<Jar>("fatJar") {
		dependsOn.addAll(listOf("compileJava", "processResources")) // We need this for Gradle optimization to work
		archiveClassifier.set("standalone") // Naming the jar
		duplicatesStrategy = DuplicatesStrategy.EXCLUDE
		manifest { attributes(mapOf("Main-Class" to application.mainClass)) } // Provided we set it up in the application plugin configuration
		val sourcesMain = sourceSets.main.get()
		val contents = configurations.runtimeClasspath.get()
				.map { if (it.isDirectory) it else zipTree(it) } +
				sourcesMain.output
		from(contents)
	}
	build {
		dependsOn(fatJar) // Trigger fat jar creation during build
	}
}

