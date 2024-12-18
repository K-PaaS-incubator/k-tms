/*
Copyright (c) 2013. The YARA Authors. All Rights Reserved.

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

#include <assert.h>
#include <string.h>
#include <time.h>
#include <ctype.h>

#include <yara_ahocorasick.h>
#include <yara_arena.h>
#include <yara_error.h>
#include <yara_exec.h>
#include <yara_exefiles.h>
#include <yara_filemap.h>
#include <yara_hash.h>
#include <yara_mem.h>
#include <yara_proc.h>
#include <yara_re.h>
#include <yara_utils.h>
#include <yara_object.h>
#include <yara_globals.h>
#include <yara_libyara.h>
#include <yara_scan.h>
#include <yara_modules.h>



void _yr_rules_lock(
    YR_RULES* rules)
{
  pthread_mutex_lock(&rules->mutex);
}


void _yr_rules_unlock(
    YR_RULES* rules)
{
  pthread_mutex_unlock(&rules->mutex);
}


YR_API int yr_rules_define_integer_variable(
    YR_RULES* rules,
    const char* identifier,
    int64_t value)
{
  YR_EXTERNAL_VARIABLE* external;

  external = rules->externals_list_head;

  while (!EXTERNAL_VARIABLE_IS_NULL(external))
  {
    if (strcmp(external->identifier, identifier) == 0)
    {
      external->value.i = value;
      break;
    }

    external++;
  }

  return ERROR_SUCCESS;
}


YR_API int yr_rules_define_boolean_variable(
    YR_RULES* rules,
    const char* identifier,
    int value)
{
  YR_EXTERNAL_VARIABLE* external;

  external = rules->externals_list_head;

  while (!EXTERNAL_VARIABLE_IS_NULL(external))
  {
    if (strcmp(external->identifier, identifier) == 0)
    {
      external->value.i = value;
      break;
    }

    external++;
  }

  return ERROR_SUCCESS;
}


YR_API int yr_rules_define_float_variable(
    YR_RULES* rules,
    const char* identifier,
    double value)
{
  YR_EXTERNAL_VARIABLE* external;

  external = rules->externals_list_head;

  while (!EXTERNAL_VARIABLE_IS_NULL(external))
  {
    if (strcmp(external->identifier, identifier) == 0)
    {
      external->value.f = value;
      break;
    }

    external++;
  }

  return ERROR_SUCCESS;
}


YR_API int yr_rules_define_string_variable(
    YR_RULES* rules,
    const char* identifier,
    const char* value)
{
  YR_EXTERNAL_VARIABLE* external;

  external = rules->externals_list_head;

  while (!EXTERNAL_VARIABLE_IS_NULL(external))
  {
    if (strcmp(external->identifier, identifier) == 0)
    {
      if (external->type == EXTERNAL_VARIABLE_TYPE_MALLOC_STRING &&
          external->value.s != NULL)
      {
        yr_free(external->value.s);
      }

      external->type = EXTERNAL_VARIABLE_TYPE_MALLOC_STRING;
      external->value.s = yr_strdup(value);

      if (external->value.s == NULL)
        return ERROR_INSUFICIENT_MEMORY;
      else
        return ERROR_SUCCESS;
    }

    external++;
  }

  return ERROR_SUCCESS;
}


void _yr_rules_clean_matches(
    YR_RULES* rules)
{
  YR_RULE* rule;
  YR_STRING* string;

  int tidx = yr_get_tidx();

  yr_rules_foreach(rules, rule)
  {
    rule->t_flags[tidx] &= ~RULE_TFLAGS_MATCH;
    rule->ns->t_flags[tidx] &= ~NAMESPACE_TFLAGS_UNSATISFIED_GLOBAL;

    yr_rule_strings_foreach(rule, string)
    {
      string->matches[tidx].count = 0;
      string->matches[tidx].head = NULL;
      string->matches[tidx].tail = NULL;
      string->unconfirmed_matches[tidx].count = 0;
      string->unconfirmed_matches[tidx].head = NULL;
      string->unconfirmed_matches[tidx].tail = NULL;
    }
  }
}

#if 0
void yr_rules_print_profiling_info(
    YR_RULES* rules)
{
  YR_RULE* rule;
  YR_STRING* string;

  clock_t clock_ticks;

  printf("===== PROFILING INFORMATION =====\n");

  yr_rules_foreach(rules, rule)
  {
    clock_ticks = rule->clock_ticks;

    yr_rule_strings_foreach(rule, string)
    {
      clock_ticks += string->clock_ticks;
    }

    printf(
        "%s:%s: %li\n",
        rule->ns->name,
        rule->identifier,
        clock_ticks);
  }

  printf("================================\n");
}
#endif

/* memory block을 yara로 스캔한다. */
int yr_rules_scan_mem_block( YR_RULES* rules, YR_MEMORY_BLOCK* block, int flags, int timeout, time_t start_time, YR_ARENA* matches_arena)
{
	YR_AC_STATE* next_state;
	YR_AC_MATCH* ac_match;
	YR_AC_STATE* current_state;

	size_t i;

	current_state = rules->automaton->root;
	i = 0;

	while (i < block->size)
	{
		ac_match = current_state->matches;

		while (ac_match != NULL)
		{
			if (ac_match->backtrack <= i)
			{
				FAIL_ON_ERROR(yr_scan_verify_match(
							ac_match,
							block->data,
							block->size,
							block->base,
							i - ac_match->backtrack,
							matches_arena,
							flags));
			}

			ac_match = ac_match->next;
		}

		next_state = yr_ac_next_state(current_state, block->data[i]);

		while (next_state == NULL && current_state->depth > 0)
		{
			current_state = current_state->failure;
			next_state = yr_ac_next_state(current_state, block->data[i]);
		}

		if (next_state != NULL)
			current_state = next_state;

		i++;

		if (timeout > 0 && i % 256 == 0)
		{
			if (difftime(time(NULL), start_time) > timeout)
				return ERROR_SCAN_TIMEOUT;
		}
	}

	ac_match = current_state->matches;

	while (ac_match != NULL)
	{
		if (ac_match->backtrack <= block->size)
		{
			FAIL_ON_ERROR(yr_scan_verify_match(
						ac_match,
						block->data,
						block->size,
						block->base,
						block->size - ac_match->backtrack,
						matches_arena,
						flags));
		}

		ac_match = ac_match->next;
	}

	return ERROR_SUCCESS;
}

