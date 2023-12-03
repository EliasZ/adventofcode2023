package it.saile.aoc2023;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class Day3 {
    public static void main(String[] args) throws Exception {
        var inputFile = Paths.get(Day3.class.getResource("/day3/input.txt").toURI());
        var lines = Files.readAllLines(inputFile);

        var partNumbers = new ArrayList<PartNumber>();
        var symbols = new HashMap<Integer, HashMap<Integer, Character>>();

        int y = 0;
        for (String line : lines) {
            char[] chars = line.toCharArray();
            StringBuilder number = null;
            int i = 0;
            do {
                char c = chars[i];
                if (Character.isDigit(c)) {
                    number = Objects.requireNonNullElse(number, new StringBuilder()).append(c);
                } else if (! Character.isDigit(c) && number != null) {
                    partNumbers.add(new PartNumber(number.toString(), y, i - number.length()));
                    number = null;
                }
                if (! Character.isDigit(c) && c != '.') {
                    symbols.computeIfAbsent(y, k -> new HashMap<>()).put(i, c);
                }
                i++;
            } while (i < chars.length);
            // end of line...
            if (number != null) {
                partNumbers.add(new PartNumber(number.toString(), y, i - number.length()));
            }
            y++;
        }

        int total = 0;

        Set<Point> lookAround = Set.of(
                new Point(-1 , 0), // up
                new Point(1 , 0), // down
                new Point(0 , -1), // left
                new Point(0 , 1), // right
                new Point(-1 , -1), // diagonal up left
                new Point(-1 , 1), // diagonal up right
                new Point(1 , -1), // diagonal down left
                new Point(1 , 1) // diagonal down right
        );

        for (PartNumber partNumber : partNumbers) {
            for (int i = 0; i < partNumber.number.length(); i++) {
                final int x = i;
                boolean found = lookAround.stream()
                        .anyMatch(point -> symbols.getOrDefault(partNumber.y + point.x, new HashMap<>()).get(partNumber.x + x + point.y) != null);
                if (found) {
                    total += Integer.parseInt(partNumber.number);
                    break;
                }
            }
        }

        System.out.println(total);
    }

    record PartNumber(String number, int y, int x) {}
}
