#!/usr/bin/env bash

#mvn clean package

echo 'Copy files...'

scp -i /Users/nick/.ssh/id_rsa \
    ../target/jwt-auth-service-1.0.0.jar \
    root@92.255.111.141:/root/

#echo 'Restart server...'

#ssh -i /Users/nick/.ssh/id_rsa root@92.255.111.141 "pgrep java | xargs kill -9"
#
#ssh -i /Users/nick/.ssh/id_rsa root@92.255.111.141 "nohup java -jar jwt-auth-service-1.0.0.jar > log.txt &"


echo 'Bye'