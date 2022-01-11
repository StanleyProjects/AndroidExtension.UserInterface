#!/bin/bash

echo "assemble vcs start..."

/bin/bash $RESOURCES_PATH/bash/util/check_variables.sh \
 ASSEMBLY_PATH GITHUB_PAT GIT_COMMIT_SRC GIT_COMMIT_DST GIT_BRANCH_DST || exit 1 # todo

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
 git config user.email "$WORKER_EMAIL"; CODE=$?
if test $CODE -ne 0; then
 echo "Git config error!"; exit 101
fi
git checkout $GIT_BRANCH_DST; CODE=$?
if test $CODE -ne 0; then
 echo "Git checkout to \"$GIT_BRANCH_DST\" error!"; exit 102
fi
git merge --no-ff --no-commit $GIT_COMMIT_SRC; CODE=$?
if test $CODE -ne 0; then
 echo "Git merge with ${GIT_COMMIT_SRC::7} failed!"; exit 103
fi

# source

CODE=$(curl -w %{http_code} -o "$DST_PATH/commit.src.json" \
 https://api.github.com/repos/$GITHUB_OWNER/$GITHUB_REPO/commits/$GIT_COMMIT_SRC)
if test $CODE -ne 200; then
 echo "Get commit source info error!"
 echo "Request error with response code $CODE!"
 exit 32
fi
echo "The commit source $(cat $DST_PATH/commit.src.json | jq -r .html_url) is ready."

AUTHOR_LOGIN_SRC="$(cat $DST_PATH/commit.src.json | jq -r .author.login)"
if test -z "$AUTHOR_LOGIN_SRC"; then
 echo "Author login source is empty!"
 exit 41
fi
CODE=$(curl -w %{http_code} -o "$DST_PATH/author.src.json" \
 https://api.github.com/users/$AUTHOR_LOGIN_SRC)
if test $CODE -ne 200; then
 echo "Get author source info error!"
 echo "Request error with response code $CODE!"
 exit 42
fi
echo "The author source $(cat $DST_PATH/author.src.json | jq -r .html_url) is ready."

# destination

CODE=$(curl -w %{http_code} -o "$DST_PATH/commit.dst.json" \
 https://api.github.com/repos/$GITHUB_OWNER/$GITHUB_REPO/commits/$GIT_COMMIT_DST)
if test $CODE -ne 200; then
 echo "Get commit destination info error!"
 echo "Request error with response code $CODE!"
 exit 32
fi
echo "The commit destination $(cat $DST_PATH/commit.dst.json | jq -r .html_url) is ready."

AUTHOR_LOGIN_DST="$(cat $DST_PATH/commit.dst.json | jq -r .author.login)"
if test -z "$AUTHOR_LOGIN_DST"; then
 echo "Author login destination is empty!"
 exit 41
fi
CODE=$(curl -w %{http_code} -o "$DST_PATH/author.dst.json" \
 https://api.github.com/users/$AUTHOR_LOGIN_DST)
if test $CODE -ne 200; then
 echo "Get author destination info error!"
 echo "Request error with response code $CODE!"
 exit 42
fi
echo "The author destination $(cat $DST_PATH/author.dst.json | jq -r .html_url) is ready."

echo "assemble vcs success"

exit 0
