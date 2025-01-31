#!/bin/bash

echo "TAS CLOUD INSTALL LOG :: apt update."

# timezone change - KST
ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

# mariadb data storage
ln -s /var/lib/mysql /usr/local/mysql
# create mysql default dir ( see rootfs/COMMON/x86_64/etc/my.cnf)
mkdir -p /usr/local/mysql/data/mysql_tmp
mkdir -p /usr/local/mysql/data/innodb
mkdir -p /usr/local/mysql/data/mysql_iblog
chown -R mysql:mysql /usr/local/mysql/data

mysql_install_db --user=mysql --basedir=/usr --datadir=/usr/local/mysql/data --auth-root-authentication-method=normal

service mariadb start
sleep 3

echo "TAS CLOUD INSTALL LOG :: mysql table create."
chmod 755 /etc/mysql/initdbscripts/*
/etc/mysql/initdbscripts/initDBEnv.sh

	
echo "TAS CLOUD INSTALL LOG :: Done."

