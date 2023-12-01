package org.example;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    maxBy.get();
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
}