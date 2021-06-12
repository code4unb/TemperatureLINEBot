pipeline {
  agent any
  stages {
    stage('checkout') {
      steps {
        deleteDir()
        sh '''chechout scm
'''
        sh '''sh \'chmod +x gradlew\'
'''
        sh 'gradlew clean'
      }
    }

  }
}