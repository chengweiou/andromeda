./gradlew bootJar
cp build/libs/andromeda-0.0.1-SNAPSHOT.jar ~/Desktop/docker/universe/andromeda/ser.jar
#cp src/main/resources/application.yml ~/Desktop/docker/universe/andromeda/config/
cp src/main/resources/application-uat.yml ~/Desktop/docker/universe/andromeda/config/
cp src/main/resources/log4j2.xml ~/Desktop/docker/universe/andromeda/config/
cp src/main/resources/jwt-public.pem ~/Desktop/docker/universe/andromeda/config/
cp src/main/resources/jwt-private.pem ~/Desktop/docker/universe/andromeda/config/
cp docker-compose.yml ~/Desktop/docker/universe/andromeda/docker-compose.yml
cd ~/Desktop/docker/universe/andromeda

docker compose down
docker compose rm -f
docker compose up -d
