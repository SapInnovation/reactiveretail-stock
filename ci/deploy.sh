#!/bin/sh -x


apk update
apk add  sshpass
apk add openssh
apk add docker
service docker start

cd source
pwd
ls -ltr

ls -ltrh /opt/

#sshpass -p $USER_PASS scp -r -o StrictHostKeyChecking=no . $USER_NAME@$HOST_NAME:/opt/stock-$SERVICE

#sshpass -p $USER_PASS ssh -o StrictHostKeyChecking=no $USER_NAME@$HOST_NAME 'export KAFKA_HOST='$KAFKA_HOST' && export DB_HOST='$DB_HOST' && 
docker stack deploy stock-$SERVICE -c ci/docker-image-deploy-$SERVICE.yml
