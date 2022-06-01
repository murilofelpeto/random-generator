package com.felpeto.random.generator.directory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DirectoryCreator {

  public static void prepareDirectory(Path path) {
    if (Files.exists(path)) {
      clearDirectory(path);
    } else {
      createDirectory(path);
    }
  }

  private static void clearDirectory(Path path) {
    for (File file : path.toFile().listFiles()) {
      file.delete();
    }
  }

  private static void createDirectory(Path path) {
    final var iterator = path.iterator();
    String currentPathString = "";

    while (iterator.hasNext()) {
      currentPathString += "/" + iterator.next().toString();
      final var currentPath = Path.of(currentPathString);

      if (!Files.exists(currentPath)) {
        try {
          Files.createDirectory(currentPath);
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }
}
