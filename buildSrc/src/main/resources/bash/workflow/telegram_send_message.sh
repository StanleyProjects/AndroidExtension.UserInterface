#!/bin/bash

echo "Telegram tries send a message..."

if test $# -ne 1; then
 echo "Script needs for 1 argument but actual $#"
 exit 11
fi

if test -z "$telegram_bot_id"; then
 echo "Variable \"telegram_bot_id\" is empty!"
 exit 21
fi

if test -z "$telegram_bot_token"; then
 echo "Variable \"telegram_bot_token\" is empty!"
 exit 22
fi

if test -z "$telegram_chat_id"; then
 echo "Variable \"telegram_chat_id\" is empty!"
 exit 23
fi

MESSAGE=$1

if test -z "$MESSAGE"; then
 echo "Message is empty!"
 exit 24
fi

url="https://api.telegram.org/bot$telegram_bot_id:$telegram_bot_token/sendMessage"

MESSAGE=${MESSAGE//"#"/"%23"}
MESSAGE=${MESSAGE//$'\n'/"%0A"}
MESSAGE=${MESSAGE//$'\r'/""}

CODE=0

CODE=$(curl -w '%{http_code}\n' -G \
 -o /dev/null \
 -s $url \
 -d chat_id=$telegram_chat_id \
 -d text="$MESSAGE" \
 -d parse_mode=markdown)

if test $CODE -ne 200; then
 echo "Request error with response code $CODE!"
 exit 41
fi

echo "The message sent by telegram bot."

exit 0
