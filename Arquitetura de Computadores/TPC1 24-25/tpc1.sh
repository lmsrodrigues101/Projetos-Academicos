#!/bin/bash

text_count=0
binary_count=0

countTypeFiles() {
    directory="$1"
    for file_in in "$directory"/*; do
        if [[ -d "$file_in" ]]; then
            countTypeFiles "$file_in"
        elif [[ -f "$file_in" ]]; then
            ./isText "$file_in"
            result=$?
            if [[ $result -eq 1 ]]; then
                ((text_count++))
            elif [[ $result -eq 0 ]]; then
                ((binary_count++))
            fi
        fi
    done
}

directory="$1"

countTypeFiles "$directory"

echo "number of binary files: $binary_count"
echo "number of text files: $text_count"
