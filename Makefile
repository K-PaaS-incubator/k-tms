#-------------------------------------------------------------
#
# Makefile -- Top level dist makefile.
#
# Copyright (c) 2003-2010, INFOSEC (www.infosec.co.kr)
# Copyright (c) 2010, Ycheol
#
#-------------------------------------------------------------
# vim: ts=4 sw=4 :

TOP		= $(shell pwd)
SUBDIRS	= bring libsrc src webconsole

export TOP

#-------------------------------------------------------------
# INCLUDE $(TOP)/.config
#-------------------------------------------------------------
ifeq ($(TOP)/.config,$(wildcard $(TOP)/.config))
-include $(TOP)/.config
endif

-include rules/common.mk

ID               = `echo $USER | awk '{print toupper($$1)}'`
CONFIG_CONFIG_IN = $(TOP)/config/Config.in

#-------------------------------------------------------------
# GLOBAL DEFINED VARIABLES
#-------------------------------------------------------------
.PHONY: datasrc distclean config bootcd image bootxen

distclean: outclean

outclean:
	rm -rf .config
	rm -rf scripts/mconf
	rm -rf scripts/conf
	rm -rf lib/*
	rm -rf include/*
	rm -rf bin/*
	rm -rf sbin/*
	rm -rf etc/*
	rm -rf nls/ko
	@cd scripts;$(MAKE) cleanall;
	@cd imgtool;$(MAKE) cleanall;

config:
	@echo "***********************************************************"
	@echo "ERROR: Oops, I don't know what to make."
	@echo "ERROR: However, this feature will be found through via.    " 
	@echo "  'shell> make menuconfig'"
	@echo "***********************************************************"

image: install
	@cd imgtool;$(MAKE) image

bootcd: image 
	@cd imgtool;$(MAKE) bootcd

bootxen:
	@cd imgtool;$(MAKE) bootxen

bootusb:
	@cd imgtool;$(MAKE) bootusb

ubuntu:
	@cd imgtool;$(MAKE) ubuntu

container:
	@cd imgtool;$(MAKE) container

datasrc:
	make -C datasrc

