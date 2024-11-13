package Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

//SupermarketSimulation class handles the simulation of a supermarket checkout process
public class SupermarketSimulation 
{
    private int maxNumSuperItems; // Max items for super express line
    private int numExpressCountersItems; // Max items for express line
    private int numStandLines; // Number of standard lines
    private double arrivalRate; //arrival rate of customers per hour
    private int maxItems; // maximum number of items a customer can purchase
    private long maxSimTime; // maximum simulation time (in milliseconds)
    private int numExpressCounters; // number of express counters
    
    //Lists that hold different checkout counters
    private LinkedList<CheckoutCounter> superExpressCounters;
    private LinkedList<CheckoutCounter> expressCounters;
    private LinkedList<CheckoutCounter> standardCounters;

    //Default Constructor
    public SupermarketSimulation() 
    {
        //Used to run the "run" method of the program
    }
    
    //Initialize the simulation with parameters
    public SupermarketSimulation(int maxNumSuperItems, int numExpressCountersItems, int numStandLines, double arrivalRate, int maxItems, long maxSimTime) 
    {
    	this.maxNumSuperItems = maxNumSuperItems;
        this.numExpressCountersItems = numExpressCountersItems;
        this.numStandLines = numStandLines;
        this.arrivalRate = arrivalRate;
        this.maxItems = maxItems;
        this.maxSimTime = maxSimTime;
        superExpressCounters = new LinkedList<>();
        expressCounters = new LinkedList<>();
        standardCounters = new LinkedList<>();
        numExpressCounters = 2; //sets the number of express counters
        
        superExpressCounters.add(new SuperExpressCounter());

        
        for(int i=0; i<numExpressCounters; i++)
        {
        	expressCounters.add(new ExpressCounter());
        }
        

        for (int i = 0; i < numStandLines; i++) 
        {
            standardCounters.add(new CheckoutCounter());
        }
    }

    // Method responsible for running the simulation
    public void runSimulation() 
    {
        Random random = new Random();
        
        //Run the program until the time runs out
        for (int time = 0; time < maxSimTime; time++) 
        {
            // Add new customers based on arrival rate
            if (random.nextDouble() < arrivalRate / 3600.0)// Dividing by 3600.0 should make this rate into a per-second probability.
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
        if (customer.getItems() <= maxNumSuperItems) 
        {
            // Add to superexpress counter
            CheckoutCounter counter = findShortestLine(superExpressCounters);
            counter.addCustomer(customer, currentTime);
        } 
        else if (customer.getItems() <= numExpressCountersItems)
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
        
        // Calculate statistics for the express counters
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
        
        CheckoutCounter overallCounter = new CheckoutCounter();
        overallCounter.setTotalWaitTime(totalWaitTime);
        overallCounter.setTotalCustomers(totalCustomers);
        overallCounter.setTotalItemsProcessed(totalItemsProcessed);
        overallCounter.setMaxLength(totalMaxLength);
        overallCounter.setFreeTime(totalFreeTime);
        
        // Print overall statistics using the new overall counter
        printCounterStatistics("Overall", overallCounter);
    }

    //Prints and calculates statistics for a counter passed into this method
    private void printCounterStatistics(String counterName, CheckoutCounter counter) 
    {
    	double averageWaitTime;
    	if (counter.getTotalCustomers() > 0) 
    	{
    	    averageWaitTime = (double) counter.getTotalWaitTime() / counter.getTotalCustomers();
    	} 
    	else 
    	{
    	    averageWaitTime = 0;
    	}
    	
        double customersPerHour = counter.getTotalCustomers() / (maxSimTime / 3600.0);
        double itemsProcessedPerHour = counter.getTotalItemsProcessed() / (maxSimTime / 3600.0);
        double averageFreeTime;
        if(!counterName.equals("Overall"))
        {//Calculate Average Free time
        	averageFreeTime = counter.getFreeTime() / (double) maxSimTime;
        }
        else
        {//Calculate Overall Free time
        	int numCounters = superExpressCounters.size() + expressCounters.size() + standardCounters.size();
        	averageFreeTime = counter.getFreeTime() / (double) numCounters;
        }

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
        int maxNumSuperItems = scanner.nextInt();
        
        
        while(maxNumSuperItems<1)
        {
        	System.out.println("Enter a bigger number of superexpress line items:");
        	maxNumSuperItems = scanner.nextInt();
        }
        
        System.out.print("Enter number of items for express line: ");
        int numExpressCountersItems = scanner.nextInt();
        while(numExpressCountersItems<1)
        {
        	System.out.println("Enter a bigger number of express line items:");
        	numExpressCountersItems = scanner.nextInt();
        }
        
        System.out.print("Enter number of standard lines: ");
        int numStandLines = scanner.nextInt();
        while(numStandLines<1)
        {
        	System.out.println("Enter a bigger number of standard lines:");
        	numStandLines = scanner.nextInt();
        }
        
        System.out.print("Enter arrival rate of customers per hour: ");
        double arrivalRate = scanner.nextDouble();
        while(arrivalRate<1)
        {
        	System.out.println("Enter a bigger arrival rate:");
        	arrivalRate = scanner.nextDouble();
        }
        
        System.out.print("Enter maximum number of items: ");
        int maxItems = scanner.nextInt();
        while(maxItems<1)
        {
        	System.out.println("Maximum number of items cannot be zero or less than zero:");
        	maxItems = scanner.nextInt();
        }
        
        System.out.print("Enter maximum simulation time (in milliseconds): ");
        long maxSimTime = scanner.nextLong();
        while(maxSimTime<1)
        {
        	System.out.println("Enter a bigger number of standard lines:");
        	maxSimTime = scanner.nextLong();
        }
        
        
        SupermarketSimulation simulation = new SupermarketSimulation(maxNumSuperItems, numExpressCountersItems, numStandLines, arrivalRate, maxItems, maxSimTime);//initialize simulation object
        simulation.runSimulation();//Start simulation
        
        scanner.close();
    }
}

