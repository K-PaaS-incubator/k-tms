#!/bin/bash

echo "TAS CLOUD INSTALL LOG :: apt update."

mkdir -p /usr/local/mysql/data/
/etc/tascloud_3rdparty/mysql/dbpasswd

cp /home/sensor/ROOT.war /usr/share/tomcat9/webapps/ROOT.war

/usr/share/tomcat9/bin/catalina.sh start

sleep 3

/home/sensor/sensor -c /home/sensor/cfg/sensor.yaml --af-packet &

echo "TAS CLOUD INSTALL LOG :: Done."

