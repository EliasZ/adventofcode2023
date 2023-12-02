package it.saile.aoc2023;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;
import static java.util.regex.Pattern.compile;

public class Day2 {
    public static void main(String[] args) throws Exception {
        var inputFile = Paths.get(Day2.class.getResource("/day2/input.txt").toURI());
        //var inputFile = Paths.get(Day2.class.getResource("/day2/test.txt").toURI());
        var pattern = compile("(\\d+)\\s(red|green|blue)");
        var maxPerColor = Map.of("red", 12, "green", 13, "blue", 14);

        // Part 1
        try (Stream<String> lines = Files.lines(inputFile)) {
            int result = lines.mapToInt(line -> {
                var matcher = pattern.matcher(line);
                while (matcher.find()) {
                    int count = parseInt(matcher.group(1));
                    String color = matcher.group(2);
                    if (count > maxPerColor.get(color)) {
                        return 0;
                    }
                }
                return parseInt(line.substring(5, line.indexOf(':')));
            }).sum();
            System.out.println(result);
        }

        // Part 2
        try (Stream<String> lines = Files.lines(inputFile)) {
            int result = lines.mapToInt(line -> {
                var matcher = pattern.matcher(line);
                var topPerColor = new HashMap<>(Map.of("red", 0, "green", 0, "blue", 0));
                while (matcher.find()) {
                    int count = parseInt(matcher.group(1));
                    String color = matcher.group(2);
                    topPerColor.compute(color, (k, v) -> Math.max(v, count));
                }
                return topPerColor.values().stream().reduce(1, (acc, num) -> acc * num);
            }).sum();
            System.out.println(result);
        }
    }
}
