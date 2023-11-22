#!/usr/bin/env groovy

/**
 * Marven clean/install package
 */

def call() {
    sh 'mvn clean install package -DskipTests'
}