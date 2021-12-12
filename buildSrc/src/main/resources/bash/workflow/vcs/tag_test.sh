#!/bin/bash

echo "tag test..."

if test $# -ne 1; then
 echo "Script needs for 1 argument but actual $#"
 exit 11
fi

TAG=$1

if test -z "$TAG"; then
 echo "Tag is empty!"
 exit 12
fi

CODE=0

REF=refs/tags/$TAG

rm -f file
CODE=$(curl -w %{http_code} -o file \
  -s https://api.github.com/repos/$GITHUB_OWNER/$GITHUB_REPO/git/$REF)
case $CODE in
 404) echo "Tag $TAG does not exist yet in $GITHUB_OWNER/$GITHUB_REPO."; exit 0;;
 200) true;; # ignored
 *) echo "Request error with response code $code!"; exit 21;;
esac
BODY=$(<file); rm file
#echo $BODY

if [[ "$BODY" =~ ^{ ]]; then
 # {"ref":"...", ...}
 if test "$(echo "$BODY" | jq -r .ref)" == "$REF"; then
  echo "Tag $TAG already exists!"
  exit 21
 fi
 exit 22 # todo
elif [[ "$BODY" =~ ^\[ ]]; then true # ignored
else exit 23 # todo
fi

ARRAY=($(echo "$BODY" | jq -r .[].ref))
SIZE=${#ARRAY[*]}
# [{"ref":"...", ...}, ...]
for ((i = 0; i < SIZE; i++)); do
 if test "${ARRAY[$i]}" == "$REF"; then
  echo "Tag $TAG already exists!"
  exit $((100 + i))
 fi
done

echo "tag test success"

exit 0
