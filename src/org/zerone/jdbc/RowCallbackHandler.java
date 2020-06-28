package org.zerone.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowCallbackHandler {
  public void process(ResultSet rs)
      throws SQLException;
}