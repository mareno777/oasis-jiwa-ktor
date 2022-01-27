./gradlew shadowJar
docker buildx build --platform linux/amd64,linux/arm64 -t mareno/oasis-jiwa-ktor:0.0.1 --push .

cd /users/reno/belajar/aws

ssh -i linux-app.cer ubuntu@ec2-108-137-2-233.ap-southeast-3.compute.amazonaws.com

docker container stop oasis-jiwa
docker container rm oasis-jiwa
docker container ls -all
docker image rm mareno/oasis-jiwa-ktor:0.0.1
docker pull mareno/oasis-jiwa-ktor:0.0.1
docker container create --name oasis-jiwa -p 8080:8080 mareno/oasis-jiwa-ktor:0.0.1
network create myNetwork
docker network connect myNetwork oasis-jiwa
docker container start oasis-jiwa