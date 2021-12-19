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

REPO_URL=https://github.com/$GITHUB_OWNER/$GITHUB_REPO
MESSAGE="GitHub build [#$GITHUB_RUN_NUMBER]($REPO_URL/actions/runs/$GITHUB_RUN_ID) failed!

[$GITHUB_OWNER](https://github.com/$GITHUB_OWNER) / [$GITHUB_REPO]($REPO_URL)

[${GIT_COMMIT_SHA::7}]($REPO_URL/commit/$GIT_COMMIT_SHA) by [$AUTHOR_NAME]($AUTHOR_URL)

pull request [#$PR_NUMBER]($REPO_URL/pull/$PR_NUMBER) closed by [$WORKER_NAME]($WORKER_URL)"

/bin/bash $RESOURCES_PATH/bash/workflow/telegram_send_message.sh "${MESSAGE@E}"

echo "The pull request #$PR_NUMBER failed!"

exit 0
