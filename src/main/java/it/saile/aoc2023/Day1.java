package it.saile.aoc2023;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;
import java.util.stream.Stream;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

public class Day1 {
    public static void main(String[] args) throws Exception {
        //Path inputFile = Paths.get(Day1.class.getResource("/day1/test.txt").toURI());
        Path inputFile = Paths.get(Day1.class.getResource("/day1/input.txt").toURI());
        var words = ofEntries(entry("1", 1), entry("2", 2), entry("3", 3),
                entry("4", 4), entry("5", 5), entry("6", 6),
                entry("7", 7), entry("8", 8), entry("9", 9),                 // 54630
                entry("one", 1), entry("two", 2), entry("three", 3),
                entry("four", 4), entry("five", 5), entry("six", 6),
                entry("seven", 7), entry("eight", 8), entry("nine", 9));     // 54770

        try (Stream<String> lines = Files.lines(inputFile)) {
            long result = lines.mapToInt(line -> {
                        var seen = new TreeMap<>();
                        words.entrySet().forEach(valPerWord -> {
                            seen.put(line.indexOf(valPerWord.getKey()), valPerWord.getValue());
                            seen.put(line.lastIndexOf(valPerWord.getKey()), valPerWord.getValue());
                        });
                        seen.remove(-1);
                        return Integer.parseInt(seen.firstEntry().getValue() + "" + seen.lastEntry().getValue());
                    })
                    .sum();
            System.out.println(result);
        }
    }
}
