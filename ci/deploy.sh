#!/bin/sh -x


apk update
apk add  sshpass
apk add openssh
apk add docker

cd source
pwd
ls -ltr

#sshpass -p $USER_PASS scp -r -o StrictHostKeyChecking=no . $USER_NAME@$HOST_NAME:/opt/stock-$SERVICE

#sshpass -p $USER_PASS ssh -o StrictHostKeyChecking=no $USER_NAME@$HOST_NAME 'export KAFKA_HOST='$KAFKA_HOST' && export DB_HOST='$DB_HOST' && 
docker stack deploy stock-'$SERVICE' -c /opt/stock-'$SERVICE'/ci/docker-image-deploy-'$SERVICE'.yml
