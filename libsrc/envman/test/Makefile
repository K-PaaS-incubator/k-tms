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
DEFINES				= -D_BSD_SOURCE -D_REENTRANT -D_POSIX_MAPPED_FILES -DLINUX \

INCLUDES			= $(TOP)/include \

LIBDIRS				=
LIBS				= -ldl -lpthread \
                      -Xlinker --start-group \
                      $(TOP)/lib/libTSCommon.a \
                      $(TOP)/lib/libTSEnvman.a \
                      -Xlinker --end-group \

CFLAGS				=
CPPFLAGS			=
LDFLAGS				=

THREAD_SAFE			= true

SRCS                = test.cc \

HEADER_DIR          = none

HEADERS             = none

TARGET              = test

include $(TOP)/rules/common.mk
