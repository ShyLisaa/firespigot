package dev.shylisaa.firespigot.api.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.shylisaa.firespigot.api.sql.builder.SqlConnectionBuilder;
import dev.shylisaa.firespigot.api.sql.type.DatabaseType;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;

public class SqlConnection {

    private Connection connection;

    public SqlConnection(SqlConnectionBuilder connectionBuilder) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(MessageFormat.format("jdbc:mysql://{0}:{1}/{2}", connectionBuilder.getHost(), String.valueOf(connectionBuilder.getPort()), connectionBuilder.getDatabase()));
        config.setUsername(connectionBuilder.getUsername());
        config.setPassword(connectionBuilder.getPassword());

        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        config.addDataSourceProperty("useServerPrepStmts", true);
        config.addDataSourceProperty("useLocalSessionState", true);
        config.addDataSourceProperty("rewriteBatchedStatements", true);
        config.addDataSourceProperty("cacheResultSetMetadata", true);
        config.addDataSourceProperty("cacheServerConfiguration", true);

        try {
            HikariDataSource dataSource = new HikariDataSource(config);
            this.connection = dataSource.getConnection();
            System.out.println("Successfully connected to database");
        } catch (SQLException exception) {
            System.out.println("Failed to connect to database");
            exception.printStackTrace();
        }
    }

    public void sendKeepAlive() {
        try {
            this.connection.isValid(1);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void close() {
        try {
            this.connection.close();
            System.out.println("Successfully closed connection");
        } catch (SQLException exception) {
            System.out.println("Failed to close connection");
            exception.printStackTrace();
        }
    }

    public void executeUpdate(String query) {
        try (Statement statement = this.connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public boolean existsInTable(String table, String databaseKey, String key, Object value) {
        try (Statement statement = this.connection.createStatement()) {
            return statement.executeQuery(MessageFormat.format("SELECT {0} FROM {1} WHERE {2} = {3}", databaseKey, table, key, value)).next();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public boolean existsInTable(String table, String key, Object value) {
        try (Statement statement = this.connection.createStatement()) {
            return statement.executeQuery(MessageFormat.format("SELECT * FROM {0} WHERE {1} = {2}", table, key, value)).next();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public void addMoreInTable(String table, String[] keys, Object[] values) {
        StringBuilder upload = new StringBuilder("INSERT INTO " + table + "(" + keys[0]);
        for (int i = 1; i < keys.length; i++) upload.append(", ").append(keys[i]);
        upload.append(") VALUES ('").append(values[0]).append("'");
        for (int i = 1; i < values.length; i++) {
            if (!values[i].equals("NULL")) {
                upload.append(", '").append(values[i]).append("'");
            } else {
                upload.append(", NULL");
            }
        }
        upload.append(");");
        executeUpdate(upload.toString());
    }

    public void updateInTable(String table, String setKey, String setValue, String keyRow, String keyValue) {
        try (Statement statement = this.connection.createStatement()) {
            if (!setValue.equals("NULL")) {
                statement.executeUpdate("UPDATE " + table + " SET " + setKey + "= '" + setValue + "' WHERE " + keyRow + "= '" + keyValue + "';");
            } else {
                statement.executeUpdate("UPDATE " + table + " SET " + setKey + "= NULL WHERE " + keyRow + "= '" + keyValue + "';");
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public int getTablePosition(String table, String type, String key, String searchBY) {
        try (Statement statement = this.connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(MessageFormat.format("SELECT * FROM {0} ORDER BY {1} DESC", table, searchBY));
            while (resultSet.next()) {
                if (resultSet.getString(type).equalsIgnoreCase(key)) {
                    return resultSet.getRow();
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return -1;
        }
        return -1;
    }

    public Object getFromTable(String table, String key, String value, String result) {
        try (Statement statement = this.connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(MessageFormat.format("SELECT * FROM {0} WHERE {1} = '{2}'", table, key, value));
            if (resultSet.next()) {
                return resultSet.getObject(result);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
        return null;
    }

    public Object getFromTable(String databaseKey, String table, String key, String value, String result) {
        try (Statement statement = this.connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(MessageFormat.format("SELECT {0} FROM {1} WHERE {2} = '{3}'", databaseKey, table, key, value));
            if (resultSet.next()) {
                return resultSet.getObject(result);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
        return null;
    }

    public void createTable(String table, Map<String, DatabaseType> content) {
        StringBuilder update = new StringBuilder("CREATE TABLE IF NOT EXISTS `").append(table).append("` (`");
        int count = 0;
        for (String key : content.keySet()) {
            update.append(key).append("` ").append(content.get(key).getDisplay()).append(count + 1 >= content.size() ? ")" : ", `");
            count++;
        }
        executeUpdate(update.toString());
    }

    public void removeAllFromTable(String table, String column, String value) {
        executeUpdate(MessageFormat.format("DELETE FROM {0} WHERE {1} = {2}", table, column, value));
    }

    public Map<String, DatabaseType> getTableInformation(String[] keys, DatabaseType[] types) {
        Map<String, DatabaseType> content = new LinkedHashMap<>();
        for(int i = 0; i < keys.length; i++) {
            content.put(keys[i], types[i]);
        }
        return content;
    }


    /**
     * Get the connection.
     * Should only be used in specific conditions and debugs.
     * Do not use in production code.
     * @return The connection
     */
    public Connection getConnection() {
        return connection;
    }
}
