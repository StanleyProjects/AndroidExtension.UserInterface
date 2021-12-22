#!/bin/bash

echo "on failed pull request to dev start..."

/bin/bash $RESOURCES_PATH/bash/workflow/vcs/close_pr.sh || exit 1 # todo

/bin/bash $RESOURCES_PATH/bash/util/check_variables.sh \
 GITHUB_OWNER GITHUB_REPO GITHUB_RUN_NUMBER GITHUB_RUN_ID || exit 1 # todo

RUN_URL="https://github.com/$GITHUB_OWNER/$GITHUB_REPO/actions/runs/$GITHUB_RUN_ID"
BODY="Closed by GitHub build [#$GITHUB_RUN_NUMBER]($RUN_URL)."
/bin/bash $RESOURCES_PATH/bash/workflow/vcs/post_comment.sh "$BODY" || exit 1 # todo

AUTHOR_NAME="$(cat ${ASSEMBLY_PATH}/vcs/author.json | jq -r .name)"
if test -z "$AUTHOR_NAME"; then
 echo "Author name is empty!"; exit 21
fi
AUTHOR_URL="$(cat ${ASSEMBLY_PATH}/vcs/author.json | jq -r .html_url)"
if test -z "$AUTHOR_URL"; then
 echo "Author url is empty!"; exit 22
fi
WORKER_NAME="$(cat ${ASSEMBLY_PATH}/vcs/worker.json | jq -r .name)"
if test -z "$WORKER_NAME"; then
 echo "Worker name is empty!"; exit 23
fi
WORKER_URL="$(cat ${ASSEMBLY_PATH}/vcs/worker.json | jq -r .html_url)"
if test -z "$WORKER_URL"; then
 echo "Worker url is empty!"; exit 24
fi

REPO_URL=https://github.com/$GITHUB_OWNER/$GITHUB_REPO
MESSAGE="GitHub build [#$GITHUB_RUN_NUMBER]($REPO_URL/actions/runs/$GITHUB_RUN_ID) failed!

[$GITHUB_OWNER](https://github.com/$GITHUB_OWNER) / [$GITHUB_REPO]($REPO_URL)

[${GIT_COMMIT_SHA::7}]($REPO_URL/commit/$GIT_COMMIT_SHA) by [$AUTHOR_NAME]($AUTHOR_URL)

pull request [#$PR_NUMBER]($REPO_URL/pull/$PR_NUMBER) closed by [$WORKER_NAME]($WORKER_URL)"

/bin/bash $RESOURCES_PATH/bash/workflow/telegram_send_message.sh "${MESSAGE@E}"

echo "The pull request #$PR_NUMBER failed!"

exit 0
