#!/bin/bash
#
#
newDb='TESS'

if [ "`mysql -u root -e 'show databases;' | grep ${newDb}`" == "${newDb}" ]; then
    echo "Database found."
else
	echo "Database not found. sql initialize start..."
	mysql -u root < $PWD/mysql/initdbscripts/create_db.sql
	mysql -u root < $PWD/mysql/initdbscripts/initCreateTable.sql
	mysql -u root < $PWD/mysql/initdbscripts/initMenu.sql
	mysql -u root < $PWD/mysql/initdbscripts/initSystem.sql
	mysql -u root < $PWD/mysql/initdbscripts/initYaraPolicy.sql
	mysql -u root < $PWD/mysql/initdbscripts/initAuditLog.sql
	mysql -u root < $PWD/mysql/initdbscripts/initDetectionPolicy.sql
	mysql -u root < $PWD/mysql/initdbscripts/initDetectionPolicyHelp.sql
	echo "sql initialize end..."
fi
