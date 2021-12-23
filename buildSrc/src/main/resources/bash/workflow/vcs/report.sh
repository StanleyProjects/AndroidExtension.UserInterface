#!/bin/bash

echo "vcs diagnostics report start..."

CODE=0

REPORT_TYPE="$(cat ${ASSEMBLY_PATH}/diagnostics/summary.json | jq -r .type)"
WORKER_NAME="$(cat ${ASSEMBLY_PATH}/vcs/worker.json | jq -r .name)"
WORKER_EMAIL="$(cat ${ASSEMBLY_PATH}/vcs/worker.json | jq -r .email)"

for it in REPORT_TYPE WORKER_NAME WORKER_EMAIL; do
 if test -z "${!it}"; then echo "$it is empty!"; exit 21; fi; done

/bin/bash $RESOURCES_PATH/bash/util/check_variables.sh \
 GITHUB_PAT GITHUB_OWNER GITHUB_REPO GITHUB_RUN_NUMBER GITHUB_RUN_ID || exit 1 # todo

REPOSITORY="$HOME/diagnostics/report"
mkdir -p $REPOSITORY || exit 1 # todo
git clone --depth=1 --branch=gh-pages \
 https://$GITHUB_PAT@github.com/$GITHUB_OWNER/$GITHUB_REPO.git $REPOSITORY || exit 1 # todo

RELATIVE_PATH="build/$GITHUB_RUN_NUMBER/$GITHUB_RUN_ID/diagnostics/report"
mkdir -p $REPOSITORY/$RELATIVE_PATH || exit 1 # todo

COMMIT_MESSAGE="GitHub build #$GITHUB_RUN_NUMBER | $WORKER_NAME added diagnostics report"

case "$REPORT_TYPE" in
 "CODE_STYLE")
  cp -r ${ASSEMBLY_PATH}/diagnostics/* $REPOSITORY/$RELATIVE_PATH || exit 1 # todo
  COMMIT_MESSAGE="${COMMIT_MESSAGE}. Code style issues."
 ;;
 *) echo "Report type \"$REPORT_TYPE\" is not supported!"; exit 51;;
esac

git -C $REPOSITORY config user.name "$WORKER_NAME" && \
 git -C $REPOSITORY config user.email "$WORKER_EMAIL" && \
 git -C $REPOSITORY add --all $REPOSITORY && \
 git -C $REPOSITORY commit -m "$COMMIT_MESSAGE" && \
 git -C $REPOSITORY tag "diagnostics/report/$GITHUB_RUN_NUMBER/$GITHUB_RUN_ID" && \
 git -C $REPOSITORY push -f && \
 git -C $REPOSITORY push --tag; CODE=$?
if test $CODE -ne 0; then
 echo "Git push failed!"
 exit 41
fi

echo "vcs diagnostics report success"

exit 0
