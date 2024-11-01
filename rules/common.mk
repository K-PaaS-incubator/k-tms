# vim: set nu ts=4 sw=4 :
#
# ====================================================================
# ==                      USER DEFINED LOCAL VARIABLES              ==
# ====================================================================
# ****************** NORMAL BUILD TARGET BASE  ***********************
#
# FINAL            = true               ;; true only allowed
# TESTING          = true               ;; TEST directory, true only, replace FINAL
# SUBDIRS          = subdirectory list  ;; or blank.
# DEFINES          = -Dxxx=val -Dyyy    ;;
# INCLUDES         =                    ;; excepts -I. -I$(INC_DIR)
# USING_QUICKSEC   = true               ;; true or false, blank
# LIBDIRS          =                    ;; excepts -L$(LIB_DIR)
# LIBS             =                    ;; -l options except boost
# CFLAGS           =                    ;; excepts -O2 -g -Wall
#                                       ;; do not add $(INCLUDES) after this.
# CPPFLAGS         =                    ;; excepts -O2 -g -Wall
#                                       ;; do not add $(INCLUDES) after this.
# LDFLAGS          =                    ;; do not add $(LIBS) or $(LIBDIRS)
# THREAD_SAFE      =                    ;; use thread safe
# SRCS             =  a.cpp b.c         ;; do not define any .o
# DUMPVAR          = true               ;; true only allowed
#
# .1. USER MAY DEFIND AND DEPENDENCY RULES
# .2. USER MAY DEFIND AND VPATH SEARCH ALGORITHMS.
#
# ****************** NORMAL SUBDIR TARGET BASE  ***********************
#
# FINAL            = false              ;; true only allowed
# SUBDIRS          =                    ;; directory order to build
#                                       ;; or do not anything.
# DUMPVAR          = true               ;; true only allowed


# ================= CHECK ENV ==================
.PHONY: chkenv

ifeq (/mc, $(wildcard /mc))

ifeq "$(strip $(PRODUCT))" ""
chkenv:
	@echo
	@echo "***********************************************************"
	@echo "ERROR: Oops, I don't know what to make."
	@echo "ERROR: Please check your Build-Environment !!!!"
	@echo "1. 'shell>t'"
	@echo "2. 'shell>tas1000'"
	@echo "***********************************************************"
	@echo
	@exit 1
else
ifeq "$(strip $(shell grep -x $(PRODUCT) $(TOP)/TARGETS))" ""
chkenv:
	@echo
	@echo "***********************************************************"
	@echo "You do have run first make menuconfig why platform mismatch..."
	@echo "The build sequence for this source tree is:"
	@echo "1. 'make menuconfig'"
	@echo "2. 'make'"
	@echo "***********************************************************"
	@echo
	@exit 1
endif
endif
ifeq "$(strip $(ARCH))" ""
chkarch:
	@echo
	@echo "***********************************************************"
	@echo "WARRING!! Oops, no. But if you want to perform on this" 
	@echo "          compilation should move into the build system."
	@echo "The build sequence for this source tree is:"
	@echo "1. 'source move to build system'"
	@echo "2. 'make'"
	@echo "***********************************************************"
	@echo
	@exit 1
endif
endif

# ================= CHECK CONFIG FILE ==========
.PHONY: chkcfg

ifneq ($(TOP)/.config, $(wildcard $(TOP)/.config))
chkcfg:
	@echo
	@echo "*****************************************************************"
	@echo "You have not run make menuconfig..."
	@echo "The build sequence for this source tree is:"
	@echo "1. 'make menuconfig'" 
	@echo "2. 'make'"
	@echo "*****************************************************************"
	@echo
	@exit 1
endif

ifdef TEST_DIRS
  $(error not supports TEST_DIRS)
endif
ifdef TEST_ROOT
  $(error not supports TEST_ROOT)
endif
ifdef TEST_DIR
  $(error not supports TEST_DIR)
endif
ifdef TEST_SETUID
  $(error not supports TEST_SETUID)
endif

common_bin         = $(commondir)/bin
common_inc         = $(commondir)/include
common_lib         = $(commondir)/lib

## ====================================================================
## COMMON EXECUTABLE PROGRAMS
## ====================================================================

DISTCC             =
ifdef DISTCC_HOSTS
  DISTCC           = distcc
endif

CC                 = $(DISTCC) $(CROSSBIN)gcc
CPP                = $(DISTCC) $(CROSSBIN)g++
CXX                = $(DISTCC) $(CROSSBIN)g++
LD                 =           $(CROSSBIN)ld
AS                 =           $(CROSSBIN)as
AR                 =           $(CROSSBIN)ar
NM                 =           $(CROSSBIN)nm
STRIP              =           $(CROSSBIN)strip
OBJCOPY            =           $(CROSSBIN)objcopy
OBJDUMP            =           $(CROSSBIN)objdump

MAKE               = make
INSTALL            = /usr/bin/install -c
SHELL              = /bin/sh -e
CSCOPE             = /usr/bin/cscope
CTAGS              = /usr/bin/ctags
CP                 = /bin/cp

## ====================================================================
## COMMON DIRECTORIS
## ====================================================================

ifdef TOP
  topdir           = $(TOP)
else
  $(error TOP variable is not defined)
endif

