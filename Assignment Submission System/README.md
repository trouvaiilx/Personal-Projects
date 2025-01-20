# Assignment Submission Program

## Overview
The Assignment Program is a Python program designed to manage student assignments, allowing users to add assignments, display them, calculate adjusted marks based on lateness, and save or load assignment data from files. The program is structured with two main components: the `assignment_app.py` module, which contains the core classes and logic, and the `assignment_driver.py` module, which provides a user interface for interaction.

## Features
- **Add Assignments**: Users can input student names, marks, and the number of days an assignment is late.
- **Display Assignments**: View all assignments with options to sort by name or adjusted mark.
- **Summarize Data**: Get statistics such as the number of assignments, average mark, and details of the highest mark assignment.
- **File Operations**: Save the current assignment list to a file and load assignments from a file.

## Classes
### `Assignment`
- Represents a single assignment with attributes for the student's name, mark, and days late.
- Methods:
  - `adjusted_percentage()`: Calculates the percentage deduction based on days late.
  - `adjusted_mark`: Property that returns the adjusted mark after applying deductions.
  - `__str__()`: Returns a string representation of the assignment.

### `AssignmentList`
- Manages a collection of `Assignment` objects.
- Methods:
  - `add_assignment(assignment)`: Adds a new assignment to the list.
  - `all_assignments()`: Returns a string representation of all assignments.
  - `average_mark()`: Calculates the average mark of all assignments.
  - `highest_mark_assignment()`: Returns the assignment with the highest adjusted mark.
  - `sorted_assignments(criteria)`: Returns assignments sorted by specified criteria.
  - `save_to_file(filename)`: Saves the assignment list to a specified file.
  - `load_from_file(filename)`: Loads assignments from a specified file.

## Usage
1. Run the `assignment_driver.py` script to start the program.
2. Follow the on-screen menu to add assignments, display them, view summaries, or save/load data.
3. Input the required information as prompted.

## Requirements
- Python 3.x or later
- No external libraries are required.

## Author
- Yuuji

## Latest Update
- May 13th 2024

## License
This project is licensed under the MIT License - see the [LICENSE](https://github.com/trouvaiilx/Personal-Projects/blob/main/LICENSE) file for details.