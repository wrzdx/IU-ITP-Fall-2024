#include <stdio.h>    // Standard I/O library for input/output operations
#include <string.h>   // String library for handling strings

// Defining constants for maximum limits
#define MAX_STUDENTS 1000      // Maximum number of students
#define MAX_STR_LEN 100        // Maximum length of a string (e.g., name, faculty)
#define MAX_EXAMS 500          // Maximum number of exams

// Enum for defining types of exams
typedef enum {
    WRITTEN,    // Written type of exam
    DIGITAL,    // Digital type of exam
    INVALID     // Invalid exam type, used as a default or error
} ExamType;

// Enum for defining different output messages
typedef enum {
    ADD_STUDENT_SUCCESS,          // Message for successful student addition
    EXISTING_STUDENT_ID_ERROR,    // Error message: student ID already exists
    INVALID_STUDENT_ID_ERROR,     // Error message: invalid student ID
    INVALID_NAME_ERROR,           // Error message: invalid student name
    INVALID_FACULTY_ERROR,        // Error message: invalid faculty name
    ADD_EXAM_SUCCESS,             // Message for successful exam addition
    EXISTING_EXAM_ID_ERROR,       // Error message: exam ID already exists
    INVALID_EXAM_ID_ERROR,        // Error message: invalid exam ID
    INVALID_EXAM_TYPE_ERROR,      // Error message: invalid exam type
    INVALID_DURATION_ERROR,       // Error message: invalid exam duration
    INVALID_SOFTWARE_ERROR,       // Error message: invalid software used for the exam
    ADD_GRADE_SUCCESS,            // Message for successful grade addition
    EXAM_NOT_FOUND_ERROR,         // Error message: exam not found
    STUDENT_NOT_FOUND_ERROR,      // Error message: student not found
    INVALID_GRADE_ERROR,          // Error message: invalid grade input
    SEARCH_STUDENT_SUCCESS,       // Message for successful student search
    SEARCH_GRADE_SUCCESS,         // Message for successful grade search
    UPDATE_EXAM_SUCCESS,          // Message for successful exam update
    UPDATE_GRADE_SUCCESS,         // Message for successful grade update
    DELETE_STUDENT_SUCCESS,       // Message for successful student deletion
    LIST_ALL_STUDENTS_FORMAT      // Format for listing all students
} OutputType;

// Enum for defining result status (success or error)
typedef enum {
    SUCCESS,    // Represents a successful operation
    ERROR       // Represents an error
} RESULT;

// Union for storing exam-specific information
typedef union {
    int duration;                    // Duration for a written exam (in minutes)
    char software[MAX_STR_LEN];      // Software required for a digital exam
} ExamInfo;

// Struct representing a student
typedef struct {
    int student_id;                   // Unique student ID
    char name[MAX_STR_LEN];           // Name of the student
    char faculty[MAX_STR_LEN];        // Faculty the student belongs to
} Student;

// Struct representing an exam
typedef struct {
    int exam_id;                      // Unique exam ID
    ExamType exam_type;               // Type of exam (written/digital)
    ExamInfo exam_info;               // Exam-specific information (duration or software)
} Exam;

// Struct representing a student's grade in an exam
typedef struct {
    int exam_id;                      // ID of the exam
    int student_id;                   // ID of the student
    int grade;                        // Grade the student received
} ExamGrade;

// Arrays for storing students, exams, and grades
Student students[MAX_STUDENTS] = {0};        // Array to store student records
int studentsOrder[MAX_STUDENTS] = {0};       // Array to maintain the order of students for easy access
Exam exams[MAX_EXAMS] = {0};                 // Array to store exam records
ExamGrade examGrades[MAX_EXAMS][MAX_STUDENTS] = {0}; // 2D array for storing grades (exam vs student)

// Counter for keeping track of the number of students
int studentCounter = 0;

