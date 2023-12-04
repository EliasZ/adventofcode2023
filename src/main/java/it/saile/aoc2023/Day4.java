package it.saile.aoc2023;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

public class Day4 {
    public static void main(String[] args) throws Exception {
        //var inputFile = Paths.get(Day4.class.getResource("/day4/test.txt").toURI());
        var inputFile = Paths.get(Day4.class.getResource("/day4/input.txt").toURI());

        // Part 1
        try (Stream<String> lines = Files.lines(inputFile)) {
            int result = lines.mapToInt(line -> {
                int splitPos = line.indexOf('|');
                Set<Integer> winning = Arrays.stream(line.substring(line.indexOf(':') + 2, splitPos - 1).split(" "))
                        .filter(str -> !str.isEmpty())
                        .map(Integer::parseInt)
                        .collect(Collectors.toSet());
                Set<Integer> mine = Arrays.stream(line.substring(splitPos + 2).split(" "))
                        .filter(str -> !str.isEmpty())
                        .map(Integer::parseInt)
                        .collect(Collectors.toSet());
                double reduce = (mine.stream()
                        .filter(winning::contains)
                        .map(nr -> 0.5)
                        .reduce(0.5, (acc, nr) -> acc * 2));
                return reduce == 0.5 ? 0 : (int) Math.ceil(reduce);
            }).sum();
            System.out.println("a " + result);
        }

        // Part 2
        Map<Integer, Integer> cardToWins = new TreeMap<>();

        try (Stream<String> lines = Files.lines(inputFile)) {
            lines.forEach(line -> {
                int splitPos = line.indexOf('|');
                int colPos = line.indexOf(':');
                int card = parseInt(line.substring(5, colPos).trim());
                Set<Integer> winning = Arrays.stream(line.substring(colPos + 2, splitPos - 1).split(" "))
                        .filter(str -> !str.isEmpty())
                        .map(Integer::parseInt)
                        .collect(Collectors.toSet());
                Set<Integer> mine = Arrays.stream(line.substring(splitPos + 2).split(" "))
                        .filter(str -> !str.isEmpty())
                        .map(Integer::parseInt)
                        .collect(Collectors.toSet());
                cardToWins.put(card, (int) mine.stream().filter(winning::contains).count());
            });
        }

        Deque<Integer> queue = new ArrayDeque<>(cardToWins.keySet());
        int total = 0;
        while (!queue.isEmpty()) {
            total++;
            int currentKey = queue.poll();
            int toAdd = cardToWins.get(currentKey);
            for (int i = 0; i < toAdd; i++) {
                queue.offer(currentKey + i + 1);
            }
        }
        System.out.println("b " + total);
    }
}
