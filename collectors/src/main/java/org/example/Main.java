package org.example;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.example.user.User;

public class Main {
    public static void main(String[] args) {
        final var testStrings = List.of("asdf", "asdf", "qwer");
        final var testUsers = List.of(new User("jim", "halpert", 32, 3),
                new User("andy", "bernard", 29, 1),
                new User("michael", "scott", 45, 5));

        final var collection = toCollection(testStrings);                                  // ["qwer", "asdf"] 2 elements because of Set
        final var list = toList(testStrings);                                              // ["asdf", "asdf", "qwer"]
        final var unmodifiableList = toUnmodifiableList(testStrings);                      // [ "asdf", "asdf", "qwer"]
        final var set = toSet(testStrings);                                                // [ "qwer", "asdf" ]
        final var unmodifiableSet = toUnmodifiableSet(testStrings);                        // [ "asdf", "qwer" }
        final var join = joining(testStrings);                                             // "asdfasdfqwer"
        final var joinDelimiter = joiningDelimiter(testStrings);                           // "asdf|asdf|qwer"
        final var joinDelimiterPrefixSuffix = joiningDelimiterPrefixSuffix(testStrings);   // "{{asdf|asdf|qwer}}"
        final var mapping = mapping(testUsers);                                            // [ "jim", "andy", "michael" ]
        final var flatMapping = flatMapping(testUsers);                                    // [ "jim", "halpert", "andy", "bernard", "michael", "scott" ]
        final var filtering = filtering(testUsers);                                        // [ user jim, user michael ]
        final var collectingAndThen = collectingAndThen(testUsers);                        // [ "jim has 3 kids", "andy has 1 kids", "michael has 5 kids" ]
        final var minBy = minBy(testUsers);                                                // User andy
        final var maxBy = maxBy(testUsers);                                                // User michael
        final var summingInt = summingInt(testStrings);                                    // 12
        final var summingLong = summingLong(testStrings);                                  // 12
        final var summingDouble = summingDouble(testStrings);                              // 3.0
        final var averagingInt = averagingInt(testStrings);                                // 4.0
        final var averagingLong = averagingLong(testStrings);                              // 4.0
        final var averagingDouble = averagingDouble(testStrings);                          // 1.0
        final var reducing = reducing(testUsers);                                          // Optional of user michael
        final var reducingWithIdentity = reducingWithIdentity(testUsers);                  // User "identity" which was passed as a parameter
        final var reducingWithIdentityAndMapper = reducingWithMapperAndIdentity(testUsers);// identity
        final var groupingBy = groupingBy(testUsers);                                      // {michael=[org.example.user.User@9af6b091], andy=[org.example.user.User@da88ad77], jim=[org.example.user.User@690ca720]}
        final var groupingByWithDownstream = groupingByWithDownstream(testUsers);          // {michael=[org.example.user.User@9af6b091], andy=[org.example.user.User@da88ad77], jim=[org.example.user.User@690ca720]} in this values are sets
        final var groupingByWithDownstreamAndMapFactory =
                groupingByWithDownstreamAndMapFactory(testUsers);                              // {andy=[org.example.user.User@da88ad77], jim=[org.example.user.User@690ca720], michael=[org.example.user.User@9af6b091]} in this case it's a treemap with values sets
        final var grouppingByConcurrent = groupingByConcurrent(testUsers);                 // {michael=[org.example.user.User@9af6b091], andy=[org.example.user.User@da88ad77], jim=[org.example.user.User@690ca720]}
        final var grouppingByConcurrentWithDownstream =
                groupingByConcurrentWithDownstream(testUsers);                             // {michael=[org.example.user.User@9af6b091], andy=[org.example.user.User@da88ad77], jim=[org.example.user.User@690ca720]}
        final var groupingByConcurrentWithDownstramAndMapFactory =
                groupingByConcurrentWithDownstreamAndMapFactory(testUsers);                // {andy=[org.example.user.User@da88ad77], jim=[org.example.user.User@690ca720], michael=[org.example.user.User@9af6b091]}
        final var partitioningBy = partitioningBy(testUsers);                              // {false=[org.example.user.User@690ca720, org.example.user.User@9af6b091], true=[org.example.user.User@da88ad77]}
        final var partitioningByWithDownstream = partitioningByDownstream(testUsers);      // {false=[org.example.user.User@9af6b091, org.example.user.User@690ca720], true=[org.example.user.User@da88ad77]}
        final var toMap = toMap(testUsers);                                                // {michael=scott, jim=halpert, andy=bernard}
        final var toUnmodifiableMap = toUnmodifiableMap(testUsers);                        // {michael=scott, andy=bernard, jim=halpert}
        final var toMapMerge = toMapMerge(testUsers);                                      // {1=andy, 3=jim, 5=michael}
        final var toUnmodifiableMapMerge = toUnmodifiableMapMerge(testUsers);              // {1=andy, 5=michael, 3=jim}
        final var toMapMergeFactory = toMapMergeMapFactory(testUsers);                     // {1=andy, 3=jim, 5=michael}
        final var toConcurrentMap = toConcurrentMap(testUsers);                            // {1=andy, 3=jim, 5=michael}
        final var toConcurrentMapMerge = toConcurrentMapMerge(testUsers);                  // {1=andy, 3=jim, 5=michael}
        final var toConcurrentMapMergeFactory = toConcurrentMapMergeFactory(testUsers);    // {1=andy, 3=jim, 5=michael}
        final var summirizingInt = summarizingInt(testStrings);                            // IntSummaryStatistics{count=3, sum=12, min=4, average=4.000000, max=4}
        final var summirizingLong = summarizingLong(testStrings);                          // LongSummaryStatistics{count=3, sum=12, min=4, average=4.000000, max=4}
        final var summirizingDouble = summingDouble(testStrings);                          // 3.0
        final var teeing = teeing(testUsers);                                              // Average kids: 3.0, average age: 35.333333333333336

        groupingByWithDownstreamAndMapFactory.clear();
    }

