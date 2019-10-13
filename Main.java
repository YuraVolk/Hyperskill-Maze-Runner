package maze;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static boolean mazeExists = false;
    private static Maze mazeGenerator;

    private static int getOption(boolean isGenerated) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Menu ===\n" +
                "1. Generate a new maze\n" +
                "2. Load a maze");
        if (isGenerated) {
            System.out.println("3. Save the maze\n" +
                    "4. Display the maze\n" +
                    "5. Find the escape");
        }
        System.out.println("0. Exit");

        return scanner.nextInt();
    }

    private static void generateMaze() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the size of a new maze");
        String dimensions = scanner.nextLine();
        int[] array = Arrays.stream(dimensions.split(" ")).mapToInt(Integer::parseInt).toArray();
        if (array.length > 1) {
            mazeGenerator = new Maze(array[0], array[1]);
        } else {
            mazeGenerator = new Maze(array[0], array[0]);
        }

        mazeExists = mazeGenerator.getState();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int option = -1;

        while (!mazeExists) {
            option = getOption(false);
            switch (option) {
                case 0:
                    System.out.print("Bye!");
                    System.exit(0);
                    break;
                case 1:
                    generateMaze();
                    break;
                case 2:
                    String filePath = scanner.next();
                    mazeGenerator.loadMaze(filePath);
                    mazeExists = mazeGenerator.getState();
                    break;
                default:
                    System.out.println("Incorrect option. Please try again");
                    break;
            }
            System.out.println();
        }
        while (option != 0) {
            option = getOption(true);
            switch (option) {
                case 0:
                    System.out.print("Bye!");
                    break;
                case 1:
                    generateMaze();
                    break;
                case 2:
                    String filePath = scanner.nextLine();
                    mazeGenerator.loadMaze(filePath);
                    break;
                case 3:
                    mazeGenerator.saveMaze(scanner.nextLine());
                    break;
                case 4:
                    mazeGenerator.printMaze();
                    break;
                case 5:
                    mazeGenerator.solveMaze();
                    break;
                default:
                    System.out.println("Incorrect option. Please try again");
                    break;
            }
            System.out.println();
        }
    }
}
