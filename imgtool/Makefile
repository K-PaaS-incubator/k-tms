# 
# Copyright (c) All rights reserved.
#
# $Id:$
# vim: set nu ts=4 sw=4 :

ifndef TOP
TOP   = $(shell cd $(dir $(firstword \
        $(foreach d, . .. ../.. ../../.. ../../../.. ../../../../.. ../../../../../..,\
        $(wildcard $(addsuffix /@TOP,$(d)))))) && pwd)
export TOP
endif

imgtool_chkfile=build.ubuntu copybin.ubuntu

all:
	@echo
	@echo "***********************************************************"
	@echo "Sorry, I don't know what to do."
	@echo "  So, Please choose one to continue:"
	@echo "-----------------------------------------------------------"
	@echo "   1. make ubuntu  : Making Ubuntu Package"
	@echo "***********************************************************"

ubuntu: chkimgtoolenv
	@./build.ubuntu

chkimgtoolenv:
	@for i in $(imgtool_chkfile); do if [ ! -x $$i ]; then chmod 777 $$i ; fi; done

clean:
	rm -rf VERSIONNAME.* *.gz version.* 

cleanall: clean
	@cd util;$(MAKE) cleanall;
