pipeline {
    agent any

    //def env = System.getenv()
    //def jenkins_home_path = env['JENKINS_HOME']
    stages {
        stage('Download SourceCode') {
            steps {
                echo '<<<<<<<<<<<<<<<<<<<<<<<<<<<<<   Remove Original SourceCode and Packages    >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>'
                sh 'rm -rf /application/jenkins/deploy'
                sh 'mkdir -p /application/jenkins/deploy'
                echo '<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<   Download SourceCode from GitHub  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>'
                sh 'git clone git@github.com:gh-yehl/fullstackuser.git "${JENKINS_FOLDER}/deploy"'
                echo "Source Code Download Completed!!!"
            }
        }

        stage('Maven Build SourceCode') {
            steps {
                echo '<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  Maven Packaging SourceCode   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>'
                sh 'mvn -f "${JENKINS_FOLDER}/deploy"/pom.xml package'
                echo 'Package SourceCode Completed'
            }
        }
        stage('Junit Test') {
            steps {
                echo '<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<   Junit test   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>'
            }
        }
        stage('Deploy To Docker') {

            steps {
                echo '<<<<<<<<<<<<<<<<<<<<<<<<<<<<   Stop and Remove Users Container in Docker   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>'
                sh 'docker stop users'
                sh 'docker rm users'
                echo '<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<   Create New Image and Users Container   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>'
                sh 'cp "${JENKINS_FOLDER}/deploy"/target/service-users-1.0-SNAPSHOT.jar "${JENKINS_FOLDER}/deploy"/src/main/docker'
                //sh 'docker build -tf "${JENKINS_FOLDER}/deploy"/src/main/docker/Dockerfile -t funny5/service-users-new .'
                //sh 'docker run -idt --memory=400M --name=users --net=backend -p 9000:9000 funny5/service-users-new'
                sh '/application/creatcontainer.sh'



                //print 'cmd /c d:\\testgroovy.bat'.execute()
                //print 'cmd /c git clone https://github.com/gh-yehl/fullstackuser.git d:\\new'.execute()
                //print "cmd /c git clone git@github.com:gh-yehl/fullstackuser.git ${jenkins_home_path}".execute()
                echo '<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<   Deploying to Docker Done   >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>'

            }
        }
    }
}