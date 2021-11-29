#!/bin/bash

echo "dev start..."

CODE=0

/bin/bash $RESOURCES_PATH/bash/workflow/prepare.sh && \
 /bin/bash $RESOURCES_PATH/bash/workflow/verify.sh && \
 /bin/bash $RESOURCES_PATH/bash/workflow/assemble/documentation.sh; CODE=$?

if test $CODE -ne 0; then
  echo "pr dev failed!"
  exit 1 # todo
fi

REPO_URL=https://github.com/$GITHUB_OWNER/$GITHUB_REPO
MESSAGE="GitHub build [#$GITHUB_RUN_NUMBER]($REPO_URL/actions/runs/$GITHUB_RUN_ID)

[$GITHUB_OWNER](https://github.com/$GITHUB_OWNER) / [$GITHUB_REPO]($REPO_URL)
commit [${GIT_COMMIT_SHA::7}]($REPO_URL/commit/$GIT_COMMIT_SHA)
pull request [#$PR_NUMBER]($REPO_URL/pull/$PR_NUMBER)"

/bin/bash $RESOURCES_PATH/bash/workflow/telegram_send_message.sh "${MESSAGE@E}"

if test $CODE -ne 0; then
  echo "dev failed!"
  exit 1
fi

echo "dev success"

exit 0
