package entidades;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import tela.App;
import conexao.Conexao;

public class Ender {
	Connection pg = Conexao.getPostgresConnection();
	Connection vmd = Conexao.getSqlConnection();
	App a = new App();
	
	public void importa(JProgressBar progressBar2, JLabel lblNewLabel_5) throws Exception {
		String msCLIEN_ENDER = "select cod_cliente, num_fone, end_cliente, bai_cliente, cep_cliente, cid_cliente, est_cliente, nom_cliente, dat_cadastro from cadclien";
		String vENDER = "Insert Into ENDER (Cod_EndFon, Des_Endere, Des_Bairro, Num_Cep, Des_Cidade, Des_Estado, Nom_Contat, Dat_Cadast) Values (?,?,?,?,?,?,?,?)";
		String vCLXED = "Insert Into CLXED (Cod_Client, Cod_EndFon) Values (?,?)";
		try (PreparedStatement pVmd = vmd.prepareStatement(vENDER);
		     PreparedStatement pPg = pg.prepareStatement(msCLIEN_ENDER);
			 PreparedStatement pVmdCLXED = vmd.prepareStatement(vCLXED);) {
			
			ResultSet rs = pPg.executeQuery();
			
			// contar a qtde de registros
			int registros = a.contaRegistros("cadclien");
			int total = registros;
			progressBar2.setMaximum(registros);
			registros = 0;
			
			while (rs.next()) {
				
				// grava no varejo
				String codigo = rs.getString("cod_cliente");
				String tel = rs.getString("num_fone");
				if(tel != null) {
					tel = tel.replaceAll("\\D", "");
					pVmd.setString(1, tel);
				}else{
					pVmd.setString(1, codigo);
				}
				
				String endereco = rs.getString("end_cliente");
				if(endereco != null) {
					pVmd.setString(2, endereco.length() > 45 ? endereco.substring(0, 45) : endereco);
				}else{
					pVmd.setString(2, endereco);
				}
				
				String bairro = rs.getString("bai_cliente");
				if(bairro != null) {
					pVmd.setString(3, bairro.length() > 25 ? bairro.substring(0, 25) : bairro);
				}else{
					pVmd.setString(3, bairro);
				}
				
				String cep = rs.getString("cep_cliente");
				if(cep != null) {
					cep = cep.replaceAll("\\D", "");
				}
				pVmd.setString(4, cep);
				
				String cidade = rs.getString("cid_cliente");
				if(cidade != null) {
					pVmd.setString(5, cidade.length() > 25 ? cidade.substring(0, 25) : cidade);
				}else{
					pVmd.setString(5, cidade);
				}
				
				String estado = rs.getString("est_cliente");
				if(estado != null) {
					pVmd.setString(6, estado.length() > 2 ? estado.substring(0, 2) : estado);
				}else{
					pVmd.setString(6, estado);
				}
				
				String contato = rs.getString("nom_cliente");
				if(contato != null) {
					pVmd.setString(7, contato.length() > 35 ? contato.substring(0, 35) : contato);
				}else{
					pVmd.setString(7, contato);
				}
				
				Date data_cadastro = rs.getDate("dat_cadastro");
				pVmd.setDate(8, data_cadastro);
				
				pVmd.executeUpdate();
				
				//CLXED
				pVmdCLXED.setString(1, codigo);
				
				if(tel != null) {
					pVmdCLXED.setString(2, tel);
				}else{
					pVmdCLXED.setString(2, codigo);
				}
				
				pVmdCLXED.executeUpdate();
				
				registros++;
				lblNewLabel_5.setText(registros+"/"+total);
				progressBar2.setValue(registros);
			}
			System.out.println("Funcionou ENDER");
			pVmd.close();
			pPg.close();
			
			progressBar2.setValue(0);

		}
	}
}
