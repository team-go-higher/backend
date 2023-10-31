
PORT=8080
PROFILE="test"

JAR_PATH="./bootstrap-0.0.1-SNAPSHOT.jar"
LOG_FILE="gohigher.out"
ENCRYPTION_KEY="lqkewharoaehgovhwaohpqk"

echo "> current profile : ${PROFILE}"

cd ~
cd backend

echo "> start build"
./gradlew bootJar

echo "> kill spring port"
fuser -k -n tcp ${PORT}

echo "> execute jar"
cd bootstrap/build/libs
nohup java -jar "${JAR_PATH}" --spring.profiles.active="${PROFILE}" --encryption-key="${ENCRYPTION_KEY}" >> "${LOG_FILE}" 2>/dev/null &
