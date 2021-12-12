#!/bin/bash

echo "assemble common start..."

CODE=0

gradle saveCommonInfo; CODE=$?
if test $CODE -ne 0; then
 echo "Save common info error $CODE!"
 exit 11
fi

if [ ! -f "$(pwd)/build/common.json" ]; then
 echo "$(pwd)/build/common.json does not exist."
 exit 21
fi

echo "assemble common success"

exit 0
