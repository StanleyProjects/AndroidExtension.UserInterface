#!/bin/bash

echo "Telegram tries send a message..."

if test $# -ne 1; then
  echo "Script needs for 1 argument but actual $#"
  exit 11
fi

TELEGRAM_BOT_ID=${telegram_bot_id:?"Variable \"telegram_bot_id\" is empty!"}
TELEGRAM_BOT_TOKEN=${telegram_bot_token:?"Variable \"telegram_bot_token\" is empty!"}
TELEGRAM_CHAT_ID=${telegram_chat_id:?"Variable \"telegram_chat_id\" is empty!"}
MESSAGE=$1

if test -z "$MESSAGE"; then
  echo "Message is empty!"
  exit 12
fi

url="https://api.telegram.org/bot$TELEGRAM_BOT_ID:$TELEGRAM_BOT_TOKEN/sendMessage"

MESSAGE=${MESSAGE//"#"/"%23"}
MESSAGE=${MESSAGE//$'\n'/"%0A"}
MESSAGE=${MESSAGE//$'\r'/""}

CODE=0

CODE=$(curl -w '%{http_code}\n' -G \
  -o /dev/null \
  -s $url \
  -d chat_id=$TELEGRAM_CHAT_ID \
  -d text="$MESSAGE" \
  -d parse_mode=markdown)

if test $CODE -ne 200; then
    echo "Request error with response code $CODE!"
  exit 21
fi

echo "The message sent by telegram bot."

exit 0