top_bin            = $(topdir)/bin
top_inc            = $(topdir)/include
top_lib            = $(topdir)/lib
top_man            = $(topdir)/man
top_share          = $(topdir)/share
top_etc            = $(topdir)/etc
top_webdoc         = $(topdir)/webdoc
top_result         = $(topdir)/test-result

ifeq (/mc, $(wildcard /mc))
   kernel_src_top		= $(TOP)/os/kernel/
   kernel_inc			= $(TOP)/os/kernel/include
   kernel_usr_inc		= $(TOP)/os/kernel/usr/include
else
   KERNEL_VER			= $(shell uname -r)
   kernel_src_top		= /lib/modules/$(KERNEL_VER)/build
   kernel_inc			= /lib/modules/$(KERNEL_VER)/build/include
   kernel_usr_inc		= /lib/modules/$(KERNEL_VER)/build/usr/include
endif

## ====================================================================
## OPTIONS
## ====================================================================

ifndef BUILD_TARGET
  BUILD_TARGET  = __DEBUG
endif

ifeq "$(strip $(BUILD_TARGET))" "__DEBUG"
  defines_dbg      = -DDeBUG -DDEBUG_MODE
  cflags_dbg       = -g -Wall -Wno-write-strings
  cppflags_dbg     = -g -Wall -Wno-write-strings
  ldflags_dbg      =
  OBJDIR           = .obj
  DEPEND_FILE      = .depend.dbg
else
  defines_dbg      =
  cflags_dbg       = -O2 -Wall
  cppflags_dbg     = -O2 -Wall
  ldflags_dbg      = -O2
  OBJDIR           = .rel
  DEPEND_FILE      = .depend.rel
endif

## ====================================================================
## DEFAULT FLAGS
## ====================================================================
#includes      = $(INCLUDES) -I. -I$(kernel_usr_inc) -I$(top_inc) 
includes      = $(INCLUDES) -I. -I$(top_inc) 

includes     += -I$(common_inc)

target_so            = false
target_a             = false
target_exe           = false
target_mod           = false
target_is_lib        = false

ifdef TARGET
ifneq "$(filter .so, $(suffix $(TARGET)))" ""
  target_so          = true
  target_is_lib      = true
endif
ifneq "$(filter .a, $(suffix $(TARGET)))" ""
  target_a           = true
  target_is_lib      = true
endif
ifneq "$(filter .o, $(suffix $(TARGET)))" ""
  target_mod         = true
  target_is_lib      = false
endif
ifeq "$(filter .so .a .o, $(suffix $(TARGET)))" ""
  target_exe         = true
endif
endif

ifeq "$(strip $(THREAD_SAFE))" "true"
defines_thread_safe = -D_REENTRANT
endif

defines         = $(defines_dbg) $(defines_thread_safe) $(DEFINES)
cflags          = $(cflags_dbg) $(CFLAGS) $(defines) $(includes)
cppflags        = $(cppflags_dbg) $(CPPFLAGS) $(defines) $(includes)
cppflags_shared = -fPIC
cflags_shared   = -fPIC
ldflags_shared  = -shared

ldflags = $(ldflags_dbg) -L$(top_lib) -L$(common_lib) $(LIBDIRS) $(LIBS)

## ====================================================================
## LOCAL TARGETS
## ====================================================================
ifdef SRCS
  CSRCS       = $(filter %.c %.cpp %.cc %.C, $(SRCS))
  tmpobj1     = $(patsubst %.c,%.o,$(SRCS))
  tmpobj2     = $(patsubst %.cpp,%.o,$(tmpobj1))
  tmpobj3     = $(patsubst %.cc,%.o,$(tmpobj2))
  tmpobj4     = $(patsubst %.C,%.o,$(tmpobj3))
  tmpobj5     = $(patsubst %.S,%.o,$(tmpobj4))
  tmpobj6     = $(patsubst %.s,%.o,$(tmpobj5))
  tmpobj      = $(tmpobj6)
  ifeq "$(strip $(target_exe))" "true"
    OBJS      = $(tmpobj)
    OBJ_OBJS  = $(addprefix $(OBJDIR)/, $(OBJS))
	  DEPS      = $(patsubst %.o,%.d,$(OBJ_OBJS))
    .PRECIOUS: $(DEPS)
  endif
  ifeq "$(strip $(target_a))" "true"
    LOBJS     = $(patsubst %.o,%.lo,$(tmpobj))
    OBJ_LOBJS = $(addprefix $(OBJDIR)/, $(LOBJS))
	  DEPS      = $(patsubst %.lo,%.d,$(OBJ_LOBJS))
    .PRECIOUS: $(OBJ_LOBJS) $(DEPS)
  endif
  ifeq "$(strip $(target_so))" "true"
    SLOBJS    = $(patsubst %.o,%.slo,$(tmpobj))
    OBJ_SLOBJS= $(addprefix $(OBJDIR)/, $(SLOBJS))
	  DEPS      = $(patsubst %.slo,%.d,$(OBJ_SLOBJS))
    .PRECIOUS: $(OBJ_SLOBJS) $(DEPS)
  endif
  ifeq "$(strip $(target_mod))" "true"
    LOBJS    = $(patsubst %.o,%.lo,$(tmpobj))
    OBJ_LOBJS= $(addprefix $(OBJDIR)/, $(LOBJS))
	  DEPS     = $(patsubst %.lo,%.d,$(OBJ_LOBJS))
    .PRECIOUS: $(OBJ_LOBJS) $(DEPS)
  endif
