./gradlew bootJar
cp build/libs/andromeda-0.0.1-SNAPSHOT.jar ~/Desktop/docker/universeandromeda/ser.jar
cp src/main/resources/application-uat.yml ~/Desktop/docker/universeandromeda/config/
cp src/main/resources/log4j2.xml ~/Desktop/docker/universeandromeda/config/
cd ~/Desktop/docker/universeandromeda
docker stop andromeda
docker run --rm -it -d --name andromeda -p 60002:8906 --network net -v /Users/chengweiou/Desktop/docker/universeandromeda:/proj/ -w /proj/ openjdk java -jar ser.jar