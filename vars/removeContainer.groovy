#!/usr/bin/env groovy

/**
 * Stop & Remove docker container
 */

def call(){
    sh 'docker stop $REPO_NAME'
    sh 'docker rm $REPO_NAME'
}