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
		val themesDestination = file("data/themes")
		val difficultiesDestination = file("data/difficulties")

		copy {
			from("src/main/resources/themes/") {
				include("*.json")
				include("README.md")
			}
			into(themesDestination)

			eachFile {
				val targetFile = File(themesDestination, path)
				if (targetFile.exists() && "README.md" != targetFile.name) {
					exclude()
				}
			}
		}
		copy {
			from("src/main/resources/difficulties/") {
				include("*.json")
				include("README.md")
			}
			into(difficultiesDestination)

			eachFile {
				val targetFile = File(difficultiesDestination, path)
				if (targetFile.exists() && "README.md" != targetFile.name) {
					exclude()
				}
			}
		}

		println("Setup completed: file JSON and README verified.")
	}
}

tasks.compileJava {
	dependsOn("setupProject")
}