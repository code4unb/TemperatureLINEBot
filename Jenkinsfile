pipeline {
  agent any
  stages {
    stage('checkout') {
      steps {
        sh 'chmod +x gradlew'
        sh '''ls
'''
        sh './gradlew clean'
      }
    }

    stage('test') {
      steps {
        sh 'gradlew test'
      }
    }

    stage('build') {
      parallel {
        stage('build-dev') {
          steps {
            sh '''gradlew build 
'''
            archiveArtifacts 'build/libs/*.jar'
          }
        }

        stage('build-prod') {
          steps {
            sh '''gradlew build snapshot
'''
            archiveArtifacts 'build/libs/*.jar'
          }
        }

      }
    }

    stage('deployment') {
      parallel {
        stage('deploy-dev') {
          steps {
            sh '''gradlew docker
'''
          }
        }

        stage('deploy-prod') {
          steps {
            sh 'gradlew docker snapshot'
          }
        }

      }
    }

  }
}