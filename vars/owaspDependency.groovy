#!/usr/bin/env groovy

/**
 * OWASP dependencyCheck of service
 */

def call() {
    sh ''' dependencyCheck additionalArguments: '--scan ./ --disableYarnAudit --disableNodeAudit', odcInstallation: 'DP-Check' '''
    sh ''' dependencyCheckPublisher pattern: '**/dependency-check-report.xml' '''
}