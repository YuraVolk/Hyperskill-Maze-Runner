package maze;

import java.util.*;

class Maze {
    private static final String wall = "\u2588\u2588";
    private static final String space = "  ";
    private final int rows;
    private final int columns;

    protected static int exitX;
    protected static int exitY;

    private static char[][] maze;
    private static char[][] wallMaze;

    Maze(int width, int height) {
        this.rows = width;
        this.columns = height;

        if (rows <= 0 || columns <= 0) {
            System.exit(0);
        } else if ((rows <= 4 && columns <= 4) && !((rows == 4 && columns > 4) || (rows > 4 && columns == 4))) {
            char[][] pseudoMaze = new char[rows][columns];
            for (char[] row : pseudoMaze)
                Arrays.fill(row, '1');

            printMaze(pseudoMaze);
        } else {
            this.maze = generateMaze();
            wallify();
            makeExitTunnel();

            printMaze(wallMaze);
        }
    }

    protected static void printMaze(char[][] maze) {
        for (char[] row : maze) {
            for (char block : row) {
                switch (block) {
                    case '1':
                        System.out.print(wall);
                        break;
                    case 'E':
                    case '0':
                    case 'T':
                    case 'S':
                    default:
                        System.out.print(space);
                        break;

                }
            }
            System.out.println();
        }
    }

    private static  boolean isOutOfBounds(int row, int col) {
        if (row < 0 || row >= wallMaze[0].length || col < 0 || col >= wallMaze.length) {
            return true;
        }
        return false;
    }

    private static void directExit(String direction) {
        switch (direction) {
            case "R":
                exitY++;
                break;
            case "L":
                exitY--;
                break;
            case "T":
                exitX--;
                break;
            case "B":
                exitX++;
                break;
        }
    }

    private static void makeExitTunnel() {
        final char[][] matrix = wallMaze;
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix [i].length; j++) {
                if (matrix[i][j] == 'E') {
                    exitX = i;
                    exitY = j;
                }
            }
        }

        int bounds = 1;
        String direction;

        while (true) {
            if (isOutOfBounds(exitY + bounds, exitX)) {
                direction = "R";
                break;
            } else if (isOutOfBounds(exitY - bounds, exitX)) {
                direction = "L";
                break;
            } else if (isOutOfBounds(exitX - bounds, exitY)) {
                direction = "T";
                break;
            } else if (isOutOfBounds(exitX + bounds, exitY)) {
                direction = "B";
                break;
            }
            bounds++;
        }
        bounds--;

        while (bounds > 1) {
            directExit(direction);
            wallMaze[exitX][exitY] = '0';
            bounds--;
        }
        directExit(direction);
        wallMaze[exitX][exitY] = 'T';
    }

    private static <T> T[]addStartingElement(T[] elements, T element) {
        T[] newArray = Arrays.copyOf(elements, elements.length + 1);
        newArray[0] = element;
        System.arraycopy(elements, 0, newArray, 1, elements.length);

        return newArray;
    }

    private static <T> T[]append(T[] array, T value) {
        T[] result = Arrays.copyOf(array, array.length + 1);
        result[result.length - 1] = value;
        return result;
    }

    private void wallify() {
        char[] wallHorizontal = new char[columns - 2];
        Arrays.fill(wallHorizontal, '1');

        maze = addStartingElement(maze, wallHorizontal);
        maze = append(maze, wallHorizontal);
        String tempRow;
        char[] toReplace;
        char[][] tempMaze = new char[rows + 2][columns + 2];
        for (int i = 0; i < maze.length; i++) {
            tempRow = "1" + String.copyValueOf(maze[i]) + "1";
            toReplace = tempRow.toCharArray();
            tempMaze[i] = toReplace;
        }

        wallMaze = tempMaze;
    }

    public char[][] generateMaze() {
        int r = rows - 2, c = columns - 2;

        StringBuilder s = new StringBuilder(c);
        for (int x = 0; x < c; x++)
            s.append('1');
        char[][] maz = new char[r][c];
        for (int x = 0; x < r; x++) maz[x] = s.toString().toCharArray();

        Point st = new Point((int)(Math.random() * r), (int)(Math.random() * c), null);
        maz[st.r][st.c] = 'S';

        ArrayList < Point > frontier = new ArrayList <Point> ();
        for (int x = -1; x <= 1; x++)
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0 || x != 0 && y != 0)
                    continue;
                try {
                    if (maz[st.r + x][st.c + y] == '0') continue;
                } catch (Exception e) {
                    continue;
                }
                frontier.add(new Point(st.r + x, st.c + y, st));
            }

        Point last = null;
        while (!frontier.isEmpty()) {
            Point cu = frontier.remove((int)(Math.random() * frontier.size()));
            Point op = cu.opposite();
            try {
                if (maz[cu.r][cu.c] == '1') {
                    if (maz[op.r][op.c] == '1') {

                        maz[cu.r][cu.c] = '0';
                        maz[op.r][op.c] = '0';
                        last = op;
                        for (int x = -1; x <= 1; x++)
                            for (int y = -1; y <= 1; y++) {
                                if (x == 0 && y == 0 || x != 0 && y != 0)
                                    continue;
                                try {
                                    if (maz[op.r + x][op.c + y] == '0') continue;
                                } catch (Exception e) {
                                    continue;
                                }
                                frontier.add(new Point(op.r + x, op.c + y, op));
                            }
                    }
                }
            } catch (Exception e) {
            }

            try {
                if (frontier.isEmpty()) {
                    maz[last.r][last.c] = 'E';
                }
            } catch (Exception e) {
                System.exit(0);
            }

        }


        return maz;
    }

    public static class Point {
        Integer r;
        Integer c;
        Point parent;
        public Point(int x, int y, Point p) {
            r = x;
            c = y;
            parent = p;
        }

        public Point opposite() {
            if (this.r.compareTo(parent.r) != 0)
                return new Point(this.r + this.r.compareTo(parent.r), this.c, this);
            if (this.c.compareTo(parent.c) != 0)
                return new Point(this.r, this.c + this.c.compareTo(parent.c), this);
            return null;
        }
    }
}
