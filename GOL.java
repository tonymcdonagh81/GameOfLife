import processing.core.PApplet;

public class GOL extends PApplet {

	private final int cellSize = 8;
	int columns, rows;
	//Arrays to hold the states of the cells
	int[][] thisGeneration;
	int[][] lastGeneration;
	//Percentage chance of a cell bing alive at the start.
	private final float chanceToStart = 20;
	//Colours of the cells.
	private final int aliveColour = color(0, 200, 0);
	private final int deadColour = color(250, 250, 250);

	public void setup() {
		//Set applet size and frame rate.
		size(800, 450);
		frameRate(12);
		
		//Set size of the arrays.
		columns = width / cellSize;
		rows = height / cellSize;
		thisGeneration = new int[columns][rows];
		lastGeneration = new int[columns][rows];

		//Set the cells to an initial state.
		for (int x = 0; x < columns; x++) {
			for (int y = 0; y < rows; y++) {
				float chanceToLive = random(100);
				int initialState;
				if (chanceToLive > chanceToStart) {
					initialState = 0;
				} else {
					initialState = 1;
				}
				thisGeneration[x][y] = initialState;
			}
		}
	}

	public void draw() {
		//Draw the Grid and populate the live and dead cells.
		for (int x = 0; x < columns; x++) {
			for (int y = 0; y < rows; y++) {
				if (thisGeneration[x][y] == 1) {
					fill(aliveColour);
				} else {
					fill(deadColour);
				}
				rect(x * cellSize, y * cellSize, cellSize, cellSize);
			}
		}
		update();
	}

	public void update() {
		//Nested cell to save the sate of the current generation.
		for (int x = 0; x < columns; x++) {
			for (int y = 0; y < rows; y++) {
				lastGeneration[x][y] = thisGeneration[x][y];
			}
		}
		//Nested forloops to get the current state of cells.
		for (int x = 0; x < columns; x++) {
			for (int y = 0; y < rows; y++) {
				int otherCells = 0;
				//Nested forloops to get the current state of the adjacent cells.
				for (int otherX = x - 1; otherX <= x + 1; otherX++) {
					for (int otherY = y - 1; otherY <= y + 1; otherY++) {
						//Test only within the bounds.
						if (((otherX >= 0) && (otherX < columns)) && ((otherY >= 0) && (otherY < rows))) {
							//Ensure cells are not checked against themselves.
							if (!((otherX == x) && (otherY == y))) {
								//Check if the adjacent cells is alive.
								if (lastGeneration[otherX][otherY] == 1) {
									otherCells++;
								}
							}
						}
					}
				}
				//Check the current live cell against the number of adjacent cells that are alive.
				if (lastGeneration[x][y] == 1) {
					//A live cell with less then 2 live adjacent cells will die due to under population, 
					//more than 3 it will also die due over population
					if (otherCells < 2 || otherCells > 3) {
						thisGeneration[x][y] = 0;
					} // With 2 or 3 live adjacent cells the cell stays alive.
				} else {
					//An empty cell with exactly 3 adjacent alive cells will become alive.
					if (otherCells == 3) {
						thisGeneration[x][y] = 1;
					}
				}
			}
		}
	}
}
