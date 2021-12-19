#!/bin/bash

echo "on failed pull request to dev start..."

REQUEST_BODY='{"state":"closed"}'
CODE=$(curl -w %{http_code} -o /dev/null -X PATCH \
 https://api.github.com/repos/$GITHUB_OWNER/$GITHUB_REPO/pulls/$PR_NUMBER \
 -H "Authorization: token $GITHUB_PAT" \
 -d "$REQUEST_BODY")
if test $CODE -ne 200; then
 echo "Pull request #$PR_NUMBER rejecting error!"
 echo "Request error with response code $CODE!"
 return 11
fi

REQUEST_BODY="{\"body\":\"\
Closed by GitHub build \
[#$GITHUB_RUN_NUMBER](https://github.com/$GITHUB_OWNER/$GITHUB_REPO/actions/runs/$GITHUB_RUN_ID) \
that failed just because.\
\"}" # todo cause ?

CODE=$(curl -w %{http_code} -o /dev/null -X POST \
 https://api.github.com/repos/$GITHUB_OWNER/$GITHUB_REPO/issues/$PR_NUMBER/comments \
 -H "Authorization: token $GITHUB_PAT" \
 -d "$REQUEST_BODY")
if test $CODE -ne 201; then
 echo "Post comment to pr #$PR_NUMBER error!"
 echo "Request error with response code $CODE!"
 return 12
fi

echo "The pull request #$PR_NUMBER failed!"

exit 0
