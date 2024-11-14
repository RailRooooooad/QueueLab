package Queue;
import java.util.*;

public class ExecutiveManagementSystem 
{
	
    private static List<Executive> executiveList = new ArrayList<>(); // List of executives
    private static Department[] departments = new Department[10]; // Array of departments
    private static Queue<Executive> unemployed = new LinkedList<>();
    private static int departmentCount = 0;

    // Main method to run the executive management system
    public static void run() 
    {
        Scanner scanner = new Scanner(System.in);
        String command;

        // Infinite loop until exit is typed
        while (true) {
            System.out.print("> ");
            command = scanner.nextLine().trim();
            String[] parts = command.split(" ");

            switch (parts[0].toLowerCase()) 
            {
                case "add":
                    addDepartment(parts[1]);
                    break;
                case "hire":
                    hireExecutive(parts[1]);
                    break;
                case "join":
                    joinDepartment(parts[1], parts[2]);
                    break;
                case "quit":
                    quitExecutive(parts[1]);
                    break;
                case "change":
                    changeDepartment(parts[1], parts[2]);
                    break;
                case "payroll":
                    displayPayroll();
                    break;
                case "salary":
                    displaySalary(parts[1]);
                    break;
                case "exit":
                    scanner.close();
                    return;
                default:
                    System.out.println("Unknown command.");
            }
        }
    }

    // Add new department
    private static void addDepartment(String departmentName) 
    {
    	// Check if the maximum number of departments has been reached
        if (departmentCount >= departments.length) 
        {
            System.out.println("Maximum number of departments reached.");
            return;
        }
        departments[departmentCount++] = new Department(departmentName);
        System.out.println("Department " + departmentName + " added.");
    }

    // Method to hire executives
    private static void hireExecutive(String personName) 
    {
        Executive executive = new Executive(personName);
        executiveList.add(executive);
        unemployed.add(executive);
        System.out.println("Executive " + personName + " hired.");
    }

    // Method to have executives join a department
    private static void joinDepartment(String personName, String departmentName) {
        Executive executive = findExecutive(personName);// Find the executive
        if (executive == null)
        {
            System.out.println("Executive not found.");
            return;
        }

        Department department = findDepartment(departmentName); //Find the department
        if (department == null) 
        {
            System.out.println("Department not found.");
            return;
        }

        unemployed.remove(executive); // Remove from unemployed queue
        department.addExecutive(executive); // Add executive to the department
        System.out.println(personName + " joined " + departmentName + ".");
    }

    // Method for an executive to quit their department
    private static void quitExecutive(String personName) 
    {
        Executive executive = findExecutive(personName);
        if (executive == null) {
            System.out.println("Executive not found.");
            return;
        }

        for (Department department : departments) {
            if (department != null && department.getExecutives().contains(executive))
            {
                department.removeExecutive(executive);
                unemployed.add(executive);
                System.out.println(personName + " has quit from the department.");
                return;
            }
        }
        System.out.println(personName + " is not in any department.");
    }

    // This method allows an executive to change departments
    private static void changeDepartment(String personName, String newDepartmentName) {
        Executive executive = findExecutive(personName);
        if (executive == null) 
        {
            System.out.println("Executive not found.");
            return;
        }

        for (Department department : departments) 
        {
            if (department != null && department.getExecutives().contains(executive)) {
                department.removeExecutive(executive);
                break;
            }
        }

        Department newDepartment = findDepartment(newDepartmentName);
        if (newDepartment == null) 
        {
            System.out.println("Department not found.");
            return;
        }

        newDepartment.addExecutive(executive); // Add executive to the new department
        System.out.println(personName + " has been moved to " + newDepartmentName + ".");
    }

    // Method to display payroll
    private static void displayPayroll() 
    {
        for (Department department : departments) 
        {
            if (department != null) 
            {
                System.out.println("Payroll for " + department.getName() + ":");
                department.displayPayroll();
            }
        }
    }

    // Method to show salary
    private static void displaySalary(String personName) 
    {
        Executive executive = findExecutive(personName);
        if (executive == null)
        {
            System.out.println("Executive not found.");
            return;
        }

        for (Department department : departments) 
        {
            if (department != null && department.getExecutives().contains(executive)) 
            {
                int salary = executive.calculateSalary(department.getSize());
                System.out.printf("%s's salary: $%d\n", personName, salary);
                return;
            }
        }
        System.out.println(personName + " is unemployed.");
    }

    // Method to find an executive by their name
    private static Executive findExecutive(String personName)
    {
        for (Executive executive : executiveList) 
        {
            if (executive.getName().equalsIgnoreCase(personName)) 
            {
                return executive;
            }
        } 
        return null; // Return null if not found
    }

    // Method to find department by its name
    private static Department findDepartment(String departmentName) 
    {
        for (Department department : departments) 
        {
            if (department != null && department.getName().equalsIgnoreCase(departmentName)) 
            {
                return department;
            }
        }
        return null; // Return null if not found
    }
}