./gradlew shadowJar
docker build -t oasis-jiwa-ktor:0.0.2

cd /users/reno/belajar/aws

ssh -i linux-app.cer ubuntu@ec2-108-137-2-233.ap-southeast-3.compute.amazonaws.com

docker container stop oasis-jiwa
docker container rm oasis-jiwa
docker container ls -all
docker image rm mareno/oasis-jiwa-ktor:0.0.2
docker pull mareno/oasis-jiwa-ktor:0.0.2
docker container create --name oasis-jiwa -p 8080:8080 -p 8443:8443 oasis-jiwa-ktor:0.0.2
docker network create myNetwork
docker network connect myNetwork oasis-jiwa

docker container create --name mongoserver --publish 1234:27017 --mount "type=bind,source=/root/mongo-data,destination=/data/db" --env MONGO_INITDB_ROOT_USERNAME=reno --env MONGO_INITDB_ROOT_PASSWORD=reno mongo:latest

docker container create --name mongoserver --publish 1234:27017 --mount "type=volume,source=mongorestore,destination=/data/db" --env MONGO_INITDB_ROOT_USERNAME=reno --env MONGO_INITDB_ROOT_PASSWORD=reno mongo:latest

docker container run --rm --name ubuntubackup --mount "type=bind,source=/root/backup-volume-oasis-jiwa,destination=/backup" --mount "type=volume,source=mongodata,destination=/data" ubuntu:latest tar cvf /backup/backup-lagi.tar.gz /data
tar cvf /backup/backup-lagi.tar.gz /data
docker container run --rm --name ubunturestore --mount "type=bind,source=/root/backup-volume-oasis-jiwa,destination=/backup" --mount "type=volume,source=mongorestore,destination=/data" ubuntu:latest bash -c "cd /data && tar xvf /backup/backup-lagi.tar.gz --strip 1"

docker container start mongoserver