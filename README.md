### spring-boot-jenkins
Spring Boot Example with Jenkins,Sonarqube,Cloud Foundary and Code Coverage Analysis(jacoco).

### jenkins setup -
* add key -> ```wget -q -O - http://pkg.jenkins-ci.org/debian/jenkins-ci.org.key | sudo apt-key add -```
* add repo -> ```sudo sh -c 'echo deb http://pkg.jenkins-ci.org/debian binary/ > /etc/apt/sources.list.d/jenkins.list```
* ```sudo apt-get update```
* ```sudo apt-get install jenkins```
* start jenkins -> http://localhost:8080 -> signup

### sonar setup -
* ```
    wget https://sonarsource.bintray.com/Distribution/sonarqube/sonarqube-5.6.4.zip

    unzip sonarqube-5.6.4.zip
    mv sonarqube-5.6.4 /opt/sonar
    
    Open /opt/sonar/conf/sonar.properties
    
    add this in web-server setting -
    
    sonar.web.host=127.0.0.1
    sonar.web.context=/sonar
    sonar.web.port=9000
    
    to start sonar -
    sudo /opt/sonar/bin/linux-x86-64/sonar.sh start(user/pass -admin by default)```

### sonar config in maven -

* ```  
  open /maven/conf/settings.xml and add following lines-
  
  <pluginGroups>
         <pluginGroup>org.sonarsource.scanner.maven</pluginGroup>
  </pluginGroups>
   <profiles>
     <profile>
            <id>sonar</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <!-- Optional URL to server. Default value is http://localhost:9000 -->
                <sonar.host.url>
                  http://127.0.0.1:9000/sonar
                </sonar.host.url>
            </properties>
       </profile>
     </profiles>
  

```
