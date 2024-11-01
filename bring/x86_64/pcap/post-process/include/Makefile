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
	mkdir -p $(TOP)/include/pcap
	cp -f pcap.h bpf.h          $(TOP)/include/pcap/

clean :
	rm -f $(TOP)/include/pcap/*.h
	
include $(TOP)/rules/common.mk
