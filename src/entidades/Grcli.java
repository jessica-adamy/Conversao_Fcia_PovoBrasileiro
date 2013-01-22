package entidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JProgressBar;

import tela.App;
import conexao.Conexao;

public class Grcli {
	Connection pg = Conexao.getPostgresConnection();
	Connection vmd = Conexao.getSqlConnection();
	App a = new App();
	
	public void deleta() throws Exception {
		try (Statement stmt = Conexao.getSqlConnection().createStatement()) {
			stmt.executeUpdate("DELETE FROM GRCLI");
			stmt.close();
			System.out.println("Deletou GRCLI");
		}
	}
	
	public void importa(JProgressBar progressBar2) throws Exception {
		String pgGRCLI = "select cod_grupo, nom_grupo from cadgrcli";
		String vGRCLI = "Insert Into GRCLI (Cod_Grpcli, Des_Grpcli) Values (?,?)";
		try (PreparedStatement pVmd = vmd.prepareStatement(vGRCLI);
			 PreparedStatement pPg = pg.prepareStatement(pgGRCLI)) {
			
			ResultSet rs = pPg.executeQuery();
			
			// contar a qtde de registros
			int registros = a.contaRegistros("cadgrcli");
			progressBar2.setMaximum(registros);
			registros = 0;
			
			while (rs.next()) {
						
				// grava no varejo
				int codigo = rs.getInt("cod_grupo");
				pVmd.setInt(1, codigo);
				
				String razao = rs.getString("nom_grupo");
				if(razao != null) {
					pVmd.setString(2, razao.length() > 25 ? razao.substring(0, 25) : razao);
				}
				
				pVmd.executeUpdate();

				registros++;
				progressBar2.setValue(registros);
			}
			System.out.println("Funcionou GRCLI");
			pVmd.close();
			pPg.close();
			
			progressBar2.setValue(0);

		}
	}
}
