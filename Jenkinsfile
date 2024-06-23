pipeline {
environment {
registry = "monacobot/order-execution-service"
registryCredential = 'docker-id'
dockerImage = ''
}

agent {
  label 'built-in'
}
stages {   
stage('Cloning our Git') {
 steps {
        git branch: 'develop', // Specify develop branch
           url: 'https://github.com/new052022/order-execution.git'
    }
}
stage('Building the application') {
      steps {
        script {
          // Execute Gradle command within the workspace
           sh './gradlew bootBuildImage'
        }
      }
}
stage('Building our image') {
steps{
script {
    def dockerHome = tool 'docker'
        env.PATH = "${dockerHome}/bin:${env.PATH}"
dockerImage = docker.build registry + ":$BUILD_NUMBER"
}
}
}
stage('Pushing the image to Docker Hub') {
            steps {
                script {
                    // Use docker.withRegistry with your registry credential ID
                    docker.withRegistry('https://registry.hub.docker.com', registryCredential) {
                        dockerImage.push()
                    }
                }
            }
}
stage('Deploy our image') {
   steps {
        script {
           
            def imageName = registry + ":$BUILD_NUMBER"
            def postgresUser = env.POSTGRES_USER
            def postgresPass = env.POSTGRES_PASS
            def dbHost = env.DB_HOST
            def secretNumber = env.SECRET_NUMBER
            def algorithm = env.ALGORITHM
          sh 'docker ps -f name=order-execution-service -q | xargs --no-run-if-empty docker container stop'
sh 'docker container ls -a -fname=order-execution-service -q | xargs -r docker container rm'
          sh """
docker run -d --name order-execution-service -p 9002:9002 -e POSTGRES_USER=${postgresUser} -e POSTGRES_PASS=${postgresPass} -e DB_HOST=${dbHost} -e SECRET_NUMBER=${secretNumber} -e ALGORITHM=${algorithm} ${imageName}
"""
        }
}
}
stage('Cleaning up') {
steps{
sh "docker rmi $registry:$BUILD_NUMBER"
}
}
}
} 