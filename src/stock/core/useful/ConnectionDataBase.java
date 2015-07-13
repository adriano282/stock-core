package stock.core.useful;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class ConnectionDataBase {
	
	public static Connection connection = null;
	public static Statement statement = null;
	
	public static Connection getConnection() 
			throws ClassNotFoundException, 
		SQLException{
				
			String servidor = "jdbc:mysql://localhost:3306/bc_teste";
			String usuario = "root";
			String senha = "jk1994adr";
			String driver = "com.mysql.jdbc.Driver";
			
			try {				
				Class.forName(driver);
				connection = DriverManager.getConnection(servidor, usuario, senha);
				statement = connection.createStatement();
					
			} catch( Exception e ) {
				e.printStackTrace();
			}
			
			return connection;
	}	
}
