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
FINAL				= true
DEFINES				=

INCLUDES			=

LIBDIRS				=
LIBS				=

CFLAGS				=
CPPFLAGS			=
LDFLAGS				=

THREAD_SAFE			= true

SRCS                = TSEnvman.c \

HEADER_DIR          = common

HEADERS             = TSEnvman.h \

TARGET              = libTSEnvman.so \
                      libTSEnvman.a

include $(TOP)/rules/common.mk
