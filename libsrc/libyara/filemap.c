
#include <fcntl.h>

#include <sys/stat.h>
#include <sys/mman.h>
#include <unistd.h>

#include <yara_filemap.h>
#include <yara_error.h>



/* yr_filemap_map
 파일 모두를 메모리에 맵핑시킨다.
 Maps a whole file into memory.

 Args:
    const char* file_path        - Path of the file to map.
    YR_MAPPED_FILE* pmapped_file - Pointer to a YR_MAPPED_FILE that will be
                                   filled with information about the mapping.
 Returns:
    One of the following error codes:
       ERROR_SUCCESS
       ERROR_INVALID_ARGUMENT
       ERROR_COULD_NOT_OPEN_FILE
       ERROR_COULD_NOT_MAP_FILE
*/
YR_API int yr_filemap_map( const char* file_path, YR_MAPPED_FILE* pmapped_file)
{
	return yr_filemap_map_ex(file_path, 0, 0, pmapped_file);
}

/* yr_filemap_map_ex
 한 파일의 한부분을 메모리에 맵핑시킨다.
 Maps a portion of a file into memory.

 Args:
    const char* file_path        - Path of the file to map.
    off_t offset                 - File offset where the mapping will begin.
                                   This offset must be multiple of 1MB and not
                                   greater than the actual file size.
    size_t size                  - Number of bytes that will be mapped. If
                                   zero or greater than the actual file size
                                   all content until the end of the file will
                                   be mapped.
    YR_MAPPED_FILE* pmapped_file - Pointer to a YR_MAPPED_FILE struct that
                                   will be filled with the new mapping.
 Returns:
    One of the following error codes:
       ERROR_SUCCESS
       ERROR_INVALID_ARGUMENT
       ERROR_COULD_NOT_OPEN_FILE
       ERROR_COULD_NOT_MAP_FILE

 POSIX
*/
YR_API int yr_filemap_map_ex( const char* file_path, off_t offset, size_t size, YR_MAPPED_FILE* pmapped_file)
{
	struct stat fstat;

	pmapped_file->data = NULL;
	pmapped_file->size = 0;
	pmapped_file->file = -1;

	if (file_path == NULL)
		return ERROR_INVALID_ARGUMENT;

	// Ensure that offset is aligned to 1MB
	if (offset >> 20 << 20 != offset)
		return ERROR_INVALID_ARGUMENT;

	if (stat(file_path, &fstat) != 0 || S_ISDIR(fstat.st_mode))
		return ERROR_COULD_NOT_OPEN_FILE;

	if (offset > fstat.st_size)
		return ERROR_COULD_NOT_MAP_FILE;

	if (size == 0)
		size = fstat.st_size - offset;

	pmapped_file->file = open(file_path, O_RDONLY);

	if (pmapped_file->file == -1)
		return ERROR_COULD_NOT_OPEN_FILE;

	pmapped_file->size = min(size, fstat.st_size - offset);

	if (pmapped_file->size != 0)
	{
		pmapped_file->data = (uint8_t*) mmap(
				0,
				pmapped_file->size,
				PROT_READ,
				MAP_PRIVATE,
				pmapped_file->file,
				offset);

		if (pmapped_file->data == MAP_FAILED)
		{
			close(pmapped_file->file);

			pmapped_file->data = NULL;
			pmapped_file->size = 0;
			pmapped_file->file = -1;

			return ERROR_COULD_NOT_MAP_FILE;
		}
	}
	else
	{
		pmapped_file->data = NULL;
	}

	return ERROR_SUCCESS;
}


/* yr_filemap_unmap
 맵핑된 파일을 푼다.
 Unmaps a file mapping.

 Args:
    YR_MAPPED_FILE* pmapped_file - Pointer to a YR_MAPPED_FILE that struct.

	POSIX
 */
YR_API void yr_filemap_unmap( YR_MAPPED_FILE* pmapped_file)
{
	if (pmapped_file->data != NULL)
		munmap(pmapped_file->data, pmapped_file->size);

	if (pmapped_file->file != -1)
		close(pmapped_file->file);

	pmapped_file->file = -1;
	pmapped_file->data = NULL;
	pmapped_file->size = 0;
}