void write_output(OutputType outputType, Student *student, Exam *exam, ExamGrade *grade) {
    // Open the file in append mode to add new output without overwriting existing data
    FILE *output = fopen("output.txt", "a");

    // Switch statement to handle different output types based on OutputType enum
    switch (outputType) {
        case EXISTING_STUDENT_ID_ERROR:
            // Output error message if the student ID already exists
            fprintf(output, "Student: %d already exists\n", student->student_id);
            break;
        case ADD_STUDENT_SUCCESS:
            // Output success message when a student is added
            fprintf(output, "Student: %d added\n", student->student_id);
            break;
        case INVALID_STUDENT_ID_ERROR:
            // Output error message for invalid student ID
            fprintf(output, "Invalid student id\n");
            break;
        case INVALID_NAME_ERROR:
            // Output error message for invalid name
            fprintf(output, "Invalid name\n");
            break;
        case INVALID_FACULTY_ERROR:
            // Output error message for invalid faculty
            fprintf(output, "Invalid faculty\n");
            break;
        case ADD_EXAM_SUCCESS:
            // Output success message when an exam is added
            fprintf(output, "Exam: %d added\n", exam->exam_id);
            break;
        case EXISTING_EXAM_ID_ERROR:
            // Output error message if the exam ID already exists
            fprintf(output, "Exam: %d already exists\n", exam->exam_id);
            break;
        case INVALID_EXAM_ID_ERROR:
            // Output error message for invalid exam ID
            fprintf(output, "Invalid exam id\n");
            break;
        case INVALID_EXAM_TYPE_ERROR:
            // Output error message for invalid exam type
            fprintf(output, "Invalid exam type\n");
            break;
        case INVALID_DURATION_ERROR:
            // Output error message for invalid exam duration (only for WRITTEN exam)
            fprintf(output, "Invalid duration\n");
            break;
        case INVALID_SOFTWARE_ERROR:
            // Output error message for invalid software (only for DIGITAL exam)
            fprintf(output, "Invalid software\n");
            break;
        case ADD_GRADE_SUCCESS:
            // Output success message when a grade is added
            fprintf(output, "Grade %d added for the student: %d\n",
                    grade->grade, grade->student_id);
            break;
        case EXAM_NOT_FOUND_ERROR:
            // Output error message if the exam is not found
            fprintf(output, "Exam not found\n");
            break;
        case STUDENT_NOT_FOUND_ERROR:
            // Output error message if the student is not found
            fprintf(output, "Student not found\n");
            break;
        case INVALID_GRADE_ERROR:
            // Output error message for invalid grade
            fprintf(output, "Invalid grade\n");
            break;
        case SEARCH_STUDENT_SUCCESS:
            // Output student details for successful search
            fprintf(output, "ID: %d, Name: %s, Faculty: %s\n",
                    student->student_id, students[student->student_id].name,
                    students[student->student_id].faculty);
            break;
        case SEARCH_GRADE_SUCCESS:
            // Output details of the student's grade and exam information
            fprintf(output, "Exam: %d, Student: %d, "
                            "Name: %s, Grade: %d, ",
                    grade->exam_id, grade->student_id, students[grade->student_id].name,
                    examGrades[grade->exam_id][grade->student_id].grade);

            // Output additional information based on the exam type (WRITTEN or DIGITAL)
            if (exams[grade->exam_id].exam_type == WRITTEN)
                fprintf(output, "Type: WRITTEN, Info: %d\n", exams[grade->exam_id].exam_info.duration);
            else
                fprintf(output, "Type: DIGITAL, Info: %s\n", exams[grade->exam_id].exam_info.software);

            break;
        case UPDATE_EXAM_SUCCESS:
            // Output success message when an exam is updated
            fprintf(output, "Exam: %d updated\n", exam->exam_id);
            break;
        case UPDATE_GRADE_SUCCESS:
            // Output success message when a grade is updated
            fprintf(output, "Grade %d updated for the student: %d\n",
                    grade->grade, grade->student_id);
            break;
        case DELETE_STUDENT_SUCCESS:
            // Output success message when a student is deleted
            fprintf(output, "Student: %d deleted\n", student->student_id);
            break;
        case LIST_ALL_STUDENTS_FORMAT:
            // Loop through studentsOrder array and output each student's details
            for (int i = 0; studentsOrder[i]; i++) {
                int student_id = students[studentsOrder[i]].student_id;
                char name[MAX_STR_LEN];
                char faculty[MAX_STR_LEN];
                // Copy name and faculty from the student array to local variables
                strcpy(name, students[studentsOrder[i]].name);
                strcpy(faculty, students[studentsOrder[i]].faculty);
                // Output student details
                fprintf(output, "ID: %d, Name: %s, Faculty: %s\n",
                        student_id, name, faculty);
            }
            break;
    }

    // Close the file after writing the output
    fclose(output);
}

