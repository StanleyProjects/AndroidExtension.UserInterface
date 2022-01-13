#!/bin/bash

echo "Telegram tries send a message..."

if test $# -ne 1; then
 echo "Script needs for 1 argument but actual $#"; exit 11
fi

MESSAGE=$1

if test -z "$MESSAGE"; then
 echo "Message is empty!"; exit 12
fi

/bin/bash $RESOURCES_PATH/bash/util/check_variables.sh \
 TELEGRAM_BOT_ID TELEGRAM_BOT_TOKEN TELEGRAM_CHAT_ID || exit 1 # todo

MESSAGE=${MESSAGE//"#"/"%23"}
MESSAGE=${MESSAGE//$'\n'/"%0A"}
MESSAGE=${MESSAGE//$'\r'/""}

URL="https://api.telegram.org/bot$TELEGRAM_BOT_ID:$TELEGRAM_BOT_TOKEN/sendMessage"

CODE=$(curl -w %{http_code} -G -o /dev/null $URL \
 -d chat_id=$TELEGRAM_CHAT_ID \
 -d text="$MESSAGE" \
 -d parse_mode=markdown)

if test $CODE -ne 200; then
 echo "Request error with response code $CODE!"; exit 21
fi

echo "The message sent by telegram bot."

exit 0
