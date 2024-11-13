package Queue;

public class Customer 
{
    private int items;
    private int arrivalTime;

    public Customer(int items, int arrivalTime) 
    {
        this.items = items;
        this.arrivalTime = arrivalTime;
    }

    
    //Getters
    public int getItems() 
    {
        return items;
    }

    public int getArrivalTime() 
    {
        return arrivalTime;
    }
}

