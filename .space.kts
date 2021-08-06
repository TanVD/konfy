job("Konfy / Build") {
    container("openjdk:11") {
        shellScript {
            content = """
              ./gradlew build  
          """
        }
    }
}

job("Konfy / Test") {
    container("openjdk:11") {
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

    container("openjdk:11") {
        shellScript {
            content = """
              ./gradlew publish    
          """
        }
    }
}
