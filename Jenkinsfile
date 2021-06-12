pipeline {
  agent any
  stages {
    stage('checkout') {
      steps {
        deleteDir()
        sh '''sh \'chmod +x gradlew\'
'''
        sh 'gradlew clean'
      }
    }

  }
}