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
          when {
            branch 'develop'
          }
          steps {
            sh '''./gradlew build '''
            archiveArtifacts 'build/libs/*.jar'
          }
        }

        stage('build-prod') {
          when {
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
          when {
            branch 'develop'
          }
          environment{
            IMAGE_NAME = "code4unb/temperaturelinebot-dev"
            CONTAINER_NAME = 'LineBot-dev'
          }
          steps {
              withCredentials([string(credentialsId: 'LINE_BOT_CHANNEL_TOKEN_DEV', variable: 'LINE_BOT_CHANNEL_TOKEN'), string(credentialsId: 'LINE_BOT_CHANNEL_SECRET_DEV', variable: 'LINE_BOT_CHANNEL_SECRET')]) {
                sh './gradlew docker -PimageName=$IMAGE_NAME'
                sh 'docker stop $CONTAINER_NAME || true'
                sh 'docker rm $CONTAINER_NAME || true'
                sh 'docker run -d --name $CONTAINER_NAME -p 443:8080 -e LINE_BOT_CHANNEL_SECRET=$LINE_BOT_CHANNEL_SECRET -e LINE_BOT_CHANNEL_TOKEN=$LINE_BOT_CHANNEL_TOKEN $IMAGE_NAME:latest'
              }
          }
        }

        stage('deploy-prod') {
          when {
            branch 'master'
          }
          steps {
            sh './gradlew docker snapshot -PimageName=code4unb/temperaturelinebot'
          }
        }

      }
    }

    stage('cleaning') {
      steps {
        sh 'docker image prune -f'
      }
    }

  }
}