endif
ifndef HEADERS
  HEADERS       = $(patsubst %.cpp,%.h,$(patsubst %.c,%.h,$(CSRCS)))
endif


DEPEND_BAK    = $(DEPEND_FILE).bak
CSCOPE_FILE   = .cscope.out
CTAGS_FILE    = .TAGS
CTAGS_FLAGS   = --recurse=yes -B -h ".h.hh.hpp" --extra=+q --fields=+i --language-force=c++ 
IMPORT_NAME   = $(OBJDIR)/subdirs.import
EXPORT_NAME   = local.export

ifdef INSTALL_DIR
  install_dir = $(top_bin)/$(INSTALL_DIR)
else
  install_dir = $(top_bin)
endif

installer=$(INSTALL)
install_flags =
ifneq "$(strip $(INSTALL_USER))" ""
  install_flags += --owner=$(INSTALL_USER)
  installer= sudo $(INSTALL)
endif
ifneq "$(strip $(INSTALL_GROUP))" ""
  install_flags += --group=$(INSTALL_GROUP)
  installer= sudo $(INSTALL)
endif
ifeq "$(strip $(INSTALL_SETUID))" "true"
  install_flags += --mode=4755
else
  install_flags += --mode=0755
endif


## ====================================================================
## DEFAULT TARGET
## ====================================================================
ifeq "$(strip $(FINAL))" "true"
  SUBDIRS =
  ifdef TARGET
    OBJ_TARGET = $(addprefix $(OBJDIR)/, $(TARGET))
    target_job = $(DUMP) $(OBJDIR) $(DEPEND_FILE) $(OBJ_TARGET)
    clean_job = local-clean
    tclean_job = local-tclean
    clear_job = local-clear
    cleanall_job = local-cleanall local-test-clean
    test_job = $(target_job) local-test
    ifdef SRCS
      depend_job = local-depend tags
      tags_job = local-tags
    endif
    rebuild_job = local-clean $(OBJDIR) $(OBJ_TARGET)
    ifeq "$(target_is_lib)" "true"
		  install_libs = $(addprefix $(top_lib)/, $(TARGET))
      target_job += local-install-libs
      install_job = $(target_job)
      ifdef HEADER_DIR
        header_dir = $(top_inc)/$(HEADER_DIR)
      else
        header_dir = $(top_inc)
      endif
      ifneq "$(strip $(HEADERS))" "none"
        target_job += local-install-headers
				install_headers = $(addprefix $(header_dir)/, $(HEADERS))
        install_job = $(target_job)
      endif
      run_job = local-run-error
      debug_job = local-debug-error
    else
      ifeq "$(target_exe)" "true"
        OBJ_EXE = $(OBJ_TARGET)
        ifdef HEADER_DIR
          header_dir = $(top_inc)/$(HEADER_DIR)
        else
          header_dir = $(top_inc)
        endif
        ifneq "$(strip $(HEADERS))" "none"
          target_job += local-install-headers
				  install_headers = $(addprefix $(header_dir)/, $(HEADERS))
        endif
	      install_job = local-install
      endif
			ifeq "$(target_mod)" "true"
	      install_job = local-install-mod
        install_module = $(addprefix $(install_dir)/, $(TARGET))
			endif
      run_job = local-run
      debug_job = local-debug
    endif
  else
    target_job = $(DUMP) error-notarget
  endif
endif

ifeq "$(strip $(FINAL))" "part"
  ## subdir이 있으면 그 subdir로 이동해서 또 컴파일해야 한다.
  ifdef TARGET
    OBJ_TARGET = $(addprefix $(OBJDIR)/, $(TARGET))
    EXP_TARGET = $(addprefix $(OBJDIR)/$(EXPORT_NAME), $(suffix $(TARGET)))
    ifdef SUBDIRS
      ifeq "$(strip $(target_exe))" "true"
        SUB_OBJS  = $(addsuffix /$(OBJDIR)/$(EXPORT_NAME), $(SUBDIRS))
      endif
      ifeq "$(strip $(target_a))" "true"
        SUB_LOBJS = $(addsuffix /$(OBJDIR)/$(EXPORT_NAME).a, $(SUBDIRS))
      endif
      ifeq "$(strip $(target_so))" "true"
        SUB_SLOBJS= $(addsuffix /$(OBJDIR)/$(EXPORT_NAME).so, $(SUBDIRS))
      endif
      target_job = $(DUMP) $(OBJDIR) subdirs $(DEPEND_FILE) $(OBJ_TARGET) $(EXP_TARGET)
    else
      target_job = $(DUMP) $(OBJDIR) $(DEPEND_FILE) $(OBJ_TARGET) $(EXP_TARGET)
    endif
    ifdef SUBDIRS
      clean_job = subdirs-clean local-clean 
    else
      clean_job = local-clean 
    endif
    ifdef SUBDIRS
      tclean_job = subdirs-tclean local-tclean 
    else
      tclean_job = local-tclean 
    endif
    ifdef SUBDIRS
      clear_job = subdirs-clear local-clear
    else
      clear_job = local-clear
    endif
    ifdef SUBDIRS
      cleanall_job = subdirs-cleanall local-cleanall
    else
      cleanall_job = local-cleanall
    endif
    test_job = $(target_job) local-test
    ifdef SRCS
      depend_job = local-depend tags
      tags_job = local-tags
    endif
    ifdef SUBDIRS
      rebuild_job = subdirs-clean local-clean subdirs $(OBJDIR) $(OBJ_TARGET)
    else
      rebuild_job = local-clean $(OBJDIR) $(OBJ_TARGET)
    endif
    ifeq "$(target_is_lib)" "true"
      install_job = $(target_job)
      ifdef HEADER_DIR
        header_dir = $(top_inc)/$(HEADER_DIR)
      else
        header_dir = $(top_inc)
      endif
      ifneq "$(strip $(HEADERS))" "none"
        target_job += local-install-headers
        install_job = $(target_job)
				install_headers = $(addprefix $(header_dir)/, $(HEADERS))
      endif
      run_job = local-run-error
      debug_job = local-debug-error
    else
      ifeq "$(target_exe)" "true"
        OBJ_EXE = $(OBJ_TARGET)
      endif
      install_job = local-install
      run_job = local-run
      debug_job = local-debug
    endif
  else
    target_job = $(DUMP) error-notarget
  endif
