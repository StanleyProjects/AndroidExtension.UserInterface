#!/bin/bash

echo "on success pull request to dev start..."

AUTHOR_NAME="$(cat ${ASSEMBLY_PATH}/vcs/author.json | jq -r .name)"
AUTHOR_URL="$(cat ${ASSEMBLY_PATH}/vcs/author.json | jq -r .html_url)"
WORKER_NAME="$(cat ${ASSEMBLY_PATH}/vcs/worker.json | jq -r .name)"
WORKER_URL="$(cat ${ASSEMBLY_PATH}/vcs/worker.json | jq -r .html_url)"
VERSION="$(cat ${ASSEMBLY_PATH}/common.json | jq -r .version)"
RESULT_COMMIT_SHA="$(git rev-parse head)"

for it in AUTHOR_NAME AUTHOR_URL WORKER_NAME WORKER_URL VERSION RESULT_COMMIT_SHA; do
 if test -z "${!it}"; then echo "$it is empty!"; exit 11; fi; done

#/bin/bash $RESOURCES_PATH/bash/workflow/vcs/merge_pr.sh \
# "Merge ${RESULT_COMMIT_SHA::7} -> $PR_SOURCE_BRANCH by $WORKER_NAME." \
# "Intermediate $VERSION." || exit 1 # todo
# todo check merge
# todo post comment

REPO_URL=https://github.com/$GITHUB_OWNER/$GITHUB_REPO
GITHUB_PAGES="https://${GITHUB_OWNER}.github.io/$GITHUB_REPO"
MESSAGE="GitHub build [#$GITHUB_RUN_NUMBER]($REPO_URL/actions/runs/$GITHUB_RUN_ID)

[$GITHUB_OWNER](https://github.com/$GITHUB_OWNER) / [$GITHUB_REPO]($REPO_URL)

[${RESULT_COMMIT_SHA::7}]($REPO_URL/commit/$RESULT_COMMIT_SHA) by [$AUTHOR_NAME]($AUTHOR_URL)

The pull request [#$PR_NUMBER]($REPO_URL/pull/$PR_NUMBER) merged by [$WORKER_NAME]($WORKER_URL)
 - intermediate [$VERSION]($GITHUB_PAGES/documentation/$VERSION)"

/bin/bash $RESOURCES_PATH/bash/workflow/telegram_send_message.sh "$MESSAGE"

echo "The pull request #$PR_NUMBER merged."

exit 0
