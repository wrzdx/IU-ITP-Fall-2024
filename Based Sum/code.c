#include <stdio.h>

// Function to validate if the digits in the string are valid for the given base
int is_valid_for_base(char arr[], int base) {
    int i = 0;
    while (arr[i]) {
        char ch = arr[i];
        if (base == 2 && (ch != '0' && ch != '1')) return 0;  // Binary: only '0' or '1'
        if (base == 8 && (ch < '0' || ch > '7')) return 0;    // Octal: only '0'-'7'
        if (base == 10 && (ch < '0' || ch > '9')) return 0;   // Decimal: only '0'-'9'
        if (base == 16 && !((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'F'))) return 0;  // Hexadecimal: only '0'-'9', 'A'-'F'
        i++;
    }
    return 1;
}

// Function to convert a decimal string to an integer
int decimal(char arr[]) {
    int number = 0, i = 0;
    while (arr[i]) {
        int digit = arr[i] - '0';
        if (digit > 9 || digit < 0) return -1;  // If any non-digit is found, return error (-1)
        number = number * 10 + digit;
        i++;
    }
    return number;
}

// Function to convert a binary string to a decimal integer
int binary(char arr[]) {
    int number = 0, i = 0;
    while (arr[i]) {
        int digit = arr[i] - '0';
        if (digit > 1 || digit < 0) return -1;  // Binary should only have '0' or '1'
        number = number * 2 + digit;  // Convert binary to decimal
        i++;
    }
    return number;
}

// Function to convert an octal string to a decimal integer
int octal(char arr[]) {
    int number = 0, i = 0;
    while (arr[i]) {
        int digit = arr[i] - '0';
        if (digit > 7 || digit < 0) return -1;  // Octal should only have '0' to '7'
        number = number * 8 + digit;  // Convert octal to decimal
        i++;
    }
    return number;
}

// Function to convert a hexadecimal string to a decimal integer
int hexadecimal(char arr[]) {
    int number = 0, i = 0;
    while (arr[i]) {
        int digit;
        if (arr[i] >= '0' && arr[i] <= '9') {
            digit = arr[i] - '0';  // For digits '0'-'9'
        } else if (arr[i] >= 'A' && arr[i] <= 'F') {
            digit = arr[i] - 'A' + 10;  // For characters 'A'-'F'
        } else {
            return -1;  // Invalid character for hexadecimal
        }
        number = number * 16 + digit;  // Convert hexadecimal to decimal
        i++;
    }
    return number;
}

int main(void) {
    FILE *file = fopen("input.txt", "r");  // Open input file

    int N;
    fscanf(file, "%d", &N);  // Read the number of numbers

    if (N < 1 || N > 40) {  // Check if N is within the valid range [1, 40]
        FILE *output_file = fopen("output.txt", "w");
        fprintf(output_file, "Invalid inputs\n");  // Write error to the output file
        fclose(output_file);
        fclose(file);
        return 0;
    }

    char numbers[50][7];  // Array to store up to 50 numbers, each number has a max length of 6 digits
    char bases[50][3];    // Array to store the base of each number (2, 8, 10, 16)

    // Read numbers from the file
    for (int i = 0; i < N; i++) {
        fscanf(file, "%s", numbers[i]);
    }

    // Read bases from the file
    for (int i = 0; i < N; i++) {
        fscanf(file, "%s", bases[i]);
    }

    fclose(file);  // Close input file

    int sum = 0;  // Variable to store the final sum

    // Loop through each number and its corresponding base
    for (int i = 0; i < N; i++) {
        int base = decimal(bases[i]);  // Convert base string to an integer

        // Check if the base is valid (2, 8, 10, or 16)
        if (!(base == 2 || base == 8 || base == 10 || base == 16)) {
            FILE *output_file = fopen("output.txt", "w");
            fprintf(output_file, "Invalid inputs\n");  // Write error to the output file
            fclose(output_file);
            return 0;
        }

        // Check if the number is valid for its base
        if (!is_valid_for_base(numbers[i], base)) {
            FILE *output_file = fopen("output.txt", "w");
            fprintf(output_file, "Invalid inputs\n");  // Write error to the output file
            fclose(output_file);
            return 0;
        }

        int number;
        // Convert the number to decimal based on its base
        switch (base) {
            case 2:
                number = binary(numbers[i]);  // Convert binary to decimal
                break;
            case 8:
                number = octal(numbers[i]);   // Convert octal to decimal
                break;
            case 10:
                number = decimal(numbers[i]); // Convert decimal to decimal
                break;
            case 16:
                number = hexadecimal(numbers[i]);  // Convert hexadecimal to decimal
                break;
            default:
                number = -1;
        }

        if (number == -1) {
            FILE *output_file = fopen("output.txt", "w");
            fprintf(output_file, "Invalid inputs\n");  // Write error if conversion fails
            fclose(output_file);
            return 0;
        }

        // Apply arithmetic operations based on the index (even or odd)
        if (i % 2 == 0) {
            number -= 10;  // For even indices: subtract 10
        } else {
            number += 10;  // For odd indices: add 10
        }

        sum += number;  // Add the result to the final sum
    }

    // Write the final sum to the output file
    FILE *output_file = fopen("output.txt", "w");
    fprintf(output_file, "%d\n", sum);  // Output the sum with a newline character
    fclose(output_file);

    return 0;  // Program executed successfully
}