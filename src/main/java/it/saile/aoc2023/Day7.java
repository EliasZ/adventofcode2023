package it.saile.aoc2023;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static java.util.Map.entry;
import static java.util.Objects.requireNonNullElse;

public class Day7 {
    private static final String CARD_ORDER;
    private static final boolean PART_TWO = false;

    static {
        CARD_ORDER = PART_TWO ? "AKQT98765432J" : "AKQT98765432J";
    }

    enum Hand {
        HIGH,
        PAIR,
        TWOPAIR,
        THREE,
        FULLHOUSE,
        FOUR,
        FIVE,
        ;

        public static Hand fromString(String hand) {
            HashMap<Character, Integer> counts = new HashMap<>();
            for (char c : hand.toCharArray()) {
                counts.compute(c, (k, v) -> v == null ? 1 : v + 1);
            }
            Set<Character> set = counts.keySet();
            if (PART_TWO && counts.containsKey('J')) {
                Character maxKey = counts.entrySet().stream()
                        .filter(c -> !c.getKey().equals('J'))
                        .max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1)
                        .orElse(entry('A', 0)).getKey(); // A or whatever as count doesn't matter here
                counts.compute(maxKey, (k, v) -> requireNonNullElse(v, 0) + counts.get('J'));
                counts.remove('J');
            }

            if (set.size() == 1) return Hand.FIVE;
            if (set.size() == 2) {
                if (counts.values().stream().anyMatch(c -> c.equals(4))) {
                    return Hand.FOUR;
                }
                return Hand.FULLHOUSE;
            }
            if (set.size() == 3) {
                if (counts.values().stream().anyMatch(c -> c.equals(3))) {
                    return Hand.THREE;
                }
                return Hand.TWOPAIR;
            }
            return set.size() == 4 ? Hand.PAIR : Hand.HIGH;
        }
    }

    record CamelCard(String input, Hand hand, Integer bid) {
        public static CamelCard fromInput(String input) {
            String[] split = input.split(" ");
            return new CamelCard(split[0], Hand.fromString(split[0]), Integer.valueOf(split[1]));
        }
    }

    public static void main(String[] args) throws Exception {
        //Path inputFile = Paths.get(Day7.class.getResource("/day7/test.txt").toURI());
        Path inputFile = Paths.get(Day7.class.getResource("/day7/input.txt").toURI());

        List<CamelCard> list = Files.readAllLines(inputFile)
                .stream()
                .map(CamelCard::fromInput)
                .sorted((c1, c2) -> {
                    if (c1.hand.ordinal() > c2.hand.ordinal()) return 1;
                    if (c1.hand.ordinal() < c2.hand.ordinal()) return -1;
                    for (int i = 0; i < c1.input.length(); i++) {
                        int indexOfC1 = CARD_ORDER.indexOf(c1.input.charAt(i));
                        int indexOfC2 = CARD_ORDER.indexOf(c2.input.charAt(i));
                        if (indexOfC1 < indexOfC2) return 1;
                        if (indexOfC1 > indexOfC2) return -1;
                    }
                    return 0; // ??? tie
                })
                .toList();
        int sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum += (i + 1) * list.get(i).bid;
        }
        System.out.println(sum);
    }
}
