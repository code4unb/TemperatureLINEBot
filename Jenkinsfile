pipeline {
  agent any
  stages {
    stage('checkout') {
      steps {
        sh 'chmod +x gradlew'
        sh 'ls'
        sh './gradlew clean'
      }
    }

    stage('test') {
      steps {
        sh './gradlew test'
      }
    }

    stage('build') {
      parallel {
        stage('build-dev') {
          when{
            branch 'develop'
          }
          steps {
            sh '''./gradlew build 
'''
            archiveArtifacts 'build/libs/*.jar'
          }
        }

        stage('build-prod') {
          when{
                    branch 'master'
          }
          steps {
            sh '''./gradlew build snapshot
'''
            archiveArtifacts 'build/libs/*.jar'
          }
        }

      }
    }

    stage('deployment') {
      parallel {
        stage('deploy-dev') {
          when{
            branch 'develop'
          }
          steps {
            sh './gradlew docker'
          }
        }

        stage('deploy-prod') {
          when{
            branch 'master'
          }
          steps {
            sh './gradlew docker snapshot'
          }
        }

      }
    }

  }
}