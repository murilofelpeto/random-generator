package com.felpeto.random;

import static java.lang.System.getenv;

import com.felpeto.random.generator.directory.DirectoryProperties;
import com.felpeto.random.generator.SqlGenerator;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

public class Main {

  private static final Scanner SCANNER = new Scanner(System.in);

  private static final Optional<String> defaultFakerDirectory = Optional.ofNullable(
      getenv("FAKER_DIRECTORY"));

  private static final Optional<String> defaultGeneratedElements = Optional.ofNullable(
      getEnvOrDefault("GENERATED_ELEMENTS", "30000"));

  public static void main(String[] args) {
    var fileExtension = getFileExtensionOption();
    var directoryProperties = getDirectoryProperties();
    var quantity = getQuantityOfMockedData();

    switch (fileExtension) {
      case 1:
        new SqlGenerator(directoryProperties, quantity).create();
        break;
    }
  }

  private static int getQuantityOfMockedData() {
    var quantity = getValueOrDefault(buildMessage("Mocked elements", defaultGeneratedElements),
        defaultGeneratedElements, Integer::valueOf);

    return quantity;
  }

  private static DirectoryProperties getDirectoryProperties() {
    var fakerDirectory = getValueOrDefault(
        buildMessage("Directory of faker files", defaultFakerDirectory),
        defaultFakerDirectory);

    return DirectoryProperties.builder()
        .directory(fakerDirectory)
        .build();
  }

  private static int getFileExtensionOption() {
    int trackOption;

    do {
      trackOption = getValueOrDefault(
          buildMessage("File extension Option [1: SQL]", Optional.empty()),
          Optional.empty(),
          Integer::valueOf);
    } while (trackOption != 1);

    return trackOption;
  }

  private static String getValueOrDefault(String message, Optional<String> defaultValue) {
    return getValueOrDefault(message, defaultValue, value -> value);
  }

  private static <T> T getValueOrDefault(String message, Optional<String> defaultValue,
      Function<String, T> converter) {
    T value = null;

    do {
      System.out.print(message);

      final var inputValue = SCANNER.nextLine();

      if (inputValue.isBlank()) {
        if (defaultValue.isPresent()) {
          value = converter.apply(defaultValue.get());
        }
      } else {
        try {
          value = converter.apply(inputValue);
        } catch (Exception e) {
          System.out.println("Type a valid value");
        }
      }
    } while (value == null);

    return value;
  }

  private static <T> String buildMessage(String message, Optional<T> defaultValue) {
    return buildMessage(message, defaultValue, false);
  }

  private static <T> String buildMessage(String message, Optional<T> defaultValue,
      boolean maskDefault) {
    return defaultValue
        .map(value -> maskDefault ? "******" : value)
        .map(value -> message + " (" + value + "): ")
        .orElse(message + ": ");
  }

  private static String getEnvOrDefault(String propertyName, String defaultValue) {
    final var value = getenv(propertyName);

    if (value == null) {
      return defaultValue;
    }

    return value;
  }
}
