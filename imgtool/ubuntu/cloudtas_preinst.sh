#!/bin/bash

echo "TAS CLOUD INSTALL LOG :: pkill apt."
sudo pkill apt
sleep 1

echo "TAS CLOUD INSTALL LOG :: apt stop."
sudo systemctl stop apt-daily.service
sudo systemctl stop apt-daily.timer
sudo systemctl stop apt-daily-upgrade.service
sudo systemctl stop apt-daily-upgrade.timer
sleep 1

echo "apt disable."
sudo systemctl disable apt-daily.service
sudo systemctl disable apt-daily.timer
sudo systemctl disable apt-daily-upgrade.service
sudo systemctl disable apt-daily-upgrade.timer
sleep 1

echo "TAS CLOUD INSTALL LOG :: apt update."
sudo apt update
sleep 1

echo "TAS CLOUD INSTALL LOG :: apt install."
sudo apt -y install net-tools
sudo apt -y install vim
sudo apt -y install wget
sudo apt -y install perl
sudo apt -y install iputils-ping
sudo apt -y install expect
sudo apt -y install libelf1
sudo apt -y install libyaml-0-2
sudo apt -y install libhyperscan5
sudo apt -y install libssh2-1
sudo apt -y install language-pack-ko
sudo apt -y install htop
sudo apt -y install rdate
sudo apt -y install ntp
sudo apt -y install whois
sudo apt -y purge apport

echo "TAS CLOUD INSTALL LOG :: dpkg nstall."
sudo dpkg -i cloudtas.deb
sleep 1

if [ -f /usr/lib/file/magic.mgc ]; then
	echo "TAS CLOUD INSTALL LOG :: cp magic.mgc"
	sudo cp -f /usr/lib/file/magic.mgc /etc/
fi


echo "TAS CLOUD INSTALL LOG :: Done."

