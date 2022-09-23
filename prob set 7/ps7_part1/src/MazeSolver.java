import java.util.LinkedList;
import java.util.Queue;

public class MazeSolver implements IMazeSolver {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
		{ -1, 0 }, // North
		{ 1, 0 }, // South
		{ 0, 1 }, // East
		{ 0, -1 } // West
	};

	private Maze maze;
	private boolean solved;
	private boolean[][] visited;
	private int endRow, endCol;
	// keeps track of the number of rooms reachable per step for numReachable
	LinkedList<Integer> roomseachstep;

	public MazeSolver() {
		// TODO: Initialize variables.
		solved = false;
		maze = null;
	}

	@Override
	public void initialize(Maze maze) {
		// TODO: Initialize the solver.
		this.maze = maze;
		visited = new boolean[maze.getRows()][maze.getColumns()];
		solved = false;
	}

	// class to keep track of room, row and column
	static class Rm {
		Room room;
		int row;
		int col;
		Rm prev;

		public Rm(Room room, int row, int col, Rm prev) {
			this.room = room;
			this.row = row;
			this.col = col;
			this.prev = prev;
		}

		public int getRow() {
			return this.row;
		}

		public int getCol() {
			return this.col;
		}

		public Room getRoom() {
			return room;
		}
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		// set all visited flag to false
		// before we begin our search
		for (int i = 0; i < maze.getRows(); ++i) {
			for (int j = 0; j < maze.getColumns(); ++j) {
				this.visited[i][j] = false;
				maze.getRoom(i, j).onPath = false;
			}
		}
		// keeps track of the number of rooms reachable per step for numReachable
		this.roomseachstep = new LinkedList<>();
		this.solved = false;
		// to keep track of number of steps
		int counter = 0;
		// keeps track of frontier for BFS
		Queue<Rm> frontier = new LinkedList<>();
		// coords of start point
		Room start = maze.getRoom(startRow, startCol);
		// start pt visited
		visited[startRow][startCol] = true;
		Rm s = new Rm(start, startRow, startCol, null);
		// find end room
		Room end = maze.getRoom(endRow, endCol);
		// enqueue the current node
		frontier.add(s);
		Rm endpt = null;
		int endcount = 0;
		while (!frontier.isEmpty()) {
			// adds the number of rooms reachable at current step, in index of current step
			roomseachstep.add(frontier.size());

			// room not endpoint, dequeue and enqueue adjacent rooms
			Queue<Rm> nextfrontier = new LinkedList<>();
			for (Rm rm: frontier) {
				Room room = rm.getRoom();
				int x = rm.getRow();
				int y = rm.getCol();

				if (room.equals(end)) {
					this.solved = true;
					endcount = counter;
					endpt = rm;
				}

				// North
				if (!room.hasNorthWall() && !visited[x - 1][y]) {
					Room north = maze.getRoom(x - 1, y);
					nextfrontier.add(new Rm(north, x - 1, y, rm));
					visited[x - 1][y] = true;
				}
				// South
				if (!room.hasSouthWall() && !visited[x + 1][y]) {
					Room south = maze.getRoom(x + 1, y);
					nextfrontier.add(new Rm(south, x + 1, y, rm));
					visited[x + 1][y] = true;
				}
				// East
				if (!room.hasEastWall() && !visited[x][y + 1]) {
					Room east = maze.getRoom(x, y + 1);
					nextfrontier.add(new Rm(east, x, y + 1, rm));
					visited[x][y + 1] = true;
				}
				// West
				if (!room.hasWestWall() && !visited[x][y - 1]) {
					Room west = maze.getRoom(x, y - 1);
					nextfrontier.add(new Rm(west, x, y - 1, rm));
					visited[x][y - 1] = true;
				}
			}
			// increase counter since room is not yet found
			counter++;
			frontier = nextfrontier;
		}
		if (solved) {
			maze.getRoom(startRow, startCol).onPath = true;
			endpt.getRoom().onPath = true;
			while (endpt.prev != null) {
				endpt.prev.getRoom().onPath = true;
				endpt = endpt.prev;
			}
			return endcount;
		} else {
			return null;
		}
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		if (k >= this.roomseachstep.size()) {
			return 0;
		} else {
			return this.roomseachstep.get(k);
		}
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("maze-empty.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 2, 3));
			MazePrinter.printMaze(maze);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
