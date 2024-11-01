#!/bin/bash

echo "TAS CLOUD INSTALL LOG :: apt update."
sudo apt update
sleep 1

if [ ! -e /usr/local/mysql ] ; then

echo "TAS CLOUD INSTALL LOG :: 3rdparty first install."
sudo apt install curl software-properties-common dirmngr ca-certificates apt-transport-https -y
sudo rm -rf /etc/apt/sources.list.d/mariadb.list*
curl -LsS https://downloads.mariadb.com/MariaDB/mariadb_repo_setup | sudo bash -s -- --mariadb-server-version=10.11
sudo apt update
sleep 1
sudo apt -y install openjdk-8-jdk 
sudo apt -y install tomcat9 tomcat9-admin
sudo apt -y install mariadb-server
sleep 3

	# mariadb data storage
	sudo ln -s /var/lib/mysql /usr/local/mysql
	# create mysql default dir ( see rootfs/COMMON/x86_64/etc/my.cnf)
	sudo mkdir -p /usr/local/mysql/data/mysql_tmp
	sudo mkdir -p /usr/local/mysql/data/innodb
	sudo mkdir -p /usr/local/mysql/data/mysql_iblog
	
	sudo mysql_install_db --user=mysql --basedir=/usr --datadir=/usr/local/mysql/data --auth-root-authentication-method=normal
	
	sudo chown -R mysql:mysql /usr/local/mysql/data
	
	sudo cp mysql/my.cnf /etc/mysql/
	
	sudo systemctl restart mysql
	sleep 1
	echo "TAS CLOUD INSTALL LOG :: tomcat setting"
	sudo cp default/tomcat9 /etc/default/
	sudo cp tomcat9/server.xml /etc/tomcat9/
	sudo cp tomcat9/keystore /etc/tomcat9/
	sudo cp /home/sensor/ROOT.war /var/lib/tomcat9/webapps/
	sudo rm -rf /var/lib/tomcat9/webapps/ROOT/
	
	sudo systemctl restart tomcat9
	sleep 1
	
	echo "TAS CLOUD INSTALL LOG :: mysql table create."
	sudo chmod 755 mysql/dbpasswd
	sudo chmod 755 mysql/initdbscripts/*
	sudo mysql/initdbscripts/initDBEnv.sh
	sudo mysql/dbpasswd
fi

echo "TAS CLOUD INSTALL LOG :: Done."

