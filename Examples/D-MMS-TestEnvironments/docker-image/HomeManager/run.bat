rem docker run --name my-mysql -e MYSQL_ROOT_PASSWORD=1234 -e MYSQL_DATABASE=hmdb -e MYSQL_USER=hm -e  MYSQL_PASSWORD=hm1234 -d -p 3306:3306 mysql:latest
docker run -v migration:/app/home-manager/migration -p 8088:8088 --net="host" --name testhm home-manager:0.1
