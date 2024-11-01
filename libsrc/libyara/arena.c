#include <string.h>
#include <assert.h>
#include <stdlib.h>
#include <stdarg.h>
#include <stddef.h>
#include <time.h>


#include <yara_arena.h>
#include <yara_mem.h>
#include <yara_error.h>
#include <yara_limits.h>

#pragma pack(push)
#pragma pack(1)

typedef struct _ARENA_FILE_HEADER
{
  char      magic[4];
  uint32_t  size;
  uint8_t   version;

} ARENA_FILE_HEADER;

#pragma pack(pop)


#define free_space(page) \
    ((page)->size - (page)->used)


/* _yr_arena_new_page
// Creates a new arena page of a given size
주어진 크기의 새로운 아레나 페이지를 만든다?  */
YR_ARENA_PAGE* _yr_arena_new_page( size_t size)
{
	YR_ARENA_PAGE* new_page;

	new_page = (YR_ARENA_PAGE*) yr_malloc(sizeof(YR_ARENA_PAGE));

	if (new_page == NULL)
		return NULL;

	new_page->address = (uint8_t*) yr_malloc(size);

	if (new_page->address == NULL)
	{
		yr_free(new_page);
		return NULL;
	}

	new_page->size = size;
	new_page->used = 0;
	new_page->next = NULL;
	new_page->prev = NULL;
	new_page->reloc_list_head = NULL;
	new_page->reloc_list_tail = NULL;

	return new_page;
}


/* _yr_arena_page_for_address
 address에 있는 곳에 위치한 그 아레나 안의 페이지를 리턴한다?
 Returns the page within he arena where an address reside.

 Args:
    YR_ARENA* arena   - Pointer to the arena
    void* address  - Address to be located

 Returns:
    A pointer the corresponding YR_ARENA_PAGE structure where the address
    resides.
*/
YR_ARENA_PAGE* _yr_arena_page_for_address( YR_ARENA* arena, void* address)
{
	YR_ARENA_PAGE* page;

	// Most of the times this function is called with an address within
	// the current page, let's check the current page first to avoid
	// looping through the page list.

	page = arena->current_page;

	if (page != NULL &&
			(uint8_t*) address >= page->address &&
			(uint8_t*) address < page->address + page->used)
		return page;

	page = arena->page_list_head;

	while (page != NULL)
	{
		if ((uint8_t*) address >= page->address &&
				(uint8_t*) address < page->address + page->used)
			return page;
		page = page->next;
	}

	return NULL;
}


/* _yr_arena_make_relocatable
 재할당할수 있는 포인터를 가진 확신한 주소의 아레나를 말해라?
 Tells the arena that certain addresses contains a relocatable pointer.

 Args:
    YR_ARENA* arena    - Pointer the arena
    void* address      - Base address
    va_list offsets    - List of offsets relative to base address
*/
int _yr_arena_make_relocatable( YR_ARENA* arena, void* base, va_list offsets)
{
	YR_RELOC* reloc;
	YR_ARENA_PAGE* page;

	size_t offset;
	size_t base_offset;

	int result = ERROR_SUCCESS;

	page = _yr_arena_page_for_address(arena, base);

	assert(page != NULL);

	base_offset = (uint8_t*) base - page->address;
	offset = va_arg(offsets, size_t);

	while (offset != -1)
	{
		assert(base_offset + offset <= page->used - sizeof(int64_t));

		reloc = (YR_RELOC*) yr_malloc(sizeof(YR_RELOC));

		if (reloc == NULL)
			return ERROR_INSUFICIENT_MEMORY;

		reloc->offset = base_offset + offset;
		reloc->next = NULL;

		if (page->reloc_list_head == NULL)
			page->reloc_list_head = reloc;

		if (page->reloc_list_tail != NULL)
			page->reloc_list_tail->next = reloc;

		page->reloc_list_tail = reloc;
		offset = va_arg(offsets, size_t);
	}

	return result;
}


/* yr_arena_create
   arena를 만든다?  */
