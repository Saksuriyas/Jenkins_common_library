def call() {
    checkout([
        $class: 'GitSCM',
        branches: [[name: '$BRANCE_NAME']],
        userRemoteConfigs: [[url: '$GIT_URL']]
    ])
}
