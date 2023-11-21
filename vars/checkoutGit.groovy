def call() {
    checkout([
        $class: 'GitSCM',
        branches: [[name: '$GIT_URL']],
        userRemoteConfigs: [[url: '$BRANCE_NAME']]
    ])
}
