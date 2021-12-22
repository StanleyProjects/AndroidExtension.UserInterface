#!/bin/bash

echo "close pull request..."

/bin/bash $RESOURCES_PATH/bash/util/check_variables.sh \
 GITHUB_OWNER GITHUB_REPO PR_NUMBER GITHUB_PAT || exit 1 # todo

CODE=$(curl -w %{http_code} -o /dev/null -X PATCH \
 https://api.github.com/repos/$GITHUB_OWNER/$GITHUB_REPO/pulls/$PR_NUMBER \
 -H "Authorization: token $GITHUB_PAT" \
 -d '{"state":"closed"}')
if test $CODE -ne 200; then
 echo "The pull request #$PR_NUMBER was not closed!"
 echo "Request error with response code $CODE!"
 exit 21
fi

echo "Pull request #$PR_NUMBER closed successfully."

exit 0
