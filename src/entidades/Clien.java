package entidades;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import tela.App;
import conexao.Conexao;

public class Clien {
	Connection pg = Conexao.getPostgresConnection();
	Connection vmd = Conexao.getSqlConnection();
	App a = new App();
	
	public void importa(JProgressBar progressBar2, JLabel lblNewLabel_5) throws Exception {
		String pgCLIEN = "select cod_cliente, nom_cliente, dat_cadastro, cod_grupo, sex_cliente, num_cnpj, num_ident, num_celular, nom_email, dat_nascto, num_fone, nom_pai, nom_mae, est_cliente from cadclien";
		String vCLIEN = "Insert Into CLIEN (Cod_Client, Nom_Client, Dat_Cadast, Cod_GrpCli, Sex_Client, Num_CpfCgc, Num_RgCgf, Num_FonCel, Des_Email, Dia_Nascim, Mes_Nascim, Ano_Nascim, Cod_EndRes, Cod_RegTri) Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try (PreparedStatement pVmd = vmd.prepareStatement(vCLIEN);
			 PreparedStatement pPg = pg.prepareStatement(pgCLIEN)) {
			
			ResultSet rs = pPg.executeQuery();
			
			// contar a qtde de registros
			int registros = a.contaRegistros("cadclien");
			int total = registros;
			progressBar2.setMaximum(registros);
			registros = 0;
			
			while (rs.next()) {
				
				// grava no varejo
				int codigo = rs.getInt("cod_cliente");
				pVmd.setInt(1, codigo);
				
				String nome = rs.getString("nom_cliente");
				if(nome != null) {
					pVmd.setString(2, nome.length() > 35 ? nome.substring(0, 35) : nome);
				}else{
					pVmd.setString(2, nome);
				}
				
				Date data_cadastro = rs.getDate("dat_cadastro");
				pVmd.setDate(3, data_cadastro);
				
				int cod_grpcli = rs.getInt("cod_grupo");
				pVmd.setInt(4, cod_grpcli);
				
				String sexo = rs.getString("sex_cliente");
				pVmd.setString(5, sexo);
				
				String cnpj = rs.getString("num_cnpj");
				if(cnpj != null) {
					cnpj = cnpj.replaceAll("\\D", "");
				}
				pVmd.setString(6, cnpj);
				
				String rg = rs.getString("num_ident");
				if(rg != null) {
					rg = rg.replaceAll("\\D", "");
				}
				pVmd.setString(7, rg);
				
				String cel = rs.getString("num_celular");
				if(cel != null) {
					cel = cel.replaceAll("\\D", "");		
				}
				pVmd.setString(8, cel);
				
				String email = rs.getString("nom_email");
				pVmd.setString(9, email);
					
				String dia_nascim = rs.getString("dat_nascto");
				if(dia_nascim != null) {
					dia_nascim = dia_nascim.substring(8,10);
				}
				pVmd.setString(10, dia_nascim);
					
					
				String mes_nascim = rs.getString("dat_nascto");
				if(mes_nascim != null) {
					mes_nascim = mes_nascim.substring(5,7);
				}
				pVmd.setString(11, mes_nascim);
				
				String ano_nascim = rs.getString("dat_nascto");
				if(ano_nascim != null) {
					ano_nascim = ano_nascim.substring(0,4);
				}
				pVmd.setString(12, ano_nascim);
				
				String fone = rs.getString("num_fone");
				if(fone != null) {
					fone = fone.replaceAll("\\D", "");		
				}
				pVmd.setString(13, fone);
			   
				String uf = rs.getString("est_cliente");
				if(uf != null){
					
//					Região tributártia
				    if(uf.equals("CE")){
				    	pVmd.setInt(14, 4);
				    }else{
				    	if(uf.equals("AC") || uf.equals("AL") || uf.equals("AP") ||
								   uf.equals("AM") || uf.equals("BA") || uf.equals("DF") ||
								   uf.equals("GO") || uf.equals("MA") || uf.equals("MT") ||
								   uf.equals("MS") || uf.equals("PB") || uf.equals("PA") ||
								   uf.equals("PE") || uf.equals("PI") || uf.equals("RN") ||
								   uf.equals("RO") || uf.equals("RR") || uf.equals("SE") || uf.equals("TO")){
							       		pVmd.setInt(14, 1);
						}else{
							if(uf.equals("ES") || uf.equals("MG") ||
							   uf.equals("PR") || uf.equals("RJ") || 
							   uf.equals("RS") || uf.equals("SC") || 
							   uf.equals("SP")){
									pVmd.setInt(14, 9);
							}else{
								pVmd.setInt(14, 4);
							}
						}
				    }
				 }
				
				pVmd.executeUpdate();

				registros++;
				lblNewLabel_5.setText(registros+"/"+total);
				progressBar2.setValue(registros);
			}
			System.out.println("Funcionou CLIEN");
			pVmd.close();
			pPg.close();
			
			progressBar2.setValue(0);

		}
	}
}
