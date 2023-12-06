package it.saile.aoc2023;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day6 {
    public static void main(String[] args) throws Exception {
        Path inputFile = Paths.get(Day6.class.getResource("/day6/input.txt").toURI());
        //Path inputFile = Paths.get(Day6.class.getResource("/day6/test.txt").toURI());

        List<String> lines = Files.readAllLines(inputFile);
        List<Long> timesA = Arrays.stream(lines.get(0).replace("Time:", "").trim().split("\\s+")).map(s -> Long.parseLong(s.trim())).toList();
        List<Long> distancesA = Arrays.stream(lines.get(1).replace("Distance:", "").trim().split("\\s+")).map(s -> Long.parseLong(s.trim())).toList();
        List<Long> allWaysA = getWays(timesA, distancesA);

        List<Long> timesB = List.of(Long.parseLong(lines.get(0).replace("Time:", "").replace(" ", "").trim()));
        List<Long> distancesB = List.of(Long.parseLong(lines.get(1).replace("Distance:", "").replace(" ", "").trim()));
        List<Long> allWaysB = getWays(timesB, distancesB);

        System.out.println("a: " + allWaysA.stream().reduce(1L, (a, b) -> a * b));
        System.out.println("b: " + allWaysB.stream().reduce(1L, (a, b) -> a * b));
    }

    private static List<Long> getWays(List<Long> times, List<Long> distances) {
        List<Long> allWays = new ArrayList<>();
        for (int i = 0; i < times.size(); i++) {
            long ways = 0;
            long maxTime = times.get(i);
            long distance = distances.get(i);
            for (long buttonPressTime = 1; buttonPressTime <= maxTime; buttonPressTime++) {
                long remainingTime = maxTime - buttonPressTime;
                long time = Math.max(remainingTime, 0);
                long achievedDistance = buttonPressTime * time;
                if (achievedDistance > distance) {
                    ways++;
                }
            }
            allWays.add(ways);
        }
        return allWays;
    }
}
