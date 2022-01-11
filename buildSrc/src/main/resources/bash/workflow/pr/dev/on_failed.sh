#!/bin/bash

echo "on failed pull request to dev start..."

/bin/bash $RESOURCES_PATH/bash/workflow/vcs/close_pr.sh || exit 1 # todo

/bin/bash $RESOURCES_PATH/bash/util/check_variables.sh \
 ASSEMBLY_PATH GIT_COMMIT_SRC GITHUB_OWNER GITHUB_REPO GITHUB_RUN_NUMBER GITHUB_RUN_ID || exit 1 # todo

AUTHOR_NAME_SRC="$(cat ${ASSEMBLY_PATH}/vcs/author.src.json | jq -r .name)"
AUTHOR_URL_SRC="$(cat ${ASSEMBLY_PATH}/vcs/author.src.json | jq -r .html_url)"
WORKER_NAME="$(cat ${ASSEMBLY_PATH}/vcs/worker.json | jq -r .name)"
WORKER_URL="$(cat ${ASSEMBLY_PATH}/vcs/worker.json | jq -r .html_url)"
for it in AUTHOR_NAME_SRC AUTHOR_URL_SRC WORKER_NAME WORKER_URL; do
 if test -z "${!it}"; then echo "$it is empty!"; exit 21; fi; done

REPO_URL=https://github.com/$GITHUB_OWNER/$GITHUB_REPO
RUN_URL="$REPO_URL/actions/runs/$GITHUB_RUN_ID"
COMMENT_BODY="Closed by GitHub build [#$GITHUB_RUN_NUMBER]($RUN_URL)"
PR_RESULT="The pull request [#$PR_NUMBER]($REPO_URL/pull/$PR_NUMBER) closed by [$WORKER_NAME]($WORKER_URL)
 - source [${GIT_COMMIT_SRC::7}]($REPO_URL/commit/$GIT_COMMIT_SRC) by [$AUTHOR_NAME_SRC]($AUTHOR_URL_SRC)"

if test -f "$ASSEMBLY_PATH/diagnostics/summary.json"; then
 REPORT_TYPE="$(cat ${ASSEMBLY_PATH}/diagnostics/summary.json | jq -r .type)"
 if test -z "$REPORT_TYPE"; then echo "Report type is empty!"; exit 101; fi
 GITHUB_PAGES="https://${GITHUB_OWNER}.github.io/$GITHUB_REPO"
 RELATIVE_PATH="build/$GITHUB_RUN_NUMBER/$GITHUB_RUN_ID/diagnostics/report"
 case "$REPORT_TYPE" in
  "CODE_STYLE")
   POSTFIX="see the code style issues [report]($GITHUB_PAGES/$RELATIVE_PATH/$REPORT_TYPE/index.html)";;
  "DOCUMENTATION")
   POSTFIX="see the documentation issues [report]($GITHUB_PAGES/$RELATIVE_PATH/$REPORT_TYPE/index.html)";;
  "UNIT_TEST")
   POSTFIX="see the unit test issues [report]($GITHUB_PAGES/$RELATIVE_PATH/$REPORT_TYPE/index.html)";;
  "COVERAGE_VERIFICATION")
   POSTFIX="see the coverage verification issues [report]($GITHUB_PAGES/$RELATIVE_PATH/$REPORT_TYPE/index.html)";;
  "README")
   POSTFIX="see the readme issues [report]($GITHUB_PAGES/$RELATIVE_PATH/$REPORT_TYPE/index.html)";;
  *) echo "Report type \"$REPORT_TYPE\" is not supported!"; exit 103;;
 esac
 COMMENT_BODY="${COMMENT_BODY}"$'\n'"- $POSTFIX"
 PR_RESULT="${PR_RESULT}"$'\n'" - $POSTFIX"
else
 COMMENT_BODY="${COMMENT_BODY}."
fi

/bin/bash $RESOURCES_PATH/bash/workflow/vcs/post_comment.sh "$COMMENT_BODY" || exit 1 # todo

MESSAGE="GitHub build [#$GITHUB_RUN_NUMBER]($REPO_URL/actions/runs/$GITHUB_RUN_ID) failed!

[$GITHUB_OWNER](https://github.com/$GITHUB_OWNER) / [$GITHUB_REPO]($REPO_URL)

$PR_RESULT"

/bin/bash $RESOURCES_PATH/bash/workflow/telegram_send_message.sh "$MESSAGE"

echo "The pull request #$PR_NUMBER failed!"

exit 0
