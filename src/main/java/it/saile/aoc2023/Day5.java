package it.saile.aoc2023;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day5 {
    public static void main(String[] args) throws Exception {
        //Path inputFile = Paths.get(Day5.class.getResource("/day5/test.txt").toURI());
        Path inputFile = Paths.get(Day5.class.getResource("/day5/input.txt").toURI());

        List<String> lines = Files.readAllLines(inputFile);
        List<List<MapEntry>> map = new ArrayList<>();

        // part 1
        //String[] seeds = lines.get(0).substring(7).split(" ");
        //Set<Range> seeds = Arrays.stream(lines.get(0).substring(7).split(" ")).map(s -> new Range(Long.parseLong(s), 1)).collect(Collectors.toSet());
        // part 2 -->
        String[] seedRanges = lines.get(0).substring(7).split(" ");
        Set<Range> seeds = new HashSet<>();

        long total = 0;
        for (int i = 0; i < seedRanges.length; i += 2) {
            long start = Long.parseLong(seedRanges[i]);
            long range = Long.parseLong(seedRanges[i + 1]);
            seeds.add(new Range(start, range));
            total += (start + range);
        }
        // <-- part 2

        List<MapEntry> newMap = new ArrayList<>();
        lines.stream().skip(3).filter(s -> !s.isEmpty()).forEach(line -> {
            if (line.contains(":")) {
                map.add(new ArrayList<>(newMap));
                newMap.clear();
                return;
            }
            String[] mapStr = line.split(" ");
            newMap.add(new MapEntry(Long.parseLong(mapStr[0]), Long.parseLong(mapStr[1]), Long.parseLong(mapStr[2])));
        });
        map.add(newMap);

        System.out.println(total + " seeds");
        long min = Long.MAX_VALUE;
        for (Range range : seeds) {
            for (long i = range.start; i < (range.start + range.range); i++) {
                total--;
                long x = i;
                //System.out.print("seed " + x + ": ");
                for (List<MapEntry> mapEntries : map) {
                    for (MapEntry mapEntry : mapEntries) {
                        if (mapEntry.isInRange(x)) {
                            x = mapEntry.getDest(x);
                            break;
                        }
                    }
                }
                //System.out.println();
                if (x < min) {
                    min = x;
                }
            }
        }
        System.out.println(min);
    }

    record MapEntry(long destRangeStart, long sourceRangeStart, long rangeLength) {
        public boolean isInRange(long x) {
            return x >= sourceRangeStart && x < sourceRangeStart + rangeLength;
        }

        public long getDest(long x) {
            return destRangeStart + (x - sourceRangeStart);
        }
    }

    record Range(long start, long range) {
    }
}
