package DB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CalendarProgram {
	
	 private static final CalendarProgram dbcontroller = new CalendarProgram();
	    private static Connection connection;
	    private static final String DB_PATH = "C:\\Users\\LecdA\\Documents\\OMP\\Database\\Sqlite\\sqlite-tools-win32-x86-3420000\\" +  "mydatabase.db";

	    static {
	        try {
	            Class.forName("org.sqlite.JDBC");
	        } catch (ClassNotFoundException e) {
	            System.err.println("Fehler beim Laden des JDBC-Treibers");
	            e.printStackTrace();
	        }
	    }
	    
	    private CalendarProgram(){
	    }
	    
	    public static CalendarProgram getInstance(){
	        return dbcontroller;
	    }
	    
	    private void initDBConnection() {
	        try {
	            if (connection != null)
	                return;
	            System.out.println("Creating Connection to Database...");
	            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
	            if (!connection.isClosed())
	                System.out.println("...Connection established");
	        } catch (SQLException e) {
	            throw new RuntimeException(e);
	        }

	       /* Runtime.getRuntime().addShutdownHook(new Thread() {
	            public void run() {
	                try {
	                    if (!connection.isClosed() && connection != null) {
	                        connection.close();
	                        if (connection.isClosed())
	                            System.out.println("Connection to Database closed");
	                    }
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	        });*/
	    }

	    private void handleDB() throws SQLException {
	    	 // Verbindung zur SQLite-Datenbank herstellen
	    	//Connection connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\LecdA\\Documents\\OMP\\Database\\Sqlite\\sqlite-tools-win32-x86-3420000\\mydatabase.db");
	        try{            
	        	String sql = "CREATE TABLE IF NOT EXISTS calendar ("
	                    + "date TEXT,"
	                    + "title TEXT,"
	                    + "description TEXT"
	                    + ")";
	            try (Statement statement = connection.createStatement()) {
	                // Tabelle erstellen
	                statement.executeUpdate(sql);
	                System.out.println("Tabelle 'calendar' erfolgreich erstellt.");
	            }
	        } catch (SQLException e) {
	            System.err.println("Fehler beim Verbinden zur Datenbank oder Ausführen des CREATE TABLE-Statements.");
	            e.printStackTrace();
	        }
	        // INSERT-Statement vorbereiten
	        	String sql = "INSERT INTO calendar (date, title, description) VALUES (?, ?, ?)";
	            try (PreparedStatement statement = connection.prepareStatement(sql)) {
	                // Werte für das INSERT-Statement setzen
	                statement.setString(1, "2023-05-21");
	                statement.setString(2, "Geburtstagsfeier");
	                statement.setString(3, "Feiere meinen Geburtstag mit Familie und Freunden");

	                // INSERT-Statement ausführen
	                statement.executeUpdate();
	                System.out.println("Ereignis erfolgreich eingefügt.");
	            }
	         catch (SQLException e) {
	            System.err.println("Fehler beim Verbinden zur Datenbank oder Ausführen des INSERT-Statements.");
	            e.printStackTrace();
	        }
	            
	         try {
	        	 Statement statement= connection.createStatement();
	        	 ResultSet result= statement.executeQuery("SELECT * FROM calendar");
	        	 while(result.next()) {
	        		 String date= result.getString("date");
	        		 String title= result.getString("title");
	        		 String desc= result.getString("description");
	            	
	        		 System.out.println(date + " | " + title + " | " + desc + " | ");
	        	 
	        	 }
	         }catch(SQLException e) {
	        	 System.out.println("Error connecting to sqlite db");
	        	 e.printStackTrace();
	         }
	    }

	    public static void main(String[] args) throws SQLException {
	        CalendarProgram dbc = CalendarProgram.getInstance();
	        dbc.initDBConnection();
	        dbc.handleDB();
	    }
	}
	
