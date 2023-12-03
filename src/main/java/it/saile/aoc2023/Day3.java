package it.saile.aoc2023;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Day3 {
    public static void main(String[] args) throws Exception {
        var inputFile = Paths.get(Day3.class.getResource("/day3/input.txt").toURI());
        var lines = Files.readAllLines(inputFile);

        var partNumbers = new ArrayList<PartNumber>();
        var gearParts = new HashMap<Integer, HashMap<Integer, GearPart>>();

        int y = 0;
        for (String line : lines) {
            char[] chars = line.toCharArray();
            StringBuilder number = null;
            int i = 0;
            do {
                char c = chars[i];
                if (Character.isDigit(c)) {
                    number = Objects.requireNonNullElse(number, new StringBuilder()).append(c);
                } else if (!Character.isDigit(c) && number != null) {
                    partNumbers.add(new PartNumber(number.toString(), y, i - number.length()));
                    number = null;
                }
                if (!Character.isDigit(c) && c != '.') {
                    gearParts.computeIfAbsent(y, k -> new HashMap<>()).put(i, new GearPart(c, new HashSet<>(), y, i));
                }
                i++;
            } while (i < chars.length);
            // end of line...
            if (number != null) {
                partNumbers.add(new PartNumber(number.toString(), y, i - number.length()));
            }
            y++;
        }


        Set<Point> lookAround = Set.of(
                new Point(-1, 0), // up
                new Point(1, 0), // down
                new Point(0, -1), // left
                new Point(0, 1), // right
                new Point(-1, -1), // diagonal up left
                new Point(-1, 1), // diagonal up right
                new Point(1, -1), // diagonal down left
                new Point(1, 1) // diagonal down right
        );

        Set<GearPart> gears = new HashSet<>();

        for (PartNumber partNumber : partNumbers) {
            for (int i = 0; i < partNumber.number.length(); i++) {
                final int x = i;
                Set<GearPart> collect = lookAround.stream()
                        .map(point -> gearParts.getOrDefault(partNumber.y + point.x, new HashMap<>()).get(partNumber.x + x + point.y))
                        .filter(Objects::nonNull)
                        .filter(part -> part.c == '*')
                        .collect(Collectors.toSet());
                gears.addAll(collect);
                collect.forEach(part -> part.adjacentTo.add(partNumber));

            }
        }

        int sumGearRatios = gears.stream()
                .filter(g -> g.adjacentTo.size() == 2)
                .reduce(0, (total, g) -> total + g.adjacentTo.stream()
                                .map(p -> Integer.parseInt(p.number))
                                .reduce(1, (a, b) -> a * b),
                        Integer::sum);
        System.out.println(sumGearRatios);
    }

    record PartNumber(String number, int y, int x) {
    }

    record GearPart(Character c, Set<PartNumber> adjacentTo, int y, int x) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GearPart gearPart = (GearPart) o;
            return y == gearPart.y && x == gearPart.x && Objects.equals(c, gearPart.c);
        }

        @Override
        public int hashCode() {
            return Objects.hash(c, y, x);
        }
    }
}
