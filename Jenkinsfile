pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Prepare') {
            steps {
                echo "[+] Granting execute permission to gradlew..."
                // 리눅스 환경에서 gradle 래퍼 실행 권한 부여
                sh "chmod +x ./gradlew"
            }
        }

        stage('Build & Test') {
            steps {
                echo "[+] Compiling and Running Tests with Gradle..."
                // 기존의 복잡한 javac, java -jar 명령 대신
                // 스프링이 제공하는 gradlew 빌드 스크립트를 실행합니다.
                sh "./gradlew clean build"
            }
        }
    }

    post {
        always {
            echo "[*] Archiving test results..."
            // 스프링 부트(Gradle)의 기본 테스트 리포트 경로로 변경
            junit "build/test-results/test/**/*.xml"
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
                to: 'hbj3000@naver.com' // 👈 본인 네이버 메일 주소로 변경!
            )
        }
    }
}