endif

ifeq "$(strip $(FINAL))" "importall"
  ifndef SUBDIRS
    SUBDIRS = $(filter-out old backup bak new xx _%, $(subst /,,$(shell echo */)))
  endif
  ifeq "$(strip $(SUBDIRS))" ""
    $(error FINAL is importall, so you must define sub partial directories)
  endif
  ifdef TARGET
    ifeq "$(strip $(target_exe))" "true"
    SUB_OBJS  = $(addsuffix /$(OBJDIR)/$(EXPORT_NAME), $(SUBDIRS))
    endif
    ifeq "$(strip $(target_mod))" "true"
    SUB_LOBJS = $(addsuffix /$(OBJDIR)/$(EXPORT_NAME).a, $(SUBDIRS))
    endif
    ifeq "$(strip $(target_a))" "true"
    SUB_LOBJS = $(addsuffix /$(OBJDIR)/$(EXPORT_NAME).a, $(SUBDIRS))
    endif
    ifeq "$(strip $(target_so))" "true"
    SUB_SLOBJS= $(addsuffix /$(OBJDIR)/$(EXPORT_NAME).so, $(SUBDIRS))
    endif
    OBJ_TARGET = $(addprefix $(OBJDIR)/, $(TARGET))
    target_job = $(DUMP) $(OBJDIR) subdirs $(DEPEND_FILE) $(OBJ_TARGET)
    clean_job = subdirs-clean local-clean
    tclean_job = subdirs-tclean local-tclean
    clear_job = subdirs-clear local-clear
    cleanall_job = subdirs-cleanall local-cleanall
    test_job = $(target_job) local-test
    ifdef SRCS
      depend_job = local-depend tags
      tags_job = local-tags
    endif
    rebuild_job = local-clean $(OBJDIR) $(OBJ_TARGET)
    ifeq "$(target_is_lib)" "true"
		  install_libs = $(addprefix $(top_lib)/, $(TARGET))
      target_job += local-install-libs
      install_job = $(target_job)
      ifdef HEADER_DIR
        header_dir = $(top_inc)/$(HEADER_DIR)
      else
        header_dir = $(top_inc)
      endif
      ifneq "$(strip $(HEADERS))" "none"
        target_job += local-install-headers
        install_job = $(target_job)
				install_headers = $(addprefix $(header_dir)/, $(HEADERS))
      endif
      run_job = local-run-error
      debug_job = local-debug-error
    else
      ifeq "$(target_exe)" "true"
        OBJ_EXE = $(OBJ_TARGET)
	      install_job = local-install
      endif
			ifeq "$(target_mod)" "true"
	      install_job = local-install-mod
        install_module = $(addprefix $(install_dir)/, $(TARGET))
			endif
      run_job = local-run
      debug_job = local-debug
    endif
  else
    target_job = $(DUMP) error-notarget
  endif
endif

ifeq "$(strip $(FINAL))" "testing"
  SUBDIRS =
  ifdef TARGET
    OBJ_TARGET = $(addprefix $(OBJDIR)/, $(TARGET))
    target_job = $(DUMP) $(OBJDIR) $(DEPEND_FILE) $(OBJ_TARGET) test-exec
    clean_job = local-clean
    tclean_job = local-tclean
    clear_job = local-clear
    cleanall_job = local-cleanall
    test_job = $(DUMP) $(OBJDIR) $(DEPEND_FILE) $(OBJ_TARGET) test-exec
    ifdef SRCS
      depend_job = local-depend tags
      tags_job = local-tags
    endif
    rebuild_job = local-clean $(OBJDIR) $(OBJ_TARGET)
    OBJ_EXE = $(OBJ_TARGET)
    ifdef HEADERS
      HEADERS=
    endif
    run_job = local-run-error
    debug_job = local-debug
  else
    target_job = $(DUMP) error-notarget
    run_job = local-run-error
    debug_job = local-debug-error
  endif
  ifdef TEST_RESULT
    REDIRECTION=true
    ifeq "$(strip $(TEST_RESULT))" ""
      RESULT_FILE=test-all.log
    else
      RESULT_FILE=$(TEST_RESULT)
    endif
  else
    REDIRECTION =
  endif
endif

