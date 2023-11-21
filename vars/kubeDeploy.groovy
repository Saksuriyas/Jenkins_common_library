#!/usr/bin/env groovy

/**
 * Kubectl deployment service & pods
 */

def call() {
    withKubeConfig(caCertificate: '', clusterName: '', contextName: '', credentialsId: 'k8s', namespace: '', restrictKubeConfigAccess: false, serverUrl: '') {
        sh "kubectl apply -f deployment.yml"
    }
}