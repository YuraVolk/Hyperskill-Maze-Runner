package maze;

import maze.Maze;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String dimensions = scanner.nextLine();
        int[] array = Arrays.stream(dimensions.split(" ")).mapToInt(Integer::parseInt).toArray();

        if (array.length > 1) {
            new Maze(array[0], array[1]);
        } else {
            new Maze(array[0], array[0]);
        }
    }
}
