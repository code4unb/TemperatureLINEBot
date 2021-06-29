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
      environment {
      SPRING_PROFILES_ACTIVE="dev"
      }
      steps {
      sh 'echo disabled'
        //sh './gradlew test'
      }
    }

    stage('build') {
      parallel {
        stage('build-dev') {
          environment {
            SPRING_PROFILES_ACTIVE="prod"
          }
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
      environment {
        IMAGE_NAME = "code4unb/temperaturelinebot"
        CONTAINER_NAME = 'LineBot'
        POSTGRES_DB="LineBot_Data"
      }
      parallel {
        stage('deploy-dev') {
          when {
            branch 'develop'
          }
          environment {
            PRODUCTION="dev"
            PUBLISHED_PORT="444"
          }
          steps {
            withCredentials([string(credentialsId: 'LINE_BOT_CHANNEL_TOKEN_DEV', variable: 'LINE_BOT_CHANNEL_TOKEN'), string(credentialsId: 'LINE_BOT_CHANNEL_SECRET_DEV', variable: 'LINE_BOT_CHANNEL_SECRET'), string(credentialsId: 'POSTGRES_PASSWORD', variable: 'POSTGRES_PASSWORD')]) {
              sh 'export IMAGE_NAME=$IMAGE_NAME'
              sh 'export LINE_BOT_CHANNEL_SECRET=$LINE_BOT_CHANNEL_SECRET'
              sh 'export LINE_BOT_CHANNEL_TOKEN=$LINE_BOT_CHANNEL_TOKEN'
              sh 'export POSTGRES_PASSWORD=$POSTGRES_PASSWORD'
              sh 'docker-compose -f docker-compose-shared.yml up -d'
              sh './gradlew composeUp'
            }
          }
        }

        stage('deploy-prod') {
          when {
            branch 'master'
          }
          environment {
            PRODUCTION="prod"
            PUBLISHED_PORT="443"
          }
          steps {
            withCredentials([string(credentialsId: 'LINE_BOT_CHANNEL_TOKEN_PROD', variable: 'LINE_BOT_CHANNEL_TOKEN'), string(credentialsId: 'LINE_BOT_CHANNEL_SECRET_PROD', variable: 'LINE_BOT_CHANNEL_SECRET'), string(credentialsId: 'POSTGRES_PASSWORD', variable: 'POSTGRES_PASSWORD')]) {
              sh 'export IMAGE_NAME=$IMAGE_NAME'
              sh 'export LINE_BOT_CHANNEL_SECRET=$LINE_BOT_CHANNEL_SECRET'
              sh 'export LINE_BOT_CHANNEL_TOKEN=$LINE_BOT_CHANNEL_TOKEN'
              sh 'export POSTGRES_PASSWORD=$POSTGRES_PASSWORD'
              sh 'docker-compose -f docker-compose-shared.yml up -d'
              sh './gradlew composeUp snapshot'
            }
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