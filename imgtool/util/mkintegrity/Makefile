.SUFFIXES : .c .o


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

OBJECTS = mkintegrity.o sha2.o
TARGET = mkintegrity
CFLAGS = -g 

.PHONY: all install clean 


$(TARGET) : $(OBJECTS)
	gcc -o $(TARGET) $(OBJECTS)

mkintegrity.o : mkintegrity.c sha2.h

sha2.o : sha2.h sha2.c

all : $(TARGET)

install : 
	cp $(TARGET) $(TESSWORKDIR)/imgtool/

clean :
	rm $(OBJECTS) $(TARGET)

