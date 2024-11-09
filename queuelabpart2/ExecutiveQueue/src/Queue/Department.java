package Queue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
class Department {
    private String name;
    private Queue<Executive> executives;

    //Constructor to initialize department object
    public Department(String name) {
        this.name = name;
        this.executives = new LinkedList<>();
    }
    
    //returns name
    public String getName() {
        return name;
    }

    //This method adds the executive to the department
    public void addExecutive(Executive executive) {
        executives.add(executive);
        // Increment seniority for all existing executives
        for (Executive exec : executives) {
            exec.incrementSeniority();
        }
    }

    // Method to remove an executive from the department
    public void removeExecutive(Executive executive) {
        executives.remove(executive);
        // Decrement seniority for all remaining executives
        for (Executive exec : executives) {
            exec.incrementSeniority();
        }
    }

    // Getter method to retrieve the queue of executives
    public Queue<Executive> getExecutives() {
        return executives;
    }

    // Method to get the number of executives in the department
    public int getSize() {
        return executives.size();
    }

    // Method to display the payroll of the department
    public void displayPayroll() {
        List<Executive> execList = new ArrayList<>(executives);
        execList.sort(Comparator.comparingInt(Executive::getSeniority).reversed());
        for (Executive executive : execList) {
            System.out.println(executive.getName() + ": $" + executive.calculateSalary(getSize()));
        }
    }
}