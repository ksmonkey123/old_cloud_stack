#!/bin/sh

mvn clean install
docker-compose build
docker-compose create
docker-compose restart

echo installing client...

rm -rvf /var/www/html/*
cp -rv client/dist/cloud-client/* /var/www/html/

echo done