// Function to check if a string contains only letters
int is_all_letters(const char *name) {
    while (*name) {
        // Check if the current character is not an uppercase or lowercase letter
        if ((*name < 'A' || *name > 'Z') && (*name < 'a' || *name > 'z'))
            return 0;  // Return 0 if a non-letter character is found
        name++;  // Move to the next character
    }
    return 1;  // Return 1 if all characters are letters
}

// Function to handle validation errors when adding a student
RESULT handle_add_student_errors(Student student) {
    // Check if student ID is out of range (should be between 1 and 999)
    if (student.student_id >= 1000 || student.student_id <= 0) {
        write_output(INVALID_STUDENT_ID_ERROR, &student, NULL, NULL);
        return ERROR;
    }
    // Check if the student ID already exists in the system
    if (students[student.student_id].student_id != 0) {
        write_output(EXISTING_STUDENT_ID_ERROR, &student, NULL, NULL);
        return ERROR;
    }
    // Validate student's name (length between 2 and 19, must contain only letters)
    if (strlen(student.name) >= 20 || strlen(student.name) <= 1 || is_all_letters(student.name) == 0) {
        write_output(INVALID_NAME_ERROR, &student, NULL, NULL);
        return ERROR;
    }
    // Validate student's faculty (length between 5 and 29, must contain only letters)
    if (strlen(student.faculty) >= 30 || strlen(student.faculty) <= 4 || is_all_letters(student.faculty) == 0) {
        write_output(INVALID_FACULTY_ERROR, &student, NULL, NULL);
        return ERROR;
    }

    return SUCCESS;  // Return SUCCESS if all checks pass
}

// Function to add a student by reading data from a file
void add_student(FILE *input) {
    Student student;
    // Read student ID, name, and faculty from input file
    fscanf(input, "%d %99s %99s", &student.student_id, student.name, student.faculty);
    // 99 is used because the maximum string length is 100 (including null terminator)

    // Handle possible errors related to adding a student
    RESULT result = handle_add_student_errors(student);
    if (result == ERROR)
        return;  // If there is an error, stop further execution

    // Add the student to the global students array
    students[student.student_id].student_id = student.student_id;
    strcpy(students[student.student_id].name, student.name);
    strcpy(students[student.student_id].faculty, student.faculty);

    // Output success message
    write_output(ADD_STUDENT_SUCCESS, &student, NULL, NULL);

    // Add the student to the studentsOrder array to maintain the order
    studentsOrder[studentCounter] = student.student_id;
    studentCounter++;  // Increment the student counter
}

// Function to determine the exam type based on the input string
ExamType handle_exam_type(const char *examType) {
    // Compare the input string with "WRITTEN"
    if (strcmp(examType, "WRITTEN") == 0)
        return WRITTEN;  // Return WRITTEN if the string matches

    // Compare the input string with "DIGITAL"
    if (strcmp(examType, "DIGITAL") == 0)
        return DIGITAL;  // Return DIGITAL if the string matches

    // Return INVALID if the input string does not match known exam types
    return INVALID;
}

