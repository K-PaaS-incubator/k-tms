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
SUBDIRS	= webconsole

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
.PHONY: distclean

distclean: outclean

outclean:
	@cd imgtool;$(MAKE) cleanall;

config:
	@echo "***********************************************************"
	@echo "ERROR: Oops, I don't know what to make."
	@echo "ERROR: However, this feature will be found through via.    " 
	@echo "  'shell> make menuconfig'"
	@echo "***********************************************************"

ubuntu: webconsole
	@cd imgtool;$(MAKE) ubuntu

