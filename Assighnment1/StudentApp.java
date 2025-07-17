import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class StudentApp {
    static final int NUM_SUBJECTS = 3;
    static final int PASS_THRESHOLD = 40;

    static String[] subjectNames = {"Math", "Physics", "Chemistry"};
    static ArrayList<Student> students = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    static class Student {
        String name;
        int age;
        int[] marks = new int[NUM_SUBJECTS];

        Student(String name, int age) {
            this.name = name;
            this.age = age;
            for (int i = 0; i < NUM_SUBJECTS; i++) marks[i] = -1;
        }

        double getAverage() {
            int sum = 0, count = 0;
            for (int mark : marks) {
                if (mark != -1) { sum += mark; count++; }
            }
            return count == 0 ? 0 : (double) sum / count;
        }

        boolean isOverallPassed() {
            if (getAverage() < PASS_THRESHOLD) return false;
            for (int mark : marks) {
                if (mark != -1 && mark < PASS_THRESHOLD) return false;
            }
            return true;
        }

        String getReport() {
            StringBuilder sb = new StringBuilder();
            sb.append("--- Student Report ---\n");
            sb.append("Name: ").append(name).append(", Age: ").append(age).append("\n");
            sb.append("Marks:\n");
            for (int i = 0; i < NUM_SUBJECTS; i++) {
                String markStr = (marks[i] == -1) ? "N/A" : String.valueOf(marks[i]);
                String status = (marks[i] == -1) ? "N/A" : (marks[i] >= PASS_THRESHOLD ? "PASS" : "FAIL");
                sb.append(String.format("  - %s: %s (%s)\n", subjectNames[i], markStr, status));
            }
            sb.append(String.format("Average: %.2f, Overall Status: %s\n", getAverage(), isOverallPassed() ? "PASS" : "FAIL"));
            sb.append("----------------------\n");
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n--- Student App Menu ---");
            System.out.println("1. Add Student");
            System.out.println("2. Enter Marks");
            System.out.println("3. View Student Report");
            System.out.println("4. Save All Reports to File");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            choice = getIntInput(); // This now correctly consumes the newline

            switch (choice) {
                case 1: addStudent(); break;
                case 2: enterMarks(); break;
                case 3: viewStudentReport(); break;
                case 4: saveAllReportsToFile(); break;
                case 5: System.out.println("Exiting application."); break;
                default: System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
        sc.close();
    }

    static void addStudent() {
        System.out.print("Enter student name: ");
        String name = sc.nextLine(); // This will now correctly wait for input
        System.out.print("Enter student age: ");
        int age = getIntInput();
        if (age <= 0) { System.out.println("Age must be positive. Student not added."); return; }
        students.add(new Student(name, age));
        System.out.println("Student '" + name + "' added.");
    }

    static void enterMarks() {
        if (students.isEmpty()) { System.out.println("No students added yet."); return; }
        System.out.print("Enter student name to enter marks for: ");
        String name = sc.nextLine(); // This will now correctly wait for input
        Student s = findStudent(name);
        if (s == null) { System.out.println("Student not found."); return; }
        for (int i = 0; i < NUM_SUBJECTS; i++) {
            System.out.print("Enter marks for " + subjectNames[i] + " (0-100): ");
            int mark = getIntInput();
            while (mark < 0 || mark > 100) {
                System.out.println("Invalid mark. Must be 0-100. Re-enter: "); mark = getIntInput();
            }
            s.marks[i] = mark;
        }
        System.out.println("Marks updated for " + s.name + ".");
    }

    static void viewStudentReport() {
        if (students.isEmpty()) { System.out.println("No students added yet."); return; }
        System.out.print("Enter student name to view report: ");
        String name = sc.nextLine(); // This will now correctly wait for input
        Student s = findStudent(name);
        if (s == null) { System.out.println("Student not found."); return; }
        System.out.println(s.getReport());
    }

    static void saveAllReportsToFile() {
        if (students.isEmpty()) { System.out.println("No students to save reports for."); return; }
        System.out.print("Enter filename (e.g., student_reports.txt): ");
        String filename = sc.nextLine(); // This will now correctly wait for input
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Student s : students) {
                writer.println(s.getReport());
            }
            System.out.println("All student reports saved to " + filename + ".");
        } catch (IOException e) {
            System.out.println("Error saving reports: " + e.getMessage());
        }
    }

    static Student findStudent(String name) {
        for (Student s : students) {
            if (s.name.equalsIgnoreCase(name.trim())) return s;
        }
        return null;
    }

    // IMPORTANT FIX: Consume the newline character immediately after nextInt()
    static int getIntInput() {
        int value;
        while (true) {
            try {
                value = sc.nextInt(); // Read the integer
                sc.nextLine();       // <--- THIS LINE IS THE FIX: Consume the leftover newline
                return value;
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a number: ");
                sc.nextLine(); // Consume the invalid input that caused the exception
            }
        }
    }
}
