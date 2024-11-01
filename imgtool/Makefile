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

imgtool_chkfile=build.img build.usb build.cd copybin.img copybin.cd

all:
	@echo
	@echo "***********************************************************"
	@echo "Sorry, I don't know what to do."
	@echo "  So, Please choose one to continue:"
	@echo "-----------------------------------------------------------"
	@echo "   1. make bootcd  : Making BootCD"
	@echo "   2. make bootusb : Making BootUSB"
	@echo "   3. make bootxen : Making Installer on other Linux"
	@echo "   4. make image   : Making integrated Image"
	@echo "   5. make ubuntu  : Making Ubuntu Package"
	@echo "***********************************************************"

bootcd: chkimgtoolenv
	@./build.cd

bootusb: chkimgtoolenv
	@./build.usb

bootxen: chkimgtoolenv
	@./build.xen

image: chkimgtoolenv
	@./build.img

ubuntu: chkimgtoolenv
	@./build.ubuntu

chkimgtoolenv:
	@for i in $(imgtool_chkfile); do if [ ! -x $$i ]; then chmod 777 $$i ; fi; done

clean:
	rm -rf VERSIONNAME.* *.gz version.* 

cleanall: clean
	@cd util;$(MAKE) cleanall;