    //toCollection takes Supplier<T> as a collection factory
    //you can create any collection type you want
    private static Collection<String> toCollection(List<String> list) {
        return list.stream()
                .collect(Collectors.toCollection(() -> new HashSet<>()));
//        .collect(Collectors.toCollection(HashSet<String>::new);
    }

    //toList simply converts stream to list of streams type
    private static List<String> toList(List<String> list) {
        final var result = list.stream().collect(Collectors.toList());
        return result;
    }

    //toUnmodifeableList converts stream into unmodifiable list of streams type
    private static List<String> toUnmodifiableList(List<String> list) {
        final var result = list.stream().collect(Collectors.toUnmodifiableList());
        //runtime exception unsupported operation on immutable collection
//    result.add("asdf");
//    result.set(1, "zxcv");
        return result;
    }

    //toSet simply converts stream to set of streams types
    private static Set<String> toSet(List<String> list) {
        return list.stream().collect(Collectors.toSet());
    }

    //simply converts stream to unmodifiable set of streams type
    private static Set<String> toUnmodifiableSet(List<String> list) {
        final var set = list.stream().collect(Collectors.toUnmodifiableSet());
        //runtime exception unsupported operation on immutable collection
//    set.add("asdf");
        return set;
    }

    //simply joins all strings in stream into one string
    private static String joining(List<String> list) {
        return list.stream().collect(Collectors.joining());
    }

    // joins all strings in stream into one string with delimiter
    private static String joiningDelimiter(List<String> list) {
        return list.stream().collect(Collectors.joining("|"));
    }

    //joins all strings in stream into one string with delimiter, prefix and suffix
    private static String joiningDelimiterPrefixSuffix(List<String> list) {
        return list.stream().collect(Collectors.joining("|", "{{", "}}"));
    }

    //mapping maps stream elements from type U to type T and then converts it to downstream
    // equivalent of .stream().map().collect(Collectors.toSomeCollection());
    private static List<String> mapping(List<User> users) {
        return users.stream().collect(Collectors.mapping(
                u -> u.getFirstName(),
                Collectors.toList()));
    }

    //flat mapping stream elements from type U to type T, through stream, how flat mapping usually
    //works, and then collects elements into collection of type T
    //equivalen of .stream.flatMap().collect(Collectors.toSomeCollection());
    private static List<String> flatMapping(List<User> users) {
        return users.stream().collect(Collectors.flatMapping(
                u -> Stream.of(u.getFirstName(), u.getLastName()),
                Collectors.toList()
        ));
    }

    //filtering filters stream and collect it to downstream
    //equivalent of .stream().filter().collect(toList());
    private static List<User> filtering(List<User> users) {
        return users.stream().collect(Collectors.filtering(
                u -> u.getKids() >= 3,
                Collectors.toList()
        ));
    }

    //collectingAndThen collects streams elements into downstream and then apply function
    // on that downstream
    private static List<String> collectingAndThen(List<User> users) {
        return users.stream().collect(Collectors.collectingAndThen(
                Collectors.toList(),
                downstream -> {
                    return downstream.stream().collect(Collectors.mapping(
                            u -> u.getFirstName() + " has " + u.getKids() + " kids",
                            Collectors.toList()
                    ));
                }
        ));
    }

    //simply counts elements in stream
    private static Long counting(List<String> list) {
        return list.stream().collect(Collectors.counting());
    }

