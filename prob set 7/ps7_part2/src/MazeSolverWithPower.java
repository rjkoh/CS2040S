import java.util.LinkedList;
import java.util.Queue;

public class MazeSolverWithPower implements IMazeSolverWithPower {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	private static int[][] DELTAS = new int[][] {
		{ -1, 0 }, // North
		{ 1, 0 }, // South
		{ 0, 1 }, // East
		{ 0, -1 } // West
	};

	private Maze maze;
	private boolean solved;
	// to keep track of unique rooms
	private boolean[][] visited;
	// to keep track of unique states - room visited at power level
	private boolean[][][] visitedwithpower;
	private int endRow, endCol;
	// keeps track of the number of rooms reachable per step for numReachable
	LinkedList<Integer> roomseachstep;

	public MazeSolverWithPower() {
		// TODO: Initialize variables.
		solved = false;
		maze = null;
	}

	@Override
	public void initialize(Maze maze) {
		// TODO: Initialize the solver.
		this.maze = maze;
		solved = false;
	}

	static class Rm {
		Room room;
		int row;
		int col;
		Rm prev;
		int superpowers;

		public Rm(Room room, int row, int col, Rm prev, int superpowers) {
			this.room = room;
			this.row = row;
			this.col = col;
			this.prev = prev;
			this.superpowers = superpowers;
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

		/* error checking
		@Override
		public String toString() {
			return String.format("(row: %d, col: %d, powers: %d)", this.row, this.col, this.superpowers);
		}
		 */
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

		this.visited = new boolean[maze.getRows()][maze.getColumns()];
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
		Rm s = new Rm(start, startRow, startCol, null, 0);
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
					nextfrontier.add(new Rm(north, x - 1, y, rm, 0));
					visited[x - 1][y] = true;
				}
				// South
				if (!room.hasSouthWall() && !visited[x + 1][y]) {
					Room south = maze.getRoom(x + 1, y);
					nextfrontier.add(new Rm(south, x + 1, y, rm, 0));
					visited[x + 1][y] = true;
				}
				// East
				if (!room.hasEastWall() && !visited[x][y + 1]) {
					Room east = maze.getRoom(x, y + 1);
					nextfrontier.add(new Rm(east, x, y + 1, rm, 0));
					visited[x][y + 1] = true;
				}
				// West
				if (!room.hasWestWall() && !visited[x][y - 1]) {
					Room west = maze.getRoom(x, y - 1);
					nextfrontier.add(new Rm(west, x, y - 1, rm, 0));
					visited[x][y - 1] = true;
				}
			}
			// increase counter since room is not yet found
			counter++;
			frontier = nextfrontier;
		}
		if (solved) {
			maze.getRoom(startRow, startCol).onPath = true;
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

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow,
							  int endCol, int superpowers) throws Exception {
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		this.visited = new boolean[maze.getRows()][maze.getColumns()];
		this.visitedwithpower = new boolean[maze.getRows()][maze.getColumns()][superpowers + 1];
		// set all visited flag to false
		// before we begin our search
		for (int i = 0; i < maze.getRows(); ++i) {
			for (int j = 0; j < maze.getColumns(); ++j) {
				for (int k = 0; k < superpowers + 1; k++) {
					this.visitedwithpower[i][j][k] = false;
				}
				maze.getRoom(i, j).onPath = false;
				this.visited[i][j] = false;
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
		Rm s = new Rm(start, startRow, startCol, null, superpowers);
		visited[startRow][startCol] = true;
		// find end room
		Room end = maze.getRoom(endRow, endCol);
		// enqueue the current node
		frontier.add(s);
		Rm endpt = null;
		int endcount = 0;
		// unique rooms at previous step count
		int roomsperstep = 1;
		while (!frontier.isEmpty()) {
			// adds the number of rooms reachable at current step, in index of current step
			this.roomseachstep.add(roomsperstep);
			// number of unique unvisited rooms at current step count
			int rooms = 0;

			// room not endpoint, dequeue and enqueue adjacent rooms
			Queue<Rm> nextfrontier = new LinkedList<>();
			for (Rm rm: frontier) {
				Room room = rm.getRoom();
				int x = rm.getRow();
				int y = rm.getCol();
				int powers = rm.superpowers;

				if (room.equals(end) && !this.solved) {
					this.solved = true;
					endcount = counter;
					endpt = rm;
					// error checking
					// System.out.println("result: " + counter);
				}

				visitedwithpower[x][y][powers] = true;

				// North
				if (x > 0) {
					Room north = maze.getRoom(x - 1, y);
					// if room was visited before with the same number of powers, then current path is slower
					// or the other path led to a dead end
					// no point adding into frontier
					if (room.hasNorthWall() && rm.superpowers > 0 && !visitedwithpower[x - 1][y][rm.superpowers - 1]) {
						nextfrontier.add(new Rm(north, x - 1, y, rm, rm.superpowers - 1));
						visitedwithpower[x - 1][y][rm.superpowers - 1] = true;
						// count only unique rooms that have not been visited
						if (!visited[x - 1][y]) {
							visited[x - 1][y] = true;
							rooms++;
						}
					// no need for power if no wall, add into frontier if unique state
					} else if (!room.hasNorthWall() && !visitedwithpower[x - 1][y][rm.superpowers]) {
						nextfrontier.add(new Rm(north, x - 1, y, rm, rm.superpowers));
						visitedwithpower[x - 1][y][rm.superpowers] = true;
						if (!visited[x - 1][y]) {
							visited[x - 1][y] = true;
							rooms++;
						}
					}
				}
				// South
				if (x < maze.getRows() - 1) {
					Room south = maze.getRoom(x + 1, y);
					if (room.hasSouthWall() && rm.superpowers > 0 && !visitedwithpower[x + 1][y][rm.superpowers - 1]) {
						nextfrontier.add(new Rm(south, x + 1, y, rm, rm.superpowers - 1));
						visitedwithpower[x + 1][y][rm.superpowers - 1] = true;
						if (!visited[x + 1][y]) {
							visited[x + 1][y] = true;
							rooms++;
						}
					} else if (!room.hasSouthWall() && !visitedwithpower[x + 1][y][rm.superpowers]) {
						nextfrontier.add(new Rm(south, x + 1, y, rm, rm.superpowers));
						visitedwithpower[x + 1][y][rm.superpowers] = true;
						if (!visited[x + 1][y]) {
							visited[x + 1][y] = true;
							rooms++;
						}
					}
				}
				// East
				if (y < maze.getColumns() - 1) {
					Room east = maze.getRoom(x, y + 1);
					if (room.hasEastWall() && rm.superpowers > 0 && !visitedwithpower[x][y + 1][rm.superpowers - 1]) {
						nextfrontier.add(new Rm(east, x, y + 1, rm, rm.superpowers - 1));
						visitedwithpower[x][y + 1][rm.superpowers - 1] = true;
						if (!visited[x][y + 1]) {
							visited[x][y + 1] = true;
							rooms++;
						}
					} else if (!room.hasEastWall() && !visitedwithpower[x][y + 1][rm.superpowers]) {
						nextfrontier.add(new Rm(east, x, y + 1, rm, rm.superpowers));
						visitedwithpower[x][y + 1][rm.superpowers] = true;
						if (!visited[x][y + 1]) {
							visited[x][y + 1] = true;
							rooms++;
						}
					}
				}
				// West
				if (y > 0) {
					Room west = maze.getRoom(x, y - 1);
					if (room.hasWestWall() && rm.superpowers > 0 && !visitedwithpower[x][y - 1][rm.superpowers - 1]) {
						nextfrontier.add(new Rm(west, x, y - 1, rm, rm.superpowers - 1));
						visitedwithpower[x][y - 1][rm.superpowers - 1] = true;
						if (!visited[x][y - 1]) {
							visited[x][y - 1] = true;
							rooms++;
						}
					} else if (!room.hasWestWall() && !visitedwithpower[x][y - 1][rm.superpowers]) {
						nextfrontier.add(new Rm(west, x, y - 1, rm, rm.superpowers));
						visitedwithpower[x][y - 1][rm.superpowers] = true;
						if (!visited[x][y - 1]) {
							visited[x][y - 1] = true;
							rooms++;
						}
					}
				}
			}
			// increase counter since room is not yet found
			counter++;
			frontier = nextfrontier;
			// since steps increased, current step becomes previous step - update rooms per step
			roomsperstep = rooms;
			// error checking
			// System.out.println(frontier);
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

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("maze-empty.txt");
			IMazeSolverWithPower solver = new MazeSolverWithPower();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 2, 3, 2));
			MazePrinter.printMaze(maze);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
/* jamies
    public Integer pathSearch(int startRow, int startCol, int endRow,
                              int endCol, int superpowers) throws Exception {
        // TODO: Find shortest path with powers allowed.
        if (maze == null) {
            throw new Exception("Oh no! You cannot call me without initializing the maze!");
        }

        if (startRow < 0 || startRow >= maze.getRows() ||
                startCol < 0 || startCol >= maze.getColumns() ||
                endRow < 0 || endRow >= maze.getRows() ||
                endCol < 0 || endRow >= maze.getColumns()) {
            throw new Exception("Invalid start/end coordinate");
        }

        restart();

        Node startNode = roomData[startRow][startCol];
        startNode.setSuperpower(0);
        startNode.setParentNode(null);
        LinkedList<Node> currFrontier = new LinkedList<>();
        currFrontier.add(startNode);
        Node end = null;
        boolean[][] roomSteps = new boolean[maze.getRows()][maze.getColumns()];
        roomSteps[startRow][startCol] = true;
        power[startRow][startCol][0] = true;
        startNode.setSteps(0);
        Set<Node> roomNodes = new HashSet<>();
        roomNodes.add(startNode);
        reachable.put(startNode.steps(), roomNodes);
        startNode.setVisited(true);

        if (startRow == endRow && startCol == endCol) {
            end = startNode;
        }

        while (!currFrontier.isEmpty()) {
            Node n = currFrontier.poll();
            Room currRoom = maze.getRoom(n.getRow(), n.getCol());
            int[][] possibleCoords = {{n.getRow() + 1, n.getCol(), currRoom.hasEastWall() ? n.superpowerUsed() + 1: n.superpowerUsed()},
                    {n.getRow() - 1, n.getCol(), currRoom.hasWestWall() ? n.superpowerUsed() + 1: n.superpowerUsed()},
                    {n.getRow(), n.getCol() + 1, currRoom.hasSouthWall() ? n.superpowerUsed() + 1: n.superpowerUsed()},
                    {n.getRow(), n.getCol() - 1, currRoom.hasNorthWall() ? n.superpowerUsed() + 1: n.superpowerUsed()}};
›
            for (int[] arr : possibleCoords) {
                int powerUsed = arr[2];
                if (canGoThrough(new Node(arr[0], arr[1], false), powerUsed, superpowers)) {
                    Node currNode = roomData[arr[0]][arr[1]];
                    if (!currNode.isVisited()) {
                        currNode.setVisited(true);
                        currNode.setSuperpower(powerUsed);
                        currNode.setParentNode(n);
                        currFrontier.add(currNode);
                        currNode.setSteps(n == null ? 0 : n.steps() + 1);
                        if (!roomSteps[arr[0]][arr[1]]) {
                            roomSteps[arr[0]][arr[1]] = true;
                            if (!reachable.containsKey(currNode.steps())) {
                                Set<Node> nodes = new HashSet<>();
                                nodes.add(currNode);
                                reachable.put(currNode.steps(), nodes);
                            } else {
                                reachable.get(currNode.steps()).add(currNode);
                            }
                        }

                        if (arr[0] == endRow && arr[1] == endCol && (end == null || pathLength(startNode, currNode) < pathLength(startNode, end))) {
                            end = currNode;
                        }
                    }
                }
            }
        }

        setOnPath(startNode, end);
        return pathLength(startNode, end);
    } */