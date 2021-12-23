#!/bin/bash

#echo "check variables..."

if test $# -eq 0; then
 echo "The script needs more than 0 arguments!"
 exit 11
fi

ARRAY=($@)
SIZE=${#ARRAY[*]}
for ((i=0; i<SIZE; i++)); do
 it="${ARRAY[i]}"
 if test -z "${!it}"; then
  echo "Value by \"$it\" is empty!"; exit $((100+i))
 fi
done

#echo "Validation of variables was successful."

exit 0
