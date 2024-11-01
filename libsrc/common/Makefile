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

SRCS                = TSDebug.c \
                      TSLinuxFunc.c \
                      TSParser.c \
                      TSSafeFunc.c \
                      TSNLSParser.c \
                      TSCommandline.c \
                      util.c \

HEADER_DIR          = common

HEADERS             = TSCommon.h \
                      TSTypedef.h \
                      TSPacketdef.h \
                      TSNLSParser.h \
                      TSSignature.h \
                      TSAuditlogdef.h \
                      TSCommandline.h \
                      TSNetmonlogdef.h \

TARGET              = libTSCommon.so \
                      libTSCommon.a

include $(TOP)/rules/common.mk
