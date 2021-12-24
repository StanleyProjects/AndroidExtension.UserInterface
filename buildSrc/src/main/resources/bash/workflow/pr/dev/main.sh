#!/bin/bash

echo "pull request to dev start..."

CODE=0

REPO_URL=https://github.com/$GITHUB_OWNER/$GITHUB_REPO
RUN_URL="$REPO_URL/actions/runs/$GITHUB_RUN_ID"
COMMENT_BODY="First line [#$GITHUB_RUN_NUMBER]($RUN_URL)"
POSTFIX=" - second line"
COMMENT_BODY="${COMMENT_BODY}:"$'\n'"$POSTFIX"
/bin/bash $RESOURCES_PATH/bash/workflow/vcs/post_comment.sh "$COMMENT_BODY" || exit 1 # todo
exit 1 # todo

/bin/bash $RESOURCES_PATH/bash/workflow/verify.sh; CODE=$?
if test $CODE -ne 0; then
 /bin/bash $RESOURCES_PATH/bash/workflow/diagnostics.sh && \
  /bin/bash $RESOURCES_PATH/bash/workflow/vcs/report.sh && \
  /bin/bash $RESOURCES_PATH/bash/workflow/pr/dev/on_failed.sh || exit 1 # todo
 exit 11
fi

ARRAY=(assemble/common assemble/documentation pr/dev/vcs)
SIZE=${#ARRAY[*]}
for ((i=0; i<SIZE; i++)); do
 it="${ARRAY[i]}"
 /bin/bash $RESOURCES_PATH/bash/workflow/${it}.sh; CODE=$?
 if test $CODE -ne 0; then
  echo "Task $it failed!"
  /bin/bash $RESOURCES_PATH/bash/workflow/pr/dev/on_failed.sh || exit 1 # todo
  exit $((100+i))
 fi
done

/bin/bash $RESOURCES_PATH/bash/workflow/pr/dev/on_success.sh || exit 1 # todo

echo "The pull request #$PR_NUMBER finish."

exit 0
