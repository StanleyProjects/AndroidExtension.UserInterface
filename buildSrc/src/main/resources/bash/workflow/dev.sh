#!/bin/bash

echo "dev start..."

WORKFLOW=$RESOURCES_PATH/bash/workflow

CODE=0

/bin/bash $WORKFLOW/verify.sh; CODE=$?

REPO_URL=https://github.com/$GITHUB_OWNER/$GITHUB_REPO
TOP="GitHub build [#$GITHUB_RUN_NUMBER]($REPO_URL/actions/runs/$GITHUB_RUN_ID)"
MID="[$GITHUB_OWNER](https://github.com/$GITHUB_OWNER)/[$GITHUB_REPO]($REPO_URL)/[${GIT_COMMIT_SHA::7}]($REPO_URL/commit/$GIT_COMMIT_SHA)"
if test $CODE -ne 0; then
  TOP="$TOP failed!"
fi
MESSAGE="$TOP\n\n$MID"

/bin/bash $WORKFLOW/telegram_send_message.sh "${MESSAGE@E}"

if test $CODE -ne 0; then
  echo "dev failed!"
  exit 1
fi

echo "dev success"

exit 0
