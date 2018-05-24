#!/bin/sh -x

apt-get update
apt-get install sudo
sudo apt-get --assume-yes install apt-transport-https ca-certificates curl software-properties-common
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo apt-key fingerprint 0EBFCD88
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
sudo apt-get update
sudo apt-get install -y docker-ce
sleep 10s
docker login -u ${REGISTRY_USER} -p ${REGISTRY_PASSWORD}
./gradlew :stock-exposer:build :stock-exposer:docker :stock-exposer:dockerPush --no-daemon
