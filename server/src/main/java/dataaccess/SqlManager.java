package dataaccess;

import com.google.gson.Gson;
import model.*;

import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

import java.sql.*;
import java.util.Properties;

public class SqlManager {

    public SqlManager() throws DataAccessException {
        configureDatabase();
    }

}
