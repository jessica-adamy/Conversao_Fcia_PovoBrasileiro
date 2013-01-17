package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {
	
	private static Connection sqlConn = null, sqlConnAux = null, sqlConnConsulta = null;
	private static Connection mysqlConn = null, mysqlConnAux = null;
	
	public static String SQL_SERVIDOR = "";
	public static String SQL_BANCO = "";
	public static String MYSQL_BANCO = "";
	//public static String SQL_SERVIDOR_CONSULTA = "";
	//public static String SQL_BANCO_CONSULTA = "";
	
	public static Connection getSqlConnection() {
		try {
			if (sqlConn == null || sqlConn.isClosed()) {			
				String url = "jdbc:jtds:sqlserver://" + SQL_SERVIDOR + "/" + SQL_BANCO;
				String usuario = "sa";
				String senha = "vls021130";
				Class.forName("net.sourceforge.jtds.jdbc.Driver");
				sqlConn = DriverManager.getConnection(url, usuario, senha);
				System.out.println("conectou " + SQL_BANCO);
			}
		} catch (SQLException e) {
			System.out.println("Ocorreu um erro: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("Erro de drive: " + e.getMessage());
		}
		return sqlConn;
	}

	public static Connection getSqlConnectionAux() {
		try {
			if (sqlConnAux == null || sqlConnAux.isClosed()) {			
				String url = "jdbc:jtds:sqlserver://" + SQL_SERVIDOR + "/" + SQL_BANCO;
				String usuario = "sa";
				String senha = "vls021130";
				Class.forName("net.sourceforge.jtds.jdbc.Driver");
				sqlConnAux = DriverManager.getConnection(url, usuario, senha);
				System.out.println("conectou " + SQL_BANCO);
			}
		} catch (SQLException e) {
			System.out.println("Ocorreu um erro: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			System.out.println("Erro de drive: " + e.getMessage());
		}
		return sqlConnAux;
	}

	public static Connection getMysqlConnection() {
		try {
			if (mysqlConn == null || mysqlConn.isClosed()) {
				String url = "jdbc:mysql://localhost/"+MYSQL_BANCO;
				String usuario = "root";
				String senha = "vls021130";
				//Class.forName("org.firebirdsql.jdbc.FBDriver");
				mysqlConn = DriverManager.getConnection(url, usuario, senha);
				mysqlConn.setAutoCommit(true);
				System.out.println("conectou MySQL");
				
//				Statement st = fbConn.createStatement();   
//	            String s = "SELECT * FROM TBFOR";   
//	            ResultSet rs = st.executeQuery(s);   
//	              while (rs.next()) {   
//	               System.out.println(rs.getString(19));   
//	               System.out.println(rs.getString(21)); 
//	               System.out.println(rs.getString(7)); 
//	   
//	               
//	              }  
	           // conexao.close();  
				 
			}
		} catch (Exception e) {
			System.out.println("Ocorreu um erro: " + e.getMessage());
		}
		return mysqlConn;
	}
	
	public static Connection getMysqlConnectionAux() {
		try {
			if (mysqlConnAux == null || mysqlConnAux.isClosed()) {
				String url = "jdbc:mysql://localhost/"+MYSQL_BANCO;
				String usuario = "root";
				String senha = "vls021130";
				//Class.forName("org.firebirdsql.jdbc.FBDriver");
				mysqlConnAux = DriverManager.getConnection(url, usuario, senha);
				mysqlConnAux.setAutoCommit(true);
				System.out.println("conectou MySQL");
			}
		} catch (Exception e) {
			System.out.println("Ocorreu um erro: " + e.getMessage());
		}
		return mysqlConnAux;
	}
	
//	public static Connection getSqlConnectionConsulta() {
//		try {
//			if (sqlConnConsulta == null || sqlConnConsulta.isClosed()) {			
//				String url = "jdbc:jtds:sqlserver://" + SQL_SERVIDOR_CONSULTA + "/" + SQL_BANCO_CONSULTA;
//				String usuario = "sa";
//				String senha = "vls021130";
//				Class.forName("net.sourceforge.jtds.jdbc.Driver");
//				sqlConnConsulta = DriverManager.getConnection(url, usuario, senha);
//				System.out.println("conectou " + SQL_BANCO_CONSULTA);
//			}
//		} catch (SQLException e) {
//			System.out.println("Ocorreu um erro: " + e.getMessage());
//		} catch (ClassNotFoundException e) {
//			System.out.println("Erro de drive: " + e.getMessage());
//		}
//		return sqlConnConsulta;
//	}

}