// Function to handle validation errors when adding an exam
RESULT handle_add_exam_errors(Exam exam) {
    // Check if the exam ID already exists in the system
    if (exams[exam.exam_id].exam_id != 0) {
        write_output(EXISTING_EXAM_ID_ERROR, NULL, &exam, NULL);
        return ERROR;
    }
    // Check if the exam ID is within the valid range (1 to 499)
    if (exam.exam_id < 1 || exam.exam_id >= 500) {
        write_output(INVALID_EXAM_ID_ERROR, NULL, &exam, NULL);
        return ERROR;
    }
    // For WRITTEN exams, check if the duration is within the valid range (40 to 180 minutes)
    if (exam.exam_type == WRITTEN &&
    (exam.exam_info.duration < 40 || exam.exam_info.duration > 180)) {
        write_output(INVALID_DURATION_ERROR, NULL, &exam, NULL);
        return ERROR;
    }
    // For DIGITAL exams, validate the software name (length between 3 and 19, all letters)
    if (exam.exam_type == DIGITAL &&
    (strlen(exam.exam_info.software) <= 2 || strlen(exam.exam_info.software) >= 20 ||
    is_all_letters(exam.exam_info.software) == 0)) {
        write_output(INVALID_SOFTWARE_ERROR, NULL, &exam, NULL);
        return ERROR;
    }
    return SUCCESS;  // Return SUCCESS if all validations pass
}

// Function to add an exam by reading data from a file
void add_exam(FILE *input) {
    Exam exam;
    char undefinedExamType[MAX_STR_LEN];  // Temporary variable for exam type as a string

    // Read the exam ID and exam type from the input file
    fscanf(input, "%d %99s", &exam.exam_id, undefinedExamType);
    // 99 is used because the maximum string length is 100 (including null terminator)

    // Handle exam type based on the string provided
    ExamType examType = handle_exam_type(undefinedExamType);

    // If the exam type is WRITTEN, read the exam duration
    if (examType == WRITTEN) {
        exam.exam_type = WRITTEN;
        fscanf(input, "%d", &exam.exam_info.duration);
    }
    // If the exam type is DIGITAL, read the software name
    else if (examType == DIGITAL) {
        exam.exam_type = DIGITAL;
        fscanf(input, "%s", exam.exam_info.software);
    }
    // If the exam type is invalid, skip and write an error message
    else {
        fscanf(input, "%*s");  // Skip the invalid input
        write_output(INVALID_EXAM_TYPE_ERROR, NULL, &exam, NULL);
        return;
    }

    // Handle possible errors related to adding the exam
    RESULT result = handle_add_exam_errors(exam);
    if (result == ERROR)
        return;  // If there is an error, stop further execution

    // Add the exam to the global exams array
    exams[exam.exam_id].exam_id = exam.exam_id;
    exams[exam.exam_id].exam_type = exam.exam_type;

    // Copy exam-specific information based on exam type
    if (exam.exam_type == DIGITAL)
        exams[exam.exam_id].exam_info = exam.exam_info;  // Copy software info for DIGITAL exam
    else
        strcpy(exams[exam.exam_id].exam_info.software, exam.exam_info.software);  // Copy duration for WRITTEN exam

    // Output success message
    write_output(ADD_EXAM_SUCCESS, NULL, &exam, NULL);
}

// Function to handle validation errors for adding or updating a grade
RESULT handle_add_update_grade_errors(ExamGrade grade) {
    // Check if exam ID is within the valid range (1 to 499)
    if (grade.exam_id < 1 || grade.exam_id >= 500) {
        write_output(INVALID_EXAM_ID_ERROR, NULL, NULL, &grade);
        return ERROR;
    }
    // Check if the exam exists in the system
    if (exams[grade.exam_id].exam_id == 0) {
        write_output(EXAM_NOT_FOUND_ERROR, NULL, NULL, &grade);
        return ERROR;
    }
    // Check if student ID is within the valid range (1 to 999)
    if (grade.student_id < 1 || grade.student_id >= 1000) {
        write_output(INVALID_STUDENT_ID_ERROR, NULL, NULL, &grade);
        return ERROR;
    }
    // Check if the student exists in the system
    if (students[grade.student_id].student_id == 0) {
        write_output(STUDENT_NOT_FOUND_ERROR, NULL, NULL, &grade);
        return ERROR;
    }
    // Check if the grade is within the valid range (0 to 100)
    if (grade.grade < 0 || grade.grade > 100) {
        write_output(INVALID_GRADE_ERROR, NULL, NULL, &grade);
        return ERROR;
    }
    return SUCCESS;  // Return SUCCESS if all validations pass
}

