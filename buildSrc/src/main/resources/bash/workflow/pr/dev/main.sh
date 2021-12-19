#!/bin/bash

echo "pull request to dev start..."

CODE=0

ARRAY=(assemble/vcs prepare verify assemble/common assemble/documentation pr/dev/vcs)
SIZE=${#ARRAY[*]}
for ((i=0; i<SIZE; i++)); do
 it="${ARRAY[i]}"
 /bin/bash $RESOURCES_PATH/bash/workflow/${it}.sh; CODE=$?
 if test $CODE -ne 0; then
  echo "Task $it failed!"
  /bin/bash $RESOURCES_PATH/bash/workflow/pr/dev/on_failed.sh
  exit $((100+i))
 fi
done
# todo merge pr

/bin/bash $RESOURCES_PATH/bash/workflow/pr/dev/on_success.sh || exit 1 # todo

echo "The pull request #$PR_NUMBER finish."

exit 0
