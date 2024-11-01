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
DUMPVAR				= true
FINAL				= part
DEFINES				=

INCLUDES			= -I../include

LIBDIRS				=
LIBS				=

CFLAGS				=
CPPFLAGS			=
LDFLAGS				=

THREAD_SAFE			= true

SRCS                = tests.c \
					  pe.c \
					  math.c \
					  elf.c \
#					  hash.c \
					  magic.c \
					  cuckoo.c \

HEADER_DIR          = 

HEADERS             = none

TARGET              = libTSYara_modules.so \
                      libTSYara_modules.a

include $(TOP)/rules/common.mk
