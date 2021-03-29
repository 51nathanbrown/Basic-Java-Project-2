import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Random;

/**
 * Part of the solution to Project 3.
 * 
 * Represents the grid of agents in the Schelling Simulation.
 * 
 * @author lewis63 and 906118437
 * @version 2019.11.12
 */
public class SimGrid extends Pane
{
    private Agent[][] grid;
    private Random gen;
    private int numCells;
    private double satisfiedPercent;
    
    
    private final static double THRESHOLD = 0.3;  

    /**
     * Creates and initializes an agent grid of the specified size.
     * 
     * @param rows the number of rows in the simulation grid
     * @param cols the number of cols in the simulation grid
     */
    public SimGrid(int rows, int cols)
    {
        gen = new Random();
        
        numCells = rows * cols;
        
        grid = new Agent[rows][cols];
        initializeGrid();
        
       
        ArrayList<GridLocation> unsatisfied = findUnsatisfiedAgents();
        satisfiedPercent = (numCells - unsatisfied.size()) / (double) numCells
            * 100;
    }

    /**
     * Fills the grid with an initial, random set of agents and vacant spaces.
     * Approximately 10% of the grid locations are left vacant. The rest are
     * evenly distributed between red and blue agents.
     */
    public void initializeGrid()
    {
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                if (gen.nextInt(10) > 8 )
                    grid[i][j] = new Agent(i, j, Color.WHITE);
                else if (gen.nextInt(10) > 4)
                    grid[i][j] = new Agent(i, j, Color.RED);
                else
                    grid[i][j] = new Agent(i, j, Color.BLUE);
                this.getChildren().add(grid[i][j].getSquare());
            }
        }
        
    }
    
    /**
     * Gets the current percentage of satisfied agents in the grid.
     * 
     * @return the percentage of satisfied agents in the simulation
     */
    public double getSatisfiedPercent()
    {
        return satisfiedPercent;
    }
    
    /**
     * Performs one step of the simulation by finding the location of all
     * unsatisfied agents, then moving each one to a randomly chosen vacant
     * location.
     * 
     * @return the number of unsatisfied agents found
     */
    public int performSimulationStep()
    {
        ArrayList<GridLocation> unsatisfied = findUnsatisfiedAgents();
        for (GridLocation location : unsatisfied) {
            GridLocation vacant = findVacantLocation();
            grid[vacant.getRow()][vacant.getCol()].setColor(
                grid[location.getRow()][location.getCol()].getColor());
            grid[location.getRow()][location.getCol()].setColor(Color.WHITE);
        }

        
        satisfiedPercent = (numCells - unsatisfied.size()) / (double) numCells
            * 100;
        
        return unsatisfied.size();
    }
    

    /**
     * Creates a list of all grid locations that contain an unsatisfied agent.
     * 
     * @return a list of the locations of all currently unsatisfied agents
     */
    private ArrayList<GridLocation> findUnsatisfiedAgents()
    {
        ArrayList<GridLocation> unsatisfied = new ArrayList<GridLocation>();
        
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 30; j++)
                if (!grid[i][j].isVacant() && !agentIsSatisfied(i, j))
                    unsatisfied.add(new GridLocation(i, j));
        }
        
        return unsatisfied;
    }
    
    /**
     * Determines if the agent at the specified location is satisfied. First
     * gets a list of all valid, non-vacant neighbors, then counts the number
     * of those neighbors that are the same type. An agent is satisfied with
     * its current location if the ratio of similar agents is greater that
     * a set threshold.
     * 
     * @return true if the agent is satisfied with its current location
     */
    private boolean agentIsSatisfied(int i, int j)
    {   
        ArrayList<Agent> neighbors = getNeighbors(i, j);
        
        int sameCount = 0;


        for (Agent neighbor : neighbors)
            if (neighbor.getColor().equals(grid[i][j].getColor()))
                sameCount++;
        
        return  (double) sameCount / neighbors.size() > THRESHOLD;
    }
    
    /**
     * Gets a list of agents that are neighbors (adjacent) to the specified
     * grid location. Checks each potential location individually, making sure
     * that each is valid (on the grid) and not vacant.
     * 
     * @return a list of agents that are adjacent to the specified location
     */
    private ArrayList<Agent> getNeighbors(int i, int j)
    {
        ArrayList<Agent> neighbors = new ArrayList<Agent>();
        
        
        if (validLocation(i - 1, j - 1) && !grid[i - 1][j - 1].isVacant())
            neighbors.add(grid[i - 1][j - 1]);
        if (validLocation(i + 1, j - 1) && !grid[i + 1][j - 1].isVacant())
            neighbors.add(grid[i + 1][j - 1]);
        if (validLocation(i + 1, j) && !grid[i + 1][j].isVacant())
            neighbors.add(grid[i + 1][j]);
        if (validLocation(i - 1, j) && !grid[i - 1][j].isVacant())
            neighbors.add(grid[i - 1][j]);
        if (validLocation(i, j + 1) && !grid[i][j + 1].isVacant())
            neighbors.add(grid[i][j + 1]);
        if (validLocation(i, j - 1) && !grid[i][j - 1].isVacant())
            neighbors.add(grid[i][j - 1]);
        if (validLocation(i - 1, j + 1) && !grid[i - 1][j + 1].isVacant())
            neighbors.add(grid[i - 1][j + 1]);
        if (validLocation(i + 1, j + 1) && !grid[i + 1][j + 1].isVacant())
            neighbors.add(grid[i + 1][j + 1]);

       
        

        return neighbors;
    }
    
    /**
     * Determines if the specified grid location is valid.
     * 
     * @return true if the specified location is a valid grid cell
     */
    private boolean validLocation(int i, int j)
    {
    
        return (i < 30 && j < 30 && i >= 0 && j >= 0);
    }
    
    /**
     * Finds a vacant location in the simulation grid. Keeps checking cell
     * locations at random until a vacant one is found.
     * 
     * @return the grid location of a vacant cell
     */
    private GridLocation findVacantLocation()
    {
    
        GridLocation location = new GridLocation(
            gen.nextInt(30), gen.nextInt(30));
        while (!grid[location.getRow()][location.getCol()].isVacant())
            location = new GridLocation(gen.nextInt(30), gen.nextInt(30));
        return location;
        
    }
    
    /**
     * Resets the simulation grid by clearing the pane of agent squares and
     * reinitializing the grid.
     */
    public void resetGrid()
    {
        getChildren().clear();
        initializeGrid();
        
        
        ArrayList<GridLocation> unsatisfied = findUnsatisfiedAgents();
        satisfiedPercent = (numCells - unsatisfied.size()) / (double) numCells
            * 100;
    }
    
}
