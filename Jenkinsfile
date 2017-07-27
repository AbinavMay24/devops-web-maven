#!groovy

pipeline {
    agent any
    def mvnHome
    stages {
         stage('Preparation') { // for display purposes
             // Get some code from a GitHub repository
             //git 'https://github.com/veersudhir83/devops-web-maven.git'
             // Get the Maven tool.
             // ** NOTE: This 'M3' Maven tool must be configured
             // **       in the global configuration.
             if (isUnix()) {
                 mvnHome = tool 'mvn3.3.9'
             } else {
                 mvnHome = tool 'D:\\1017141\\Tools\\apache-maven-3.5.0'
             }
         }
         stage('Build') {
            // Run the maven build
            if (isUnix()) {
               sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean package"
            } else {
               bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore clean package/)
            }
         }
         stage('Results') {
            junit '**/target/surefire-reports/TEST-*.xml'
            archive 'target/*.jar'
         }
    }
}
