#!/bin/bash

echo "assemble documentation start..."

CODE=0

gradle lib:assembleDocumentation; CODE=$?

if test $CODE -ne 0; then
 echo "gradle assemble error"
 exit 11
fi

echo "assemble documentation success"

exit 0
