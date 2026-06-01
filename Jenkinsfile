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
                // 👉 이 부분을 수정/추가합니다!
                echo "Build and test succeeded"
                sh """
                    echo "=======================================" > build-result.txt
                    echo "지속적 통합(CI) 빌드 및 테스트 결과 보고서" >> build-result.txt
                    echo "상태: SUCCESS (성공)" >> build-result.txt
                    echo "빌드 시간: \$(date)" >> build-result.txt
                    echo "=======================================" >> build-result.txt
                """
                // 생성된 txt 파일을 젠킨스에 박제(저장)하는 명령어입니다.
                archiveArtifacts artifacts: "build-result.txt", allowEmptyArchive: true
            }
        }
    }
