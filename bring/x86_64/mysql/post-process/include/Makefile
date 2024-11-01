#-------------------------------------------------------------
# Copyright (c) All rights reserved.
#
# $id:$
#-------------------------------------------------------------
# vim: ts=4 sw=4 :

ifndef TOP
TOP  = $(shell cd $(dir $(firstword \
        $(foreach d, . .. ../.. ../../.. ../../../.. ../../../../.. ../../../../../..,\
        $(wildcard $(addsuffix /@TOP,$(d)))))) && pwd)
export TOP
endif

#-------------------------------------------------------------
# INCLUDE $(TOP)/.config
#-------------------------------------------------------------
ifeq ($(TOP)/.config,$(wildcard $(TOP)/.config))
-include $(TOP)/.config
endif

#-------------------------------------------------------------
# USER DEFINED LOCAL VARIABLES
#-------------------------------------------------------------
DUMPVAR             = true
FINAL               = true
SUBDIR				= 

all :
	mkdir -p $(TOP)/include/mysql
	cp -f mysql/mysql.h $(TOP)/include/mysql/
	cp -f mysql/mysql_version.h $(TOP)/include/mysql/
	cp -f mysql/mysql_com.h $(TOP)/include/mysql/
	cp -f mysql/mysql_time.h $(TOP)/include/mysql/
	cp -f mysql/my_list.h $(TOP)/include/mysql/
	cp -f mysql/typelib.h $(TOP)/include/mysql/
	cp -f mysql/my_alloc.h $(TOP)/include/mysql/

clean :
	rm -rf $(TOP)/include/mysql

include $(TOP)/rules/common.mk
