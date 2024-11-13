package Queue;

import java.util.LinkedList;
import java.util.Queue;

//This class represents the checkout counter in the supermarket
public class CheckoutCounter 
{
	// Queue to hold the customers waiting at this counter
    private Queue<Customer> queue;

    private int totalCustomers; // Total number of customers served
    private int totalWaitTime; // Total wait time for all customers
    private int freeTime; // Time when the counter is not serving customers
    private int maxLength; // Maximum length of the queue observed
    private int totalItemsProcessed; // Total number of items processed

    // Constructor to initialize the checkout counter
    public CheckoutCounter()
    {
    	// Initialize the queue to hold customers
        queue = new LinkedList<>();
    }

    // Method to add a customer to the queue
    public void addCustomer(Customer customer, int currentTime)
    {
        queue.offer(customer);
        
        // Calculate wait time based on the number of items and customers in the queue
        int waitTime = Math.max(0, (queue.size() - 1) * 5 * customer.getItems());
        
        // Update total wait time as well as statistics
        totalWaitTime += waitTime;
        totalCustomers++;
        totalItemsProcessed += customer.getItems();
        
        // Update the maximum queue length if the current queue is bigger
        maxLength = Math.max(maxLength, queue.size());
    }

    // Method to process customers in the queue based on the current time
    public void processCustomers(int currentTime)
    {
    	// Process customers until the queue is empty or the next customer cannot be processed yet
    	while (!queue.isEmpty() && (currentTime - queue.peek().getArrivalTime()) >= (5 * queue.peek().getItems())) 
        {
    		queue.poll();
        }
        freeTime++;
    }

    // Getters
    public int getMaxLength()
    {
    	return maxLength;
    }
    
    public int getTotalCustomers()
    {
    	return totalCustomers;
    }
    
    public int getTotalItemsProcessed()
    {
    	return totalItemsProcessed;
    }

    public int getTotalWaitTime()
    {
    	return totalWaitTime;
    }

    public int getFreeTime()
    {
    	return freeTime;
    }
    
    public int getQueueSize()
    {
    	return queue.size();
    }
    
    
    //Setters
    public void setMaxLength(int maxLength)
    {
    	this.maxLength=maxLength;
    }
    
    public void setFreeTime(int freeTime)
    {
    	this.freeTime = freeTime;    	
    }
    
    public void setTotalItemsProcessed(int totalItemsProcessed)
    {
    	this.totalItemsProcessed = totalItemsProcessed;
    }
    
    public void setTotalWaitTime(int totalWaitTime)
    {
    	this.totalWaitTime = totalWaitTime;
    }
    
    public void setTotalCustomers(int totalCustomers)
    {
    	this.totalCustomers = totalCustomers;
    }
    
}