ifeq "$(findstring $(FINAL), true testing importall part)" ""
  ifndef SUBDIRS
    SUBDIRS = $(filter-out old backup bak new xx _%, $(subst /,,$(shell echo */)))
  endif
  ifeq "$(strip $(SUBDIRS))" ""
    target_job = error-nosubdir
  else
    target_job = $(DUMP) $(EXT_PRE) subdirs $(EXT_POST)
    clean_job = local-clean subdirs-clean
    tclean_job = local-tclean subdirs-tclean
    clear_job = local-clear subdirs-clear
    cleanall_job = local-cleanall subdirs-cleanall
    rebuild_job = subdirs-clean subdirs
    install_job = subdirs-install
    run_job = local-run
    debug_job = local-debug
    depend_job = subdirs-depend
    tags_job = subdirs-tags
    test_job = subdirs-test
    test_clean_job = subdirs-test-clean
  endif
endif

SUBDIR_MAKES = $(addsuffix /Makefile, $(SUBDIRS))

.PHONY: all clean clear cleanall rebuild install\
        depend tags test run debug dbg\
        dump\
        \
        subdirs subdirs-clean subdirs-cleanall subdirs-clear subdirs-tclean\
        subdirs-install subdirs-depend subdirs-tags subdirs-test\
        local-run-error local-debug-error\
        local-tags TAGS local-test local-test-clean test-exec\
        local-run local-debug\
        local-clean local-clear local-cleanall local-tclean\
        local-install local-install-libs local-install-headers\
        local-localhost local-localhost-libs\
        local-depend\
        error-notarget error-nosubdir \
        $(SUBDIRS)

.SUFFIXES: .S .s .o .slo .lo .cpp .c .h .cc .hh

.DEFAULT: all

## ====================================================================
## COMMON BUILD TARGETS
## ====================================================================

all: chkenv $(target_job) $(local_target_job)

clean: $(clean_job)

tclean: $(tclean_job)

clear: $(clear_job)

cleanall: $(cleanall_job)

rebuild: chkenv $(rebuild_job)

install: chkenv $(local_target_job) $(install_job) $(local_install_job)

depend: chkenv $(depend_job)

tags: $(tags_job)

test: chkenv $(test_job)

run: chkenv $(run_job)

debug: chkenv $(debug_job)

dbg: chkenv $(debug_job)

## ====================================================================
## INTERNAL BUILD TARGETS
## ====================================================================

$(OBJDIR):
	@test -d $@ || mkdir $@

# make d for depend output
$(OBJDIR)/%.d : %.S
	@$(CC) -M -w $(cflags) $^ \
		-MT $(patsubst %.d,%.o,$@) \
		-MT $(patsubst %.d,%.lo,$@) \
		-MT $(patsubst %.d,%.slo,$@) \
		-MF $@
	@echo $^" "

$(OBJDIR)/%.d : %.c
	@$(CC) -M -w $(cflags) $^ \
		-MT $(patsubst %.d,%.o,$@) \
		-MT $(patsubst %.d,%.lo,$@) \
		-MT $(patsubst %.d,%.slo,$@) \
		-MF $@
	@echo $^" "

$(OBJDIR)/%.d : %.cc
	@$(CC) -M -w $(cflags) $^ \
		-MT $(patsubst %.d,%.o,$@) \
		-MT $(patsubst %.d,%.lo,$@) \
		-MT $(patsubst %.d,%.slo,$@) \
		-MF $@
	@echo $^" "

$(OBJDIR)/%.d : %.C
	@$(CC) -M -w $(cflags) $^ \
		-MT $(patsubst %.d,%.o,$@) \
		-MT $(patsubst %.d,%.lo,$@) \
		-MT $(patsubst %.d,%.slo,$@) \
		-MF $@
	@echo $^" "

$(OBJDIR)/%.d : %.cpp
	@$(CC) -M -w $(cflags) $^ \
		-MT $(patsubst %.d,%.o,$@) \
		-MT $(patsubst %.d,%.lo,$@) \
		-MT $(patsubst %.d,%.slo,$@) \
		-MF $@
	@echo $^" "

# make o for binary output
$(OBJDIR)/%.s : %.S
	$(CC) -E $(cflags) $< | grep -v '^#' > $@

$(OBJDIR)/%.o : $(OBJDIR)/%.s
	$(CC) $(cflags) -c $< -o $@

$(OBJDIR)/%.o : %.c
	$(CC) $(cflags) -c $< -o $@

$(OBJDIR)/%.o : %.cc
	$(CPP) $(cppflags) -c $< -o $@

$(OBJDIR)/%.o : %.C
	$(CPP) $(cppflags) -c $< -o $@

$(OBJDIR)/%.o : %.cpp
	$(CPP) $(cppflags) -c $< -o $@

# make lo for static library
$(OBJDIR)/%.lo : $(OBJDIR)/%.s
	$(CC) $(cflags) -c $< -o $@

$(OBJDIR)/%.lo : %.c
	$(CC) $(cflags) -c $< -o $@

$(OBJDIR)/%.lo : %.cc
	$(CPP) $(cppflags) -c $< -o $@

$(OBJDIR)/%.lo : %.C
	$(CPP) $(cppflags) -c $< -o $@

$(OBJDIR)/%.lo : %.cpp
	$(CPP) $(cppflags) -c $< -o $@

# make o for binary output
$(OBJDIR)/%.slo : $(OBJDIR)/%.s
	$(CC) $(cflags_shared) $(cflags) -c $< -o $@

$(OBJDIR)/%.slo : %.c
	$(CC) $(cflags_shared) $(cflags) -c $< -o $@

$(OBJDIR)/%.slo : %.cc
	$(CPP) $(cppflags_shared) $(cppflags) -c $< -o $@

