package entidades;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import tela.App;
import conexao.Conexao;

public class Produ {
	Connection pg = Conexao.getPostgresConnection();
	Connection vmd = Conexao.getSqlConnection();
	
	App a = new App();
	
	public void importa(JProgressBar progressBar2, JLabel lblNewLabel_5) throws Exception {
		String pgPRODU= "select cod_reduzido, nom_produto, dat_cadastro, cod_laborat, cod_barra, vlr_venda, vlr_custo, flg_ativo, flg_descvenda from cadprodu";
		String vPRODU = "Insert Into Produ (Cod_Produt, Des_Produt, Des_Resumi, Des_Comple, Dat_Implan, Cod_Fabric, Cod_Ean, Cod_Classi, Cod_Seccao, Cod_GrpPrc) Values (?,?,?,?,?,?,?,?,?,?)";
		
		try (PreparedStatement pVmd = vmd.prepareStatement(vPRODU);
			 PreparedStatement pPg = pg.prepareStatement(pgPRODU)) {
			
			ResultSet rs = pPg.executeQuery();
			
			// contar a qtde de registros
			int registros = a.contaRegistros("cadprodu");
			int total = registros;
			progressBar2.setMaximum(registros);
			registros = 0;
			
			while (rs.next()) {
				
				// grava no varejo
				int codigo = rs.getInt("cod_reduzido");
				pVmd.setInt(1, codigo);
				
				String nome = rs.getString("nom_produto");
				if(nome != null) {
					pVmd.setString(2, nome.length() > 40 ? nome.substring(0, 40) : nome);
					pVmd.setString(3, nome.length() > 24 ? nome.substring(0, 24) : nome);
					pVmd.setString(4, nome.length() > 50 ? nome.substring(0, 50) : nome);
				}else{
					pVmd.setString(2, nome);
					pVmd.setString(3, nome);
					pVmd.setString(4, nome);
				}
				
				Date data_cadastro = rs.getDate("dat_cadastro");
				pVmd.setDate(5, data_cadastro);
				
			
				String cod_fabricante = rs.getString("cod_laborat");
				if (cod_fabricante != null) {
					pVmd.setString(6, cod_fabricante);
				}else {
					pVmd.setString(6, "9999");
				}
				
				String ean = rs.getString("cod_barra");
				pVmd.setString(7, ean);
				
				pVmd.setString(8, "0199");
				pVmd.setString(9, "10");
				pVmd.setString(10, "A");
				
				pVmd.executeUpdate();
				
				cadastraCamposQueFaltamPRODU(ean);

				registros++;
				lblNewLabel_5.setText(registros+"/"+total);
				progressBar2.setValue(registros);
			}
			
			System.out.println("Funcionou Produ");
			pVmd.close();
			pPg.close();
			
			progressBar2.setValue(0);

		}
	}
	
	public void cadastraCamposQueFaltamPRODU(String cod_ean) throws SQLException {
		String vProduVmdBase = "select * from PRODU where Cod_Ean = '" +cod_ean+"'";
		String vProduVmd = "Update Produ set Des_UniVen=?,Des_UniCom=? ,Qtd_FraVen=?, Cod_Classi=?, Cod_Seccao=?, Cod_SubBas=?, Cod_GrpPrc=?, Cod_AbcFar=?, Ctr_Preco=?, Ctr_Lista=?, Ctr_Venda=?, Tip_Por344=?, Ctr_Origem=?,Cod_ClaTri=?, Cod_ClaFis=?, Qtd_EmbVen=?, NUM_REGMS=?, Cod_Ncm=? where Cod_Ean = '"+cod_ean+"'";
		try (PreparedStatement v1 = Conexao.getSqlConnectionConsulta().prepareStatement(vProduVmdBase);
			 PreparedStatement v2 = Conexao.getSqlConnectionAux().prepareStatement(vProduVmd)) {
			
			ResultSet rs = v1.executeQuery();

			while (rs.next()) {
				// grava no varejo
				v2.setString(1, rs.getString("Des_UniVen"));
				v2.setString(2, rs.getString("Des_UniCom"));
				v2.setString(3, rs.getString("Qtd_FraVen"));
				v2.setString(4, rs.getString("Cod_Classi"));
			    v2.setInt(5, rs.getInt("Cod_Seccao"));
			    
			    if (rs.getInt("Cod_SubBas") != 0) {
			    	v2.setInt(6, rs.getInt("Cod_SubBas"));
			    } else {
			    	v2.setString(6, null);
			    }
			    
			    v2.setString(7, rs.getString("Cod_GrpPrc"));
			    v2.setString(8, rs.getString("Cod_AbcFar"));
			    v2.setString(9, rs.getString("Ctr_Preco"));
			    v2.setString(10, rs.getString("Ctr_Lista"));
			    v2.setString(11, rs.getString("Ctr_Venda"));
			    v2.setString(12, rs.getString("Tip_Por344"));
			    v2.setString(13, rs.getString("Ctr_Origem"));
			    v2.setString(14, rs.getString("Cod_ClaTri"));
			    v2.setString(15, rs.getString("Cod_ClaFis"));
			    v2.setString(16, rs.getString("Qtd_EmbVen"));
			    v2.setString(17, rs.getString("NUM_REGMS"));
			    v2.setString(18, rs.getString("Cod_Ncm"));
			    
				v2.executeUpdate();
			}
			v1.close();
			v2.close();
	}
	}
}
