#!/bin/bash

echo "assemble vcs start..."

/bin/bash $RESOURCES_PATH/bash/util/check_variables.sh \
 ASSEMBLY_PATH GITHUB_PAT GIT_COMMIT_SHA || exit 1 # todo

CODE=0

DST_PATH="${ASSEMBLY_PATH}/vcs"
rm -rf $DST_PATH
mkdir -p $DST_PATH || exit 21

CODE=$(curl -w %{http_code} -o "$DST_PATH/worker.json" \
 https://api.github.com/user \
 -H "Authorization: token $GITHUB_PAT")
if test $CODE -ne 200; then
 echo "Get worker info error!"
 echo "Request error with response code $CODE!"
 exit 31
fi
echo "The worker $(cat $DST_PATH/worker.json | jq -r .html_url) is ready."

WORKER_NAME="$(cat $ASSEMBLY_PATH/vcs/worker.json | jq -r .name)"
WORKER_EMAIL="$(cat $ASSEMBLY_PATH/vcs/worker.json | jq -r .email)"
for it in WORKER_NAME WORKER_EMAIL; do
 if test -z "${!it}"; then echo "$it is empty!"; exit 11; fi; done

git config user.name "$WORKER_NAME" && \
 git config user.email "$WORKER_EMAIL" && \
 git fetch origin $GIT_COMMIT_SHA && \
 git merge --no-ff --no-commit $GIT_COMMIT_SHA; CODE=$?
if test $CODE -ne 0; then
 echo "Git merge failed!"
 exit 101
fi

CODE=$(curl -w %{http_code} -o "$DST_PATH/commit.json" \
 https://api.github.com/repos/$GITHUB_OWNER/$GITHUB_REPO/commits/$GIT_COMMIT_SHA)
if test $CODE -ne 200; then
 echo "Get commit info error!"
 echo "Request error with response code $CODE!"
 exit 32
fi
echo "The commit $(cat $DST_PATH/commit.json | jq -r .html_url) is ready."

AUTHOR_LOGIN="$(cat $DST_PATH/commit.json | jq -r .author.login)"
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
echo "The author $(cat $DST_PATH/author.json | jq -r .html_url) is ready."

echo "assemble vcs success"

exit 0
