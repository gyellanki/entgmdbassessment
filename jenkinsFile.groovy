#!/usr/bin/env groovy

def ciBaseImage = '/compozed/ci-base-se:latest'
def ciDockerRegistry
def stopBuild
def commit
def DEV_SPACE = 'dev'
def UAT_SPACE = 'uat'
def INT_SPACE = 'int'


def NON_PROD_ENVIRONMENT = 'paas-nonprod-gl'
def LEARN_ENVIRONMENT = 'caas-eks-lrn'

node('linux_agent') {
    env.FULL_VERSION =  "1.0"
    cleanWs()
    checkout scm
    ciDockerRegistry = env.DOCKER_REGISTRY
    env.PROJECT_NAME = 'entgmdbassessment'
    env.ORG_NAME = 'TelematicsOperatorDataServices'
    commit = sh(returnStdout: true, script: 'git rev-parse HEAD')
    env.GITHUB_COMMIT_ID = commit?.trim()

    docker.withRegistry('https://' + env.DOCKER_REGISTRY, 'appfabric_sys') {
        docker.image(ciDockerRegistry + ciBaseImage).inside('--privileged') {
            withEnv([
                    'GRADLE_USER_HOME=~/.gradle'
            ]) {
                sh 'chmod +x ./gradlew'

                withCredentials(
                        [usernamePassword(credentialsId: 'appfabric_sys', passwordVariable: 'SYS_PASSWORD', usernameVariable: 'SYS_USER')]
                ) {
                    stage('Build') {
                        sh './gradlew clean build --refresh-dependencies'
                    }
                }

                withCredentials(
                        [usernamePassword(credentialsId: 'appfabric_sys', passwordVariable: 'SYS_PASSWORD', usernameVariable: 'SYS_USER')]
                ) {
                    stage('Artifactory Push') {
                        sh './gradlew artifactoryPublish'
                    }
                }
            }
        }
    }
}

conveyorDeploy(DEV_SPACE, NON_PROD_ENVIRONMENT, false)
checkpoint "after DEV deployment"
stopBuild = confirmNextStep('deploy to UAT?', 4)
if (stopBuild) {
    return
}
conveyorDeploy(UAT_SPACE, NON_PROD_ENVIRONMENT, false)
checkpoint "after UAT deployment"
stopBuild = confirmNextStep('deploy to INT?', 4)
if (stopBuild) {
    return
}

conveyorDeploy(INT_SPACE, NON_PROD_ENVIRONMENT, true)


def conveyorDeploy(space, environment, boolean enableDatadog) {
    node('linux_agent') {
        def spaceUpperCase = space.toUpperCase()
        def stageName = spaceUpperCase

        def spaceLowerCase = space.toLowerCase()


        withCredentials([
                usernamePassword([credentialsId: 'appfabric_sys', passwordVariable: 'SYS_PASSWORD', usernameVariable: 'SYS_USER'])
        ]) {

            stage("Deploy ${stageName}") {
                def conveyorLibrary = library('conveyor-jenkins-library').util.Conveyor.new(this)

                conveyorLibrary.deployAsc(
                        username: "${env.SYS_USER}",
                        password: "${env.SYS_PASSWORD}",
                        appName: "entgmdbassessment-${spaceLowerCase}",
                        environment: "${environment}",
                        productID: '041800001SG5',
                        serviceNowGroup: 'UBI_TG-Telematics_SUP',
                        serviceNowUserID: "${env.SNOW_AUTHOR}",
                        artifactUrl:  "https://artifactory.allstate.com/artifactory/TelematicsDataServices/com/galvanize/gmdb/entgmdbassessment/${env.FULL_VERSION}/entgmdbassessment-${env.FULL_VERSION}.jar",
                        envVars: [
                                ["name": "SPRING_PROFILES_ACTIVE", "value": "${spaceLowerCase}"],
                                ["name": "DD_VERSION", "value": "${env.FULL_VERSION}"],
                                ["name": "DD_BUILDPACK", "value": "java"],
                                ["name": "DD_TAGS", "value": "paas2"],
                                ["name": "JAVA_TOOL_OPTIONS", "value": "-Djava.security.krb5.kdc=AD.ALLSTATE.COM:88 -Djava.security.krb5.realm=AD.ALLSTATE.COM"]
                        ],
                        space: "${spaceLowerCase}",
                        memory: "1Gi",
                        cpu: "300m",
                        cpuLimit: "1500m",
                        runtime: "java",
                        probes: [
                                ["probe": "liveness", "endpoint": "/actuator/health/liveness", "failureThreshold": 3, "timeout": 2]
                        ]
                )
            }
        }
    }
}


def confirmNextStep(message, count, unit = 'HOURS') {
    def stopBuild = false

    try {
        stage(message) {
            timeout(time: count, unit: unit) {
                input message: message
            }
        }
    }
    catch (e) {
        stopBuild = true
        currentBuild.result = 'SUCCESS'
    }

    return stopBuild
}