int yr_arena_create( size_t initial_size, int flags, YR_ARENA** arena)
{
	YR_ARENA* new_arena;
	YR_ARENA_PAGE* new_page;

	*arena = NULL;
	new_arena = (YR_ARENA*) yr_malloc(sizeof(YR_ARENA));

	if (new_arena == NULL)
		return ERROR_INSUFICIENT_MEMORY;

	new_page = _yr_arena_new_page(initial_size);

	if (new_page == NULL)
	{
		yr_free(new_arena);
		return ERROR_INSUFICIENT_MEMORY;
	}

	new_arena->page_list_head = new_page;
	new_arena->current_page = new_page;
	new_arena->flags = flags | ARENA_FLAGS_COALESCED;

	*arena = new_arena;
	return ERROR_SUCCESS;
}


/* yr_arena_destroy
 arena의 자원을 해재한다. 
 Destroys an arena releasing its resource.

 Args:
    YR_ARENA* arena  - Pointer to the arena.

 Returns:
    ERROR_SUCCESS if succeed or the corresponding error code otherwise.
*/
void yr_arena_destroy( YR_ARENA* arena)
{
	YR_RELOC* reloc;
	YR_RELOC* next_reloc;
	YR_ARENA_PAGE* page;
	YR_ARENA_PAGE* next_page;

	if (arena == NULL)
		return;

	page = arena->page_list_head;

	while(page != NULL)
	{
		next_page = page->next;
		reloc = page->reloc_list_head;

		while (reloc != NULL)
		{
			next_reloc = reloc->next;
			yr_free(reloc);
			reloc = next_reloc;
		}

		yr_free(page->address);
		yr_free(page);

		page = next_page;
	}

	yr_free(arena);
}


/* yr_arena_base_address
 아레나의 base 주소를 반환해준다.
 Returns the base address for the arena.

 Args:
    YR_ARENA* arena  - Pointer to the arena.

 Returns:
    A pointer
*/
void* yr_arena_base_address(
  YR_ARENA* arena)
{
  return arena->page_list_head->address;
}


/* yr_arena_next_address
 주어진 주소와 offset으로 주소+offset에 존재하는 주소를 리턴한다. 
 arena는 인접하지않은 메모리 영역의 모음이다. 만약 페이지의 끝을 가리키는 주소라면
 주소 + offset 은 페이지 경계를 가로지르고 다음 페이지를 포함하는 어딘가를 가리킬 것이다.
 이 함수는 이런 상황은 관리한다.
 이것은 또한 음수의 offset도 동작한다.
 Given an address and an offset, returns the address where
 address + offset resides. The arena is a collection of non-contigous
 regions of memory (pages), if address is pointing at the end of a page,
 address + offset could cross the page boundary and point at somewhere
 within the next page, this function handles these situations. It works
 also with negative offsets.

 Args:
    YR_ARENA* arena  - Pointer to the arena.
    void* address    - Base address.
    int offset       - Offset.

 Returns:
    A pointer
*/
void* yr_arena_next_address( YR_ARENA* arena, void* address, int offset)
{
	YR_ARENA_PAGE* page;

	page = _yr_arena_page_for_address(arena, address);

	assert(page != NULL);

	if ((uint8_t*) address + offset >= page->address &&
			(uint8_t*) address + offset < page->address + page->used)
	{
		return (uint8_t*) address + offset;
	}

	if (offset > 0)
	{
		offset -= page->address + page->used - (uint8_t*) address;
		page = page->next;

		while (page != NULL)
		{
			if (offset < page->used)
				return page->address + offset;

			offset -= page->used;
			page = page->next;
		}
	}
	else
	{
		offset += page->used;
		page = page->prev;

		while (page != NULL)
		{
			if (offset < page->used)
				return page->address + page->used + offset;

			offset += page->used;
			page = page->prev;
		}
	}

	return NULL;
}


//
// yr_arena_coalesce
//
// Coalesce the arena into a single page. This is a required step before
// saving the arena to a file.
//
// Args:
//    YR_ARENA* arena  - Pointer to the arena.
//
// Returns:
//    ERROR_SUCCESS if succeed or the corresponding error code otherwise.
//

