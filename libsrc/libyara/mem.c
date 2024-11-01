
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#include <yara_error.h>

int yr_heap_alloc()
{
	return ERROR_SUCCESS;
}


int yr_heap_free()
{
	return ERROR_SUCCESS;
}


void* yr_calloc(size_t count, size_t size)
{
	return calloc(count, size);
}


void* yr_malloc(size_t size)
{
	return malloc(size);
}


void* yr_realloc(void* ptr, size_t size)
{
	return realloc(ptr, size);
}


void yr_free(void *ptr)
{
	free(ptr);
}


char* yr_strdup(const char *str)
{
	return strdup(str);
}


char* yr_strndup(const char *str, size_t n)
{
	return strndup(str, n); 
}
