pipeline {
  agent any
  stages {
    stage('checkout') {
      steps {
        sh 'chmod +x gradlew'
        sh 'gradlew clean'
      }
    }

  }
}