$(OBJDIR)/%.slo : %.C
	$(CPP) $(cppflags_shared) $(cppflags) -c $< -o $@

$(OBJDIR)/%.slo : %.cpp
	$(CPP) $(cppflags_shared) $(cppflags) -c $< -o $@

## FINAL OUTPUT
ifdef SUBDIRS
$(OBJ_EXE): $(OBJ_OBJS) $(SUB_OBJS)
	@-rm -f $(IMPORT_NAME).exe
	@for ii in $(SUBDIRS) ; do \
		cat $$ii/$(OBJDIR)/$(EXPORT_NAME) | sed "s/^/$$ii\//" >> $(IMPORT_NAME).exe;\
	done
	$(CC) -o $(LDFLAGS) $@ $(filter %.o, $^) `cat $(IMPORT_NAME)` $(ldflags) 
else
$(OBJ_EXE): $(OBJ_OBJS)
	$(CC) $(LDFLAGS) -o $@ $^ $(ldflags)
endif

ifdef SUBDIRS
$(OBJDIR)/%.so: $(OBJ_SLOBJS) $(SUB_SLOBJS)
	@echo $?
	@echo $(SUB_SLOBJS)
	@-rm -f $(IMPORT_NAME).so
	for ii in $(SUBDIRS) ; do \
		cat $$ii/$(OBJDIR)/$(EXPORT_NAME).so | sed "s/^/$$ii\//" >> $(IMPORT_NAME).so;\
	done
	$(CPP) $(ldflags_shared) $(LDFLAGS) -o $@ $(filter %.slo, $^) `cat $(IMPORT_NAME).so` $(ldflags) 
	@-rm -f $(IMPORT_NAME)
else
$(OBJDIR)/%.so: $(OBJ_SLOBJS)
	$(CC) $(ldflags_shared) $(LDFLAGS) -o $@ $^ $(ldflags)
endif

ifdef SUBDIRS
$(OBJDIR)/%.a: $(OBJ_LOBJS) $(SUB_LOBJS)
	@-rm -f $(IMPORT_NAME).a
	for ii in $(SUBDIRS) ; do \
		cat $$ii/$(OBJDIR)/$(EXPORT_NAME).a | sed "s/^/$$ii\//" >> $(IMPORT_NAME).a;\
	done
	$(AR) r $@ $(filter %.lo, $^)  `cat $(IMPORT_NAME).a`
	$(AR) s $@
else
$(OBJDIR)/%.a: $(OBJ_LOBJS)
	$(AR) r $@ $^
	$(AR) s $@
endif

ifdef SUBDIRS
$(OBJDIR)/%.o: $(OBJ_LOBJS) $(SUB_LOBJS)
	@-rm -f $(IMPORT_NAME).a
	for ii in $(SUBDIRS) ; do \
		cat $$ii/$(OBJDIR)/$(EXPORT_NAME).a | sed "s/^/$$ii\//" >> $(IMPORT_NAME).a;\
	done
	ld $(ldflags_dbg) $(LDFLAGS) -o $@ $(filter %.lo, $^)  `cat $(IMPORT_NAME).a` $(ldflags)
else
$(OBJDIR)/%.o: $(OBJ_LOBJS)
	ld $(ldflags_dbg) $(LDFLAGS) -o $@  $^ $(ldflags)
endif

## ====================================================================
## COMMON PHONY TARGETS
## ====================================================================

menuconfig: chkenv scripts/mconf
	@./scripts/mconf $(CONFIG_CONFIG_IN)

scripts/mconf: 
	$(MAKE) -C scripts ncurses conf mconf

subdirs: $(SUBDIRS) $(SUBDIR_MAKES)
	@for d in $(SUBDIRS);\
	do\
		$(MAKE) -C $$d; \
	done;

subdirs-tclean: $(SUBDIRS) $(SUBDIR_MAKES)
	@for d in $(SUBDIRS);\
	do\
		$(MAKE) -C $$d tclean; \
	done;

subdirs-clean: $(SUBDIRS) $(SUBDIR_MAKES)
	@for d in $(SUBDIRS);\
	do\
		$(MAKE) -C $$d clean; \
	done;

subdirs-cleanall: $(SUBDIRS) $(SUBDIR_MAKES)
	@for d in $(SUBDIRS);\
	do\
		$(MAKE) -C $$d cleanall; \
	done;

subdirs-clear: $(SUBDIRS) $(SUBDIR_MAKES)
	@for d in $(SUBDIRS);\
	do\
		$(MAKE) -C $$d clear; \
	done;

subdirs-install: $(SUBDIRS) $(SUBDIR_MAKES)
	@for d in $(SUBDIRS);\
	do\
		$(MAKE) -C $$d install; \
	done;

subdirs-depend: $(SUBDIRS) $(SUBDIR_MAKES)
	@for d in $(SUBDIRS);\
	do\
		$(MAKE) -C $$d depend; \
	done;

subdirs-tags: $(SUBDIRS) $(SUBDIR_MAKES)
	@for d in $(SUBDIRS);\
	do\
		$(MAKE) -C $$d tags; \
	done;

subdirs-test: $(SUBDIRS) $(SUBDIR_MAKES)
	@for d in $(SUBDIRS);\
	do\
		$(MAKE) -C $$d test; \
	done;