int yr_arena_coalesce(
    YR_ARENA* arena)
{
  YR_ARENA_PAGE* page;
  YR_ARENA_PAGE* big_page;
  YR_ARENA_PAGE* next_page;
  YR_RELOC* reloc;

  uint8_t** reloc_address;
  uint8_t* reloc_target;
  int total_size = 0;

  page = arena->page_list_head;

  while(page != NULL)
  {
    total_size += page->used;
    page = page->next;
  }

  // Create a new page that will contain the entire arena.
  big_page = _yr_arena_new_page(total_size);

  if (big_page == NULL)
    return ERROR_INSUFICIENT_MEMORY;

  // Copy data from current pages to the big page and adjust relocs.
  page = arena->page_list_head;

  while (page != NULL)
  {
    page->new_address = big_page->address + big_page->used;
    memcpy(page->new_address, page->address, page->used);

    reloc = page->reloc_list_head;

    while(reloc != NULL)
    {
      reloc->offset += big_page->used;
      reloc = reloc->next;
    }

    if (big_page->reloc_list_head == NULL)
      big_page->reloc_list_head = page->reloc_list_head;

    if (big_page->reloc_list_tail != NULL)
      big_page->reloc_list_tail->next = page->reloc_list_head;

    if (page->reloc_list_tail != NULL)
      big_page->reloc_list_tail = page->reloc_list_tail;

    big_page->used += page->used;
    page = page->next;
  }

  // Relocate pointers.
  reloc = big_page->reloc_list_head;

  while (reloc != NULL)
  {
    reloc_address = (uint8_t**) (big_page->address + reloc->offset);
    reloc_target = *reloc_address;

    if (reloc_target != NULL)
    {
      page = _yr_arena_page_for_address(arena, reloc_target);
      assert(page != NULL);
      *reloc_address = page->new_address + (reloc_target - page->address);
    }

    reloc = reloc->next;
  }

  // Release current pages.
  page = arena->page_list_head;

  while(page != NULL)
  {
    next_page = page->next;
    yr_free(page->address);
    yr_free(page);
    page = next_page;
  }

  arena->page_list_head = big_page;
  arena->current_page = big_page;
  arena->flags |= ARENA_FLAGS_COALESCED;

  return ERROR_SUCCESS;
}


/* yr_arena_reserve_memory

   미래의 할당을 위해 아레나가 충분히 인접한메모리를 가지도록 보장한다.
   만약 현재 사용할 수 있는 공간이 주이진 사이즈보다 작다면 새로운 페이지를 할당한다.
 Ensures that the arena have enough contiguous memory for future allocations.
 if the available space in the current page is lower than "size", a new page
 is allocated.

 Args:
    YR_ARENA* arena         - Pointer to the arena.
    size_t size             - Size of the region to be reserved.
*/
int yr_arena_reserve_memory(
    YR_ARENA* arena,
    size_t size)
{
  YR_ARENA_PAGE* new_page;
  uint8_t* new_page_address;

  if (size > free_space(arena->current_page))
  {
    if (arena->flags & ARENA_FLAGS_FIXED_SIZE)
      return ERROR_INSUFICIENT_MEMORY;

    size_t new_page_size;
    // Requested space is bigger than current page's empty space,
    // lets calculate the size for a new page.

    new_page_size = arena->current_page->size * 2;

    while (new_page_size < size)
      new_page_size *= 2;

    if (arena->current_page->used == 0)
    {
      // Current page is not used at all, it can be reallocated.

      new_page_address = (uint8_t*) yr_realloc(
          arena->current_page->address,
          new_page_size);

      if (new_page_address == NULL)
        return ERROR_INSUFICIENT_MEMORY;

      arena->current_page->address = new_page_address;
      arena->current_page->size = new_page_size;
    }
    else
    {
      new_page = _yr_arena_new_page(new_page_size);

      if (new_page == NULL)
        return ERROR_INSUFICIENT_MEMORY;

      new_page->prev = arena->current_page;
      arena->current_page->next = new_page;
      arena->current_page = new_page;
      arena->flags &= ~ARENA_FLAGS_COALESCED;
    }
  }

  return ERROR_SUCCESS;
}


