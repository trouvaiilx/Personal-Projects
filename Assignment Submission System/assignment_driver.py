# author: Yuuji
# assignmentDriver.py
# Latest Update : 05/13/2024

from assignment_app import Assignment, AssignmentList


def display_menu():
    """
    Displays the main menu of the program.
    It will return the user's input choice.
    """
    header = "\nClass Marks"
    dashes = "-" * len(header)
    print(header.center(40))
    print(dashes.center(40, "-"))
    print("1 Add students and marks")
    print("2 Display all assignments")
    print("3 Display summaries of all assignments")
    print("4 Save data to file")
    print("5 Load data from file")
    print("0 End the program")
    choice = input("Your choice? ")
    return choice


def add_assignment(assignment_list):
    """
    Prompts the user to input student name, mark, and days late,
    then adds a new assignment to the assignment list
    """
    name = input("Enter student name: ")
    mark = None
    while mark is None:
        try:
            # Getting the mark from the user and validate it
            mark = int(input("Enter mark (0-100): "))
            if not (0 <= mark <= 100):
                raise ValueError
        except ValueError:
            print("Invalid mark! "
                  "Please enter a valid integer between 0 and 100.")
            mark = None

    days_late = None
    while days_late is None:
        try:
            # Getting the days late from the user and validate it
            days_late = int(input("Enter days late (0-14): "))
            if not (0 <= days_late <= 14):
                raise ValueError
        except ValueError:
            print("Invalid days late! "
                  "Please enter a valid integer between 0 and 14.")
            days_late = None

    # To create a new Assignment object and add it to the assignment list
    assignment_list.add_assignment(Assignment(name, mark, days_late))


def display_assignments(assignment_list):
    # For displaying assignments based on a given sorting criteria
    criteria = input("Enter criteria for displaying assignments "
                     "(original/name/mark): ")
    if criteria == 'original':
        print(assignment_list.all_assignments())
    elif criteria == 'name':
        # To sort assignments by their name and display them
        for assignment in assignment_list.sorted_assignments('name'):
            print(assignment)
    elif criteria == 'mark':
        # To sort assignments by their adjusted mark and display them
        for assignment in (
                assignment_list.sorted_assignments('adjustedMark')):
            print(assignment)


def display_summaries(assignment_list):
    # To display summary statistics of the assignments
    print(f"Number of submitted assignments: "
          f"{assignment_list.no_of_assignments}")
    print(f"Average mark: {assignment_list.average_mark()}")
    print(
        f"Number of assignments submitted late: "
        f"{sum(1 for assignment in assignment_list.assignments 
               if assignment.days_late > 0)}"
    )
    highest_mark_assignment = assignment_list.highest_mark_assignment()
    print(f"Details of student with highest mark assignment: "
          f"{highest_mark_assignment}")


def save_to_file(assignment_list):
    """
    Prompts the user to input a filename and saves
    the assignment data to that file.
    """
    filename = input("Enter filename to save data: ")
    assignment_list.save_to_file(filename)
    print("Data saved to file.")


def load_from_file():
    """
    Prompts the user to input a filename, loads assignment data
    from the file, and returns an AssignmentList object
    containing the loaded data.
    """
    filename = input("Enter filename to load data from: ")
    assignment_list = AssignmentList("")
    assignment_list.load_from_file(filename)
    return assignment_list


def main():
    # The main function to execute the program.
    assignment_list = AssignmentList("BDA100")

    while True:
        choice = display_menu()

        if choice == '1':
            add_assignment(assignment_list)
        elif choice == '2':
            display_assignments(assignment_list)
        elif choice == '3':
            display_summaries(assignment_list)
        elif choice == '4':
            save_to_file(assignment_list)
        elif choice == '5':
            assignment_list = load_from_file()
            print("Data loaded from file.")
        elif choice == '0':
            print("End of the program.")
            break
        else:
            print("Invalid choice! Please try again.")


main()