// Function to add a grade by reading data from a file
void add_grade(FILE *input) {
    ExamGrade grade;
    // Read the exam ID, student ID, and grade from the input file
    fscanf(input, "%d %d %d", &grade.exam_id, &grade.student_id, &grade.grade);

    // Handle possible errors related to adding/updating the grade
    RESULT result = handle_add_update_grade_errors(grade);
    if (result == ERROR) return;  // If there is an error, stop further execution

    // Add the grade to the global examGrades array
    examGrades[grade.exam_id][grade.student_id].grade = grade.grade;
    examGrades[grade.exam_id][grade.student_id].exam_id = grade.exam_id;
    examGrades[grade.exam_id][grade.student_id].student_id = grade.student_id;

    // Output success message
    write_output(ADD_GRADE_SUCCESS, NULL, NULL, &grade);
}

// Function to handle validation errors when searching for a student
RESULT handle_search_student_errors(Student student) {
    // Check if student ID is within the valid range (1 to 999)
    if (student.student_id < 1 || student.student_id >= 1000) {
        write_output(INVALID_STUDENT_ID_ERROR, &student, NULL, NULL);
        return ERROR;
    }
    // Check if the student exists in the system
    if (students[student.student_id].student_id == 0) {
        write_output(STUDENT_NOT_FOUND_ERROR, &student, NULL, NULL);
        return ERROR;
    }
    return SUCCESS;  // Return SUCCESS if the student exists
}

// Function to search for a student by reading the student ID from a file
void search_student(FILE *input) {
    Student student;
    // Read the student ID from the input file
    fscanf(input, "%d", &student.student_id);

    // Handle possible errors related to searching for the student
    RESULT result = handle_search_student_errors(student);
    if (result == ERROR) return;  // If there is an error, stop further execution

    // Output the student's information upon successful search
    write_output(SEARCH_STUDENT_SUCCESS, &student, NULL, NULL);
}

// Function to handle validation errors when updating an exam
RESULT handle_update_exam_errors(Exam exam) {
    // Check if the exam ID is within the valid range (1 to 499)
    if (exam.exam_id < 1 || exam.exam_id >= 500) {
        write_output(INVALID_EXAM_ID_ERROR, NULL, &exam, NULL);
        return ERROR;
    }
    // For WRITTEN exams, check if the duration is within the valid range (40 to 180 minutes)
    if (exam.exam_type == WRITTEN &&
    (exam.exam_info.duration < 40 || exam.exam_info.duration > 180)) {
        write_output(INVALID_DURATION_ERROR, NULL, &exam, NULL);
        return ERROR;
    }
    // For DIGITAL exams, validate the software name (length between 3 and 19, must contain only letters)
    if (exam.exam_type == DIGITAL &&
    (strlen(exam.exam_info.software) <= 2 || strlen(exam.exam_info.software) >= 20 ||
    is_all_letters(exam.exam_info.software) == 0)) {
        write_output(INVALID_SOFTWARE_ERROR, NULL, &exam, NULL);
        return ERROR;
    }
    return SUCCESS;  // Return SUCCESS if all validations pass
}

