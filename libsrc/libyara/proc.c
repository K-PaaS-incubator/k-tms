/*
Copyright (c) 2007-2013. The YARA Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

#define _XOPEN_SOURCE 500

#include <fcntl.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/ptrace.h>
#include <sys/wait.h>

#include <yara_error.h>
#include <yara_proc.h>
#include <yara_mem.h>

#if defined(__FreeBSD__) || defined(__FreeBSD_kernel__) || \
    defined(__OpenBSD__) || defined(__MACH__)
#define PTRACE_ATTACH PT_ATTACH
#define PTRACE_DETACH PT_DETACH
#endif

#include <errno.h>

int yr_process_get_memory(
    pid_t pid,
    YR_MEMORY_BLOCK** first_block)
{
  char buffer[256];
  unsigned char* data = NULL;
  size_t begin, end, length;

  YR_MEMORY_BLOCK* new_block;
  YR_MEMORY_BLOCK* current_block = NULL;

  FILE *maps = NULL;

  int mem = -1;
  int result;
  int attached = 0;

  *first_block = NULL;

  snprintf(buffer, sizeof(buffer), "/proc/%u/maps", pid);

  maps = fopen(buffer, "r");

  if (maps == NULL)
  {
    result = ERROR_COULD_NOT_ATTACH_TO_PROCESS;
    goto _exit;
  }

  snprintf(buffer, sizeof(buffer), "/proc/%u/mem", pid);

  mem = open(buffer, O_RDONLY);

  if (mem == -1)
  {
    result = ERROR_COULD_NOT_ATTACH_TO_PROCESS;
    goto _exit;
  }

  if (ptrace(PTRACE_ATTACH, pid, NULL, 0) != -1)
  {
    attached = 1;
  }
  else
  {
    result = ERROR_COULD_NOT_ATTACH_TO_PROCESS;
    goto _exit;
  }

  wait(NULL);

  while (fgets(buffer, sizeof(buffer), maps) != NULL)
  {
    sscanf(buffer, "%zx-%zx", &begin, &end);

    length = end - begin;

    data = (unsigned char *)yr_malloc(length);

    if (data == NULL)
    {
      result = ERROR_INSUFICIENT_MEMORY;
      goto _exit;
    }

    if (pread(mem, data, length, begin) != -1)
    {
      new_block = (YR_MEMORY_BLOCK*) yr_malloc(sizeof(YR_MEMORY_BLOCK));

      if (new_block == NULL)
      {
        result = ERROR_INSUFICIENT_MEMORY;
        goto _exit;
      }

      if (*first_block == NULL)
        *first_block = new_block;

      new_block->base = begin;
      new_block->size = length;
      new_block->data = data;
      new_block->next = NULL;

      if (current_block != NULL)
        current_block->next = new_block;

      current_block = new_block;
    }
    else
    {
      yr_free(data);
      data = NULL;
    }
  }

  result = ERROR_SUCCESS;

_exit:

  if (attached)
    ptrace(PTRACE_DETACH, pid, NULL, 0);

  if (mem != -1)
    close(mem);

  if (maps != NULL)
    fclose(maps);

  if (data != NULL)
    yr_free(data);

  return result;
}
