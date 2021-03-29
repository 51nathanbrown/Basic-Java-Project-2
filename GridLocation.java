/**
 * Part of the solution to Project 3.
 * 
 * Represents one 2-dimensional grid location.
 * 
 * @author lewis63 
 * @version 2019.11.12
 */
public class GridLocation
{
    private int row;
    private int col;
    

    /**
     * Sets the grid location.
     * 
     * @param row the row of this grid location
     * @param col the column of this grid location
     */
    public GridLocation(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    /**
     * Gets the row of this location in the grid.
     * 
     * @return the row value 
     */
    public int getRow()
    {
        return row;
    }

    /**
     * Gets the column of this location in the grid.
     * 
     * @return the column value 
     */
    public int getCol()
    {
        return col;
    }
}