// Function to update an exam by reading new data from a file
void update_exam(FILE *input) {
    Exam newExam;  // Variable to store the updated exam information
    char undefinedExamType[MAX_STR_LEN];  // Temporary variable for the exam type as a string

    // Read the exam ID and exam type from the input file
    fscanf(input, "%d %99s", &newExam.exam_id, undefinedExamType);
    // 99 is used because the maximum string length is 100 (including null terminator)

    // Handle the exam type based on the input string
    ExamType examType = handle_exam_type(undefinedExamType);

    // If the exam type is WRITTEN, read the exam duration
    if (examType == WRITTEN) {
        newExam.exam_type = WRITTEN;
        fscanf(input, "%d", &newExam.exam_info.duration);
    }
    // If the exam type is DIGITAL, read the software name
    else if (examType == DIGITAL) {
        newExam.exam_type = DIGITAL;
        fscanf(input, "%s", newExam.exam_info.software);
    }
    // If the exam type is invalid, skip and write an error message
    else {
        fscanf(input, "%*s");  // Skip the invalid input
        write_output(INVALID_EXAM_TYPE_ERROR, NULL, &newExam, NULL);
        return;
    }

    // Handle possible errors related to updating the exam
    RESULT result = handle_update_exam_errors(newExam);
    if (result == ERROR)
        return;  // If there is an error, stop further execution

    // Update the exam details in the global exams array
    exams[newExam.exam_id].exam_id = newExam.exam_id;
    exams[newExam.exam_id].exam_type = newExam.exam_type;

    // Update exam-specific information based on the exam type
    if (newExam.exam_type == DIGITAL)
        exams[newExam.exam_id].exam_info = newExam.exam_info;  // Update software info for DIGITAL exam
    else
        strcpy(exams[newExam.exam_id].exam_info.software, newExam.exam_info.software);  // Update duration for WRITTEN exam

    // Output success message
    write_output(UPDATE_EXAM_SUCCESS, NULL, &newExam, NULL);
}

// Function to update a student's grade by reading new data from a file
void update_grade(FILE *input) {
    ExamGrade newGrade;  // Variable to store the updated grade information

    // Read the exam ID, student ID, and grade from the input file
    fscanf(input, "%d %d %d", &newGrade.exam_id, &newGrade.student_id, &newGrade.grade);

    // Handle possible errors related to adding or updating the grade
    RESULT result = handle_add_update_grade_errors(newGrade);
    if (result == ERROR) return;  // If there is an error, stop further execution

    // Update the grade details in the global examGrades array
    examGrades[newGrade.exam_id][newGrade.student_id].grade = newGrade.grade;
    examGrades[newGrade.exam_id][newGrade.student_id].exam_id = newGrade.exam_id;
    examGrades[newGrade.exam_id][newGrade.student_id].student_id = newGrade.student_id;

    // Output success message
    write_output(UPDATE_GRADE_SUCCESS, NULL, NULL, &newGrade);
}

// Function to handle validation errors when searching for a grade
RESULT handle_search_grade_errors(ExamGrade grade) {
    // Check if exam ID is within the valid range (1 to 499)
    if (grade.exam_id < 1 || grade.exam_id >= 500) {
        write_output(INVALID_EXAM_ID_ERROR, NULL, NULL, &grade);
        return ERROR;
    }
    // Check if the exam exists in the system
    if (exams[grade.exam_id].exam_id == 0) {
        write_output(EXAM_NOT_FOUND_ERROR, NULL, NULL, &grade);
    }
    // Check if student ID is within the valid range (1 to 999)
    if (grade.student_id < 1 || grade.student_id >= 1000) {
        write_output(INVALID_STUDENT_ID_ERROR, NULL, NULL, &grade);
        return ERROR;
    }
    // Check if the student exists in the system
    if (students[grade.student_id].student_id == 0) {
        write_output(STUDENT_NOT_FOUND_ERROR, NULL, NULL, &grade);
        return ERROR;
    }
    return SUCCESS;  // Return SUCCESS if all validations pass
}

