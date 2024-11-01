
#include <string.h>
#include <stdio.h>
#include <ctype.h>

#include <yara_error.h>
#include <yara_re.h>
#include <yara_modules.h>
#include <yara_mem.h>


#include <pthread.h>
pthread_key_t tidx_key;
pthread_key_t recovery_state_key;


char lowercase[256];
char altercase[256];

/* yr_initialize
   메인 thread 에서 다른 libyara 함수를 쓰기 전에 불려질 것이다. */
YR_API int yr_initialize(void)
{
	int i;

	for (i = 0; i < 256; i++)
	{
		if (i >= 'a' && i <= 'z')
			altercase[i] = i - 32;
		else if (i >= 'A' && i <= 'Z')
			altercase[i] = i + 32;
		else
			altercase[i] = i;

		lowercase[i] = tolower(i);
	}

	FAIL_ON_ERROR(yr_heap_alloc());

	pthread_key_create(&tidx_key, NULL);
	pthread_key_create(&recovery_state_key, NULL);

	FAIL_ON_ERROR(yr_re_initialize());
	FAIL_ON_ERROR(yr_modules_initialize());

	return ERROR_SUCCESS;
}


/* yr_finalize_thread
   종료 전에, libyara를 사용하는 모든 쓰레드데 의해 불려질 것이다.*/
YR_API void yr_finalize_thread(void)
{
	yr_re_finalize_thread();
}


/* yr_finalize
   메인 쓰레드가 종료되기 전에 불려질 것이다. 
   메인 쓰레드에서 꼭 yr_fianlize를 부를 필요는 없다.
   왜냐하면 yr_finalize 이전에 불려졌기 때문이다. */
YR_API int yr_finalize(void)
{
	yr_re_finalize_thread();

	pthread_key_delete(tidx_key);
	pthread_key_delete(recovery_state_key);

	FAIL_ON_ERROR(yr_re_finalize());
	FAIL_ON_ERROR(yr_modules_finalize());
	FAIL_ON_ERROR(yr_heap_free());

	return ERROR_SUCCESS;
}

//
// _yr_set_tidx
//
// Set the thread index (tidx) for the current thread. The tidx is the index
// that will be used by the thread to access thread-specific data stored in
// YR_RULES structure.
//
// Args:
//    int tidx   - The zero-based tidx that will be associated to the current
//                 thread.
//

YR_API void yr_set_tidx(int tidx)
{
	pthread_setspecific(tidx_key, (void*) (size_t) (tidx + 1));
}


//
// _yr_get_tidx
//
// Get the thread index (tidx) for the current thread.
//
// Returns:
//    The tidx for the current thread or -1 if the current thread doesn't
//    have any tidx associated.
//

YR_API int yr_get_tidx(void)
{
	return (int) (size_t) pthread_getspecific(tidx_key) - 1;
}
