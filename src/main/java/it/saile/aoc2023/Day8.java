package it.saile.aoc2023;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Map.entry;
import static java.util.stream.Collectors.toMap;

public class Day8 {
    private static final Path INPUT_FILE;
    static {
        try {
            INPUT_FILE = Paths.get(Day8.class.getResource("/day8/input.txt").toURI());
            //INPUT_FILE = Paths.get(Day8.class.getResource("/day8/test.txt").toURI());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        part1();
        part2();
    }

    private static void part1() throws Exception {
        List<String> strings = Files.readAllLines(INPUT_FILE);
        String[] directions = getDirections(strings);
        Map<String, String[]> map = getMap(strings);

        String current = "AAA";
        int steps = 0;
        boolean found = false;
        while (! found) {
            for (int i = 0; i < directions.length; i++) {
                current = map.get(current)[Integer.parseInt(directions[i])];
                steps++;
                found = current.equals("ZZZ");
                if (found) {
                    break;
                }
            }
        }
        System.out.println(steps);
    }

    private static void part2() throws Exception {
        List<String> strings = Files.readAllLines(INPUT_FILE);
        String[] directions = getDirections(strings);
        Map<String, String[]> map = getMap(strings);

        Map<String, Long> toEnd = map.keySet().stream().filter(k -> k.charAt(2) == 'A').collect(Collectors.toMap(Function.identity(), (v) -> 0L));

        for (String s : toEnd.keySet()) {
            String current = s;
            long steps = 0;
            boolean found = false;
            while (! found) {
                for (int i = 0; i < directions.length; i++) {
                    current = map.get(current)[Integer.parseInt(directions[i])];
                    steps++;
                    found = current.charAt(2) == 'Z';
                    if (found) {
                        toEnd.put(s, steps);
                        break;
                    }
                }
            }
        }

        List<Long> values = toEnd.values().stream().toList();
        long total = values.get(0);
        for (int i = 1; i < values.size(); i++) {
            total = lcm(total, values.get(i));
        }
        System.out.println(total);
    }

    private static String[] getDirections(List<String> strings) {
        return strings.get(0)
                .replace("L", "0")
                .replace("R", "1")
                .split("");
    }

    private static Map<String, String[]> getMap(List<String> strings) {
        return strings.stream()
                .skip(2)
                .map(s -> entry(
                        s.substring(0, 3),
                        s.substring(7)
                                .replace(")", "")
                                .split(", ")))
                .collect(toMap(Entry::getKey, Entry::getValue));
    }

    private static long lcm(long x, long y) {
        long max = Math.max(x, y);
        long min = Math.min(x, y);
        long lcm = max;
        while (lcm % min != 0) {
            lcm += max;
        }
        return lcm;
    }
}
