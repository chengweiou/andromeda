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
