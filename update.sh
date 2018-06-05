#!/bin/sh

mvn clean install -DskipTests
docker-compose build
docker-compose create
docker-compose restart

echo installing client...

rm -rvf /var/www/html/*
cp -rv client/* /var/www/html/

echo done