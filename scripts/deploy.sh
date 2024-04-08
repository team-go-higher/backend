DEV_PORT=8080
TEST_PORT=8081

DEV_PROFILE="dev"
TEST_PROFILE="test"

DEPLOY_FOLDER="gohigher"
JAR_FILE="bootstrap-0.0.1-SNAPSHOT.jar"

TEST_FOLDER="test"
DEV_LOG_FILE="gohigher.out"
TEST_LOG_FILE="gohigher-test.out"

cd ~
cd ${DEPLOY_FOLDER}

echo "> kill spring port"
fuser -k -n tcp ${DEV_PORT}
fuser -k -n tcp ${TEST_PORT}

echo "> execute dev jar"
cd bootstrap/build/libs
nohup java -jar --spring.profiles.active=${DEV_PROFILE} ${JAR_FILE} 1>${DEV_LOG_FILE} 2>&1 &

echo "> execute test jar"
mkdir ${TEST_FOLDER}
mv ${JAR_FILE} /${TEST_FOLDER}
cd test
nohup java -jar --spring.profiles.active=${TEST_PROFILE} --server.port=${TEST_PORT} ${JAR_FILE} 1>${TEST_LOG_FILE} 2>&1 &

