package entidades;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JProgressBar;

import tela.App;
import conexao.Conexao;

public class Prxlj {
	Connection pg = Conexao.getPostgresConnection();
	Connection vmd = Conexao.getSqlConnection();
	App a = new App();
	
	public void importa(JProgressBar progressBar2) throws Exception {

		String mProdu = "select cod_reduzido, nom_produto, dat_cadastro, cod_laborat, cod_barra, vlr_venda, vlr_custo, flg_ativo, flg_descvenda from cadprodu";
		String vPrxlj = "Update PRXLJ set Prc_VenAtu = ?, Prc_CusLiq = ?, Prc_CusLiqMed = ?, Prc_CusEnt = ?, Flg_BlqCom = ?, Flg_BlqVen = ?, Flg_BlqDsc=? where Cod_Produt = ?";
		try (PreparedStatement  pVmd = vmd.prepareStatement(vPrxlj);
			 PreparedStatement pMs = pg.prepareStatement(mProdu)) {
			
			ResultSet rs = pMs.executeQuery();
			
			// contar a qtde de registros
			int registros = a.contaRegistros("cadprodu");
			progressBar2.setMaximum(registros);
			registros = 0;

			while (rs.next()) {
				// grava no varejo
				
				int codigo =  rs.getInt("cod_reduzido");
				String vlr_venda = rs.getString("vlr_venda"); 
				pVmd.setString(1, vlr_venda);
				
				String vlr_custo = rs.getString("vlr_custo"); 
				pVmd.setString(2, vlr_custo);
				pVmd.setString(3, vlr_custo);
				pVmd.setString(4, vlr_custo);
				
				String flg_ativo = rs.getString("flg_ativo");
				pVmd.setBoolean(5, flg_ativo.equals("A") ? true : false);
				pVmd.setBoolean(6, flg_ativo.equals("A") ? true : false);
				
				String  flg_descvenda = rs.getString("flg_descvenda");
				pVmd.setBoolean(7, flg_descvenda.equals("A") ? true : false);
				
				pVmd.setInt(8, codigo);
				
				pVmd.executeUpdate();

				registros++;
				System.out.println(registros);
				progressBar2.setValue(registros);

			}
			System.out.println("CADASTROU PRXLJ");
			pg.close();
			rs.close();
			
			progressBar2.setValue(0);
		}
	}
}
