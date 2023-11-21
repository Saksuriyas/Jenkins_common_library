#!/usr/bin/env groovy

/**
 * Send notifications based on build status string
   - sendNotifications 'STARTED'
   - sendNotifications currentBuild.result
 */

def call(String buildStatus = 'STARTED') {
  // build status of null means successful
  buildStatus = buildStatus ?: 'SUCCESS'

  // Default values
  def colorName = 'RED'
  def colorCode = '#FF0000'
  def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
  def summary = "${subject} (${env.BUILD_URL})"
  def details = """<p>${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
    <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>"""

  // Override default values based on build status
  if (buildStatus == 'STARTED') {
    color = 'YELLOW'
    colorCode = '#FFFF00'
  } else if (buildStatus == 'SUCCESS') {
    color = 'GREEN'
    colorCode = '#00FF00'
  } else {
    color = 'RED'
    colorCode = '#FF0000'
  }

  // Send slack notifications
  slackSend (color: colorCode, message: summary)

  // Send email notifications
  emailext (
    attachLog: true,
    subject: "'${buildStatus}'",
    body: "Project: ${env.JOB_NAME}<br/>" +
        "Build Number: ${env.BUILD_NUMBER}<br/>" +
        "URL: ${env.BUILD_URL}<br/>",
    to: '$EMAILS',
    attachmentsPattern: 'trivy.txt'
  )
}