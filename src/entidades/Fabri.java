package entidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import tela.App;
import conexao.Conexao;

public class Fabri {
	Connection pg = Conexao.getPostgresConnection();
	Connection vmd = Conexao.getSqlConnection();
	App a = new App();
	
	public void importa(JProgressBar progressBar2, JLabel lblNewLabel_5) throws Exception {
		String pgFABRI = "select cod_laborat, nom_laborat, num_cnpj from cadlabor";
		String vFABRI = "Insert Into FABRI (Cod_Fabric, Des_Fabric, Num_Cnpj) Values (?,?,?)";
		try (PreparedStatement pVmd = vmd.prepareStatement(vFABRI);
			 PreparedStatement pPg = pg.prepareStatement(pgFABRI)) {
			
			ResultSet rs = pPg.executeQuery();
			
			// contar a qtde de registros
			int registros = a.contaRegistros("cadlabor");
			int total = registros;
			progressBar2.setMaximum(registros);
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
				lblNewLabel_5.setText(registros+"/"+total);
				progressBar2.setValue(registros);
			}
			
//			Fabricante Padrão
			pVmd.setInt(1, 9999);
			pVmd.setString(2, "A CADASTRAR");
			pVmd.setString(3, "");
			
			pVmd.executeUpdate();
			
			System.out.println("Funcionou FABRI");
			pVmd.close();
			pPg.close();
			
			progressBar2.setValue(0);

		}
	}
}