    //with comparator compares each element in stream and return minimum value according to comparator
    private static Optional<User> minBy(List<User> users) {
        return users.stream().collect(Collectors.minBy(
                        (u1, u2) -> {
                            if (u1.getKids() > u2.getKids())
                                return 1;
                            else if (u1.getKids() == u2.getKids())
                                return 0;
                            else
                                return -1;
                        }
                )
        );
    }

    //with comparator compares each element in stream and return maximum value according to comparator
    private static Optional<User> maxBy(List<User> users) {
        return users.stream().collect(Collectors.maxBy(
                (u1, u2) -> {
                    if (u1.getKids() > u2.getKids())
                        return 1;
                    else if (u1.getKids() == u2.getKids())
                        return 0;
                    else
                        return -1;
                }
        ));
    }

    //reducing stream of values into int value through toIntFunction
    private static Integer summingInt(List<String> list) {
        return list.stream().collect(Collectors.summingInt(
                str -> str.length()
        ));
    }

    //reducing stream of values into long value through toLongFunction
    private static Long summingLong(List<String> list) {
        return list.stream().collect(Collectors.summingLong(
                str -> str.length()
        ));
    }

    //reducing stream of values into double value through toDoubleFunction
    private static Double summingDouble(List<String> list) {
        return list.stream().collect(Collectors.summingDouble(
                str -> str.length() / 3
        ));
    }

    //map stream values to int and reducing to average double value
    private static Double averagingInt(List<String> list) {
        return list.stream().collect(Collectors.averagingInt(
                str -> str.length()
        ));
    }

    //map stram values to long and reducing to average double value
    private static Double averagingLong(List<String> list) {
        return list.stream().collect(Collectors.averagingLong(
                str -> str.length()
        ));
    }

    //map stream values to duble and reducing them to average double value
    private static Double averagingDouble(List<String> list) {
        return list.stream().collect(Collectors.averagingDouble(
                str -> str.length() / 3
        ));
    }

    //reducing stream values
    private static Optional<User> reducing(List<User> users) {
        return users.stream().collect(Collectors.reducing(
                (u1, u2) -> u1.getKids() > 3 ? u1 : u2
        ));
    }

    //reducing stream values with identity
    //also return identity if there no values in stream
    private static User reducingWithIdentity(List<User> users) {
        return users.stream().collect(Collectors.reducing(
                new User("identity", "identity", 0, 0),
                (u1, u2) -> u1.getKids() == 0 ? u1 : u2
        ));
    }

    //reducing stream values with identity
    // before maps stream values to identity type
    //returns identity if there is not elements in stream
    private static String reducingWithMapperAndIdentity(List<User> users) {
        return users.stream().collect(Collectors.reducing(
                "identity",
                u -> u.getFirstName(),
                (u1, u2) -> u1.length() == u1.length() ? u1 : u2
        ));
    }

    //groupingBy implementing group by operation
    //classifier provides function result of which using as a key in result map
    private static Map<String, List<User>> groupingBy(List<User> users) {
        return users.stream().collect(Collectors.groupingBy(
                u -> u.getFirstName()
        ));
    }

    //classifier provides function result of which using as key in reult map
    // downstream is a container for values
    private static Map<String, Set<User>> groupingByWithDownstream(List<User> users) {
        return users.stream().collect(Collectors.groupingBy(
                u -> u.getFirstName(),
                Collectors.toSet()
        ));
    }

    //classifier provides function reslt which is using as key in result map
    // map factory provider supplier for resulting map
    // downstream is a container for values
    private static Map<String, Set<User>> groupingByWithDownstreamAndMapFactory(List<User> users) {
        return users.stream().collect(Collectors.groupingBy(
                u -> u.getFirstName(),
                TreeMap<String, Set<User>>::new,
                Collectors.toSet()
        ));
    }

    //groupingBy implementing group by operation
    //classifier provides function result of which using as a key in result map
    private static Map<String, List<User>> groupingByConcurrent(List<User> users) {
        return users.stream().collect(Collectors.groupingBy(
                u -> u.getFirstName()
        ));
    }

    //classifier provides function result of which using as key in reult map
    // downstream is a container for values
    private static Map<String, Set<User>> groupingByConcurrentWithDownstream(List<User> users) {
        return users.stream().collect(Collectors.groupingBy(
                u -> u.getFirstName(),
                Collectors.toSet()
        ));
    }

    //classifier provides function reslt which is using as key in result map
    // map factory provider supplier for resulting map
    // downstream is a container for values
    private static Map<String, Set<User>> groupingByConcurrentWithDownstreamAndMapFactory(List<User> users) {
        return users.stream().collect(Collectors.groupingBy(
                u -> u.getFirstName(),
                TreeMap<String, Set<User>>::new,
                Collectors.toSet()
        ));
    }

