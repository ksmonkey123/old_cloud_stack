echo installing client...

rm -rvf /var/www/html/*
cp -rv client/dist/cloud-client/* /var/www/html/

echo done