
#include <yara_exec.h>
#include <yara_modules.h>
#include <yara_libyara.h>


#define MODULE(name) \
    int name ## __declarations(YR_OBJECT* module); \
    int name ## __load(YR_SCAN_CONTEXT* context, \
                       YR_OBJECT* module, \
                       void* module_data, \
                       size_t module_data_size); \
    int name ## __unload(YR_OBJECT* main_structure); \
    int name ## __initialize(YR_MODULE* module); \
    int name ## __finalize(YR_MODULE* module);


#include <modules/module_list>

#undef MODULE


#define MODULE(name) \
    { 0, \
      #name, \
      name##__declarations, \
      name##__load, \
      name##__unload, \
      name##__initialize, \
      name##__finalize \
    },

YR_MODULE yr_modules_table[] =
{
  #include <modules/module_list>
};

#undef MODULE

/* yr_modules_initialize
   module 초기화 . 각가의 초기화 함수 호출 */
int yr_modules_initialize()
{
	unsigned int i, result;

	for (i = 0; i < sizeof(yr_modules_table) / sizeof(YR_MODULE); i++)
	{
		result = yr_modules_table[i].initialize(&yr_modules_table[i]);

		if (result != ERROR_SUCCESS)
			return result;
	}

	return ERROR_SUCCESS;
}


/* yr_modules_finalize
   module 종료. 각가의 종료 함수 호출 */
int yr_modules_finalize()
{
  unsigned int i, result;

  for (i = 0; i < sizeof(yr_modules_table) / sizeof(YR_MODULE); i++)
  {
    result = yr_modules_table[i].finalize(&yr_modules_table[i]);

    if (result != ERROR_SUCCESS)
      return result;
  }

  return ERROR_SUCCESS;
}


int yr_modules_do_declarations(
    const char* module_name,
    YR_OBJECT* main_structure)
{
  unsigned int i;

  for (i = 0; i < sizeof(yr_modules_table) / sizeof(YR_MODULE); i++)
  {
    if (strcmp(yr_modules_table[i].name, module_name) == 0)
      return yr_modules_table[i].declarations(main_structure);
  }

  return ERROR_UNKNOWN_MODULE;
}


int yr_modules_load(
    const char* module_name,
    YR_SCAN_CONTEXT* context)
{
  YR_MODULE_IMPORT mi;
  YR_OBJECT* module_structure;

  int result;
  unsigned int i;

  module_structure = (YR_OBJECT*) yr_hash_table_lookup(
      context->objects_table,
      module_name,
      NULL);

  // if module_structure != NULL, the module was already
  // loaded, return successfully without doing nothing.

  if (module_structure != NULL)
    return ERROR_SUCCESS;

  // not loaded yet

  FAIL_ON_ERROR(yr_object_create(
      OBJECT_TYPE_STRUCTURE,
      module_name,
      NULL,
      &module_structure));

  mi.module_name = module_name;
  mi.module_data = NULL;
  mi.module_data_size = 0;

  result = context->callback(
      CALLBACK_MSG_IMPORT_MODULE,
      &mi,
      context->user_data);

  if (result == CALLBACK_ERROR)
    return ERROR_CALLBACK_ERROR;

  FAIL_ON_ERROR_WITH_CLEANUP(
      yr_modules_do_declarations(module_name, module_structure),
      yr_object_destroy(module_structure));

  FAIL_ON_ERROR_WITH_CLEANUP(
      yr_hash_table_add(
          context->objects_table,
          module_name,
          NULL,
          module_structure),
      yr_object_destroy(module_structure));

  for (i = 0; i < sizeof(yr_modules_table) / sizeof(YR_MODULE); i++)
  {
    if (strcmp(yr_modules_table[i].name, module_name) == 0)
    {
      result = yr_modules_table[i].load(
          context,
          module_structure,
          mi.module_data,
          mi.module_data_size);

      if (result != ERROR_SUCCESS)
        return result;

      yr_modules_table[i].is_loaded |= 1 << yr_get_tidx();
    }
  }

  return ERROR_SUCCESS;
}


int yr_modules_unload_all(
    YR_SCAN_CONTEXT* context)
{
  YR_OBJECT* module_structure;
  tidx_mask_t tidx_mask = 1 << yr_get_tidx();
  unsigned int i;

  for (i = 0; i < sizeof(yr_modules_table) / sizeof(YR_MODULE); i++)
  {
    if (yr_modules_table[i].is_loaded & tidx_mask)
    {
      module_structure = (YR_OBJECT*) yr_hash_table_lookup(
          context->objects_table,
          yr_modules_table[i].name,
          NULL);

      assert(module_structure != NULL);

      yr_modules_table[i].unload(module_structure);
      yr_modules_table[i].is_loaded &= ~tidx_mask;
    }
  }

  return ERROR_SUCCESS;
}


void yr_modules_print_data(
    YR_SCAN_CONTEXT* context)
{
  YR_OBJECT* module_structure;
  tidx_mask_t tidx_mask = 1 << yr_get_tidx();
  unsigned int i;

  for ( i = 0; i < sizeof(yr_modules_table) / sizeof(YR_MODULE); i++)
  {
    if (yr_modules_table[i].is_loaded & tidx_mask)
    {
      module_structure = (YR_OBJECT*) yr_hash_table_lookup(
          context->objects_table,
          yr_modules_table[i].name,
          NULL);

      assert(module_structure != NULL);

      yr_object_print_data(module_structure, 0);
    }
  }
}
