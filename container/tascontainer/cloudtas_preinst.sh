#!/bin/bash

echo "TAS CLOUD INSTALL LOG :: pkill apt."
 pkill apt
sleep 1

echo "TAS CLOUD INSTALL LOG :: apt update."
 apt update
sleep 1

echo "TAS CLOUD INSTALL LOG :: apt install."
 apt -y install net-tools
 apt -y install vim
 apt -y install wget
 apt -y install perl
 apt -y install iputils-ping
 apt -y install expect
 apt -y install libelf1
 apt -y install libyaml-0-2
 apt -y install libhyperscan5
 apt -y install libssh2-1
 apt -y install htop
 apt -y install rdate
 apt -y install ntp
 apt -y install whois
 apt -y install file
 apt -y install fdisk
 apt -y install psmisc
 apt -y purge apport
 apt -y install openjdk-8-jdk 
 apt -y install tomcat9 tomcat9-admin
 apt -y install mariadb-client

echo "TAS CLOUD INSTALL LOG :: dpkg install."
 dpkg -i /root/cloudtas.deb
sleep 1

echo "TAS CLOUD INSTALL LOG :: mysql passwd folder create."
mkdir -p /usr/local/mysql/data/

echo "TAS CLOUD INSTALL LOG :: tomcat setting"
chmod 777 /home/sensor/rule/*
ln -s  /etc/tomcat9 /usr/share/tomcat9/conf
mkdir /usr/share/tomcat9/logs
cp /etc/tascloud_3rdparty/default/tomcat9 /etc/default/
cp /etc/tascloud_3rdparty/tomcat9/server.xml /etc/tomcat9/
cp /etc/tascloud_3rdparty/tomcat9/keystore /etc/tomcat9/

rm -rf /var/lib/tomcat9/webapps/ROOT/
sed -i '/ReadWritePaths=\/var\/log\/tomcat9/a\ReadWritePaths=\/home\/sensor\/rule\/' /usr/lib/systemd/system/tomcat9.service
sed -i '/ReadWritePaths=\/home\/sensor\/rule/a\ReadWritePaths=\/usr\/local\/mysql\/data\/' /usr/lib/systemd/system/tomcat9.service

mkdir /usr/share/tomcat9/webapps
cp /home/sensor/ROOT.war /usr/share/tomcat9/webapps/ROOT.war

echo "TAS CLOUD INSTALL LOG :: KEK, DEK created."

if [ -f /usr/lib/file/magic.mgc ]; then
	echo "TAS CLOUD INSTALL LOG :: cp magic.mgc"
	 cp -f /usr/lib/file/magic.mgc /etc/
fi

echo "TAS CLOUD INSTALL LOG :: Done."