/* yr_arena_allocate_memory
   arena를 포함하는 메모리를 할당한다.
 Args:
    YR_ARENA* arena         - Pointer to the arena.
    size_t size             - Size of the region to be allocated.
    void** allocated_memory - Address of a pointer to newly allocated
*/
int yr_arena_allocate_memory( YR_ARENA* arena, size_t size, void** allocated_memory)
{
	FAIL_ON_ERROR(yr_arena_reserve_memory(arena, size));

	*allocated_memory = arena->current_page->address + \
						arena->current_page->used;

	arena->current_page->used += size;

	return ERROR_SUCCESS;
}


/* yr_arena_allocate_struct
   arena를 포함한 구조체를 할당한다. 이 함수는 yr_arena_allocate_memory와
   유사하지만 포인터가 존재하는 오프셋의 가변길이 리스트를  추가적으로 받는다.
   이것은 메모리가 재할당될때 조정해줘야한 하는, 아레나 포인터의 위치를 유지시킨다. 
   다음은 이 함수를 적용하는 예이다.
 Allocates a structure within the arena. This function is similar to
 yr_arena_allocate_memory but additionaly receives a variable-length
 list of offsets within the structure where pointers reside. This allows
 the arena to keep track of pointers that must be adjusted when memory
 is relocated. This is an example on how to invoke this function:

  yr_arena_allocate_struct(
        arena,
        sizeof(MY_STRUCTURE),
        (void**) &my_structure_ptr,
        offsetof(MY_STRUCTURE, field_1),
        offsetof(MY_STRUCTURE, field_2),
        ..
        offsetof(MY_STRUCTURE, field_N),
        EOL);

 Args:
    YR_ARENA* arena         - Pointer to the arena.
    size_t size             - Size of the region to be allocated.
    void** allocated_memory - Address of a pointer to newly allocated
                              region.
    ...                     - Variable number of offsets relative to the
                              beginning of the struct. Offsets are of type
                              size_t.
 */
int yr_arena_allocate_struct( YR_ARENA* arena, size_t size, void** allocated_memory, ...)
{
	int result;

	va_list offsets;
	va_start(offsets, allocated_memory);

	result = yr_arena_allocate_memory(arena, size, allocated_memory);

	if (result == ERROR_SUCCESS)
		result = _yr_arena_make_relocatable(arena, *allocated_memory, offsets);

	va_end(offsets);

	memset(*allocated_memory, 0, size);

	return result;
}


//
// yr_arena_make_relocatable
//
// Tells the arena that certain addresses contains a relocatable pointer.
//
// Args:
//    YR_ARENA* arena    - Pointer to the arena.
//    void* base         - Address within the arena.
//    ...                - Variable number of size_t arguments with offsets
//                         relative to base.
//
// Returns:
//    ERROR_SUCCESS if succeed or the corresponding error code otherwise.
//

int yr_arena_make_relocatable(
    YR_ARENA* arena,
    void* base,
    ...)
{
  int result;

  va_list offsets;
  va_start(offsets, base);

  result = _yr_arena_make_relocatable(arena, base, offsets);

  va_end(offsets);

  return result;
}


/* arena에 데이터를 쓴다 */
int yr_arena_write_data( YR_ARENA* arena, void* data, size_t size, void** written_data)
{
	void* output;

	if (size > free_space(arena->current_page))
	{
        	int result;	
		result = yr_arena_allocate_memory(arena, size, &output);

		if (result != ERROR_SUCCESS)
			return result;
	}
	else
	{
		output = arena->current_page->address + arena->current_page->used;
		arena->current_page->used += size;
	}

	memcpy(output, data, size);

	if (written_data != NULL)
		*written_data = output;

	return ERROR_SUCCESS;
}


/* arena에 스트링 문자열을 쓴다.   */
int yr_arena_write_string( YR_ARENA* arena, const char* string, char** written_string)
{
  return yr_arena_write_data( arena, (void*) string, strlen(string) + 1, (void**) written_string);
}


//
// yr_arena_append
//
// Appends source_arena to target_arena. This operation destroys source_arena,
// after returning any pointer to source_arena is no longer valid.
//
// Args:
//    YR_ARENA* target_arena    - Pointer to target the arena.
//    YR_ARENA* source_arena    - Pointer to source arena.
//
// Returns:
//    ERROR_SUCCESS if succeed or the corresponding error code otherwise.
//

