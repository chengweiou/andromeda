### uat 环境

数据库选一
当前为 pgsql 如果替换为 mysql 8 需添加mysql启动参数到docker-compose
docker run --rm -it --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=123456 -d mysql/mysql-server

#### 上传
```
./uat.sh
```
first time:
```
change active profile to uat
mkdir -pv ~/Desktop/docker/universe/andromeda/config
cp src/main/resources/application.yml ~/Desktop/docker/universe/andromeda/config/
chmod +x uat.sh
./uat.sh
```


##### 单独启动容器
```
docker network net
docker run --rm -it --name pgsql -p 5432:5432 --network net -v ~/Desktop/docker/pgsql/data:/var/lib/postgresql/data -e POSTGRES_PASSWORD=123456 -d postgres
docker run --rm -it -d --name redis -p 6379:6379 --network net -d redis
docker run --rm -it -d --name andromeda -p 60002:8906 --network net -v ~/Desktop/docker/universe/andromeda:/proj/ -w /proj/ openjdk java -jar ser.jar
```