/*입력받은 메모리 블럭을 yara로 스캔한다. */
YR_API int yr_rules_scan_mem_blocks( YR_RULES* rules, YR_MEMORY_BLOCK* block, int flags, YR_CALLBACK_FUNC callback, void* user_data, int timeout)
{
	YR_SCAN_CONTEXT context;
	YR_RULE* rule;
	YR_OBJECT* object;
	YR_EXTERNAL_VARIABLE* external;
	YR_ARENA* matches_arena = NULL;

	time_t start_time;
	tidx_mask_t bit;

	int message;
	int tidx = 0;
	int result = ERROR_SUCCESS;

	if (block == NULL)
		return ERROR_SUCCESS;

	context.flags = flags;
	context.callback = callback;
	context.user_data = user_data;
	context.file_size = block->size;
	context.mem_block = block;
	context.entry_point = UNDEFINED;
	context.objects_table = NULL;

	_yr_rules_lock(rules);

	bit = 1;

	while (rules->tidx_mask & bit)
	{
		tidx++;
		bit <<= 1;
	}

	if (tidx < MAX_THREADS)
		rules->tidx_mask |= bit;
	else
		result = ERROR_TOO_MANY_SCAN_THREADS;

	_yr_rules_unlock(rules);

	if (result != ERROR_SUCCESS)
		return result;

	yr_set_tidx(tidx);

	result = yr_arena_create(1024, 0, &matches_arena);

	if (result != ERROR_SUCCESS)
		goto _exit;

	result = yr_hash_table_create(64, &context.objects_table);

	if (result != ERROR_SUCCESS)
		goto _exit;

	external = rules->externals_list_head;

	while (!EXTERNAL_VARIABLE_IS_NULL(external))
	{
		result = yr_object_from_external_variable(
				external,
				&object);

		if (result == ERROR_SUCCESS)
			result = yr_hash_table_add(
					context.objects_table,
					external->identifier,
					NULL,
					(void*) object);

		if (result != ERROR_SUCCESS)
			goto _exit;

		external++;
	}

	start_time = time(NULL);

	while (block != NULL)
	{
		if (context.entry_point == UNDEFINED)
		{
			if (flags & SCAN_FLAGS_PROCESS_MEMORY)
				context.entry_point = yr_get_entry_point_address(
						block->data,
						block->size,
						block->base);
			else
				context.entry_point = yr_get_entry_point_offset(
						block->data,
						block->size);
		}

		result = yr_rules_scan_mem_block(
				rules,
				block,
				flags,
				timeout,
				start_time,
				matches_arena);

		if (result != ERROR_SUCCESS)
			goto _exit;

		block = block->next;
	}

	result = yr_execute_code(
			rules,
			&context,
			timeout,
			start_time);

	if (result != ERROR_SUCCESS)
		goto _exit;

	yr_rules_foreach(rules, rule)
	{
		if (RULE_IS_GLOBAL(rule) && !(rule->t_flags[tidx] & RULE_TFLAGS_MATCH))
		{
			rule->ns->t_flags[tidx] |= NAMESPACE_TFLAGS_UNSATISFIED_GLOBAL;
		}
	}

	yr_rules_foreach(rules, rule)
	{
		if (rule->t_flags[tidx] & RULE_TFLAGS_MATCH &&
				!(rule->ns->t_flags[tidx] & NAMESPACE_TFLAGS_UNSATISFIED_GLOBAL))
		{
			message = CALLBACK_MSG_RULE_MATCHING;
		}
		else
		{
			message = CALLBACK_MSG_RULE_NOT_MATCHING;
		}

		if (!RULE_IS_PRIVATE(rule))
		{
			switch (callback(message, rule, user_data))
			{
				case CALLBACK_ABORT:
					result = ERROR_SUCCESS;
					goto _exit;

				case CALLBACK_ERROR:
					result = ERROR_CALLBACK_ERROR;
					goto _exit;
				default:
					break;
			}
		}
	}

	callback(CALLBACK_MSG_SCAN_FINISHED, NULL, user_data);

_exit:

	//  yr_modules_print_data(&context);

	yr_modules_unload_all(&context);

	_yr_rules_clean_matches(rules);

	if (matches_arena != NULL)
		yr_arena_destroy(matches_arena);

	if (context.objects_table != NULL)
		yr_hash_table_destroy(
				context.objects_table,
				(YR_HASH_TABLE_FREE_VALUE_FUNC) yr_object_destroy);

	_yr_rules_lock(rules);
	rules->tidx_mask &= ~(1 << tidx);
	_yr_rules_unlock(rules);

	yr_set_tidx(-1);

	return result;
}

