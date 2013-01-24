package entidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import tela.App;

import conexao.Conexao;

public class Forne {
	Connection pg = Conexao.getPostgresConnection();
	Connection vmd = Conexao.getSqlConnection();
	App a = new App();
	
	public void importa(JProgressBar progressBar2, JLabel lblNewLabel_5) throws Exception {
		String pgFORNE = "select cod_fornec, nom_fornec, num_cnpj, num_inscest, cid_fornec, est_fornec, cod_municipio, end_fornec, bai_fornec, cep_fornec, cep_fornec, num_fone, num_fax, nom_vendedor, nom_observ from cadforne";
		String vFORNE = "Insert Into FORNE (Cod_Fornec, Des_RazSoc, Des_Fantas, Num_CgcCpf, Num_CgfRg, Des_Cidade, Des_Estado, Cod_RegTri, Cod_IBGE, Des_Endere, Des_Bairro, Num_Cep, Num_Fone, Num_Fax, Nom_Contat, Des_Observ, Flg_Bloque) Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,0)";
		try (PreparedStatement pVmd = vmd.prepareStatement(vFORNE);
			 PreparedStatement pPg = pg.prepareStatement(pgFORNE)) {
			
			ResultSet rs = pPg.executeQuery();
			
			// contar a qtde de registros
			int registros = a.contaRegistros("cadforne");
			int total = registros;
			progressBar2.setMaximum(registros);
			registros = 0;
			
			while (rs.next()) {
						
				// grava no varejo
				int codigo = rs.getInt("cod_fornec");
				pVmd.setInt(1, codigo);
				
				String razao = rs.getString("nom_fornec");
				if(razao != null) {
					pVmd.setString(2, razao.length() > 35 ? razao.substring(0, 35) : razao);
					pVmd.setString(3, razao.length() > 25 ? razao.substring(0, 25) : razao);
				}else {
					pVmd.setString(2, razao);
					pVmd.setString(3, razao);
				}
				
				String cnpj = rs.getString("num_cnpj");
				if(cnpj != null) {
					cnpj = cnpj.replaceAll("\\D", "");	
				}
				pVmd.setString(4, cnpj);
				
				
				String insc_est = rs.getString("num_inscest");
				if(insc_est != null) {
					insc_est = insc_est.replaceAll("\\D", "");
				}
				pVmd.setString(5, insc_est);
				
				String cidade = rs.getString("cid_fornec");
				if(cidade != null) {
					pVmd.setString(6, cidade.length() > 25 ? cidade.substring(0, 25) : cidade);
				}else {
					pVmd.setString(6, cidade);
				}
				
				String uf = rs.getString("est_fornec");
				pVmd.setString(7, uf);
				
				if(uf != null) {
//					Região tributártia
				    if(uf.equals("CE")){
				    	pVmd.setInt(8, 4);
				    }else {
				    	if(uf.equals("AC") || uf.equals("AL") || uf.equals("AP") ||
						   uf.equals("AM") || uf.equals("BA") || uf.equals("DF") ||
						   uf.equals("GO") || uf.equals("MA") || uf.equals("MT") ||
						   uf.equals("MS") || uf.equals("PB") || uf.equals("PA") ||
						   uf.equals("PE") || uf.equals("PI") || uf.equals("RN") ||
						   uf.equals("RO") || uf.equals("RR") || uf.equals("SE") || uf.equals("TO")){
					    	pVmd.setInt(8, 1);
						}else {
							if(uf.equals("ES") || uf.equals("MG") ||
						       uf.equals("PR") || uf.equals("RJ") || 
							   uf.equals("RS") || uf.equals("SC") || 
							   uf.equals("SP")){
						    	pVmd.setInt(8, 9);
							}else {
								pVmd.setString(8, null);
						}
				    }
				 }
				}else{
					pVmd.setString(8, null);
				}
				
			    String ibge = rs.getString("cod_municipio");
			    if(ibge != null) {
			    	ibge = ibge.replaceAll("\\D", "");
			    }
			    pVmd.setString(9, "");
			    
			    String endereco = rs.getString("end_fornec");
			    if(endereco != null) {
			    	pVmd.setString(10, endereco.length() > 25 ? endereco.substring(0, 25) : endereco);
			    }else {
			    	pVmd.setString(10, endereco);
			    }
			    
			    String bairro = rs.getString("bai_fornec");
			    if(bairro != null) {
			    	pVmd.setString(11, bairro.length() > 25 ? bairro.substring(0, 25) : bairro);
			    }else {
			    	pVmd.setString(11, bairro);
			    }
			    
			    String cep = rs.getString("cep_fornec");
			    if(cep != null) {
			    	cep = cep.replaceAll("\\D", "");
			    }
			    pVmd.setString(12, cep);
			    
			    String tel = rs.getString("num_fone");
			    if(tel != null) {
			    	tel = tel.replaceAll("\\D", "");
			    	pVmd.setString(13, tel.length() > 11 ? tel.substring(0, 11) : tel);
			    }else {
			    	pVmd.setString(13, tel);
			    }
			    
			    String fax = rs.getString("num_fax");
			    if(fax != null) {
			    	fax = fax.replaceAll("\\D", "");
			    	pVmd.setString(14, fax.length() > 11 ? fax.substring(0, 11) : fax);
			    }else {
			    	pVmd.setString(14, fax);
			    }
			    
			    String contato = rs.getString("nom_vendedor");
			    if(contato != null) {
			    	pVmd.setString(15, contato.length() > 25 ? contato.substring(0, 25) : contato);
			    }else {
			    	pVmd.setString(15, contato);
			    }
			    
			    String obs = rs.getString("nom_observ");
			    if(obs != null) {
			    	pVmd.setString(16, obs.length() > 16 ? obs.substring(0, 16) : obs);
			    }else {
			    	pVmd.setString(16, obs);
			    }
			    
				pVmd.executeUpdate();

				registros++;
				lblNewLabel_5.setText(registros+"/"+total);
				progressBar2.setValue(registros);
			}
			System.out.println("Funcionou FORNE");
			pVmd.close();
			pPg.close();
			
			progressBar2.setValue(0);

		}
	}
}
