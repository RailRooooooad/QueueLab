package Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

//SupermarketSimulation class handles the simulation of a supermarket checkout process
public class SupermarketSimulation 
{
	// Global variables to hold simulation parameters
    private int numSuper; // Max items for superexpress line
    private int numExp; // Max items for express line
    private int numStandLines; // Number of standard lines
    private double arrivalRate; //arrival rate of customers per hour
    private int maxItems; // maximum number of items a customer can purchase
    private long maxSimTime; // maximum simulation time (in milliseconds)
    
    // Lists that hold different types of checkout counters
    private LinkedList<CheckoutCounter> superExpressCounters;
    private LinkedList<CheckoutCounter> expressCounters;
    private LinkedList<CheckoutCounter> standardCounters;

    //Default Constructor
    public SupermarketSimulation() 
    {
        //Used to run the "run" method of the program
    }
    
    //Initialize the simulation with parameters
    public SupermarketSimulation(int numSuper, int numExp, int numStandLines, double arrivalRate, int maxItems, long maxSimTime) 
    {
    	this.numSuper = numSuper;
        this.numExp = numExp;
        this.numStandLines = numStandLines;
        this.arrivalRate = arrivalRate;
        this.maxItems = maxItems;
        this.maxSimTime = maxSimTime;
        superExpressCounters = new LinkedList<>();
        expressCounters = new LinkedList<>();
        standardCounters = new LinkedList<>();
        
        // Initialize the single super express counter
        superExpressCounters.add(new SuperExpressCounter());
        // Initialize the two express counters
        expressCounters.add(new ExpressCounter());
        expressCounters.add(new ExpressCounter());
        
        // Add the specified number of standard counters
        for (int i = 0; i < numStandLines; i++) 
        {
            standardCounters.add(new CheckoutCounter());
        }
    }

    // Method responsible for running the simulation
    public void runSimulation() 
    {
        Random random = new Random(); // Initialize Random number generator
        for (int time = 0; time < maxSimTime; time++) 
        {
            // Add new customers based on arrival rate
            if (random.nextDouble() < arrivalRate / 3600.0) 
            {
            	// Generate random number of items for the customer
            	int items = random.nextInt(maxItems) + 1;
                int arrivalTime = time;
                Customer customer = new Customer(items, arrivalTime);

                // Add customer to the right counter
                addCustomerToCounter(customer, time);
            }

            // Process customers in all counter types
            for (CheckoutCounter counter : superExpressCounters) 
            {
                counter.processCustomers(time); 
            }
            for (CheckoutCounter counter : expressCounters) 
            {
                counter.processCustomers(time);
            }
            for (CheckoutCounter counter : standardCounters) 
            {
                counter.processCustomers(time);
            }
        }
        // Calculate and print statistics after everything is complete
        calculateStatistics();
    }

    // Method to find the checkout counter with the shortest line
    private CheckoutCounter findShortestLine(List<CheckoutCounter> counters)
    {
        CheckoutCounter shortestLine = counters.get(0);
        for (CheckoutCounter counter : counters) 
        {
        	// Update if a counter with a shorter line is found
            if (counter.getQueueSize() < shortestLine.getQueueSize()) 
            {
                shortestLine = counter;
            }
        }
        return shortestLine; // Return the counter with the shortest line
    }
    
    // Method to add a customer to the appropriate counter based on their items
    private void addCustomerToCounter(Customer customer, int currentTime) 
    {
        if (customer.getItems() <= numSuper) 
        {
            // Add to superexpress counter
            CheckoutCounter counter = findShortestLine(superExpressCounters);
            counter.addCustomer(customer, currentTime);
        } 
        else if (customer.getItems() <= numExp)
        {
            // add to express counter
            CheckoutCounter counter = findShortestLine(expressCounters);
            counter.addCustomer(customer, currentTime);
        } 
        else 
        {
            // add to standard counter
            CheckoutCounter counter = findShortestLine(standardCounters);
            counter.addCustomer(customer, currentTime);
        }
    }


