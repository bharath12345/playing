#!/bin/bash

# This script should NOT be checked-in to Git
# Running this script set the environment variables for the Heroku app 'bharathplays'

# To run it in the same shell, source it with the command -
#     $. ./exportvars.sh
# Running it in the same shell, without sourcing, will not lead to variable getting exported!

# Postgres Database Credentials

# Twitter Credentials
at="TWITTER_APP_TOKEN=262534519-OarcKBDjQMmZYGS9rU5CJSwdHAdiXs4eXJOoOCiA"
as="TWITTER_APP_SECRET=9MBU2hcAKHQWBtEPE1ihFlM3IlLjVwN8uhUFhuGMsiws3"
ck="TWITTER_CONSUMER_KEY=Adqs6idtfSjWUp1LOLB2g"
cs="TWITTER_CONSUMER_SECRET=OPQJ9RvKu3fwL8cirFJwMAHs5iof3aEaIy7hosCyI"

twitterkeys="$at $as $ck $cs"
export $twitterkeys
# echo $twitterkeys


#heroku config:set $twitterkeys --app bharathplays
