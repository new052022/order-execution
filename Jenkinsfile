pipeline {
    environment {
        registry = "moritz007/order-execution"
        registryCredential = 'docker-hub-credentials'
    }
    tools {
        jdk 'JDK 21'
    }
    agent {
        label 'built-in'
    }
    stages {
        stage('Clean old containers') {
            steps {
                script {
                    sh '''
                    echo "Stopping and removing old containers..."
                    docker ps -a --filter ancestor=moritz007/order-execution --format "{{.ID}}" | xargs --no-run-if-empty docker stop || true
                    docker ps -a --filter ancestor=moritz007/order-execution --format "{{.ID}}" | xargs --no-run-if-empty docker rm -f || true

                    echo "Removing old images..."
                    docker images --filter reference=moritz007/order-execution --format "{{.ID}}" | xargs --no-run-if-empty docker rmi -f || true
                    '''
                }
            }
        }

        stage('Cloning our Git') {
            steps {
                git branch: 'develop', url: 'https://github.com/new052022/order-execution.git'
            }
        }

        stage('Building the application') {
            steps {
                script {
                    sh './gradlew bootBuildImage'
                }
            }
        }

        stage('Building our image') {
            steps {
                script {
                    dockerImage = docker.build("${registry}:${BUILD_NUMBER}")
                }
            }
        }

        stage('Pushing the image to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', registryCredential) {
                        dockerImage.push()
                    }
                }
            }
        }

        stage('Deploy our image') {
            steps {
                script {
                    withEnv([
                        "POSTGRES_USER=${env.POSTGRES_USER}",
                        "POSTGRES_PASS=${env.POSTGRES_PASS}",
                        "DB_HOST=${env.DB_HOST}",
                        "SECRET_NUMBER=${env.SECRET_NUMBER}",
                        "ALGORITHM=${env.ALGORITHM}"
                    ]) {
                        sh '''
                        echo "Stopping and removing previous container..."
                        docker ps -f name=order-execution -q | xargs --no-run-if-empty docker stop || true
                        docker ps -a -f name=order-execution -q | xargs --no-run-if-empty docker rm -f || true

                        echo "Deploying new container..."
                        docker run -d --name order-execution -p 9002:9002 \
                            -e POSTGRES_USER="$POSTGRES_USER" \
                            -e POSTGRES_PASS="$POSTGRES_PASS" \
                            -e DB_HOST="$DB_HOST" \
                            -e SECRET_NUMBER="$SECRET_NUMBER" \
                            -e ALGORITHM="$ALGORITHM" \
                            ${registry}:${BUILD_NUMBER}
                        '''
                    }
                }
            }
        }

        stage('Cleaning up') {
            steps {
                script {
                    sh '''
                    echo "Cleaning up old images..."
                    docker images --filter reference=moritz007/order-execution --format "{{.ID}}" | xargs --no-run-if-empty docker rmi -f || true
                    '''
                }
            }
        }
    }
}
