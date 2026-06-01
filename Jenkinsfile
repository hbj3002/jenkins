stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Prepare') {
            steps {
                // dir('Practice')를 넣어주어 Practice 폴더 안에서 명령어가 돌게 만듭니다.
                dir('Practice') {
                    sh """
                        mkdir -p ${CLASS_DIR}
                        mkdir -p ${REPORT_DIR}
                        mkdir -p lib
                        echo "[+] Downloading JUnit JAR..."
                        curl -L -o ${JUNIT_JAR_PATH} ${JUNIT_JAR_URL}
                    """
                }
            }
        }
        stage('Build') {
            steps {
                dir('Practice') {
                    sh """
                        echo "[+] compiling source files..."
                        find src -name "*.java" > sources.txt
                        javac -encoding UTF-8 -d ./${CLASS_DIR} -cp ./${JUNIT_JAR_PATH} @sources.txt
                    """
                }
            }
        }
        stage('Test') {
            steps {
                dir('Practice') {
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
    }