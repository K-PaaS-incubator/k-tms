#include <yara_sizedstr.h>


int sized_string_cmp(
  SIZED_STRING* s1,
  SIZED_STRING* s2)
{
  int i = 0;

  while (s1->length > i &&
         s2->length > i &&
         s1->c_string[i] == s2->c_string[i])
  {
    i++;
  }

  if (i == s1->length && i == s2->length)
    return 0;
  else if (i == s1->length)
    return -1;
  else if (i == s2->length)
    return 1;
  else if (s1->c_string[i] < s2->c_string[i])
    return -1;
  else
    return 1;
}
