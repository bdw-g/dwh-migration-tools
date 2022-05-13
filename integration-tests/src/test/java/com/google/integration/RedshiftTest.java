package com.google.integration;

import static com.google.base.TestBase.assertListsEqual;
import static com.google.base.TestConstants.PASSWORD_DB;
import static com.google.base.TestConstants.URL_DB;
import static com.google.base.TestConstants.USERNAME_DB;

import com.google.common.collect.LinkedHashMultiset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RedshiftTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(RedshiftTest.class);
  private static Connection connection;

  @BeforeClass
  public static void beforeClass() throws SQLException {
    connection = DriverManager.getConnection(URL_DB, USERNAME_DB, PASSWORD_DB);
  }

  @Test
  public void templateTest() throws SQLException {
    String sql = "select * from customers;";

    LinkedHashMultiset<String> dbList = LinkedHashMultiset.create();
    LinkedHashMultiset<String> outputList = LinkedHashMultiset.create();

    //Extract from RedShift
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      ResultSet rs = preparedStatement.executeQuery();

      while (rs.next()) {
        String customernumber = rs.getString("customernumber");
        String customername = rs.getString("customername");
        String phonenumber = rs.getString("phonenumber");
        String postalcode = rs.getString("postalcode");
        String locale = rs.getString("locale");
        String datecreated = rs.getString("datecreated");
        String email = rs.getString("email");

        LOGGER.info(String.format("%s, %s, %s, %s, %s, %s, %s", customernumber.trim(), customername,
            phonenumber, postalcode, locale, datecreated, email));
      }
    }

    //Extract from output files
    // TODO

    //Reduce and compare
    LinkedHashMultiset<String> dbListCopy = LinkedHashMultiset.create(dbList);
    outputList.forEach(dbList::remove);
    dbListCopy.forEach(outputList::remove);

    assertListsEqual(dbList, outputList);
  }
}
