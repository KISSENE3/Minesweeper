plugins {
	id("java")
}

group = "com.kissene3"
version = "1.0-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {
	testImplementation(platform("org.junit:junit-bom:5.10.0"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	compileOnly("org.jetbrains:annotations:26.0.2")

	implementation("com.google.code.gson:gson:2.13.2")
}

tasks.test {
	useJUnitPlatform()
}

tasks.register("setupProject") {
	doLast {
		mkdir("data/saves")
		mkdir("data/themes") // This directory is for custom themes
		println("Directories data/saves and data/themes created successfully.")
	}
}

tasks.compileJava {
	dependsOn("setupProject")
}