    //partitioning by groups stream values into map with boolean key
    private static Map<Boolean, List<User>> partitioningBy(List<User> users) {
        return users.stream().collect(Collectors.partitioningBy(
                u -> u.getFirstName().equals("andy")
        ));
    }

    //partitioning by groups stream values into map with boolean key and provided downstream as values
    private static Map<Boolean, Set<User>> partitioningByDownstream(List<User> users) {
        return users.stream().collect(Collectors.partitioningBy(
                user -> user.getFirstName().equals("andy"),
                Collectors.toSet()
        ));
    }

    //toMap collect stream values into map with provided key mapper and value mapper
    private static Map<String, String> toMap(List<User> users) {
        return users.stream().collect(Collectors.toMap(
                user -> user.getFirstName(),
                user -> user.getLastName()
        ));
    }

    //toUnmodifiableMap collect stream value into unmodifiable map with provided key mapper and value mapper
    private static Map<String, String> toUnmodifiableMap(List<User> users) {
        return users.stream().collect(Collectors.toUnmodifiableMap(
                user -> user.getFirstName(),
                user -> user.getLastName()
        ));
    }

    //toMap collect stream values into map with provided key mapper and value mapper and merge function in case
    // of the same key
    private static Map<Integer, String> toMapMerge(List<User> users) {
        return users.stream().collect(Collectors.toMap(
                user -> user.getKids(),
                user -> user.getFirstName(),
                (newValue, oldValue) -> newValue + ", " + oldValue
        ));
    }

    //toUnmodifiableMap collect stream values into unmodifiable map with provided key mapper and value mapper
    // and merge function in case of the same key
    private static Map<Integer, String> toUnmodifiableMapMerge(List<User> users) {
        return users.stream().collect(Collectors.toUnmodifiableMap(
                user -> user.getKids(),
                user -> user.getFirstName(),
                (newValue, oldValue) -> newValue + ", " + oldValue
        ));
    }

    //toMap collect stream value into map with provided key mapper and value mapper
    // and merge function in case the same key and map factory
    private static Map<Integer, String> toMapMergeMapFactory(List<User> users) {
        return users.stream().collect(Collectors.toMap(
                user -> user.getKids(),
                user -> user.getFirstName(),
                (newValue, oldValue) -> newValue + ", " + oldValue,
                () -> new TreeMap<Integer, String>()
        ));
    }

    //toConcurrentMap collect stream values into concurrent map with provided key mapper and value mapper
    private static Map<Integer, String> toConcurrentMap(List<User> users) {
        return users.stream().collect(Collectors.toConcurrentMap(
                user -> user.getKids(),
                user -> user.getFirstName()
        ));
    }

    //toConcurrentMap collect stream value into conurrent map with prodided key mapper, value mapper
    // and merge function in case of the same key
    private static Map<Integer, String> toConcurrentMapMerge(List<User> users) {
        return users.stream().collect(Collectors.toConcurrentMap(
                user -> user.getKids(),
                user -> user.getFirstName(),
                (newValue, oldValue) -> newValue + ", " + oldValue
        ));
    }

    //toCOncurrentMap collect stream values into concurrent map with provided key mapper, value mapper,
    // and merge function in case of the same key and map factory
    private static Map<Integer, String> toConcurrentMapMergeFactory(List<User> users) {
        return users.stream().collect(Collectors.toConcurrentMap(
                        user -> user.getKids(),
                        user -> user.getFirstName(),
                        (newValue, oldValue) -> newValue + ", " + oldValue,
                        () -> new ConcurrentHashMap<>()
                )
        );
    }

    //summarizingInt collects stream values into IntSummaryStatistics which can be used after
    // for getting some stats as count, min, max, average etc.
    private static IntSummaryStatistics summarizingInt(List<String> str) {
        return str.stream().collect(Collectors.summarizingInt(
                s -> s.length()
        ));
    }

    //summarizingLong collects stream values into LongSummaryStatistics which can be used after
    // for getting some stats as count, min, max, average etc.
    private static LongSummaryStatistics summarizingLong(List<String> str) {
        return str.stream().collect(Collectors.summarizingLong(
                s -> s.getBytes().length
        ));
    }

    //summarizingDouble collects stream values into DoubleSummaryStatistics which can be used after
    // for getting some stats as count, min, max, average etc.
    private static DoubleSummaryStatistics summarizingDouble(List<String> str) {
        return str.stream().collect(Collectors.summarizingDouble(
                s -> s.getBytes().length
        ));
    }

    //teeing applies two collectors on each element in stream and them combine them into result
    private static String teeing(List<User> users) {
        return users.stream().collect(Collectors.teeing(
                Collectors.averagingInt(user -> user.getKids()),
                Collectors.averagingInt(user -> user.getAge()),
                (averageKids, averageAge) -> "Average kids: " + averageKids + ", average age: " + averageAge
        ));
    }
}