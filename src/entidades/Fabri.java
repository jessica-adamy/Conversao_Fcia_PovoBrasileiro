package entidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import tela.App;
import conexao.Conexao;

public class Fabri {
	Connection pg = Conexao.getPostgresConnection();
	Connection vmd = Conexao.getSqlConnection();
	App a = new App();
	
	public void deleta() throws Exception {
		try (Statement stmt = Conexao.getSqlConnection().createStatement()) {
			stmt.executeUpdate("DELETE FROM FABRI");
			stmt.close();
			System.out.println("Deletou FABRI");
		}
	}
	
	public void importa() throws Exception {
		String pgFABRI = "select cod_laborat, nom_laborat, num_cnpj from cadlabor";
		String vFABRI = "Insert Into FABRI (Cod_Fabric, Des_Fabric, Num_Cnpj) Values (?,?,?)";
		try (PreparedStatement pVmd = vmd.prepareStatement(vFABRI);
			 PreparedStatement pPg = pg.prepareStatement(pgFABRI)) {
			
			ResultSet rs = pPg.executeQuery();
			
			// contar a qtde de registros
			int registros = a.contaRegistros("cadlabor");
			a.progressBar2.setMaximum(registros);
			registros = 0;
			
			while (rs.next()) {
						
				// grava no varejo
				int codigo = rs.getInt("cod_laborat");
				pVmd.setInt(1, codigo);
				
				String descricao = rs.getString("nom_laborat");
				if(descricao != null) {
					pVmd.setString(2, descricao.length() > 25 ? descricao.substring(0, 25) : descricao);
				}else {
					pVmd.setString(2, descricao);
				}
				
				String cnpj = rs.getString("num_cnpj");
				if(cnpj != null) {
					cnpj = cnpj.replaceAll("\\D", "");		
				}
				pVmd.setString(3, cnpj);
				
				pVmd.executeUpdate();

				registros++;
				a.progressBar2.setValue(registros);
			}
			System.out.println("Funcionou FABRI");
			pVmd.close();
			pPg.close();
			
			a.progressBar2.setValue(0);

		}
	}

}
