package FlexiRent.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import FlexiRent.Exception.AlertException;
import javafx.stage.Stage;
 
/**
 *
 * @author Pavan Kumar Bore Gowda
 */
public class CreateRentalPropertyTable implements DBPath{
 
    
    public static void createNewTable() throws AlertException {
        
        
        // SQL statement for creating a new table
       String sql = "CREATE TABLE IF NOT EXISTS RentalProperties (\n"
                + "	Propert_id text PRIMARY KEY,\n"
                + "	Address text NOT NULL,\n"
                + "	Type integer,\n"
                + "	NOB integer,\n"
                + "	Status integer,\n"
                + "	Maintainance text ,\n"
                + "	Image text \n"
                + ");";
        
        String sqltabledrop ="Drop table RentalProperties;";
        String sqlDBdrop ="DETACH DATABASE testdb";
        
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
        	throw new AlertException(e.getLocalizedMessage(),new Stage());
        }
    }
 
    /**
     * @param args the command line arguments
     * @throws AlertException 
     */
    public static void main(String[] args) throws AlertException {
        createNewTable();
    }
 
}