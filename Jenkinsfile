pipeline {
  agent any
  stages {
    stage('checkout') {
      steps {
        deleteDir()
        sh 'chmod +x gradlew'
        sh 'gradlew clean'
      }
    }

  }
}