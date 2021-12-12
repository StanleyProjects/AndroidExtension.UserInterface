#!/bin/bash

echo "assemble common start..."

CODE=0

VERSION=$(gradle -q saveCommonInfo)

if test -z "$VERSION"; then
  echo "version error"
  exit 11
fi

echo "assemble common success"

exit 0
