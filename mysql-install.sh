#!/bin/bash

sudo apt update

sudo apt install mysql-server
# set Y


#sudo systemctl status mysql


sudo mysql_secure_installation
# set Y
# set 0
# set  root pwd 2x
# y 5x times