local-run:
	LD_LIBRARY_PATH=$(top_lib):$(common_lib) $(OBJ_TARGET) $(RUN_ARGS)


local-run-error:
	@echo "no files to run"

local-debug:
	SHELL=/bin/sh LD_LIBRARY_PATH=$(top_lib):$(common_lib) gdb --args $(OBJ_TARGET) $(DEBUG_ARGS)

local-debug-error:
	@echo "no files to debug"

TAGS:
	$(CTAGS) $(CTAGS_FLAGS) -f $(CTAGS_FILE) $(SRCS) $(HEADERS) *.h
	$(CSCOPE) -b -f$(CSCOPE_FILE) \
		$(shell echo "" | g++ -v -E -xc++ -  2>&1| grep usr | grep include | grep -v onfigure | grep -v ignor| sed -e "s/^/-I/") \
		$(includes) \
		-I$(common_inc) \
		$(SRCS)

local-tags:$(CSCOPE_FILE) $(CTAGS_FILE)

$(CTAGS_FILE):$(SRCS)
	$(CTAGS) $(CTAGS_FLAGS) -f $@ $(SRCS) $(HEADERS) *.h

$(CSCOPE_FILE):$(SRCS)
	$(CSCOPE) -b -f$@ \
		$(shell echo "" | g++ -v -E -xc++ -  2>&1| grep usr | grep include | grep -v onfigure | grep -v ignor| sed -e "s/^/-I/") \
		$(includes) \
		-I$(common_inc) \
		$(SRCS)

local-test: tests tests/Makefile
	@if [ -d tests ]; then\
		$(MAKE) -C tests;\
	fi

local-test-clean:
	@if [ -d tests ]; then\
		$(MAKE) -C tests cleanall;\
	fi

local-tclean:
	rm -f $(OBJ_TARGET) $(EXP_TARGET)

local-clean:
	rm -f *.o svn-*.tmp  *~ a.out core $(OBJS) $(OBJ_OBJS) $(LOBJS) $(OBJ_LOBJS) $(SLOBJS) $(OBJ_SLOBJS) $(OBJ_TARGET) $(CSCOPE_FILE) $(STUB_H) $(STUB_S) $(EXP_TARGET)

local-clear:
	rm -f *.o svn-*.tmp *~ Makefile2 $(DEPS) $(DEPEND_FILE) $(DEPEND_BAK) $(CSCOPE_FILE) $(STUB_H) $(STUB_S)

local-cleanall:
	rm -rf *.o svn-*.tmp  *~ a.out core $(OBJS) $(OBJ_OBJS) $(LOBJS) $(OBJ_LOBJS) $(SLOBJS) $(OBJ_SLOBJS) $(OBJ_TARGET) $(DEPS) $(DEPEND_FILE) $(DEPEND_BAK) $(CSCOPE_FILE) $(CTAGS_FILE) .rel .obj .libs $(STUB_H) $(STUB_S) $(EXP_TARGET) 
 
$(install_dir):
	test -d $@ || mkdir -p $@

local-install: $(target_job) $(install_dir)
	@echo "Installing executable file $(TARGET) to $(install_dir) ..."
ifeq "$(strip $(BUILD_TARGET))" "__DEBUG"
	@$(installer) $(install_flags) $(OBJ_TARGET) $(install_dir)
else
	@$(installer) --strip $(install_flags) $(OBJ_TARGET) $(install_dir)
endif
	@echo " Done\n"

local-install-mod: $(target_job) $(install_dir) $(install_module)

$(install_dir)/%.o:$(OBJDIR)/%.o
	@echo "Installing kernel module $(TARGET) to $(install_dir) ..."
ifeq "$(strip $(BUILD_TARGET))" "__DEBUG"
	@$(installer) $(install_flags) $(OBJ_TARGET) $(install_dir)
else
	@$(installer) $(install_flags) $(OBJ_TARGET) $(install_dir)
endif
	@echo " Done\n"

$(top_lib)/%.a:$(OBJDIR)/%.a
	@echo "Installing $< to $(top_lib) ..."
ifeq "$(strip $(BUILD_TARGET))" "__DEBUG"
	@$(INSTALL) --mode=0755 $< $(top_lib)
else
	@$(INSTALL) --strip --mode=0755 $< $(top_lib)
endif
	@echo " Done\n"

$(top_lib)/%.so:$(OBJDIR)/%.so
	@echo "Installing $< to $(top_lib) ..."
ifeq "$(strip $(BUILD_TARGET))" "__DEBUG"
	@$(INSTALL) --mode=0755 $< $(top_lib)
else
	@$(INSTALL) --strip --mode=0755 $< $(top_lib)
endif
	@echo " Done\n"

local-install-libs: $(OBJ_TARGET) $(install_libs)

$(header_dir):
	@test -d $@ || mkdir -p $@

$(header_dir)/%.h:%.h
	@echo "Installing $< to $(header_dir) ..."
	@if [ "$(header_dir)" != "$(top_inc)" -a -f $(top_inc)/$^ ]; then \
		rm -f $(top_inc)/$^ ; fi
	@$(INSTALL) -p --mode=0644 $< $(header_dir)
	@echo " Done\n"

$(header_dir)/%.hh:%.hh
	@echo "Installing $< to $(header_dir) ..."
	@$(INSTALL) -p --mode=0644 $< $(header_dir)
	@echo "Done\n"

