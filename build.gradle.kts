@file:Suppress("SpellCheckingInspection")

plugins {
	id("java")
}

group = "com.kissene3"
version = "1.1"

repositories {
	mavenCentral()
}

dependencies {
	testImplementation(platform("org.junit:junit-bom:5.10.0"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	compileOnly("org.jetbrains:annotations:26.0.2")

	implementation("com.google.code.gson:gson:2.13.2")
	implementation("com.github.weisj:jsvg:2.2.0")
}

tasks.test {
	useJUnitPlatform()
}

tasks.jar {
	manifest {
		attributes["Main-Class"] = "com.kissene3.main.Main"
		attributes["Implementation-Version"] = project.version
	}

	from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}