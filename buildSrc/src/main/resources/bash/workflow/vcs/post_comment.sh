#!/bin/bash

echo "post comment..."

if test $# -ne 1; then
 echo "Script needs for 1 argument but actual $#"
 exit 11
fi

BODY=$1
if test -z "$BODY"; then
 echo "Body is empty!"
 exit 12
fi

/bin/bash $RESOURCES_PATH/bash/util/check_variables.sh \
 GITHUB_OWNER GITHUB_REPO PR_NUMBER GITHUB_PAT || exit 1 # todo

BODY="${BODY/$'\n'/\n}"
CODE=$(curl -w %{http_code} -o /dev/null -X POST \
 https://api.github.com/repos/$GITHUB_OWNER/$GITHUB_REPO/issues/$PR_NUMBER/comments \
 -H "Authorization: token $GITHUB_PAT" \
 -d "{\"body\":\"$BODY\"}")
if test $CODE -ne 201; then
 echo "Post comment to pr #$PR_NUMBER error!"
 echo "Request error with response code $CODE!"
 exit 21
fi

exit 0
