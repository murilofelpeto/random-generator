package com.felpeto.random.generator.directory;

import static com.felpeto.random.generator.directory.DirectoryCreator.prepareDirectory;

import com.felpeto.random.generator.entity.Property;
import com.felpeto.random.generator.entity.PropertyKind;
import com.github.javafaker.Faker;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Locale;
import java.util.UUID;

public class FileCreator {

  private static final Faker faker = new Faker(new Locale("pt-BR"));
  private static final String INSERT_PROPERTY = "insert into property (uuid, property_kind, country, state, city, zipcode, street_name, house_number, complement, tenant) values ";

  public static void generateRandomFilesAtResource(
      final int quantity,
      final String folderPath,
      final String extension) {

    prepareDirectory(Path.of(folderPath));
    generateProperty(quantity, folderPath, extension);
  }

  private static void generateProperty(
      final int quantity,
      final String folderPath,
      final String extension) {

    final var propertyList = new LinkedList<Property>();

    for (int i = 0; i < quantity; i++) {
      final var property = Property.builder()
          .city(faker.address().city().replaceAll("'", ""))
          .complement(faker.pokemon().name().replaceAll("'", ""))
          .country(faker.address().country().replaceAll("'", ""))
          .propertyKind(faker.options().option(PropertyKind.class))
          .houseNumber(faker.number().numberBetween(1, 9999))
          .state(faker.address().stateAbbr())
          .streetName(faker.address().streetName().replaceAll("'", ""))
          .tenant(faker.name().username().replaceAll("'", ""))
          .uuid(UUID.randomUUID())
          .zipcode(faker.address().zipCode())
          .build();

      propertyList.add(property);
    }

    final var filePath = folderPath + "/property" + extension;

    try (FileWriter writer = new FileWriter(filePath);
        BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
      bufferedWriter.write(INSERT_PROPERTY);
      bufferedWriter.write(System.getProperty("line.separator"));
      propertyList.parallelStream()
          .forEach(property -> {
            try {
              bufferedWriter.write(getQuery(property));
            } catch (IOException e) {
              e.printStackTrace();
            }
          });

    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  private static String getQuery(final Property property) {
    StringBuilder sb = new StringBuilder();
    sb.append("(UNHEX(REPLACE('");
    sb.append(property.getUuid());
    sb.append("','-','')),'");
    sb.append(property.getPropertyKind().name());
    sb.append("','");
    sb.append(property.getCountry());
    sb.append("','");
    sb.append(property.getState());
    sb.append("','");
    sb.append(property.getCity());
    sb.append("','");
    sb.append(property.getZipcode());
    sb.append("','");
    sb.append(property.getStreetName());
    sb.append("','");
    sb.append(property.getHouseNumber());
    sb.append("','");
    sb.append(property.getComplement());
    sb.append("','");
    sb.append(property.getTenant());
    sb.append("'),");
    sb.append(System.getProperty("line.separator"));
    return sb.toString();
  }
}
