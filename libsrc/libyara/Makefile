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
FINAL				= importall
SUBDIRS             = modules 

DEFINES				=

INCLUDES			=  -I./include

LIBDIRS				=
LIBS				=

CFLAGS				=
CPPFLAGS			=
LDFLAGS				=

THREAD_SAFE			= true

SRCS                = mem.c \
					  hash.c \
					  ahocorasick.c \
					  arena.c \
					  atoms.c \
					  compiler.c \
					  exec.c \
					  exefiles.c \
					  filemap.c \
					  object.c \
					  proc.c \
					  rules.c \
					  scan.c \
					  sizedstr.c \
					  strutils.c \
					  libyara.c \
					  re.c \
					  modules.c \
					  hex_lexer.c \
					  re_lexer.c \
					  hex_grammar.c \
					  re_grammar.c \
					  lexer.c \
					  grammar.c \
					  parser.c \

HEADER_DIR          = yara

HEADERS             = include/yara.h \
					  include/yara_atoms.h \
					  include/yara_error.h \
					  include/yara_filemap.h \
					  include/yara_hex_lexer.h \
					  include/yara_limits.h \
					  include/yara_object.h \
					  include/yara_proc.h \
					  include/yara_rules.h \
					  include/yara_strutils.h \
					  include/yara_ahocorasick.h \
					  include/yara_compiler.h \
					  include/yara_exec.h \
					  include/yara_globals.h \
					  include/yara_lexer.h \
					  include/yara_mem.h \
					  include/yara_parser.h \
					  include/yara_re.h \
					  include/yara_scan.h \
					  include/yara_types.h \
					  include/yara_arena.h \
					  include/yara_elf.h \
					  include/yara_exefiles.h \
					  include/yara_hash.h \
					  include/yara_libyara.h \
					  include/yara_modules.h \
					  include/yara_pe.h \
					  include/yara_re_lexer.h \
					  include/yara_sizedstr.h \
					  include/yara_utils.h \
					  
TARGET              = libTSYara.so \
                      libTSYara.a

include $(TOP)/rules/common.mk
