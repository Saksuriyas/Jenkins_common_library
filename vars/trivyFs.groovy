#!/usr/bin/env groovy

/**
 * Trivy files scanning
 */

def call() {
    sh 'trivy fs . > trivyfs.txt'
}