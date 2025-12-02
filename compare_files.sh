#!/bin/bash

file1=""
file2=""
result=""

# Function to check if file exists
check_file() {
    local file=$1
    while [ ! -f "$file" ]; do
        echo "Error: File '$file' does not exist. Please try again."
        read -p "Enter file path: " file
    done
    echo "$file"
}

# Function to count lines with "Hello"
count_hello() {
    grep -c "Hello" "$1" 2>/dev/null || echo 0
}

while true; do
    echo ""
    echo "===== MENU ====="
    echo "1. Input File 1"
    echo "2. Input File 2"
    echo "3. Determine which file has more lines with word \"Hello\""
    echo "4. Display result"
    echo "5. Exit"
    echo "================"
    read -p "Select an option (1-5): " choice
    
    case $choice in
        1)
            read -p "Enter path for File 1: " input_file
            file1=$(check_file "$input_file")
            echo "File 1 set to: $file1"
            ;;
        2)
            read -p "Enter path for File 2: " input_file
            file2=$(check_file "$input_file")
            echo "File 2 set to: $file2"
            ;;
        3)
            if [ -z "$file1" ] || [ -z "$file2" ]; then
                echo "Error: Please input both files first (options 1 and 2)."
            else
                count1=$(count_hello "$file1")
                count2=$(count_hello "$file2")
                
                echo "File 1 ($file1) has $count1 lines with 'Hello'"
                echo "File 2 ($file2) has $count2 lines with 'Hello'"
                
                if [ "$count1" -gt "$count2" ]; then
                    result="File 1 ($file1) has more lines with 'Hello' ($count1 vs $count2)"
                elif [ "$count2" -gt "$count1" ]; then
                    result="File 2 ($file2) has more lines with 'Hello' ($count2 vs $count1)"
                else
                    result="Both files have the same number of lines with 'Hello' ($count1)"
                fi
                echo "Analysis complete!"
            fi
            ;;
        4)
            if [ -z "$result" ]; then
                echo "No result available. Please run option 3 first."
            else
                echo ""
                echo "===== RESULT ====="
                echo "$result"
                echo "=================="
            fi
            ;;
        5)
            echo "Exiting program. Goodbye!"
            exit 0
            ;;
        *)
            echo "Invalid option. Please select 1-5."
            ;;
    esac
done

