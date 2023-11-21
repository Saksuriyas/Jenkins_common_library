#!/usr/bin/env groovy

/**
 * Sonarqube quaily grade of A,B,C,D
 */

def call(credentialsId) {
    waitForQualityGate abortPipeline: false, credentialsId: credentialsId   
}