$(header_dir)/%.hpp:%.hpp
	@echo "Installing $< to $(header_dir) ..."
	@$(INSTALL) -p --mode=0644 $< $(header_dir)
	@echo " Done\n"

local-install-headers: $(header_dir) $(install_headers)

local-depend:$(DEPEND_FILE)

$(DEPEND_FILE):$(DEPS)
	@if [ "$^" != "" ]; then \
		echo ".. dependency updated.\n"; \
		cat $^ > $@; \
	fi;

error-notarget:
	@echo "No target defined. Check if there is any sub directories."

error-nosubdir:
	@echo "There is no sub directories to make. Please define targets to make."

#
# ================= IMPORT and EXPORT ==================
#
$(OBJDIR)/$(EXPORT_NAME): $(OBJ_OBJS) $(SUB_OBJS)
	@-rm -f $@
	@for ii in $(SUBDIRS) ; do \
		echo $$ii/$(OBJDIR)/$(EXPORT_NAME) >> $@; \
	done
	@for ii in $(filter %.o,$^) ; do \
		echo $(EXP_PREFIX)/$$ii >> $@; \
	done
	@echo "export local object files to $@"


$(OBJDIR)/$(EXPORT_NAME).so : $(OBJ_SLOBJS) $(SUB_SLOBJS)
	@-rm -f $@
	@for ii in $(SUBDIRS) ; do \
		cat $$ii/$(OBJDIR)/$(EXPORT_NAME).so | sed "s/^/$$ii\//" >> $@; \
	done
	@for ii in $(filter %.slo, $^) ; do \
		echo $$ii >> $@; \
	done
	@echo "export local object files to $@"


$(OBJDIR)/$(EXPORT_NAME).a : $(OBJ_LOBJS) $(SUB_LOBJS)
	@-rm -f $@
	@for ii in $(SUBDIRS) ; do \
		cat $$ii/$(OBJDIR)/$(EXPORT_NAME).a | sed "s/^/$$ii\//" >> $@; \
	done
	@for ii in $(filter %.lo, $^) ; do \
		echo $$ii >> $@; \
	done
	@echo "export local object files to $@"

# ================= TESTING ==================

$(top_result):
	@test -d $@ || mkdir $@

test-exec: $(OBJ_TARGET) $(top_result)
	-@rm -f local-test.log
	@if test "$(REDIRECTION)" == "true"; then \
		LD_LIBRARY_PATH=$(top_lib):$(common_lib) $(OBJDIR)/$(TARGET) $(TEST_OPTS) >> test-local.log; \
		cp test-local.log $(top_result)/$(TARGET).log;\
		echo "" >> $(top_result)/$(RESULT_FILE);\
		echo "--- start of result $(PWD)" >> $(top_result)/$(RESULT_FILE);\
		cat test-local.log >> $(top_result)/$(RESULT_FILE);\
		echo "--- end of result $(PWD)" >> $(top_result)/$(RESULT_FILE);\
	else \
		LD_LIBRARY_PATH=$(top_lib):$(common_lib) $(OBJDIR)/$(TARGET) $(TEST_OPTS); \
	fi;

dump:
	@echo "--------------------------------------------------------------------"
	@echo "DUMP VARIABLES for $(CURDIR)"
	@echo "--------------------------------------------------------------------"
	@echo "FINAL             =" $(FINAL)
	@echo "DEFINES           =" $(DEFINES)
	@echo "INCLUDES          =" $(INCLUDES)
	@echo "LIBDIRS           =" $(LIBDIRS)
	@echo "LIBS              =" $(LIBS)
	@echo "CFLAGS            =" $(CFLAGS)
	@echo "CPPFLAGS          =" $(CPPFLAGS)
	@echo "LDFLAGS           =" $(LDFLAGS)
	@echo "THREAD_SAFE       =" $(THREAD_SAFE)
	@echo "SRCS              =" $(SRCS)
	@echo "HEADERS           =" $(HEADERS)
	@echo "OBJS              =" $(OBJS)
	@echo "OBJ_OBJS          =" $(OBJ_OBJS)
	@echo "LOBJS             =" $(LOBJS)
	@echo "OBJ_LOBJS         =" $(OBJ_LOBJS)
	@echo "SLOBJS            =" $(SLOBJS)
	@echo "OBJ_SLOBJS        =" $(OBJ_SLOBJS)
	@echo "TARGET            =" $(TARGET)
	@echo "OBJ_TARGET        =" $(OBJ_TARGET)
	@echo "OBJ_EXE           =" $(OBJ_EXE)
	@echo "cflags            =" $(cflags)
	@echo "cppflags          =" $(cppflags)
	@echo "cppflags_shared   =" $(cppflags_shared)
	@echo "ldflags           =" $(ldflags)
	@echo "ldflags_shared    =" $(ldflags_shared)
	@echo "SUBDIRS           =" $(SUBDIRS)
	@echo "target_job        =" $(target_job)
	@echo "clean_job         =" $(clean_job)
	@echo "clear_job         =" $(clear_job)
	@echo "cleanall_job      =" $(cleanall_job)
	@echo "install_job       =" $(install_job)
	@echo "test_job          =" $(test_job)
	@echo "test_clean_job    =" $(test_clean_job)
	@echo "run_job           =" $(run_job)
	@echo "debug_job         =" $(test_debug_job)
	@echo "--------------------------------------------------------------------"

ifneq "$(wildcard $(DEPEND_FILE))" ""
include $(DEPEND_FILE)
endif

