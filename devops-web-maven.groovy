#!/usr/bin/env groovy

/**
 * @ Maintainer sudheer veeravalli<veersudhir83@gmail.com>
 */

/* Only keep the 10 most recent builds. */
def projectProperties = [
        buildDiscarder(logRotator(artifactDaysToKeepStr: '20', artifactNumToKeepStr: '20', daysToKeepStr: '20', numToKeepStr: '20')),
        [$class: 'GithubProjectProperty', projectUrlStr: 'https://github.com/veersudhir83/devops-web-maven.git/']
        //,pipelineTriggers([pollSCM('H/10 * * * *')])
]

properties(projectProperties)

try {
    node {
        def mvnHome
        def appName = 'devops-web-maven'
        def buildNumber = env.BUILD_NUMBER
        def workspaceRoot = env.WORKSPACE
        def artifactoryPublishInfo
        def artifactoryServer = Artifactory.server 'artifactory-oss-6.3.3'
        env.JAVA_HOME="${tool 'jdk1.8'}"
        env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"

        def uploadArtifactUnix = """{
            "files": [
                {
                    "pattern": "${workspaceRoot}/${appName}/target/${appName}.jar",
                    "target": "devops-project/${appName}/${buildNumber}/"
                }
            ]
        }"""

        def uploadArtifactWindows = """{
            "files": [
                {
                    "pattern": "${workspaceRoot}\\${appName}\\target\\${appName}.jar",
                    "target": "devops-project/${appName}/${buildNumber}/"
                }
            ]
        }"""

        stage('Tool Setup'){

            // ** NOTE: These tools must be configured in the jenkins global configuration.
            try {
                mvnHome = tool name: 'mvn3.5.4', type: 'maven'
            } catch (exc) {
                error "Failure in Tool Setup stage: ${exc}"
            }
        }

        stage('Checkout') {
            try {
                dir('devops-web-maven') {
                    git url: 'https://github.com/veersudhir83/devops-web-maven.git',
                            branch: 'master'
                }
            } catch (exc) {
                error "Failure in Checkout stage: ${exc}"
            }
        }

        stage('Build') {
            try {
                if (isUnix()) {
                    dir('devops-web-maven/') {
                        sh "'${mvnHome}/bin/mvn' clean package"
                        sh "cp ./target/devops-web-maven*.jar ./target/devops-web-maven.jar"
                    }
                } else {
                    dir('devops-web-maven\\') {
                        bat(/"${mvnHome}\bin\mvn" --batch-mode clean package/)
                        bat(/copy .\\target\\devops-web-maven*.jar .\\target\\devops-web-maven.jar/)
                    }
                }
            } catch (exc) {
                error "Failure in Build stage: ${exc}"
            }
        }

        stage('Archive') {
            try {
                if (isUnix()) {
                    dir('devops-web-maven/') {
                        if (fileExists('target/devops-web-maven.jar')) {
                            // upload artifactory and also publish build info
                            artifactoryPublishInfo = artifactoryServer.upload(uploadArtifactUnix)
                            artifactoryPublishInfo.retention maxBuilds: 5
                            // and publish build info to artifactory
                            artifactoryServer.publishBuildInfo(artifactoryPublishInfo)
                        } else {
                            error 'Publish: Failed during file upload/publish to artifactory'
                        }
                    }
                } else {
                    dir('devops-web-maven\\') {
                        if (fileExists('target\\devops-web-maven.jar')) {
                            // upload artifactory and also publish build info
                            artifactoryPublishInfo = artifactoryServer.upload(uploadArtifactWindows)
                            artifactoryPublishInfo.retention maxBuilds: 5
                            // and publish build info to artifactory
                            artifactoryServer.publishBuildInfo(artifactoryPublishInfo)
                        } else {
                            error 'Publish: Failed during file upload/publish to artifactory'
                        }
                    }
                }
            } catch (exc) {
                error "Failure in Archive stage: ${exc}"
            }
        }
    }
} catch (exc) {
    error "Caught: ${exc}"
}
