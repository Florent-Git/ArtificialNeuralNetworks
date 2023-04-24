plugins {
    kotlin("jvm") version "1.8.10"
    kotlin("plugin.serialization") version "1.8.10"
}

group = "be.mgx"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("info.picocli:picocli:4.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("de.brudaswen.kotlinx.serialization:kotlinx-serialization-csv:2.0.0")

    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    implementation("ch.qos.logback:logback-classic:1.4.6")

    implementation("org.beryx:text-io:3.4.1")

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.0")

    implementation("org.jfree:jfreechart:1.5.3")
}

tasks.test {
    useJUnitPlatform()
}

enum class Lesson {
    PERCEPTRON,
    PERCEPTRON_MONO,
    PERCEPTRON_MULTI,
    SIGN_LANG;

    override fun toString(): String {
        return super.toString()
            .replace("_", "-").toLowerCase()
//            .lowercase()
    }
}

enum class Operations {
    INIT,
    TRAIN,
    EXECUTE;

    override fun toString(): String {
        return super.toString()
            .replace("_", "-").toLowerCase()
//            .lowercase()
    }
}

enum class Types(
    val dataFile: String,
    val lesson: Lesson,
    val exampleInput: String
) {
    LOGICAL_AND("table_and_basic.csv", Lesson.PERCEPTRON, "1,1"),
    LOGICAL_AND_GRADIENT("table_and_grad.csv", Lesson.PERCEPTRON, "1,1"),
    LOGICAL_AND_ADALINE("table_and_grad.csv", Lesson.PERCEPTRON, "1,1"),
    LINEAR_CLASSIFICATION_GRADIENT("table_2_9.csv", Lesson.PERCEPTRON, "10,-3"),
    LINEAR_CLASSIFICATION_ADALINE("table_2_9.csv", Lesson.PERCEPTRON, "10,-3"),
    NONLINEAR_CLASSIFICATION_GRADIENT("table_2_10.csv", Lesson.PERCEPTRON, "10, -3"),
    NONLINEAR_CLASSIFICATION_ADALINE("table_2_10.csv", Lesson.PERCEPTRON, "10, -3"),
    LINEAR_REGRESSION_GRADIENT("table_2_11.csv", Lesson.PERCEPTRON, "10"),
    LINEAR_REGRESSION_ADALINE("table_2_11.csv", Lesson.PERCEPTRON, "10"),
    THREE_CLASS("table_3_1.csv", Lesson.PERCEPTRON_MONO, "-1, 3"),
    FOUR_CLASS("table_3_5.csv", Lesson.PERCEPTRON_MONO, "1,0,0,0,1,0,1,0,1,0,0,0,1,0,0,0,1,0,1,0,1,0,0,0,1"),
    XOR_MULTI("table_4_3.csv", Lesson.PERCEPTRON_MULTI, "1, 0")
    ;

    override fun toString(): String {
        return super.toString()
            .replace("_", "-").toLowerCase()
//            .lowercase()
    }
}

val modelFile: String by project
val dataFileDir: String by project

for (type in Types.values()) {
    for (operation in Operations.values()) {
        tasks.create<JavaExec>("${type}_$operation") {
            group = type.lesson.toString()

            val dataFile = "$dataFileDir/${type.dataFile}"

            classpath = project.the<SourceSetContainer>()["main"].runtimeClasspath
            mainClass.set("be.mgx.MainKt")

            val arguments = mutableListOf<String>()

            arguments += type.lesson.toString()
            arguments += type.toString()
            arguments += operation.toString()
            arguments += "--model"
            arguments += modelFile

            if (operation == Operations.TRAIN) {
                arguments += "--data"
                arguments += dataFile
            }

            if (operation == Operations.EXECUTE) arguments += type.exampleInput

            standardInput = System.`in`
            args(arguments)
        }
    }
}
