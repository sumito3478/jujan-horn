#!/usr/bin/env bash
set -ux

datetime=$(TZ=UTC date '+%y%m%d.%H%M')

sed -i -e\
  's/\(version = '"'"'[0-9]*.[0-9]*.[0-9]*-[a-zA-Z]*\).*'"'"'/\1.'"$datetime'"'/g'\
  build.gradle

git add build.gradle
git commit

# vim: set ts=2 sw=2 et:
