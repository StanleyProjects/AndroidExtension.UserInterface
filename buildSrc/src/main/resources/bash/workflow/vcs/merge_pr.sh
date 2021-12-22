#!/bin/bash

echo "merge pull request..."

if test $# -ne 2; then
 echo "Script needs for 2 argument but actual $#"
 exit 11
fi

TITLE=$1
if test -z "$TITLE"; then
 echo "Title is empty!"
 exit 12
fi

MESSAGE=$1
if test -z "$MESSAGE"; then
 echo "Message is empty!"
 exit 13
fi

/bin/bash $RESOURCES_PATH/bash/util/check_variables.sh \
 GITHUB_OWNER GITHUB_REPO PR_NUMBER GITHUB_PAT || exit 1 # todo

REQUEST_BODY="{\"commit_title\":\"$TITLE\",\"commit_message\":\"$MESSAGE\"}"
CODE=$(curl -w %{http_code} -o /dev/null -X PUT \
 https://api.github.com/repos/$GITHUB_OWNER/$GITHUB_REPO/pulls/$PR_NUMBER/merge \
 -H "Authorization: token $GITHUB_PAT" \
 -d "$REQUEST_BODY")
if test $CODE -ne 200; then
 echo "The pull request #$PR_NUMBER was not merged!"
 echo "Request error with response code $CODE!"
 exit 21
fi

echo "Pull request #$PR_NUMBER merged successfully."

exit 0
