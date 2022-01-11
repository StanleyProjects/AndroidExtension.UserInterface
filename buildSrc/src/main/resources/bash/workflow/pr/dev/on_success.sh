#!/bin/bash

echo "on success pull request to dev start..."

/bin/bash $RESOURCES_PATH/bash/util/check_variables.sh \
 ASSEMBLY_PATH GIT_COMMIT_SRC \
 GITHUB_OWNER GITHUB_REPO GITHUB_RUN_NUMBER GITHUB_RUN_ID || exit 1 # todo

AUTHOR_NAME_SRC="$(cat $ASSEMBLY_PATH/vcs/author.src.json | jq -r .name)"
AUTHOR_URL_SRC="$(cat $ASSEMBLY_PATH/vcs/author.src.json | jq -r .html_url)"
WORKER_NAME="$(cat ${ASSEMBLY_PATH}/vcs/worker.json | jq -r .name)"
WORKER_URL="$(cat ${ASSEMBLY_PATH}/vcs/worker.json | jq -r .html_url)"
VERSION="$(cat ${ASSEMBLY_PATH}/common.json | jq -r .version)"

CODE=0

rm -f $ASSEMBLY_PATH/vcs/pr.${PR_NUMBER}.json
CODE=$(curl -w %{http_code} -o "$ASSEMBLY_PATH/vcs/pr.${PR_NUMBER}.json" \
 https://api.github.com/repos/$GITHUB_OWNER/$GITHUB_REPO/pulls/$PR_NUMBER)
if test $CODE -ne 200; then
 echo "Get pull request $PR_NUMBER info error!"
 echo "Request error with response code $CODE!"
 exit 101
fi

PR_STATE="$(cat $ASSEMBLY_PATH/vcs/pr.${PR_NUMBER}.json | jq -r .state)"
PR_MERGED="$(cat $ASSEMBLY_PATH/vcs/pr.${PR_NUMBER}.json | jq -r .merged)"

for it in AUTHOR_NAME_SRC AUTHOR_URL_SRC WORKER_NAME WORKER_URL VERSION PR_STATE PR_MERGED; do
 if test -z "${!it}"; then echo "$it is empty!"; exit 11; fi; done

if test "$PR_STATE" -ne "closed"; then
 echo "Pull request $PR_NUMBER state is \"$PR_STATE\"!"; exit 21
fi

if test "$PR_MERGED" -ne "true"; then
 echo "Pull request $PR_NUMBER is not merged!"; exit 22
fi

REPO_URL=https://github.com/$GITHUB_OWNER/$GITHUB_REPO
RUN_URL=$REPO_URL/actions/runs/$GITHUB_RUN_ID
COMMENT_BODY="Merged by GitHub build [#$GITHUB_RUN_NUMBER]($RUN_URL)
- intermediate [$VERSION]($GITHUB_PAGES/documentation/$VERSION)"
/bin/bash $RESOURCES_PATH/bash/workflow/vcs/post_comment.sh "$COMMENT_BODY" || exit 1 # todo

GITHUB_PAGES="https://${GITHUB_OWNER}.github.io/$GITHUB_REPO"
MESSAGE="GitHub build [#$GITHUB_RUN_NUMBER]($RUN_URL)

[$GITHUB_OWNER](https://github.com/$GITHUB_OWNER) / [$GITHUB_REPO]($REPO_URL)

The pull request [#$PR_NUMBER]($REPO_URL/pull/$PR_NUMBER) merged by [$WORKER_NAME]($WORKER_URL)
 - source ${GIT_COMMIT_SRC::7} by [$AUTHOR_NAME_SRC]($AUTHOR_URL_SRC)
 - intermediate [$VERSION]($GITHUB_PAGES/documentation/$VERSION)"

/bin/bash $RESOURCES_PATH/bash/workflow/telegram_send_message.sh "$MESSAGE"

echo "The pull request #$PR_NUMBER merged."

exit 0
