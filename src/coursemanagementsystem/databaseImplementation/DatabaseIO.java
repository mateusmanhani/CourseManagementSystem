
package coursemanagementsystem.databaseImplementation;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Mateus Manhani
 */
public interface DatabaseIO {
    Connection getConnection() throws SQLException;
    public void closeConnection(Connection conn) throws SQLException;
}