// Function to search for a student's grade by reading the exam and student ID from the file
void search_grade(FILE *input) {
    ExamGrade grade;

    // Read the exam ID and student ID from the input file
    fscanf(input, "%d %d", &grade.exam_id, &grade.student_id);

    // Handle possible errors related to searching for the grade
    RESULT result = handle_search_grade_errors(grade);
    if (result == ERROR) return;  // If there is an error, stop further execution

    // Output the grade search result
    write_output(SEARCH_GRADE_SUCCESS, NULL, NULL, &grade);
}

// Function to update the student order after a student has been deleted
void update_student_order(Student student) {
    int studentNumber = 0;

    // Find the position of the student in the studentsOrder array
    while (studentsOrder[studentNumber] != student.student_id)
        studentNumber++;

    // Shift all students after the deleted one forward in the array
    while (studentNumber < MAX_STUDENTS - 1) {
        studentsOrder[studentNumber] = studentsOrder[studentNumber + 1];
        studentNumber++;
    }

    // Set the last element to 0 after shifting
    studentsOrder[MAX_STUDENTS - 1] = 0;
    studentCounter--;  // Decrease the student counter
}

// Function to delete all grades associated with a student
void delete_student_grades(Student student) {
    // Loop through all exams and set the student's grades to 0 (clearing them)
    for (int examId = 0; examId < MAX_EXAMS; examId++)
        memset(&examGrades[examId][student.student_id], 0, sizeof(ExamGrade));
}

// Function to delete a student by their ID
void delete_student(FILE *input) {
    Student student;

    // Read the student ID from the input file
    fscanf(input, "%d", &student.student_id);

    // Clear the student's information in the students array
    memset(&students[student.student_id], 0, sizeof(Student));

    // Update the studentsOrder array to maintain order after deletion
    update_student_order(student);

    // Delete the grades associated with the student
    delete_student_grades(student);

    // Output success message
    write_output(DELETE_STUDENT_SUCCESS, &student, NULL, NULL);
}

// Function to handle different types of queries based on the input command
void handle_query(FILE *input, const char *word) {
    // Compare the input command to handle corresponding operations
    if (strcmp(word, "ADD_STUDENT") == 0)
        add_student(input);  // Call add_student if the command is "ADD_STUDENT"
    else if (strcmp(word, "ADD_EXAM") == 0)
        add_exam(input);  // Call add_exam if the command is "ADD_EXAM"
    else if (strcmp(word, "ADD_GRADE") == 0)
        add_grade(input);  // Call add_grade if the command is "ADD_GRADE"
    else if (strcmp(word, "UPDATE_EXAM") == 0)
        update_exam(input);  // Call update_exam if the command is "UPDATE_EXAM"
    else if (strcmp(word, "UPDATE_GRADE") == 0)
        update_grade(input);  // Call update_grade if the command is "UPDATE_GRADE"
    else if (strcmp(word, "SEARCH_GRADE") == 0)
        search_grade(input);  // Call search_grade if the command is "SEARCH_GRADE"
    else if (strcmp(word, "DELETE_STUDENT") == 0)
        delete_student(input);  // Call delete_student if the command is "DELETE_STUDENT"
    else if (strcmp(word, "SEARCH_STUDENT") == 0)
        search_student(input);  // Call search_student if the command is "SEARCH_STUDENT"
    else if (strcmp(word, "LIST_ALL_STUDENTS") == 0)
        write_output(LIST_ALL_STUDENTS_FORMAT, NULL, NULL, NULL);  // Output list of all students
}

// Main function to process the input and handle queries
int main() {
    // Open the input file for reading
    FILE *input = fopen("input.txt", "r");

    // Open the output file for writing, and immediately close to clear its contents
    FILE *output = fopen("output.txt", "w");
    fclose(output);

    char query[MAX_STR_LEN];  // Variable to store the command from the input

    // Continuously read commands from the input file
    while (fscanf(input, "%s", query) == 1) {
        // If the command is "END", exit the loop and stop reading further commands
        if (strcmp(query, "END") == 0)
            break;
        // Handle the current query using the handle_query function
        handle_query(input, query);
    }

    // Close the input file after processing all queries
    fclose(input);
    return 0;  // Exit the program
}


