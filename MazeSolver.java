package maze;

import java.util.*;

class MazeSolver {
    private static String[][] maze;
    private static int startRow;
    private static int startCol;
    private static int endRow;
    private static int endCol;

    private static ArrayList<Integer> coordinatesX = new ArrayList<>();
    private static ArrayList<Integer> coordinatesY = new ArrayList<>();


    public MazeSolver(char[][] setMaze) {
        String[][] converted = new String[setMaze.length][setMaze[0].length];

        this.maze = converted;
        prepareMaze(converted, setMaze);
        findExitPossiblePath();
        printMaze();
    }

    static void printMaze() {
        for (String[] row : maze) {
            for (String block : row) {
                if (block.length() > 1) {
                    System.out.print(block + " ");
                } else {
                    System.out.print(block + "  ");
                }
            }
            System.out.println();
        }
    }

    private static void prepareMaze(String[][] set, char[][] maze) {
        for(int i = 0; i < maze.length; i++) {
            for(int j = 0; j < maze[i].length; j++) {
                switch(maze[i][j]) {
                    case 'S':
                        startRow = i;
                        startCol = j;
                        set[i][j] = "S";
                        break;
                    case 'T':
                        endRow = i;
                        endCol = j;
                        set[i][j] = "T";
                        break;
                    case '1':
                        set[i][j] = "o";
                        break;
                    case 'E':
                    default:
                        set[i][j] = "0";
                        break;
                }
            }
        }
    }

    private static boolean setNeighbour(int i, String direction, int x, int y) {
        switch (direction) {
            case "L":
                if (maze[x][y - 1] == "T") {
                    return true;
                }
                if (maze[x][y - 1] == "0") {
                    maze[x][y - 1] = Integer.toString(i + 1);
                    coordinatesX.add(x);
                    coordinatesY.add(y - 1);
                    return false;
                }
                break;
            case "R":
                if (maze[x][y + 1] == "T") {
                    return true;
                }
                if (maze[x][y + 1] == "0") {
                    maze[x][y + 1] = Integer.toString(i + 1);
                    coordinatesX.add(x);
                    coordinatesY.add(y + 1);
                    return false;
                }
                break;
            case "T":
                if (maze[x - 1][y] == "T") {
                    return true;
                }
                if (maze[x - 1][y] == "0") {
                    maze[x - 1][y] = Integer.toString(i + 1);
                    coordinatesX.add(x - 1);
                    coordinatesY.add(y);
                    return false;
                }
                break;
            case "B":
                if (maze[x + 1][y] == "T") {
                    return true;
                }
                if (maze[x + 1][y] == "0") {
                    maze[x + 1][y] = Integer.toString(i + 1);
                    coordinatesX.add(x + 1);
                    coordinatesY.add(y);
                    return false;
                }
                break;
            default:
                return false;
        }
        return false;
    }

    private static boolean setFourNeighbours(int i, int x, int y) {
        boolean t1 = setNeighbour(i, "L", x, y);
        boolean t2 = setNeighbour(i, "R", x, y);
        boolean t3 = setNeighbour(i, "T", x, y);
        boolean t4 = setNeighbour(i, "B", x, y);
        return (t1 || t2 || t3 || t4);
    }

    private static boolean iterateOver(int i) {
        List<Integer> coordinatesXref = new ArrayList<>(coordinatesX);
        List<Integer> coordinatesYref = new ArrayList<>(coordinatesY);
        coordinatesX.clear();
        coordinatesY.clear();
        boolean isFound;
        boolean isTarget = false;
        for (int j = 0; j < coordinatesXref.size(); j++) {
            isFound = setFourNeighbours(i, coordinatesXref.get(j), coordinatesYref.get(j));
            if (isFound) {
                isTarget = true;
            }
        }

        return isTarget;
    }

    private static void findExitPossiblePath() {
        int i = 0;
        System.out.println(startRow + " " + startCol);
        boolean found = setFourNeighbours(i, startRow, startCol);
        while (!found) {
            i++;
            found = iterateOver(i);
        }
    }
}
