pipeline {
    agent any

    // 🚀 [수정] 젠킨스 Tools에 등록한 JDK21을 사용하도록 선언합니다.
    tools {
        jdk 'JDK21'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Prepare') {
            steps {
                echo "[+] Granting execute permission to gradlew..."
                sh "chmod +x ./gradlew"
            }
        }

        stage('Build & Test') {
            steps {
                echo "[+] Compiling and Running Tests with Gradle..."
                // 정석대로 자바 21 환경에서 깔끔하게 빌드와 테스트를 수행합니다.
                sh "./gradlew clean build"
            }
        }
    }

    post {
        always {
            echo "[*] Archiving test results..."
            junit allowEmptyResults: true, testResults: 'build/test-results/test/**/*.xml'
        }

        failure {
            echo "❌ Build or test failed"
        }

        success {
            echo "✅ Build and test succeeded"
            sh """
                echo "=======================================" > build-result.txt
                echo "지속적 통합(CI) 빌드 및 테스트 결과 보고서" >> build-result.txt
                echo "상태: SUCCESS (성공)" >> build-result.txt
                echo "빌드 시간: \$(date)" >> build-result.txt
                echo "=======================================" >> build-result.txt
            """
            archiveArtifacts artifacts: "build-result.txt", allowEmptyArchive: true

            emailext (
                subject: "✅ [Jenkins] 빌드 성공: ${env.JOB_NAME} [#${env.BUILD_NUMBER}]",
                body: """
                    <h3>🎉 Jenkins 빌드가 성공적으로 완료되었습니다.</h3>
                    <p><b>프로젝트명:</b> ${env.JOB_NAME}</p>
                    <p><b>빌드 번호:</b> #${env.BUILD_NUMBER}</p>
                    <p><b>테스트 결과 및 로그 확인:</b> <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>
                    <hr>
                    <p>본 메일은 Jenkins에서 자동 발송되었습니다.</p>
                """,
                to: 'hbj3000@naver.com'
            )
        }
    }
}