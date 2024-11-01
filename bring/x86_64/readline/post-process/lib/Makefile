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
	cp -f libhistory.so.6			$(TOP)/lib/
	cp -f libreadline.so.6			$(TOP)/lib/
	cp -f libhistory.so				$(TOP)/lib/
	cp -f libreadline.so			$(TOP)/lib/

clean :
	rm -f $(TOP)/lib/bhistory.so.6
	rm -f $(TOP)/lib/libreadline.so.6
	rm -f $(TOP)/lib/libhistory.so
	rm -f $(TOP)/lib/libreadline.so

include $(TOP)/rules/common.mk
