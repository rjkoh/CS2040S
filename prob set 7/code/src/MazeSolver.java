import java.util.*;

import java.util.function.Function;

public class MazeSolver implements IMazeSolver {
	private static final int TRUE_WALL = Integer.MAX_VALUE;
	private static final int EMPTY_SPACE = 0;
	private static final List<Function<Room, Integer>> WALL_FUNCTIONS = Arrays.asList(
			Room::getNorthWall,
			Room::getEastWall,
			Room::getWestWall,
			Room::getSouthWall
	);
	private static final int[][] DELTAS = new int[][] {
			{ -1, 0 }, // North
			{ 0, 1 }, // East
			{ 0, -1 }, // West
			{ 1, 0 } // South
	};

	private Maze maze;
	boolean[][] visited;
	int[][] fears;
	boolean solved;
	// keeps track if a Rm has been added into the frontier before
	boolean[][] queued;
	PriorityQueue<Rm> frontier;

	public MazeSolver() {
		// TODO: Initialize variables.
		this.maze = null;
		this.solved = false;
		frontier = new PriorityQueue<>();
	}

	@Override
	public void initialize(Maze maze) {
		// TODO: Initialize the solver.
		this.maze = maze;
		visited = new boolean[maze.getRows()][maze.getColumns()];
		fears = new int[maze.getRows()][maze.getColumns()];
		solved = false;
		queued = new boolean[maze.getRows()][maze.getColumns()];
	}

	static class Rm implements Comparable<Rm> {
		Room room;
		int row;
		int col;
		int fear;

		public Rm(Room room, int row, int col, int fear) {
			this.room = room;
			this.row = row;
			this.col = col;
			this.fear = fear;
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

		@Override
		public int compareTo(Rm rm) {
			if (rm.fear > this.fear) {
				return -1;
			} else if (this.fear > rm.fear) {
				return 1;
			} else {
				return 0;
			}
		}

		@Override
		public String toString() {
			return String.format("(row: %s, col: %s, fear: %s)", row, col, fear);
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof Rm) {
				Rm rm = (Rm) o;
				return rm.getRow() == row && rm.getCol() == col;
			}
			return false;
		}
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// TODO: Find minimum fear level.
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
				this.fears[i][j] = Integer.MAX_VALUE;
				this.queued[i][j] = false;
 			}
		}

		frontier.clear();
		this.solved = false;
		// keeps track of frontier for Djikstra, arranged by ascending fear
		PriorityQueue<Rm> frontier = new PriorityQueue<>();
		Room start = maze.getRoom(startRow, startCol);
		Rm s = new Rm(start, startRow, startCol, 0);
		fears[startRow][startCol] = 0;
		queued[startRow][startCol] = true;
		frontier.add(s);
		while (!frontier.isEmpty() && !solved) {
			Rm rm = frontier.poll();
			Room room = rm.getRoom();
			int x = rm.getRow();
			int y = rm.getCol();
			System.out.println("popped: " + rm);

			if (x == endRow && y == endCol) {
				solved = true;
				break;
			}

			for (int i = 0; i < 4; i++) {
				int nextfear = WALL_FUNCTIONS.get(i).apply(room);
				int nextx = x + DELTAS[i][0];
				int nexty = y + DELTAS[i][1];
				if (nextx >= 0 && nexty >= 0 && nextx < maze.getRows() && nexty < maze.getColumns()) {
					if (!visited[nextx][nexty]) {
						if (nextfear == EMPTY_SPACE) {
							Rm next = relax(rm, nextx, nexty, 1);
							// decreaseKey
							if (queued[nextx][nexty]) {
								frontier.removeIf(element -> element.equals(next) && element.fear > next.fear);
							}
							frontier.add(next);
							queued[nextx][nexty] = true;
						} else if (nextfear != TRUE_WALL) {
							Rm next = relax(rm, nextx, nexty, nextfear);
							// decreaseKey
							if (queued[nextx][nexty]) {
								frontier.removeIf(element -> element.equals(next) && element.fear > next.fear);
							}
							frontier.add(next);
							queued[nextx][nexty] = true;
						}
					}
				}
			}
			visited[x][y] = true;
			System.out.println(frontier);
		}
		if (solved) {
			return fears[endRow][endCol];
		} else {
			return null;
		}
	}

	private Rm relax(Rm prev, int nextrow, int nextcol, int fear) {
		int prevrow = prev.getRow();
		int prevcol = prev.getCol();
		if (fears[nextrow][nextcol] > fears[prevrow][prevcol] + fear) {
			fears[nextrow][nextcol] = fears[prevrow][prevcol] + fear;
		}
		return new Rm(maze.getRoom(nextrow, nextcol), nextrow, nextcol, fears[nextrow][nextcol]);
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// TODO: Find minimum fear level given new rules.
		return null;
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol, int sRow, int sCol) throws Exception {
		// TODO: Find minimum fear level given new rules and special room.
		return null;
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("haunted-maze-sample.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 1, 0, 5));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
