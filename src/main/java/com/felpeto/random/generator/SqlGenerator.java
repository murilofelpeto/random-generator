package com.felpeto.random.generator;

import static com.felpeto.random.generator.directory.FileCreator.generateRandomFilesAtResource;

import com.felpeto.random.generator.directory.DirectoryProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class SqlGenerator {

  private final DirectoryProperties directoryProperties;
  private final Integer quantity;

  @SneakyThrows
  public void create() {

    generateRandomFilesAtResource(
        quantity,
        directoryProperties.getDirectory(),
        ".sql");

  }
}
