package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoVmd {
public static void main(String args[]){
	Connection conexao = null;
	try{
		String url = "jdbc:firebirdsql://localhost/C:/Tmp/BD.FDB";
		String usuario = "SYSDBA";
		String senha = "masterkey";
		Class.forName("org.firebirdsql.jdbc.FBDriver");
		conexao = DriverManager.getConnection(url, usuario, senha);
		System.out.println("conectou");
	} catch(Exception e) {
		System.out.println("Ocorreu um erro: " +e.getMessage());
	}
	
	finally{
		try{
			if (conexao != null)
				conexao.close();
		}catch(SQLException e){
			System.out.println("Erro ao fechar conexao: " +e.getMessage());
		}
	}
}
}
