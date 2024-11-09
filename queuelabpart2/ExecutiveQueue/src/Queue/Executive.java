package Queue;

class Executive {
    private String name;
    private int seniority;

    public Executive(String name) {
        this.name = name;
        this.seniority = 0; // Initialize seniority
    }
    
    //returns name
    public String getName() {
        return name;
    }

    //returns seniority
    public int getSeniority() {
        return seniority;
    }

    //increases seniority counter
    public void incrementSeniority() {
        this.seniority++;
    }

    //Calculate salary
    public int calculateSalary(int numExecutivesInDepartment) {
        return 40000 + (5000 * (numExecutivesInDepartment - seniority - 1));
    }
}