#!/bin/sh

mvn clean install

echo installing client...

rm -rvf /var/www/html/*
cp -rv client/dist/cloud-client/* /var/www/html/

echo done
