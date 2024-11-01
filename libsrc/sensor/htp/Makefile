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

SRCS                = bstr.c \
					  bstr_builder.c \
					  htp_base64.c \
					  htp_config.c \
					  htp_connection.c \
					  htp_connection_parser.c \
					  htp_content_handlers.c \
					  htp_cookies.c \
					  htp_decompressors.c \
					  htp_hooks.c \
					  htp_list.c \
					  htp_multipart.c \
					  htp_parsers.c \
					  htp_php.c \
					  htp_request.c \
					  htp_request_apache_2_2.c \
					  htp_request_generic.c \
					  htp_request_parsers.c \
					  htp_response.c \
					  htp_response_generic.c \
					  htp_table.c \
					  htp_transaction.c \
					  htp_transcoder.c \
					  htp_urlencoded.c \
					  htp_utf8_decoder.c \
					  htp_util.c \
					  strlcat.c \
					  strlcpy.c 


HEADER_DIR          = htp

HEADERS             = htp.h \
					  bstr.h \
					  bstr_builder.h \
					  htp_base64.h \
					  htp_config.h \
					  htp_config_private.h \
					  htp_connection_parser.h \
					  htp_connection_parser_private.h \
					  htp_connection_private.h \
					  htp_core.h \
					  htp_decompressors.h \
					  htp_hooks.h \
					  htp_list.h \
					  htp_list_private.h \
					  htp_multipart.h \
					  htp_multipart_private.h \
					  htp_private.h \
					  htp_table.h \
					  htp_table_private.h \
					  htp_transaction.h \
					  htp_urlencoded.h \
					  htp_utf8_decoder.h \
					  htp_version.h




TARGET              = libhtp.so \
                      libhtp.a

include $(TOP)/rules/common.mk