int yr_arena_append(
    YR_ARENA* target_arena,
    YR_ARENA* source_arena)
{
  target_arena->current_page->next = source_arena->page_list_head;
  source_arena->page_list_head->prev = target_arena->current_page;
  target_arena->current_page = source_arena->current_page;

  yr_free(source_arena);

  return ERROR_SUCCESS;
}



/* yr_arena_duplicate
   정확한 복사로 만들어진 아레나 사본을 만든다. 
   이함수는 합쳐진 아레나가 필요하다.
 Duplicates the arena, making an exact copy. This function requires the
 arena to be coalesced.

 Args:
    YR_ARENA* arena        - Pointer to the arena.
    YR_ARENA** duplicated  - Address where a pointer to the new arena arena
                             will be returned.

 Returns:
    ERROR_SUCCESS if succeed or the corresponding error code otherwise.
*/
int yr_arena_duplicate( YR_ARENA* arena, YR_ARENA** duplicated)
{
	YR_RELOC* reloc;
	YR_RELOC* new_reloc;
	YR_ARENA_PAGE* page;
	YR_ARENA_PAGE* new_page;
	YR_ARENA* new_arena;
	uint8_t** reloc_address;
	uint8_t* reloc_target;

	// Only coalesced arenas can be duplicated.
	assert(arena->flags & ARENA_FLAGS_COALESCED);

	new_arena = (YR_ARENA*) yr_malloc(sizeof(YR_ARENA));

	if (new_arena == NULL)
		return ERROR_INSUFICIENT_MEMORY;

	page = arena->page_list_head;
	new_page = _yr_arena_new_page(page->size);

	if (new_page == NULL)
	{
		yr_free(new_arena);
		return ERROR_INSUFICIENT_MEMORY;
	}

	memcpy(new_page->address, page->address, page->size);

	new_page->used = page->used;

	reloc = page->reloc_list_head;

	while (reloc != NULL)
	{
		new_reloc = (YR_RELOC*) yr_malloc(sizeof(YR_RELOC));

		if (new_reloc == NULL)
			return ERROR_INSUFICIENT_MEMORY;

		new_reloc->offset = reloc->offset;
		new_reloc->next = NULL;

		if (new_page->reloc_list_head == NULL)
			new_page->reloc_list_head = new_reloc;

		if (new_page->reloc_list_tail != NULL)
			new_page->reloc_list_tail->next = new_reloc;

		new_page->reloc_list_tail = new_reloc;

		reloc_address = (uint8_t**) (new_page->address + new_reloc->offset);
		reloc_target = *reloc_address;

		if (reloc_target != NULL)
		{
			assert(reloc_target >= page->address);
			assert(reloc_target < page->address + page->used);

			*reloc_address = reloc_target - \
							 page->address + \
							 new_page->address;
		}

		reloc = reloc->next;
	}

	new_arena->page_list_head = new_page;
	new_arena->current_page = new_page;
	new_arena->flags |= ARENA_FLAGS_COALESCED;

	*duplicated = new_arena;

	return ERROR_SUCCESS;
}


//
// yr_arena_save
//
// Saves the arena into a file. If the file exists its overwritten. This
// function requires the arena to be coalesced.
//
// Args:
//    YR_ARENA* arena          - Pointer to the arena.
//    const char* filename     - File path.
//
// Returns:
//    ERROR_SUCCESS if succeed or the corresponding error code otherwise.
//