    // Method to calculate and print all the statistics after the simulation completes
    private void calculateStatistics() 
    {
        int totalWaitTime = 0;
        int totalCustomers = 0;
        int totalItemsProcessed = 0;
        int totalMaxLength = 0;
        int totalFreeTime = 0;
        int numCounters = superExpressCounters.size() + expressCounters.size() + standardCounters.size();
        double overallAverageWaitTime;
        
        // Calculate statistics for super express counters
        for (CheckoutCounter counter : superExpressCounters) 
        {
            totalWaitTime += counter.getTotalWaitTime();
            totalCustomers += counter.getTotalCustomers();
            totalItemsProcessed += counter.getTotalItemsProcessed();
            totalMaxLength = Math.max(totalMaxLength, counter.getMaxLength());
            totalFreeTime += counter.getFreeTime();
            printCounterStatistics("Super Express Counter", counter);
        }
        
        // Calculate statistics for express counters
        for (CheckoutCounter counter : expressCounters) 
        {
            totalWaitTime += counter.getTotalWaitTime();
            totalCustomers += counter.getTotalCustomers();
            totalItemsProcessed += counter.getTotalItemsProcessed();
            totalMaxLength = Math.max(totalMaxLength, counter.getMaxLength());
            totalFreeTime += counter.getFreeTime();
            printCounterStatistics("Express Counter", counter);
        }

        // Calculate statistics for standard counters
        for (CheckoutCounter counter : standardCounters) 
        {
            totalWaitTime += counter.getTotalWaitTime();
            totalCustomers += counter.getTotalCustomers();
            totalItemsProcessed += counter.getTotalItemsProcessed();
            totalMaxLength = Math.max(totalMaxLength, counter.getMaxLength());
            totalFreeTime += counter.getFreeTime();
            printCounterStatistics("Standard Counter", counter);
        }
        
        //Calculate Overall Statistics
        if (totalCustomers > 0) 
        {
            overallAverageWaitTime = (double) totalWaitTime / totalCustomers;
        } 
        else 
        {
            overallAverageWaitTime = 0;
        }
        double overallCustomersPerHour = totalCustomers / (maxSimTime / 3600.0);
        double overallItemsProcessedPerHour = totalItemsProcessed / (maxSimTime / 3600.0);
        double overallFreeTime = totalFreeTime / (double) numCounters;

        //Print Overall Statistics
        System.out.printf("Overall Average Waiting Time: %.2f seconds\n", overallAverageWaitTime);
        System.out.printf("Overall Customers per Hour: %.2f\n", overallCustomersPerHour);
        System.out.printf("Overall Items Processed per Hour: %.2f\n", overallItemsProcessedPerHour);
        System.out.printf("Overall Maximum Line Length: %d\n", totalMaxLength);
        System.out.printf("Overall Free Time: %.2f seconds\n", overallFreeTime);
    }

    //Prints statistics for a given counter to the console
    private void printCounterStatistics(String counterName, CheckoutCounter counter) 
    {
        double averageWaitTime = counter.getTotalCustomers() > 0 ? (double) counter.getTotalWaitTime() / counter.getTotalCustomers() : 0;
        double customersPerHour = counter.getTotalCustomers() / (maxSimTime / 3600.0);
        double itemsProcessedPerHour = counter.getTotalItemsProcessed() / (maxSimTime / 3600.0);
        double averageFreeTime = counter.getFreeTime() / (double) maxSimTime;

        System.out.printf("%s Average Waiting Time: %.2f seconds\n", counterName, averageWaitTime);
        System.out.printf("%s Customers per Hour: %.2f\n", counterName, customersPerHour);
        System.out.printf("%s Items Processed per Hour: %.2f\n", counterName, itemsProcessedPerHour);
        System.out.printf("%s Maximum Line Length: %d\n", counterName, counter.getMaxLength());
        System.out.printf("%s Free Time: %.2f seconds\n", counterName, averageFreeTime);
    }

    public  void run()//This method asks for user input and gathers parameters to start the simulation
    {
        Scanner scanner = new Scanner(System.in);//Initialize scanner
        System.out.print("Enter number of items for superexpress line: ");
        int numSuper = scanner.nextInt();
        System.out.print("Enter number of items for express line: ");
        int numExp = scanner.nextInt();
        System.out.print("Enter number of standard lines: ");
        int numStandLines = scanner.nextInt();
        System.out.print("Enter arrival rate of customers per hour: ");
        double arrivalRate = scanner.nextDouble();
        System.out.print("Enter maximum number of items: ");
        int maxItems = scanner.nextInt();
        System.out.print("Enter maximum simulation time (in milliseconds): ");
        long maxSimTime = scanner.nextLong();

        SupermarketSimulation simulation = new SupermarketSimulation(numSuper, numExp, numStandLines, arrivalRate, maxItems, maxSimTime);//initialize simulation object
        simulation.runSimulation();//Start simulation
        
        scanner.close();
    }
}

