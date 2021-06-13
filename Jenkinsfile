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
        sh ' echo ./gradlew test || true'
      }
    }

    stage('build') {
      parallel {
        stage('build-dev') {
          when {
            branch 'developa'
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
              withCredentials([string(credentialsId: 'LINE_BOT_CHANNEL_TOKEN_DEV', variable: 'LINE_BOT_CHANNEL_TOKEN'), string(credentialsId: 'LINE_BOT_CHANNEL_SECRET_DEV', variable: 'LINE_BOT_CHANNEL_SECRET'),string(credentialsId: 'SSL_KEYSTORE_PASSWORD', variable: 'SSL_KEYSTORE_PASSWORD'),file(credentialsId: 'SSL_KEYSTORE_FILE', variable: 'SSL_KEYSTORE_FILE') ]) {
                script{
                  writeFile(file:"key.p12",text:readFile(file:SSL_KEYSTORE_FILE))
                }
                withEnv(['KEYSTORE_PASSWORD=$SSL_KEYSTORE_PASSWORD']) {
                    sh './gradlew docker -PimageName=$IMAGE_NAME'
                }
                sh 'docker stop $CONTAINER_NAME || true'
                sh 'docker rm $CONTAINER_NAME || true'
                sh 'docker run -d --name $CONTAINER_NAME -p 443:8080 -e LINE_BOT_CHANNEL_SECRET=$LINE_BOT_CHANNEL_SECRET -e LINE_BOT_CHANNEL_TOKEN=$LINE_BOT_CHANNEL_TOKEN -e SSL_KEYSTORE_PASSWORD=$SSL_KEYSTORE_PASSWORD $IMAGE_NAME:latest'
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