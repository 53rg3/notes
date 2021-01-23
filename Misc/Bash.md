# Bash

Put this stuff into single cookbook items

```bash
#!/bin/bash
# set -x # for debug
set -e

##############
# TODO maybe add assertions for mandatory values: https://stackoverflow.com/a/13864829/4179212
##############

### DEFAULTS
NAME="Klaus"
EMAIL="no.mail.to.grab@here.com"

### HELPERS
function exit_with_error_msg {
    echo -e "\e[91m< $1\e[0m"
    exit 1
}

function confirm_with_yes {
    read -r -p "> Confirm with yes: " input
    if [[ ${input} != "yes" ]]; then
        exit_with_error_msg "Error, exiting. Got input: \"${input}\""
    fi
}

function info() {
    echo -e "\n\e[33m$1\e[0m"
}

function showKeyValue() {
  echo -e "$1: \e[36m$2\e[0m"
}

### SCRIPT

info "You're about to execute 'git init'"

read -rp "> Remote Repository (mandatory): " input
if [[ ${input} == "" ]]; then
    exit_with_error_msg "Remote Repository is mandatory, exiting."
fi
REPO=${input}

read -rp "> Author Name (default = ${NAME}): " input
if [[ ${input} != "" ]]; then
    NAME=${input}
fi

read -rp "> Author E-Mail (default = ${EMAIL}): " input
if [[ ${input} != "" ]]; then
    EMAIL=${input}
fi

showKeyValue "Repo" "${REPO}"
showKeyValue "Name" "${NAME}"
showKeyValue "E-Mail" "${EMAIL}"
info "Check settings. Confirm to continue."
confirm_with_yes

git init
git config user.name "${NAME}"
git config user.email "${EMAIL}"
git remote add origin "${REPO}"
info "Also check result of 'git config --list' before committing!"
git config --list | cat
```

