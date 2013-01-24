package entidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import tela.App;
import conexao.Conexao;

public class Grcli {
	Connection pg = Conexao.getPostgresConnection();
	Connection vmd = Conexao.getSqlConnection();
	App a = new App();
	
	public void importa(JProgressBar progressBar2, JLabel lblNewLabel_5) throws Exception {
		String pgGRCLI = "select cod_grupo, nom_grupo from cadgrcli";
		String vGRCLI = "Insert Into GRCLI (Cod_Grpcli, Des_Grpcli) Values (?,?)";
		try (PreparedStatement pVmd = vmd.prepareStatement(vGRCLI);
			 PreparedStatement pPg = pg.prepareStatement(pgGRCLI)) {
			
			ResultSet rs = pPg.executeQuery();
			
			// contar a qtde de registros
			int registros = a.contaRegistros("cadgrcli");
			int total = registros;
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
				lblNewLabel_5.setText(registros+"/"+total);
				progressBar2.setValue(registros);
			}
			System.out.println("Funcionou GRCLI");
			pVmd.close();
			pPg.close();
			
			progressBar2.setValue(0);

		}
	}
}
