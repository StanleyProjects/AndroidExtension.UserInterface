#!/bin/bash

echo "GitHub release..."

if test $# -ne 1; then
 echo "Script needs for 1 argument but actual $#"
 exit 11
fi

REQUEST_BODY=$1

if test -z "$REQUEST_BODY"; then
 echo "Request body is empty!"
 exit 12
fi

CODE=0

rm -f file
CODE=$(curl -w %{http_code} -o file -X POST \
 -s https://api.github.com/repos/$GITHUB_OWNER/$GITHUB_REPO/releases \
 -H "Authorization: token $GITHUB_PAT" \
 -d "$REQUEST_BODY")
if test $CODE -ne 201; then
 echo "GitHub release $TAG error!"
 echo "Request error with response code $CODE!"
 exit 21
fi
BODY=$(<file); rm file
RELEASE_ID=$(echo "$BODY" | jq -r .id)

echo "The release $RELEASE_ID is created."
echo "GitHub release $TAG success"

exit 0
