package it.saile.aoc2023;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Day9 {
    void main() throws Exception {
        //final Path inputFile = Paths.get(Day9.class.getResource("/day9/test.txt").toURI());
        final Path inputFile = Paths.get(Day9.class.getResource("/day9/input.txt").toURI());
        List<String> strings = Files.readAllLines(inputFile);
        List<Long> totalsA = new ArrayList<>();
        List<Long> totalsB = new ArrayList<>();

        for (String string : strings) {
            long[] list = Arrays.stream(string.split(" ")).mapToLong(Long::parseLong).toArray();
            List<long[]> lists = new ArrayList<>();
            lists.add(list);
            long[] differences;

            do {
                differences = new long[list.length - 1];

                for (int i = 1; i < list.length; i++) {
                    differences[i - 1] = list[i] - list[i - 1];
                }
                list = differences;
                lists.add(differences);
            } while (!Arrays.stream(differences).allMatch(v -> v == 0));
            lists.removeLast(); // all 0s
            List<long[]> reversed = lists.reversed();

            long[] first = reversed.getFirst();
            totalsA.add(reversed.stream()
                    .skip(1)
                    .mapToLong(x -> x[x.length - 1])
                    .reduce(first[first.length - 1], Long::sum));
            totalsB.add(reversed.stream()
                    .skip(1)
                    .mapToLong(x -> x[0])
                    .reduce(first[0], (c, a) -> a - c));
        }
        System.out.println("a " + totalsA.stream().reduce(0L, Long::sum));
        System.out.println("b " + totalsB.stream().reduce(0L, Long::sum));
    }

    record Sequence(List<Long> numbers) {

    }
}
