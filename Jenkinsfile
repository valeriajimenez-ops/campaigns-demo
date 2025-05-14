pipeline {
    agent {
        node {
            // Usar el agente incorporado de Jenkins
            label 'built-in'
        }
    }
    tools {
        // Asegúrate de que este nombre coincide con tu configuración de Jenkins
        maven 'Maven 3.9.x' // Usa el nombre exacto que tienes configurado
    }
    stages {
        stage('Packaging') {
            steps {
                echo 'Packaging Backend..'
                // Empaquetar la aplicación Java del backend
                bat 'mvn clean package'
            }
        }
        stage('Copying jar file') {
            steps {
                echo 'Copying jar file..'
                // Copiar el archivo .jar generado usando PowerShell
                powershell 'Copy-Item -Path "target/*.jar" -Destination "." -Force'
            }
        }
        stage('cleanup') {
            steps {
                echo 'Cleaning up unused Docker resources for Backend...'
                // Limpia recursos Docker con la etiqueta específica del backend
                bat 'docker system prune -a --volumes --force --filter "label=campaign-demo-server"'
            }
        }
        stage('build image') {
            steps {
                echo 'Building Backend Docker image...'
                // Construye la imagen Docker para el backend con tu nombre de usuario
                bat 'docker build -t valeriajimenez-ops/campaign-demo:v1 --label campaign-demo-server .'
            }
        }
        stage('run container') {
            steps {
                echo 'Checking for and stopping/removing old Backend container if exists...'
                script {
                    powershell '''
                      # Verificar si el contenedor del backend existe
                      $container = docker inspect -f "{{.Id}}" campaign-demo-server 2>$null

                      # Si el comando docker inspect no dio error (encontró el contenedor)
                      if ($LASTEXITCODE -eq 0) {
                          echo "Container campaign-demo-server found. Stopping..."
                          docker stop campaign-demo-server
                          echo "Container campaign-demo-server stopped. Removing..."
                          docker rm campaign-demo-server
                          echo "Container campaign-demo-server removed."
                      } else {
                          echo "Container campaign-demo-server does not exist. Skipping stop/remove."
                      }

                      # Forzar un código de salida exitoso para el script PowerShell
                      exit 0;
                    '''
                }
                echo 'Running new Backend Docker container...'
                // Ejecuta el nuevo contenedor del backend, mapeando el puerto 5000
                bat 'docker run -d --name campaign-demo-server --label campaign-demo-server -p 5000:5000 valeriajimenez-ops/campaign-demo:v1'
            }
        }
    }
}