/* 입력받은 메모리를 yara로 스캔한다. */
YR_API int yr_rules_scan_mem( YR_RULES* rules, uint8_t* buffer, size_t buffer_size, int flags, YR_CALLBACK_FUNC callback, void* user_data, int timeout)
{
	YR_MEMORY_BLOCK block;

	block.data = buffer;
	block.size = buffer_size;
	block.base = 0;
	block.next = NULL;

	return yr_rules_scan_mem_blocks(
			rules,
			&block,
			flags,
			callback,
			user_data,
			timeout);
}

/* 입력받은 파일경로를 yara로 스캔한다 */
YR_API int yr_rules_scan_file( YR_RULES* rules, const char* filename, int flags, YR_CALLBACK_FUNC callback, void* user_data, int timeout)
{
	YR_MAPPED_FILE mfile;
	int result;

	result = yr_filemap_map(filename, &mfile);

	if (result == ERROR_SUCCESS)
	{
		result = yr_rules_scan_mem(
				rules,
				mfile.data,
				mfile.size,
				flags,
				callback,
				user_data,
				timeout);

		yr_filemap_unmap(&mfile);
	}

	return result;
}


YR_API int yr_rules_scan_proc(
    YR_RULES* rules,
    int pid,
    int flags,
    YR_CALLBACK_FUNC callback,
    void* user_data,
    int timeout)
{
  YR_MEMORY_BLOCK* first_block;
  YR_MEMORY_BLOCK* next_block;
  YR_MEMORY_BLOCK* block;

  int result;

  result = yr_process_get_memory(pid, &first_block);

  if (result == ERROR_SUCCESS)
    result = yr_rules_scan_mem_blocks(
        rules,
        first_block,
        flags | SCAN_FLAGS_PROCESS_MEMORY,
        callback,
        user_data,
        timeout);

  block = first_block;

  while (block != NULL)
  {
    next_block = block->next;

    yr_free(block->data);
    yr_free(block);

    block = next_block;
  }

  return result;
}


YR_API int yr_rules_save(
    YR_RULES* rules,
    const char* filename)
{
  assert(rules->tidx_mask == 0);
  return yr_arena_save(rules->arena, filename);
}


YR_API int yr_rules_load(
  const char* filename,
  YR_RULES** rules)
{
  YR_RULES* new_rules;
  YARA_RULES_FILE_HEADER* header;

  int result;

  new_rules = (YR_RULES*) yr_malloc(sizeof(YR_RULES));

  if (new_rules == NULL)
    return ERROR_INSUFICIENT_MEMORY;

  result = yr_arena_load(filename, &new_rules->arena);

  if (result != ERROR_SUCCESS)
  {
    yr_free(new_rules);
    return result;
  }

  header = (YARA_RULES_FILE_HEADER*) yr_arena_base_address(new_rules->arena);
  new_rules->automaton = header->automaton;
  new_rules->code_start = header->code_start;
  new_rules->externals_list_head = header->externals_list_head;
  new_rules->rules_list_head = header->rules_list_head;
  new_rules->tidx_mask = 0;

  result = pthread_mutex_init(&new_rules->mutex, NULL);

  if (result != 0)
    return ERROR_INTERNAL_FATAL_ERROR;

  *rules = new_rules;

  return ERROR_SUCCESS;
}

/* rule 자원을 해제한다. */
YR_API int yr_rules_destroy( YR_RULES* rules)
{
	YR_EXTERNAL_VARIABLE* external;

	external = rules->externals_list_head;

	while (!EXTERNAL_VARIABLE_IS_NULL(external))
	{
		if (external->type == EXTERNAL_VARIABLE_TYPE_MALLOC_STRING)
			yr_free(external->value.s);

		external++;
	}

	pthread_mutex_destroy(&rules->mutex);

	yr_arena_destroy(rules->arena);
	yr_free(rules);

	return ERROR_SUCCESS;
}
