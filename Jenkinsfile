#!/usr/bin/env groovy

/* Only keep the 10 most recent builds. */
def projectProperties = [
        buildDiscarder(logRotator(daysToKeepStr: '10', numToKeepStr: '10')),
        [$class: 'GithubProjectProperty', projectUrlStr: 'https://github.com/veersudhir83/devops-web-maven.git/'],
        pipelineTriggers([pollSCM('H/5 * * * *')])
]

properties(projectProperties)

try {
    node {
        def mvnHome
        stage('Preparation') { // for display purposes
            // Get some code from a GitHub repository
            git url: 'https://github.com/veersudhir83/devops-web-maven.git',
                    branch: 'master'
            // Get the Maven tool.
            // ** NOTE: This 'M3' Maven tool must be configured in the global configuration.
            if (isUnix()) {
                mvnHome = tool 'mvn3.3.9'
            } else {
                mvnHome = tool 'mvn3'
            }
        }
        stage('Build') {
            // Run the maven build
            if (isUnix()) {
                sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean package"
            } else {
                bat(/"${mvnHome}\bin\mvn" --batch-mode -Dmaven.test.failure.ignore clean package/)
            }
        }
        stage('Results') {
            junit '**/target/surefire-reports/TEST-*.xml'
            archive 'target/*.jar'
        }
    }
} catch (exc) {
    echo "Caught: ${exc}"
}