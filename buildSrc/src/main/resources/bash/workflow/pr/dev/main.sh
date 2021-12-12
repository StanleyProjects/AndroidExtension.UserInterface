#!/bin/bash

echo "pull request to dev start..."

CODE=0

/bin/bash $RESOURCES_PATH/bash/workflow/prepare.sh && \
 /bin/bash $RESOURCES_PATH/bash/workflow/verify.sh && \
 /bin/bash $RESOURCES_PATH/bash/workflow/assemble/common.sh && \
 /bin/bash $RESOURCES_PATH/bash/workflow/assemble/documentation.sh && \
 /bin/bash $RESOURCES_PATH/bash/workflow/assemble/vcs.sh && \
 /bin/bash $RESOURCES_PATH/bash/workflow/pr/dev/vcs.sh CODE=$?
# todo merge pr

if test $CODE -ne 0; then
  echo "pr dev failed!"
  exit 1 # todo
fi

/bin/bash $RESOURCES_PATH/bash/workflow/pr/dev/on_success.sh || exit 1 # todo

echo "The pull request #$PR_NUMBER finish."

exit 0
