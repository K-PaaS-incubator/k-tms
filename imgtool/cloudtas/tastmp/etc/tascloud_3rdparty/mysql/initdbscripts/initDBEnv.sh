#!/bin/bash
#
#
newDb='TESS'

if [ "`mysql -u root -e 'show databases;' | grep ${newDb}`" == "${newDb}" ]; then
    echo "Database found."
else
	echo "Database not found. sql initialize start..."
	mysql -u root < /etc/mysql/initdbscripts/create_db.sql
	mysql -u root < /etc/mysql/initdbscripts/initCreateTable.sql
	mysql -u root < /etc/mysql/initdbscripts/initMenu.sql
	mysql -u root < /etc/mysql/initdbscripts/initSystem.sql
	mysql -u root < /etc/mysql/initdbscripts/initYaraPolicy.sql
	mysql -u root < /etc/mysql/initdbscripts/initAuditLog.sql
	mysql -u root < /etc/mysql/initdbscripts/initDetectionPolicy.sql
	echo "sql initialize end..."
fi
