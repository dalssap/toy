group = "com.dalssap.toy.bot"

dependencies {
    implementation(project(":common"))


    implementation("org.telegram:telegrambots-springboot-longpolling-starter:8.3.0")
    implementation("org.telegram:telegrambots-client:8.3.0")
}

tasks.processResources {
    from("${project(":common").projectDir}/src/main/resources")
}