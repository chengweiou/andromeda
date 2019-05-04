./gradlew bootJar
cp build/libs/andromeda-0.0.1-SNAPSHOT.jar ~/Desktop/docker/andromeda/ser.jar
cp src/main/resources/application-uat.yml ~/Desktop/docker/andromeda/config/
cp src/main/resources/log4j2.xml ~/Desktop/docker/andromeda/config/
cd ~/Desktop/docker/andromeda
docker stop andromeda
docker run --rm -it -d --name andromeda -p 60002:8906git  --network net -v /Users/chengweiou/Desktop/docker/andromeda:/proj/ -w /proj/ openjdk java -jar ser.jar