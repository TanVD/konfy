job("Konfy / Build") {
    container("amazoncorretto:17-alpine3.19-jdk") {
        shellScript {
            content = """
              ./gradlew build  
          """
        }
    }
}

job("Konfy / Test") {
    container("amazoncorretto:17-alpine3.19-jdk") {
        shellScript {
            content = """
              ./gradlew test  
          """
        }
    }
}

job("Konfy / Release") {
    startOn {
        gitPush {
            enabled = false
        }
    }

    container("amazoncorretto:17-alpine3.19-jdk") {
//        env["JB_SPACE_CLIENT_ID"] = "{{ project:spaceUsername }}"
//        env["JB_SPACE_CLIENT_SECRET"] = "{{ project:spacePassword }}"

        shellScript {
            content = """
              ./gradlew publish    
          """
        }
    }
}
