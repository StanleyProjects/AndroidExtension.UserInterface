#!/bin/bash

echo "assemble vcs start..."

if test -z "$GITHUB_PAT"; then
 echo "GitHub personal access token is empty!"
 exit 11
fi

if test -z "$GIT_COMMIT_SHA"; then
 echo "Git commit sha is empty!"
 exit 12
fi

CODE=0

DST_PATH="${ASSEMBLY_PATH}/vcs"
rm -rf $DST_PATH
mkdir -p $DST_PATH || return 21

CODE=$(curl -w %{http_code} -o "$DST_PATH/worker.json" \
 https://api.github.com/user \
 -H "Authorization: token $GITHUB_PAT")
if test $CODE -ne 200; then
 echo "Get worker info error!"
 echo "Request error with response code $CODE!"
 exit 31
fi
echo "The worker $(echo "$(<$DST_PATH/worker.json)" | jq -r .html_url) is ready."

CODE=$(curl -w %{http_code} -o "$DST_PATH/commit.json" \
 https://api.github.com/repos/$GITHUB_OWNER/$GITHUB_REPO/commits/$GIT_COMMIT_SHA)
if test $CODE -ne 200; then
 echo "Get commit info error!"
 echo "Request error with response code $CODE!"
 exit 32
fi
echo "The commit $(echo "$(<$DST_PATH/commit.json)" | jq -r .html_url) is ready."

AUTHOR_LOGIN="$(echo "$(<$DST_PATH/commit.json)" | jq -r .author.login)"
if test -z "$AUTHOR_LOGIN"; then
 echo "Author login is empty!"
 exit 41
fi
CODE=$(curl -w %{http_code} -o "$DST_PATH/author.json" \
 https://api.github.com/users/$AUTHOR_LOGIN)
if test $CODE -ne 200; then
 echo "Get author info error!"
 echo "Request error with response code $CODE!"
 exit 42
fi
echo "The author $(echo "$(<$DST_PATH/author.json)" | jq -r .html_url) is ready."

echo "assemble vcs success"

exit 0
