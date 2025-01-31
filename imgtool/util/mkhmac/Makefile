#-------------------------------------------------------------
# Copyright (c) All rights reserved.
#
# $id:$
#-------------------------------------------------------------
# vim: ts=4 sw=4 :

ifndef TOP
TOP   = $(shell cd $(dir $(firstword \
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
DUMPVAR         = true
FINAL           = true

THREAD_SAFE     = true

INCLUDES        = -I$(TOP)/include/common

LIBDIRS         =

LIBS            = -lpthread \
				  -Xlinker --start-group \
				  $(TOP)/lib/libTSCommon.a \
				  $(TOP)/lib/libTSCapture.a \
				  -Xlinker --end-group \

DEFINES         =

CPPFLAGS        =

SRCS            = sha1.cc \
				  mkhmac.cc \

HEADERS         = none

TARGET          = mkhmac

include common.mk

#-------------------------------------------------------------
# for Current System 
#-------------------------------------------------------------
#CC      		= gcc
#CPP				= g++
