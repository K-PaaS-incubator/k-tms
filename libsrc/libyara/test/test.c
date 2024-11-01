#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include <yara.h>

void help()
{
    printf("Usage: ./test [OPTION] [size]\n"
           "  -h                help\n"
           "  -t                sleep intverval\n"
           "  -s [size]         data length\n");
    exit(0);
}

/* 컴파일 에러시 호출되는 함수 */
void print_compiler_error( int error_level, const char* file_name, int line_number, const char* message, void* user_data)
{
	if (error_level == YARA_ERROR_LEVEL_ERROR)
	{
		fprintf(stderr, "%s(%d): error: %s\n", file_name, line_number, message);
	}
	else
	{
//		if (!ignore_warnings)
			fprintf(stderr, "%s(%d): warning: %s\n", file_name, line_number, message);
	}
}

int callback(int message, void* message_data, void* user_data)
{

	switch(message)
	{
		case CALLBACK_MSG_RULE_MATCHING:
			{
				YR_RULE *rule = (YR_RULE *)message_data;
				printf("%s:", rule->ns->name);
				printf("%s ", rule->identifier);
			}
		case CALLBACK_MSG_RULE_NOT_MATCHING:

		default : 
			break;
	}

	return CALLBACK_ERROR;


}

int main(int argc, char *argv[])
{
	YR_COMPILER* compiler = NULL;
	YR_RULES* rules = NULL;
	int yara_error = 0;

	const char * ns = NULL;
	const char * file_name = NULL;
	FILE * rule_file = NULL;

	int timeout = 1000000;


	if (yr_initialize() != ERROR_SUCCESS)
	{
		exit(EXIT_FAILURE);
	}
	if (yr_compiler_create(&compiler))
	{
		exit(EXIT_FAILURE);
	}
	yr_compiler_set_callback( compiler, print_compiler_error, NULL);

	rule_file = fopen("yara.pol","r");
	if( rule_file == NULL )
		goto exit__;
	yara_error = yr_compiler_add_file(compiler, rule_file, ns, file_name);

	fclose(rule_file);
	if(yara_error != ERROR_SUCCESS)
		goto exit__;

	yara_error = yr_compiler_get_rules(compiler, &rules);
	if(yara_error != ERROR_SUCCESS)
		goto exit__;
	yr_compiler_destroy(compiler);
	compiler = NULL;

	yara_error = yr_rules_scan_file( rules, "./test_file.txt", SCAN_FLAGS_FAST_MODE, callback, NULL, timeout);



exit__:

	if (compiler != NULL)
		yr_compiler_destroy(compiler);

	if (rules != NULL)
		yr_rules_destroy(rules);

	yr_finalize();
	return 0;
}
