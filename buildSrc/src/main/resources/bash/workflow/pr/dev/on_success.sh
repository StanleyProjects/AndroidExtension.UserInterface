#!/bin/bash

echo "on success pull request to dev start..."

AUTHOR_NAME="$(echo "$(<$DST_PATH/author.json)" | jq -r .name)"
if test -z "$AUTHOR_NAME"; then
 echo "Author name is empty!"
 exit 11
fi
AUTHOR_URL="$(echo "$(<$DST_PATH/author.json)" | jq -r .html_url)"
if test -z "$AUTHOR_URL"; then
 echo "Author url is empty!"
 exit 12
fi

REPO_URL=https://github.com/$GITHUB_OWNER/$GITHUB_REPO
MESSAGE="GitHub build [#$GITHUB_RUN_NUMBER]($REPO_URL/actions/runs/$GITHUB_RUN_ID)

[$GITHUB_OWNER](https://github.com/$GITHUB_OWNER) / [$GITHUB_REPO]($REPO_URL)

[${GIT_COMMIT_SHA::7}]($REPO_URL/commit/$GIT_COMMIT_SHA) by [$AUTHOR_NAME]($AUTHOR_URL)

pull request [#$PR_NUMBER]($REPO_URL/pull/$PR_NUMBER)"

/bin/bash $RESOURCES_PATH/bash/workflow/telegram_send_message.sh "${MESSAGE@E}"

exit 1 # todo

echo "pull request to dev success"

exit 0
