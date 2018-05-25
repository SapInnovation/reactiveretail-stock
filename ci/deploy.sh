#!/bin/sh -x


apk update
apk add  sshpass
apk add openssh

cd source
pwd
ls -ltr

sshpass -p $USER_PASS scp -r -o StrictHostKeyChecking=no . $USER_NAME@$HOST_NAME:/opt/stock

  sshpass -p $USER_PASS ssh -o StrictHostKeyChecking=no $USER_NAME@$HOST_NAME 'docker stack deploy stock-exposer -c /opt/stock/ci/docker-image-deploy.yml'
