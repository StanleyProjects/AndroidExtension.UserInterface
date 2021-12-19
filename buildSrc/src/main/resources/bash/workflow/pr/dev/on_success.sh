#!/bin/bash

echo "on success pull request to dev start..."

AUTHOR_NAME="$(cat ${ASSEMBLY_PATH}/vcs/author.json | jq -r .name)"
if test -z "$AUTHOR_NAME"; then
 echo "Author name is empty!"
 exit 21
fi
AUTHOR_URL="$(cat ${ASSEMBLY_PATH}/vcs/author.json | jq -r .html_url)"
if test -z "$AUTHOR_URL"; then
 echo "Author url is empty!"
 exit 22
fi
WORKER_NAME="$(cat ${ASSEMBLY_PATH}/vcs/worker.json | jq -r .name)"
if test -z "$WORKER_NAME"; then
 echo "Worker name is empty!"
 exit 23
fi
WORKER_URL="$(cat ${ASSEMBLY_PATH}/vcs/worker.json | jq -r .html_url)"
if test -z "$WORKER_URL"; then
 echo "Worker url is empty!"
 exit 24
fi
VERSION="$(cat ${ASSEMBLY_PATH}/common.json | jq -r .version)"
if test -z "$VERSION"; then
 echo "Version is empty!"
 exit 25
fi

REQUEST_BODY="{\
\"commit_title\":\"Merge ${GIT_COMMIT_SHA::7} -> $PR_SOURCE_BRANCH by $WORKER_NAME.\",\
\"commit_message\":\"Intermediate $VERSION.\"\
}"
CODE=$(curl -w %{http_code} -o /dev/null -X PUT \
 https://api.github.com/repos/$GITHUB_OWNER/$GITHUB_REPO/pulls/$PR_NUMBER/merge \
 -H "Authorization: token $GITHUB_PAT" \
 -d "$REQUEST_BODY")
if test $CODE -ne 200; then
 echo "Pull request #$PR_NUMBER merging error!"
 echo "Request error with response code $CODE!"
 exit 11
fi

echo "The pull request #$PR_NUMBER merged."

REPO_URL=https://github.com/$GITHUB_OWNER/$GITHUB_REPO
MESSAGE="GitHub build [#$GITHUB_RUN_NUMBER]($REPO_URL/actions/runs/$GITHUB_RUN_ID)

[$GITHUB_OWNER](https://github.com/$GITHUB_OWNER) / [$GITHUB_REPO]($REPO_URL)

[${GIT_COMMIT_SHA::7}]($REPO_URL/commit/$GIT_COMMIT_SHA) by [$AUTHOR_NAME]($AUTHOR_URL)

pull request [#$PR_NUMBER]($REPO_URL/pull/$PR_NUMBER) merged by [$WORKER_NAME]($WORKER_URL)"

/bin/bash $RESOURCES_PATH/bash/workflow/telegram_send_message.sh "${MESSAGE@E}"

echo "pull request to dev success"

exit 0
