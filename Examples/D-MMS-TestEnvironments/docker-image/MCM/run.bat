docker run -v migration:/app/mcm/migration -p 8089:8088 -e dbPort=4306 --net="host" --name testmcm mcm:0.1
