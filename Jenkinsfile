pipeline {
    agent any

    environment {
        JUNIT_JAR_URL = 'https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.7.1/junit-platform-console-standalone-1.7.1.jar'
        JUNIT_JAR_PATH = 'lib/junit.jar'
        CLASS_DIR = 'classes'
        REPORT_DIR = 'test-reports'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Prepare') {
            steps {
                sh """
                    mkdir -p ${CLASS_DIR}
                    mkdir -p ${REPORT_DIR}
                    mkdir -p lib
                    echo "[+] Downloading JUnit JAR..."
                    curl -L -o ${JUNIT_JAR_PATH} ${JUNIT_JAR_URL}
                """
            }
        }

        stage('Build') {
            steps {
                sh """
                    echo "[+] Compiling source files..."
                    find src -name "*.java" > sources.txt
                    javac -encoding UTF-8 -d ./${CLASS_DIR} -cp ./${JUNIT_JAR_PATH} @sources.txt
                """
            }
        }

        stage('Test') {
            steps {
                sh """
                    echo "[+] Running tests with JUnit..."
                    java -jar ${JUNIT_JAR_PATH} \
                    --class-path ${CLASS_DIR} \
                    --scan-class-path \
                    --details=tree \
                    --details-theme=ascii \
                    --reports-dir ${REPORT_DIR} \
                    --config=junit.platform.output.capture.stdout=true \
                    --config=junit.platform.reporting.open.xml.enabled=true \
                    > ${REPORT_DIR}/test-output.txt
                """
            }
        }
    }

    post {
        always {
            echo "[*] Archiving test results..."
            junit "${REPORT_DIR}/**/*.xml"
            archiveArtifacts artifacts: "${REPORT_DIR}/**/*", allowEmptyArchive: true
        }

        failure {
            echo "Build or test failed"
        }

        success {
            echo "Build and test succeeded"
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
                to: 'your_email@naver.com' // 👈 본인 네이버 메일 주소로 꼭 변경하세요!
            )
        }
    }
} // 👈 이 맨 마지막 괄호가 누락되어 에러가 났었습니다.