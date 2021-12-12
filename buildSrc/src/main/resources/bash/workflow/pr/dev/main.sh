#!/bin/bash

echo "pull request to dev start..."

CODE=0

for it in (prepare verify assemble/common assemble/documentation assemble/vcs pr/dev/vcs); do
 /bin/bash $RESOURCES_PATH/bash/workflow/${it}.sh; CODE=$?
 if test $CODE -ne 0; then
  echo "Task $it failed!"; exit 1 # todo
 fi
done
# todo merge pr

/bin/bash $RESOURCES_PATH/bash/workflow/pr/dev/on_success.sh || exit 1 # todo

echo "The pull request #$PR_NUMBER finish."

exit 0
