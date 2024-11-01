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

INCLUDES        = -I./

LIBDIRS         =

LIBS            = 

DEFINES         =

CPPFLAGS        =

SRCS            = crc32.c \
				  mkimage.c \

HEADERS         = none

TARGET          = mkimage

include common.mk

#-------------------------------------------------------------
# for Current System 
#-------------------------------------------------------------
CC      		= gcc
CPP 			= g++