int yr_arena_save(
  YR_ARENA* arena,
  const char* filename)
{
  YR_ARENA_PAGE* page;
  YR_RELOC* reloc;
  FILE* fh;
  ARENA_FILE_HEADER header;

  int32_t end_marker = -1;
  uint8_t** reloc_address;
  uint8_t* reloc_target;

  // Only coalesced arenas can be saved.
  assert(arena->flags & ARENA_FLAGS_COALESCED);

  fh = fopen(filename, "wb");

  if (fh == NULL)
    return ERROR_COULD_NOT_OPEN_FILE;

  page = arena->page_list_head;
  reloc = page->reloc_list_head;

  // Convert pointers to offsets before saving.
  while (reloc != NULL)
  {
    reloc_address = (uint8_t**) (page->address + reloc->offset);
    reloc_target = *reloc_address;

    if (reloc_target != NULL)
    {
      assert(reloc_target >= page->address);
      assert(reloc_target < page->address + page->used);
      *reloc_address = (uint8_t*) (*reloc_address - page->address);
    }
    else
    {
      *reloc_address = (uint8_t*) (size_t) 0xFFFABADA;
    }

    reloc = reloc->next;
  }

  header.magic[0] = 'Y';
  header.magic[1] = 'A';
  header.magic[2] = 'R';
  header.magic[3] = 'A';
  header.size = page->size;
  header.version = ARENA_FILE_VERSION;

  fwrite(&header, sizeof(header), 1, fh);
  fwrite(page->address, sizeof(uint8_t), header.size, fh);

  reloc = page->reloc_list_head;

  // Convert offsets back to pointers.
  while (reloc != NULL)
  {
    fwrite(&reloc->offset, sizeof(reloc->offset), 1, fh);

    reloc_address = (uint8_t**) (page->address + reloc->offset);
    reloc_target = *reloc_address;

    if (reloc_target != (void*) (size_t) 0xFFFABADA)
      *reloc_address += (size_t) page->address;
    else
      *reloc_address = 0;

    reloc = reloc->next;
  }

  fwrite(&end_marker, sizeof(end_marker), 1, fh);
  fclose(fh);

  return ERROR_SUCCESS;
}


//
// yr_arena_load
//
// Loads an arena from a file.
//
// Args:
//    const char* filename  - File path.
//    YR_ARENA**            - Address where a pointer to the loaded arena
//                            will be returned.
//
// Returns:
//    ERROR_SUCCESS if succeed or the corresponding error code otherwise.
//

int yr_arena_load(
    const char* filename,
    YR_ARENA** arena)
{
  FILE* fh;
  YR_ARENA_PAGE* page;
  YR_ARENA* new_arena;
  ARENA_FILE_HEADER header;

  int32_t reloc_offset;
  uint8_t** reloc_address;
  uint8_t* reloc_target;

  long file_size;
  int result;

  fh = fopen(filename, "rb");

  if (fh == NULL)
    return ERROR_COULD_NOT_OPEN_FILE;

  fseek(fh, 0, SEEK_END);
  file_size = ftell(fh);
  fseek(fh, 0, SEEK_SET);

  if (fread(&header, sizeof(header), 1, fh) != 1)
  {
    fclose(fh);
    return ERROR_INVALID_FILE;
  }

  if (header.magic[0] != 'Y' ||
      header.magic[1] != 'A' ||
      header.magic[2] != 'R' ||
      header.magic[3] != 'A')
  {
    fclose(fh);
    return ERROR_INVALID_FILE;
  }

  if (header.size >= file_size)
  {
    fclose(fh);
    return ERROR_CORRUPT_FILE;
  }

  if (header.version > ARENA_FILE_VERSION)
  {
    fclose(fh);
    return ERROR_UNSUPPORTED_FILE_VERSION;
  }

  result = yr_arena_create(header.size, 0, &new_arena);

  if (result != ERROR_SUCCESS)
  {
    fclose(fh);
    return result;
  }

  page = new_arena->current_page;

  if (fread(page->address, header.size, 1, fh) != 1)
  {
    fclose(fh);
    yr_arena_destroy(new_arena);
    return ERROR_CORRUPT_FILE;
  }

  page->used = header.size;

  if (fread(&reloc_offset, sizeof(reloc_offset), 1, fh) != 1)
  {
    fclose(fh);
    yr_arena_destroy(new_arena);
    return ERROR_CORRUPT_FILE;
  }

  while (reloc_offset != -1)
  {
    yr_arena_make_relocatable(new_arena, page->address, reloc_offset, EOL);

    reloc_address = (uint8_t**) (page->address + reloc_offset);
    reloc_target = *reloc_address;

    if (reloc_target != (uint8_t*) (size_t) 0xFFFABADA)
      *reloc_address += (size_t) page->address;
    else
      *reloc_address = 0;

    if (fread(&reloc_offset, sizeof(reloc_offset), 1, fh) != 1)
    {
      fclose(fh);
      yr_arena_destroy(new_arena);
      return ERROR_CORRUPT_FILE;
    }
  }

  fclose(fh);

  *arena = new_arena;

  return ERROR_SUCCESS;
}
