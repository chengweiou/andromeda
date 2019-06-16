./gradlew bootJar
cp build/libs/andromeda-0.0.1-SNAPSHOT.jar ~/Desktop/docker/universe/andromeda/ser.jar
cp src/main/resources/application-uat.yml ~/Desktop/docker/universe/andromeda/config/
cp src/main/resources/log4j2.xml ~/Desktop/docker/universe/andromeda/config/
cd ~/Desktop/docker/universe/andromeda
docker stop andromeda
docker run --rm -it -d --name andromeda -p 60002:8906 --network net -v /Users/chengweiou/Desktop/docker/universe/andromeda:/proj/ -w /proj/ openjdk java -jar ser.jar