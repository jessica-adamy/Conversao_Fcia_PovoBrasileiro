package conexao;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexaoFB {  
  
    /** 
     * @param args the command line arguments 
     */  
    public static void main(String[] args) {  
        // TODO code application logic here  
        try {  
  
            String url = "jdbc:postgresql://localhost:5432/povo_brasileiro";  
            String usuario = "postgres";  
            String senha = "Vls021130";  
            Class.forName("org.postgresql.Driver");  
  
            Connection con;  
  
            con = DriverManager.getConnection(url, usuario, senha);  
  
            System.out.println("Conexão realizada com sucesso.");  
  
            con.close();  
  
        } catch (Exception e) {  
             e.printStackTrace();  
        }  
    }  
}  
