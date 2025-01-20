# author: Yuuji
# assignmentApp.py
# Latest Update : 05/13/2024

class Assignment:
    def __init__(self, student_name, student_mark, days_late):
        """
        Initialize the assignment with a
        student name, mark, and number of days late.
        """
        self.student_name = student_name
        self.student_mark = student_mark
        self.days_late = days_late

    @property
    def name(self):
        # A read-only property to get the student name
        return self.student_name

    @property
    def mark(self):
        # A read-only property to get the student mark
        return self.student_mark

    def adjusted_percentage(self):
        """
        To calculate the percentage of the assignment mark that is adjusted
        based on the number of days late.
        """
        return self.days_late * 5

    @property
    def adjusted_mark(self):
        """
        To calculate the adjusted mark based on the number of days late.
        - If the assignment is late, the adjusted mark is calculated by
          deducting 5% of the original mark for each day late.
        - If the assignment is not late, the adjusted mark is
          the same as the original mark.
        """
        if self.days_late > 0:
            adjusted_mark = max(0, self.student_mark -
                                (self.student_mark *
                                 self.adjusted_percentage() / 100))
            return round(adjusted_mark)
        else:
            return self.student_mark

    def __str__(self):
        """
        Define how to print the assignment object as a string.
        - If the assignment is late, the string includes the adjusted
          mark and the percentage deduction.
        - If the assignment is not late, the string only includes
          the original mark.
        """
        if self.days_late > 0:
            return (f"{self.student_name}: mark {self.adjusted_mark} "
                    f"(adjusted by {self.adjusted_percentage()}%)")
        else:
            return f"{self.student_name}: mark {self.student_mark}"


class AssignmentList:
    def __init__(self, subject_name):
        # To initialize the assignment list with a subject name
        self.subject_name = subject_name
        self.assignments = []

    @property
    def no_of_assignments(self):
        # A read-only property to get the number of assignments in the list
        return len(self.assignments)

    def add_assignment(self, assignment):
        # It will add an assignment to the list
        self.assignments.append(assignment)

    def all_assignments(self):
        # Return a string representation of all the assignments in the list
        return '\n'.join([str(assignment) for assignment
                          in self.assignments])

    def average_mark(self):
        """
        To calculate and return the average mark
        of all the assignments in the list
        """
        total_marks = sum([assignment.adjusted_mark for assignment
                           in self.assignments])
        return round(total_marks / self.no_of_assignments)

    def highest_mark_assignment(self):
        # Return the assignment with the highest adjusted mark
        return max(self.assignments, key=lambda x: x.adjusted_mark)

    def sorted_assignments(self, criteria):
        # Return a list of assignments sorted based on the given criteria
        if criteria == 'name':
            return sorted(self.assignments, key=lambda x: x.name)
        elif criteria == 'adjustedMark':
            return sorted(self.assignments,
                          key=lambda x: x.adjusted_mark, reverse=True)

    def save_to_file(self, filename):
        # Save the assignment list to a file
        with open(filename, 'w') as f:
            # Write subject name as the first line
            f.write(f"{self.subject_name}\n")
            for assignment in self.assignments:
                f.write(f"{assignment.name}, {assignment.adjusted_mark}, "
                        f"{assignment.days_late}\n")
                # To write each assignment's data in the format:
                # name, mark, days_late

    def load_from_file(self, filename):
        # Load the assignment list from a file

        # Clear existing assignments
        self.assignments = []
        with open(filename, 'r') as file:
            lines = file.readlines()
            # Read subject name from the first line
            self.subject_name = lines[0].strip()
            for line in lines[1:]:
                name, mark, days_late = line.strip().split(',')
                self.assignments.append(Assignment(name, int(mark),
                                                   int(days_late)))
                # To create Assignment objects and add them to the list
