package tela;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import net.miginfocom.swing.MigLayout;
import conexao.Conexao;

public class App extends JFrame {

	private static final long serialVersionUID = 1L;

	private JCheckBox cboxPRODU;
	private JCheckBox cboxFABRI;
	private JTextField txtFBBanco;
	private JTextField txtVmdServidor;
	private JTextField txtVmdBanco;
	private JButton btn_limpa_dados;
	private JProgressBar progressBar;
	private JButton btn_processa;
	private JProgressBar progressBar2;
	private JCheckBox cboxFORNE;
	private JCheckBox cboxCLIEN;
	private JCheckBox cboxENDER;
	private JCheckBox cboxTBNCM;
	private JCheckBox cboxTBSEC;
	private JCheckBox cboxPRXLJ;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App frame = new App();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public App() {
		setTitle("inFarma - Conversor de dados");
		setResizable(false);
		setBounds(100, 100, 460, 314);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panelTop = new JPanel();
		getContentPane().add(panelTop, BorderLayout.NORTH);
		panelTop.setLayout(new MigLayout("", "[fill][grow][][grow]", "[][][][]"));
		
		JLabel lblNewLabel = new JLabel("BD Antigo");
		panelTop.add(lblNewLabel, "cell 0 0,alignx trailing");
		
		txtFBBanco= new JTextField();
		txtFBBanco.setText("softpharma");
		panelTop.add(txtFBBanco, "cell 1 0,growx");
		txtFBBanco.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("VMD Servidor");
		panelTop.add(lblNewLabel_1, "cell 0 1,alignx trailing");

		txtVmdServidor = new JTextField();
		txtVmdServidor.setText("localhost");
		panelTop.add(txtVmdServidor, "cell 1 1,growx");
		txtVmdServidor.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Banco");
		panelTop.add(lblNewLabel_2, "cell 2 1,alignx trailing,aligny baseline");

		txtVmdBanco = new JTextField();
		txtVmdBanco.setText("VMD_Vazio");
		panelTop.add(txtVmdBanco, "cell 3 1,growx");
		txtVmdBanco.setColumns(10);

		JLabel lblNewLabel_3 = new JLabel("Converte uma base para o Varejo");
		panelTop.add(lblNewLabel_3, "cell 0 3 4 1");

		final JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);

		class ProcessaWorker extends SwingWorker<Void, Void> {

			@Override
			protected Void doInBackground() throws Exception {
				progressBar.setValue(0);
				progressBar.setMaximum(31);
				btn_limpa_dados.setEnabled(false);
				btn_processa.setEnabled(false);
				
				int resp = JOptionPane.showConfirmDialog(panel, "Confirma?", "Processar Dados", JOptionPane.YES_NO_OPTION);
																		
				if (resp == 0) {
					Connection ms = Conexao.getMysqlConnection();
					Connection vmd = Conexao.getSqlConnection();
						
					if (cboxTBNCM.isSelected() && cboxTBSEC.isSelected()
						 && cboxFABRI.isSelected() && cboxPRODU.isSelected() 
						 && cboxCLIEN.isSelected() && cboxFORNE.isSelected() 
						 && cboxENDER.isSelected()) {

						// APAGANDO DADOS
						// PRODUTO
						try (Statement stmt = vmd.createStatement()) {
							stmt.executeUpdate("DELETE FROM PRODU");
							stmt.close();
							System.out.println("Deletou PRODU");
							progressBar.setValue(1);
						}

						// FABRI
						try (Statement stmt = vmd.createStatement()) {
							stmt.executeUpdate("DELETE FROM FABRI");
							stmt.close();
							System.out.println("Deletou FABRI");
							progressBar.setValue(2);
						}

						// FORNE					
						try (Statement stmt = Conexao.getSqlConnection().createStatement()) {
							stmt.executeUpdate("DELETE FROM FORNE");
							stmt.close();
							System.out.println("Deletou FORNE");
							progressBar.setValue(3);
						}
						
						// TBNCM
						try (Statement stmt = Conexao.getSqlConnection().createStatement()) {
							stmt.executeUpdate("DELETE FROM TBNCM");
							stmt.close();
							System.out.println("Deletou TBNCM");
							progressBar.setValue(4);
						}
						
						// TBSEC
						try (Statement stmt = Conexao.getSqlConnection().createStatement()) {
							stmt.executeUpdate("DELETE FROM TBSEC");
							stmt.close();
							System.out.println("Deletou TBSEC");
							progressBar.setValue(5);
						}
						
						// CLIEN					
						try (Statement stmt = Conexao.getSqlConnection().createStatement()) {
							stmt.executeUpdate("DELETE FROM CLIEN");
							stmt.close();
							System.out.println("Deletou CLIEN");
							progressBar.setValue(6);
						}
						
						// CLXED
						try (Statement stmt = Conexao.getSqlConnection().createStatement()) {
							stmt.executeUpdate("DELETE FROM CLXED");
							stmt.close();
							System.out.println("Deletou CLXED");
							progressBar.setValue(7);
						}
						
						// ENDER
						try (Statement stmt = Conexao.getSqlConnection().createStatement()) {
							stmt.executeUpdate("DELETE FROM ENDER");
							stmt.close();
							System.out.println("Deletou ENDER");
							progressBar.setValue(8);
						}
					}

					// IMPORTAÇÃO
					// TBNCM
					if (cboxTBNCM.isSelected()) {
						System.out.println("COMEÇOU TBNCM");
						try (Statement stmt = vmd.createStatement()) {
							stmt.executeUpdate("DELETE FROM TBNCM");
							stmt.close();
							System.out.println("Deletou TBNCM");
							progressBar.setValue(9);
						}
						
						progressBar2.setValue(0);
						
						String msGRPRC = "SELECT * FROM ESTNCM";
						String vGRPRC = "Insert Into TBNCM (Cod_Ncm, Des_Ncm) Values (?,?)";
						try (PreparedStatement pVmd = vmd.prepareStatement(vGRPRC);
							 PreparedStatement pMs = ms.prepareStatement(msGRPRC)) {
							ResultSet rs = pMs.executeQuery();
							
							// contar a qtde de registros
							int registros = contaRegistros("ESTNCM");
							progressBar2.setMaximum(registros);
							registros = 0;

							while (rs.next()) {
								// grava no varejo
								String ncm = rs.getString("ncm_codigo");
								if(ncm != null) {
									ncm = ncm.replaceAll("\\D", "");
									if(ncm.length() <= 8){
										pVmd.setString(1, rs.getString("ncm_codigo"));
										pVmd.setString(2, rs.getString("ncm_descricao"));
										pVmd.executeUpdate();
									}
								}

								registros++;
								progressBar2.setValue(registros);
							}
							System.out.println("Funcionou TBNCM");
							pVmd.close();
							pMs.close();

							progressBar2.setValue(0);
						}
						progressBar.setValue(10);
					}
					
					//TBSEC
					if (cboxTBSEC.isSelected()) {
						System.out.println("COMEÇOU TBSEC");
						try (Statement stmt = vmd.createStatement()) {
							stmt.executeUpdate("IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS C WHERE TABLE_NAME = 'tbsec' AND TABLE_SCHEMA = 'dbo' AND COLUMN_NAME = 'cod_aux' AND DATA_TYPE = 'VARCHAR' AND CHARACTER_MAXIMUM_LENGTH = 15 ) BEGIN ALTER TABLE tbsec ADD cod_aux VARCHAR(15) END ");
							stmt.close();
							System.out.println("CRIOU COLUNA AUX EM TBSEC");
							progressBar.setValue(11);
						}

						try (Statement stmt = vmd.createStatement()) {
							stmt.executeUpdate("DELETE FROM TBSEC");
							stmt.close();
							System.out.println("Deletou TBSEC");
							progressBar.setValue(12);
						}

						progressBar2.setValue(0);

						String msTBSEC = "SELECT * FROM ESTSEC";
						String vTBSEC = "Insert Into TBSEC (Cod_Seccao, Des_Seccao, Cod_Aux) Values (?,?,?)";
						try (PreparedStatement pVmd = vmd.prepareStatement(vTBSEC);
							 PreparedStatement pMs = ms.prepareStatement(msTBSEC)) {
							
							ResultSet rs = pMs.executeQuery();

							// contar a qtde de registros
							int registros = contaRegistros("ESTSEC");
							progressBar2.setMaximum(registros);
							registros = 0;

							while (rs.next()) {
							 // grava no varejo
								pVmd.setInt(1, prox("Cod_Seccao", "TBSEC"));
							    pVmd.setString(2, rs.getString("sec_descricao"));
							    pVmd.setString(3, rs.getString("sec_secao"));
							    pVmd.executeUpdate();

								registros++;
								progressBar2.setValue(registros);
							}
							
							System.out.println("Funcionou TBSEC");
							pVmd.close();
							pMs.close();

							progressBar2.setValue(0);
						}
						progressBar.setValue(13);
					}
						
					//FABRI
					if (cboxFABRI.isSelected()) {
						System.out.println("COMEÇOU FABRI");
						try (Statement stmt = vmd.createStatement()) {
							stmt.executeUpdate("DELETE FROM FABRI");
							stmt.close();
							System.out.println("Deletou FABRI");
							progressBar.setValue(14);
						}

						progressBar2.setValue(0);

						String msFabri = "SELECT * FROM ESTLAB";
						String vFabri = "Insert Into FABRI (Cod_Fabric, Des_Fabric, Num_Cnpj) Values (?,?,?)";
						try (PreparedStatement pVmd = vmd.prepareStatement(vFabri);
							 PreparedStatement pFb = ms.prepareStatement(msFabri)) {
							
							ResultSet rs = pFb.executeQuery();

							// contar a qtde de registros
							int registros = contaRegistros("ESTLAB");
							progressBar2.setMaximum(registros);
							registros = 0;

							while (rs.next()) {
							 // grava no varejo
								pVmd.setInt(1, rs.getInt("lab_laboratorio"));
							    pVmd.setString(2, rs.getString("lab_razao").length() > 25 ? rs.getString("lab_razao").substring(0, 24) : rs.getString("lab_razao"));
								pVmd.setString(3, rs.getString("lab_cnpj").length() > 14 ? rs.getString("lab_cnpj").substring(0, 14) : rs.getString("lab_cnpj"));
								
								pVmd.executeUpdate();

								registros++;
								progressBar2.setValue(registros);
							}
							
							System.out.println("Funcionou Fabri");
							pVmd.close();
							pFb.close();

							progressBar2.setValue(0);
						}
						progressBar.setValue(15);
					}
						
					//PRODU
					if (cboxPRODU.isSelected()) {
						System.out.println("COMEÇOU PRODU");
						try (Statement stmt = vmd.createStatement()) {
							stmt.executeUpdate("DELETE FROM PRODU");
							System.out.println("Deletou PRODU");
							progressBar.setValue(16);
						}

						progressBar2.setValue(0);

						String MsPRODU = "select * from estcad";
						String vPRODU = "Insert Into PRODU (Cod_Produt, Des_Produt, Des_Resumi, Des_Comple, Dat_Implan, Cod_Fabric, Cod_EAN, Cod_Ncm, Qtd_FraVen, Qtd_EmbVen, Ctr_Origem, Cod_Classi, Cod_Seccao, Cod_ClaTri, Ctr_Preco, Ctr_Lista, Ctr_Venda, Cod_GrpPrc) Values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
						try (PreparedStatement pVmd = vmd.prepareStatement(vPRODU);
							 PreparedStatement pMs = ms.prepareStatement(MsPRODU);) {
							
							ResultSet rs = pMs.executeQuery();

							// contar a qtde de registros
							int registros = contaRegistros("ESTCAD");
							progressBar2.setMaximum(registros);
							registros = 0;
							
							while (rs.next()) {
							 // GRAVA NA PRODU
								pVmd.setInt(1, rs.getInt("CAD_CODIGO"));
							    pVmd.setString(2, rs.getString("CAD_DESCRICAO").length() > 40 ? rs.getString("CAD_DESCRICAO").substring(0, 39) : rs.getString("CAD_DESCRICAO"));
							    pVmd.setString(3, rs.getString("CAD_DESCRICAO").length() > 24 ? rs.getString("CAD_DESCRICAO").substring(0, 23) : rs.getString("CAD_DESCRICAO"));
							    pVmd.setString(4, rs.getString("CAD_DESCRICAO").length() > 50 ? rs.getString("CAD_DESCRICAO").substring(0, 49) : rs.getString("CAD_DESCRICAO"));
							    pVmd.setDate(5, rs.getDate("CAD_DT_CADASTRO"));
							    pVmd.setString(6, rs.getString("CAD_LABORATORIO"));
							    pVmd.setString(7, rs.getString("CAD_COD_BARRA"));
							   
							    String ncm = rs.getString("CAD_NCM");
							    if(ncm != null) {
							    	ncm = ncm.replaceAll("\\D", "");
							    	if(ncm.length() <= 8 && !ncm.equals("")) {
							    		pVmd.setString(8, ncm);
									}else{
										pVmd.setString(8, "30049099");
									}
							    	pVmd.setString(8, "30049099");
							    }
							    
						    	pVmd.setString(9, rs.getString("CAD_QTDE_CAIXA"));
						    	pVmd.setInt(10, rs.getInt("CAD_QTDE_EMBALAGEM"));
						    	pVmd.setInt(11, 0);
						    	pVmd.setString(12, "0199");
						    	pVmd.setInt(13, 1);
						    	pVmd.setString(14, "A");
						    	pVmd.setString(15, "C");
						    	pVmd.setString(16, "N");
						    	pVmd.setString(17, "L");
						    	pVmd.setString(18, "A");
								
								pVmd.executeUpdate();
								
								//cadastraCamposQueFaltamPRODU(rs.getString("CODBARRA"));
								registros++;
								progressBar2.setValue(registros);
							}
							
							System.out.println("Funcionou PRODU");
							pVmd.close();
							pMs.close();

							progressBar2.setValue(0);
						}
						progressBar.setValue(17);
					}
					
					// PRXLJ
					if (cboxPRXLJ.isSelected()) {
						
						progressBar.setValue(18);
						
						String mProdu = "select CAD_CODIGO, CAD_CUSTO_MEDIO, CAD_ULT_PCOMPRA, CAD_PCUSTO from estcad1";
						String vPrxlj = "Update PRXLJ set Prc_CusLiqMed = ?, Prc_CusEnt = ?, Prc_CusLiq = ?, Prc_VenAtu = ? where Cod_Produt = ?";
						try (PreparedStatement  pVmd = vmd.prepareStatement(vPrxlj);
							 PreparedStatement pMs = ms.prepareStatement(mProdu)) {
							
							ResultSet rs = pMs.executeQuery();
							
							// contar a qtde de registros
							int registros = contaRegistros("ESTCAD1");
							progressBar2.setMaximum(registros);
							registros = 0;

							while (rs.next()) {
								// grava no varejo
								System.out.println(rs.getInt("CAD_CODIGO"));
								pVmd.setInt(1, rs.getInt("CAD_CUSTO_MEDIO"));
								pVmd.setString(2, rs.getString("CAD_ULT_PCOMPRA"));
								pVmd.setString(3, rs.getString("CAD_ULT_PCOMPRA"));
								pVmd.setString(4, rs.getString("CAD_PCUSTO"));
								pVmd.setInt(5, rs.getInt("CAD_CODIGO"));
								
								pVmd.executeUpdate();
								
								registros++;
								progressBar2.setValue(registros);

							}
							System.out.println("CADASTROU PRXLJ");
							ms.close();
							rs.close();
							
							progressBar2.setValue(0);
					}
						
						progressBar.setValue(19);
					}
					
					//FORNE					
					if (cboxFORNE.isSelected()) {
						System.out.println("COMEÇOU FORNE");
						try (Statement stmt = vmd.createStatement()) {
							stmt.executeUpdate("DELETE FROM FORNE");
							stmt.close();
							System.out.println("Deletou FORNE");
							progressBar.setValue(20);
						}

						progressBar2.setValue(0);

						String msFORNE = "select a.*, b.cid_cod_ibge as cod_ibge, b.cid_nome as cidade, b.cid_uf as uf from cpacdf a join carcid b on b.cid_cidade = a.cdf_cidade";
						String vFORNE = "Insert Into FORNE (Cod_Fornec, Num_CgcCpf, Num_CgfRg, Des_RazSoc, Des_Fantas, Cod_IBGE, Des_Endere, Des_Bairro, Num_Cep, Flg_Bloque, Num_Fone, Num_Fax, Des_Estado, Des_Cidade, Cod_RegTri, Nom_Contat, Des_Observ) Values (?,?,?,?,?,?,?,?,?,0,?,?,?,?,?,?,?)";
						try (PreparedStatement pVmd = vmd.prepareStatement(vFORNE);
							 PreparedStatement pMs = ms.prepareStatement(msFORNE)) {
							
							ResultSet rs = pMs.executeQuery();

							// contar a qtde de registros
							int registros = contaRegistros("CPACDF");
							progressBar2.setMaximum(registros);
							registros = 0;
							

							while (rs.next()) {
								String tel = rs.getString("CDF_FONE");
								if(tel != null) {
									tel = tel.replaceAll("\\D", "");
								}
										
								// grava no varejo
								pVmd.setInt(1, rs.getInt("CDF_COD_FORN"));
							    pVmd.setString(2, rs.getString("CDF_CNPJ").replace(".", "").replace("/", "").replace("-", ""));
							    pVmd.setString(3, rs.getString("CDF_INSCRICAO").replace(".", "").replace("/", "").replace("-", ""));
							    pVmd.setString(4, rs.getString("CDF_RAZAO").length() > 35 ? rs.getString("CDF_RAZAO").substring(0, 34) : rs.getString("CDF_RAZAO"));
							    pVmd.setString(5, rs.getString("CDF_RAZAO").length() > 25 ? rs.getString("CDF_RAZAO").substring(0, 24) : rs.getString("CDF_RAZAO"));
							    pVmd.setInt(6, rs.getInt("COD_IBGE"));
							    pVmd.setString(7, rs.getString("CDF_ENDERECO").length() > 25 ? rs.getString("CDF_ENDERECO").substring(0, 24) : rs.getString("CDF_ENDERECO"));
							    pVmd.setString(8, rs.getString("CDF_BAIRRO").length() > 25 ? rs.getString("CDF_BAIRRO").substring(0, 24) : rs.getString("CDF_BAIRRO"));
							    pVmd.setString(9, rs.getString("CDF_CEP").replace(".", "").replace("/", "").replace("-", ""));
							    
							    pVmd.setString(10, tel.length() > 11 ? tel.substring(0, 11) : tel);
							    pVmd.setString(11, rs.getString("CDF_FAX").replace(".", "").replace("(", "").replace(")", "").replace("-", "").replaceAll(" ", ""));
							    pVmd.setString(12, rs.getString("UF"));
							    pVmd.setString(13, rs.getString("CIDADE").length() > 25 ? rs.getString("CIDADE").substring(0, 24) : rs.getString("CIDADE"));
							    
							    if(rs.getString("UF").equals("CE")){
							    	pVmd.setInt(14, 4);
							    }
							    
							    if(rs.getString("UF").equals("AC") || rs.getString("UF").equals("AL") || rs.getString("UF").equals("AP") ||
								   rs.getString("UF").equals("AM") || rs.getString("UF").equals("BA") || rs.getString("UF").equals("DF") ||
								   rs.getString("UF").equals("GO") || rs.getString("UF").equals("MA") || rs.getString("UF").equals("MT") ||
								   rs.getString("UF").equals("MS") || rs.getString("UF").equals("PB") || rs.getString("UF").equals("PA") ||
								   rs.getString("UF").equals("PE") || rs.getString("UF").equals("PI") || rs.getString("UF").equals("RN") ||
								   rs.getString("UF").equals("RO") || rs.getString("UF").equals("RR") || rs.getString("UF").equals("SE") || rs.getString("UF").equals("TO")){
							    	pVmd.setInt(14, 1);
							    }
							    
							    if(rs.getString("UF").equals("ES") || rs.getString("UF").equals("MG") ||
							       rs.getString("UF").equals("PR") || rs.getString("UF").equals("RJ") || 
								   rs.getString("UF").equals("RS") || rs.getString("UF").equals("SC") || 
								   rs.getString("UF").equals("SP")){
							       
							    	pVmd.setInt(14, 9);
							    }
							    
							    pVmd.setString(15, rs.getString("CDF_CONTATO").length() > 25 ? rs.getString("CDF_CONTATO").substring(0, 24) : rs.getString("CDF_CONTATO"));
							    pVmd.setString(16, rs.getString("CDF_OBS").length() > 16 ? rs.getString("CDF_OBS").substring(0, 15) : rs.getString("CDF_OBS"));
							    
								// todo demais campo
								pVmd.executeUpdate();

								registros++;
								progressBar2.setValue(registros);
							}
							System.out.println("Funcionou FORNE");
							pVmd.close();
							pMs.close();

							progressBar2.setValue(0);
						}
						progressBar.setValue(21);
					}
					
					//CLIEN
					if (cboxCLIEN.isSelected()) {
						System.out.println("COMEÇOU CLIEN");
						  try (Statement stmt = vmd.createStatement()) {
								stmt.executeUpdate("IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS C WHERE TABLE_NAME = 'clien' AND TABLE_SCHEMA = 'dbo' AND COLUMN_NAME = 'clien_aux' AND DATA_TYPE = 'INT') BEGIN ALTER TABLE clien ADD clien_aux INT END ");
								stmt.close();
								System.out.println("CRIOU COLUNA clien_AUX");
								progressBar.setValue(22);
							}
							
							try (Statement stmt = vmd.createStatement()) {
									stmt.executeUpdate("IF NOT EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS C WHERE TABLE_NAME = 'clien' AND TABLE_SCHEMA = 'dbo' AND COLUMN_NAME = 'empre_aux' AND DATA_TYPE = 'INT') BEGIN ALTER TABLE clien ADD empre_aux INT END ");
									stmt.close();
									System.out.println("CRIOU COLUNA empre_AUX");
									progressBar.setValue(23);
								}

							try (Statement stmt = vmd.createStatement()) {
								stmt.executeUpdate("DELETE FROM CLIEN");
								stmt.close();
								System.out.println("Deletou");
								progressBar.setValue(24);
							}

							progressBar2.setValue(0);

						String msCLIEN = "select a.*,b.cid_nome as cidade, b.cid_uf as uf, c.bai_nome as bairro from carcdc a left join cadbai c on c.bai_codigo = a.cdc_bairro and c.bai_cidade = a.cdc_cidade left join carcid b on b.cid_cidade = a.cdc_cidade";
						String vCLIEN = "Insert Into CLIEN (Cod_Client, Nom_Client, Dat_Cadast, Num_CpfCgc, Num_RgCgf, Num_FonCel, Des_Email, Des_Observ, Cod_RegTri, clien_aux, empre_aux, Sex_Client, Cod_GrpCli, Ctr_Vencim, Dat_UltCpr, Cod_EndRes) Values (?,?,?,?,?,?,?,?,?,?,?,null,1,'N',?,?)";
						try (PreparedStatement pVmd = vmd.prepareStatement(vCLIEN);
							 PreparedStatement pMs = ms.prepareStatement(msCLIEN)) {
							
							ResultSet rs = pMs.executeQuery();

							// contar a qtde de registros
							int registros = contaRegistros("CARCDC");
							progressBar2.setMaximum(registros);
							registros = 0;

							while (rs.next()) {
								
								// grava no varejo
								int codigo = prox("Cod_Client", "CLIEN");
								pVmd.setInt(1, codigo);
							    pVmd.setString(2, rs.getString("CDC_RAZAO").length() > 35 ? rs.getString("CDC_RAZAO").substring(0, 34) : rs.getString("CDC_RAZAO"));
							    pVmd.setDate(3, rs.getDate("CDC_DT_CADASTRO"));
							    
							    String cpf = rs.getString("CDC_CPF") ;
							    if(cpf != null){
							    	 cpf = cpf.replaceAll("\\D", "");
							    pVmd.setString(4, cpf.length() > 14 ? cpf.substring(0,14) : cpf );
							    }
							    
							    String rg = rs.getString("CDC_RG") ;
							    if(rg != null){
							    	rg = rg.replaceAll("\\D", "");
							    pVmd.setString(5, rg.length() > 15 ? rg.substring(0,15) : rg );
							    }
							    
							    String cel = rs.getString("CDC_CELULAR") ;
							    if(cel != null){
							    	cel = cel.replaceAll("\\D", "");
							    pVmd.setString(6, cel.length() > 15 ? cel.substring(0,15) : cel );
							    }
							    
							    pVmd.setString(7, rs.getString("CDC_EMAIL"));
							    
							    String obs = rs.getString("CDC_OBSERVACAO1") ;
							    if(obs != null){
							    pVmd.setString(8, obs.length() > 16 ? obs.substring(0,16) : obs );
							    }
							    
							    if(rs.getString("UF").equals("CE")){
							    	pVmd.setInt(9, 4);
							    }
							    if(rs.getString("UF").equals("AC") || rs.getString("UF").equals("AL") || rs.getString("UF").equals("AP") ||
								   rs.getString("UF").equals("AM") || rs.getString("UF").equals("BA") || rs.getString("UF").equals("DF") ||
								   rs.getString("UF").equals("GO") || rs.getString("UF").equals("MA") || rs.getString("UF").equals("MT") ||
								   rs.getString("UF").equals("MS") || rs.getString("UF").equals("PB") || rs.getString("UF").equals("PA") ||
								   rs.getString("UF").equals("PE") || rs.getString("UF").equals("PI") || rs.getString("UF").equals("RN") ||
								   rs.getString("UF").equals("RO") || rs.getString("UF").equals("RR") || rs.getString("UF").equals("SE") || rs.getString("UF").equals("TO")){
							    	pVmd.setInt(9, 1);
							    }
							    
							    if(rs.getString("UF").equals("ES") || rs.getString("UF").equals("MG") ||
									       rs.getString("UF").equals("PR") || rs.getString("UF").equals("RJ") || 
									       rs.getString("UF").equals("RS") || rs.getString("UF").equals("SC") || 
									       rs.getString("UF").equals("SP")){
							    	pVmd.setInt(9, 9);
							    }
							    
							   pVmd.setString(10, rs.getString("CDC_CLIENTE"));
							   pVmd.setString(11, rs.getString("CDC_EMPRESA"));
							   pVmd.setDate(12, rs.getDate("CDC_DT_ULT_COMPRA"));
							   
							   String tel = rs.getString("CDC_TELEFONE") ;
							   if(tel != null && !tel.equals("")){
								   tel = tel.replace("\\D", "");
								   if(tel.length() > 15){
									   tel = tel.substring(0,15);
								   }
								   pVmd.setString(13, tel);
							   }else{
								   pVmd.setString(13, Integer.toString(codigo));
							   }
							   
								pVmd.executeUpdate();

								registros++;
								progressBar2.setValue(registros);
							}
							System.out.println("Funcionou CLIEN");
							pVmd.close();
							pMs.close();

							progressBar2.setValue(0);
						}
						progressBar.setValue(25);
						
					}
					
					//ENDER
					if (cboxENDER.isSelected()) {
						System.out.println("COMEÇOU ENDER");
						try (Statement stmt = vmd.createStatement()) {
							stmt.executeUpdate("DELETE FROM CLXED");
							stmt.close();
							System.out.println("Deletou");
							progressBar.setValue(26);
						}
						
						try (Statement stmt = vmd.createStatement()) {
							stmt.executeUpdate("DELETE FROM ENDER");
							stmt.close();
							System.out.println("Deletou");
							progressBar.setValue(27);
						}

						progressBar2.setValue(0);

						String msCLIEN_ENDER = "select a.cdc_cliente, a.cdc_cpf, a.cdc_empresa, a.cdc_telefone, a.cdc_razao, a.cdc_endereco, a.cdc_cep, a.cdc_dt_cadastro, " +
								                "a.cdc_proximo, a.cdc_complemento, b.cid_nome as cidade, b.cid_uf as uf, c.bai_nome as bairro " +
								                "from carcdc a left join cadbai c on c.bai_codigo = a.cdc_bairro and c.bai_cidade = a.cdc_cidade " +
								                "left join carcid b on b.cid_cidade = a.cdc_cidade";
						String vENDER = "Insert Into ENDER (Cod_EndFon, Nom_Contat, Des_Endere, Des_Bairro, Num_CEP, Des_Estado, Des_Cidade, Dat_Cadast) Values (?,?,?,?,?,?,?,?)";
						String vCLXED = "Insert Into CLXED (Cod_Client, Cod_EndFon) Values (?,?)";
						//String vCLIEN = "Update CLIEN set Cod_EndRes = ? where Cod_Client = ?";
						try (PreparedStatement pVmd = vmd.prepareStatement(vENDER);
							 PreparedStatement pMS = ms.prepareStatement(msCLIEN_ENDER);
							PreparedStatement pVmdCLXED = vmd.prepareStatement(vCLXED);
							// PreparedStatement pVmdCLIEN = vmd.prepareStatement(vCLIEN)
									 ) {
							
							ResultSet rs = pMS.executeQuery();

							// contar a qtde de registros
							int registros = contaRegistros("CARCDC");
							progressBar2.setMaximum(registros);
							registros = 0;

							while (rs.next()) {
								// grava no varejo
								//esta indo codigo repetido
							  int codigo = getCod_Clien(rs.getInt("CDC_CLIENTE"), rs.getInt("CDC_EMPRESA"));
								
								String tel = rs.getString("CDC_TELEFONE");
							   if(tel != null){
								   tel = tel.replaceAll("\\D", "");
								   if(tel.length() > 15){
									   tel = tel.substring(0,15);
								   }
								   
								   if(!existeCod_EndFon(tel)){
									   pVmd.setString(1, tel);
								  
							   
							   String razao = rs.getString("CDC_RAZAO");
							   if(razao != null){
								 pVmd.setString(2, razao.length() > 35 ? razao.substring(0, 35) : razao); 
							   }
							   
							   pVmd.setString(3, rs.getString("CDC_ENDERECO"));
							    
							    String bairro = rs.getString("BAIRRO");
							    if(bairro != null){
							    	pVmd.setString(4, bairro.length() > 25 ? bairro.substring(0, 25) : bairro);
							    }
							   
							   
							    String cep = rs.getString("CDC_CEP") ;
								   if(cep != null){
									   cep = cep.replaceAll("\\D", "");
									   pVmd.setString(5, cep.length() > 8 ? cep.substring(0,8) : cep);
								   }
							    
							    pVmd.setString(6, rs.getString("UF"));
							    
							    String cidade = rs.getString("CIDADE");
							    if(cidade != null){
							    	pVmd.setString(7, cidade.length() > 25 ? cidade.substring(0,15) : cidade);
							    }
							    
							    pVmd.setDate(8, rs.getDate("CDC_DT_CADASTRO"));
								pVmd.executeUpdate();
								
								//CLXED
								pVmdCLXED.setInt(1, codigo);
								pVmdCLXED.setString(2, tel);
								pVmdCLXED.executeUpdate();
								
								//CLIEN
								//pVmdCLIEN.setString(1, wfone);
								//pVmdCLIEN.setString(2, rs.getString("CODCLI"));
								//pVmdCLIEN.executeUpdate();


								registros++;
								progressBar2.setValue(registros);
								 }
								 }
							}
							System.out.println("Funcionou ENDER");
							pVmd.close();
							pMS.close();

							progressBar2.setValue(0);
						}
						progressBar.setValue(28);
						
					}
					
					  try (Statement stmt = vmd.createStatement()) {
							stmt.executeUpdate("IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS C WHERE TABLE_NAME = 'tbsec' AND TABLE_SCHEMA = 'dbo' AND COLUMN_NAME = 'cod_aux' AND DATA_TYPE = 'VARCHAR' AND CHARACTER_MAXIMUM_LENGTH = 15 ) BEGIN ALTER TABLE tbsec DROP COLUMN cod_aux END ");
							stmt.close();
							System.out.println("DELETOU COLUNA COD_AUX DA TBSEC");
							progressBar.setValue(29);
						}
					  
					  try (Statement stmt = vmd.createStatement()) {
							stmt.executeUpdate("IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS C WHERE TABLE_NAME = 'clien' AND TABLE_SCHEMA = 'dbo' AND COLUMN_NAME = 'clien_aux' AND DATA_TYPE = 'VARCHAR' AND CHARACTER_MAXIMUM_LENGTH = 15 ) BEGIN ALTER TABLE clien DROP COLUMN clien_aux END ");
							stmt.close();
							System.out.println("DELETOU COLUNA CLIEN_AUX DA CLIEN");
							progressBar.setValue(30);
						}
					
					  try (Statement stmt = vmd.createStatement()) {
							stmt.executeUpdate("IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.COLUMNS C WHERE TABLE_NAME = 'clien' AND TABLE_SCHEMA = 'dbo' AND COLUMN_NAME = 'empre_aux' AND DATA_TYPE = 'VARCHAR' AND CHARACTER_MAXIMUM_LENGTH = 15 ) BEGIN ALTER TABLE clien DROP COLUMN empre_aux END ");
							stmt.close();
							System.out.println("DELETOU COLUNA EMPRE_AUX DA CLIEN");
							progressBar.setValue(31);
						}
					
					
					// JOptionPane.showMessageDialog(null,
					// "Dados importados com sucesso.");
					JOptionPane.showMessageDialog(getContentPane(),
							"Processamento de dados realizado com sucesso",
							"Informação", JOptionPane.INFORMATION_MESSAGE);

				} else {
					JOptionPane.showMessageDialog(getContentPane(),
							"Processamento de dados cancelado", "Informação",
							JOptionPane.INFORMATION_MESSAGE);
				}

				return null;
			}
			
			@Override
			protected void done() {
				try {
					progressBar.setValue(0);
					btn_limpa_dados.setEnabled(true);
					btn_processa.setEnabled(true);
					getContentPane().setCursor(Cursor.getDefaultCursor());
					// Descobre como está o processo. É responsável por lançar
					// as exceptions
					get();
					// JOptionPane.showMessageDialog(getContentPane(),
					// "Processamento de dados realizado com sucesso",
					// "Informação", JOptionPane.INFORMATION_MESSAGE);
				} catch (ExecutionException e) {
					final String msg = String.format(
							"Erro ao exportar dados: %s", e.getCause()
									.toString());
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							JOptionPane.showMessageDialog(getContentPane(),
									"Erro ao exportar: " + msg, "Erro",
									JOptionPane.ERROR_MESSAGE);
						}
					});
				} catch (InterruptedException e) {
					System.out.println("Processo de exportação foi interrompido");
				}
			}
		}

		btn_processa = new JButton("Processa");
		btn_processa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				Conexao.MYSQL_BANCO = txtFBBanco.getText();
				Conexao.SQL_BANCO = txtVmdBanco.getText();
				Conexao.SQL_SERVIDOR = txtVmdServidor.getText();
				//Conexao.SQL_SERVIDOR_CONSULTA = txtVmdServidorConsulta.getText();
				//Conexao.SQL_BANCO_CONSULTA = txtVmdBancoConsulta.getText();
				new ProcessaWorker().execute();
			}
		});

		class LimpaDadosWorker extends SwingWorker<Void, Void> {

			@Override
			protected Void doInBackground() throws Exception {
				progressBar.setValue(0);
				progressBar.setMaximum(6);
				btn_limpa_dados.setEnabled(false);
				btn_processa.setEnabled(false);
				int resp = JOptionPane.showConfirmDialog(panel, "Confirma?",
						"Limpeza de Dados", JOptionPane.YES_NO_OPTION);

				if(resp == 0){
				// APAGANDO DADOS 

				// PRODUTO
				if (cboxPRODU.isSelected()) {
					try (Statement stmt = Conexao.getSqlConnection().createStatement()) {
						stmt.executeUpdate("DELETE FROM PRODU");
						stmt.close();
						System.out.println("Deletou");
						progressBar.setValue(1);
					}
				}

				// FABRI
				if (cboxFABRI.isSelected()) {
					try (Statement stmt = Conexao.getSqlConnection().createStatement()) {
						stmt.executeUpdate("DELETE FROM FABRI");
						stmt.close();
						System.out.println("Deletou");
						progressBar.setValue(2);
					}
				}

				//FORNE					
				if (cboxFORNE.isSelected()) {

					try (Statement stmt = Conexao.getSqlConnection().createStatement()) {
						stmt.executeUpdate("DELETE FROM FORNE");
						stmt.close();
						System.out.println("Deletou");
						progressBar.setValue(3);
					}
				}
				
				//CLIEN					
				if (cboxCLIEN.isSelected()) {

					try (Statement stmt = Conexao.getSqlConnection().createStatement()) {
						stmt.executeUpdate("DELETE FROM CLIEN");
						stmt.close();
						System.out.println("Deletou");
						progressBar.setValue(4);
					}
				}
				
				//ENDER
				if (cboxENDER.isSelected()) {
				try (Statement stmt = Conexao.getSqlConnection().createStatement()) {
					stmt.executeUpdate("DELETE FROM CLXED");
					stmt.close();
					System.out.println("Deletou");
					progressBar.setValue(5);
				}
				
				try (Statement stmt = Conexao.getSqlConnection().createStatement()) {
					stmt.executeUpdate("DELETE FROM ENDER");
					stmt.close();
					System.out.println("Deletou");
					progressBar.setValue(6);
				}
				}
				// JOptionPane.showMessageDialog(null,
				// "Dados importados com sucesso.");
				JOptionPane.showMessageDialog(getContentPane(),
						"Limpeza de dados realizada com sucesso",
						"Informação", JOptionPane.INFORMATION_MESSAGE);

			} else {
				JOptionPane.showMessageDialog(getContentPane(),
						"Limpeza de dados cancelada", "Informação",
						JOptionPane.INFORMATION_MESSAGE);
			}
				return null;
			}

			@Override
			protected void done() {
				try {
					progressBar.setValue(0);
					btn_limpa_dados.setEnabled(true);
					btn_processa.setEnabled(true);
					getContentPane().setCursor(Cursor.getDefaultCursor());

					// Descobre como está o processo. É responsável por lançar
					// as exceptions
					get();
					
//					JOptionPane.showMessageDialog(getContentPane(),
//							"Limpeza de dados realizada com sucesso", "Info",
//							JOptionPane.INFORMATION_MESSAGE);
				} catch (ExecutionException e) {
					final String msg = String.format("Erro ao limpar dados: %s", e.getCause().toString());
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							JOptionPane.showMessageDialog(getContentPane(),
									"Erro ao limpar: " + msg, "Erro",
									JOptionPane.ERROR_MESSAGE);
						}
					});
				} catch (InterruptedException e) {
					System.out.println("Processo de exportação foi interrompido");
				}
			}
		}

		btn_limpa_dados = new JButton("Limpa Dados");
		btn_limpa_dados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				Conexao.MYSQL_BANCO = txtFBBanco.getText();
				Conexao.SQL_BANCO = txtVmdBanco.getText();
				Conexao.SQL_SERVIDOR = txtVmdServidor.getText();
				new LimpaDadosWorker().execute();
			}
		});
		panel.add(btn_limpa_dados);
		panel.add(btn_processa);

		JPanel panel_1 = new JPanel();
		panel_1.setToolTipText("");
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new MigLayout("", "[][][][][][][grow,fill]", "[][][][][][][][][][][][]"));
				
		cboxTBNCM = new JCheckBox("1-TBNCM");
		cboxTBNCM.setSelected(true);
		panel_1.add(cboxTBNCM, "cell 0 0");
		
		cboxFABRI = new JCheckBox("3-FABRI");
		cboxFABRI.setSelected(true);
		panel_1.add(cboxFABRI, "cell 1 0");
				
		cboxPRXLJ = new JCheckBox("5-PRXLJ");
		cboxPRXLJ.setSelected(true);
		panel_1.add(cboxPRXLJ, "cell 2 0");
		
		cboxCLIEN = new JCheckBox("7-CLIEN");
		cboxCLIEN.setSelected(true);
		panel_1.add(cboxCLIEN, "cell 3 0");
		
		cboxTBSEC = new JCheckBox("2-TBSEC");
		cboxTBSEC.setSelected(true);
		panel_1.add(cboxTBSEC, "cell 0 1");

		cboxPRODU = new JCheckBox("4-PRODU");
		cboxPRODU.setSelected(true);
		panel_1.add(cboxPRODU, "cell 1 1");

		cboxFORNE = new JCheckBox("6-FORNE");
		cboxFORNE.setSelected(true);
		panel_1.add(cboxFORNE, "cell 2 1");

		cboxENDER = new JCheckBox("8-ENDER");
		cboxENDER.setSelected(true);
		panel_1.add(cboxENDER, "cell 3 1");

		progressBar = new JProgressBar();
		progressBar.setMaximum(31);
		panel_1.add(progressBar, "cell 0 10 7 1,growx");

		progressBar2 = new JProgressBar();
		panel_1.add(progressBar2, "cell 0 11 7 1,growx");

	}

	private int prox(String chave, String tabela) {
		try (Statement s = Conexao.getSqlConnectionAux().createStatement();
				ResultSet rs = s.executeQuery("(Select Isnull(MAX(" + chave	+ "), 0) + 1 From " + tabela + ")")) {
			if (rs.next())
				return rs.getInt(1);
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	private int getCod_Clien(int clien, int empre) throws SQLException {
		
			String sql = "SELECT Cod_Client FROM Clien WHERE clien_aux = "+clien+"and empre_aux ="+empre;
			try (PreparedStatement ps = Conexao.getSqlConnectionAux().prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
				while(rs.next()) {
				return rs.getInt("Cod_Client");
			}
		}
			return -1;
	}

	private int contaRegistros(String tabela) throws SQLException {
		String sql = "SELECT count(*) qtde FROM " + tabela;
			try (PreparedStatement ps = Conexao.getMysqlConnectionAux().prepareStatement(sql);
					ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
			}
			return 0;
		}
	}

	private boolean existeCod_EndFon(String Cod_EndFon) throws SQLException {
		String sql = "SELECT CAST(CASE WHEN EXISTS(SELECT * FROM ENDER where Cod_EndFon = '"+Cod_EndFon+"') THEN 1 ELSE 0 END AS BIT)";
		try (PreparedStatement ps = Conexao.getSqlConnectionAux().prepareStatement(sql);
			ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getBoolean(1);
				} else
					return false;
		}
	}
	
//	private void gravaPrxlj() throws SQLException {
//		String mProdu = "select CAD_CODIGO, CAD_CUSTO_MEDIO, CAD_ULT_PCOMPRA, CAD_PCUSTO from estcad1";
//		String vPrxlj = "Update PRXLJ set Prc_CusLiqMed = ?, Prc_CusEnt = ?, Prc_CusLiq = ?, Prc_VenAtu = ? where Cod_Produt = ?";
//		try (PreparedStatement ps = Conexao.getSqlConnectionAux().prepareStatement(vPrxlj);
//			 PreparedStatement ms = Conexao.getMysqlConnectionAux().prepareStatement(mProdu)) {
//			
//			ResultSet rs = ms.executeQuery();
//
//			while (rs.next()) {
//				// grava no varejo
//				System.out.println(rs.getInt("CAD_CODIGO"));
//				ps.setInt(1, rs.getInt("CAD_CUSTO_MEDIO"));
//			    ps.setString(2, rs.getString("CAD_ULT_PCOMPRA"));
//			    ps.setString(3, rs.getString("CAD_ULT_PCOMPRA"));
//			    ps.setString(4, rs.getString("CAD_PCUSTO"));
//			    ps.setInt(5, rs.getInt("CAD_CODIGO"));
//				
//				ps.executeUpdate();
//
//			}
//			System.out.println("CADASTROU PRXLJ");
//			ms.close();
//			ps.close();
//	}
//	}
	

	// private int contaRegistros2(String tabela, String where, Connection c)
	// throws SQLException {
	// String sql = "SELECT count(*) qtde FROM "+tabela+" "+where;
	// try (PreparedStatement ps = c.prepareStatement(sql)) {
	// ResultSet rs = ps.executeQuery();
	// if (rs.next()) {
	// return rs.getInt(1);
	// }
	// return 0;
	// }
	// }
	// public JCheckBox getCboxFORNE() {
	// return cboxFORNE;
	// }
	public JCheckBox getCboxPRXLJ() {
		return cboxCLIEN;
	}
	
	public JCheckBox getCboxENDER() {
		return cboxENDER;
	}
	public JCheckBox getCboxTBNCM() {
		return cboxTBNCM;
	}
	public JCheckBox getCboxTBSEC() {
		return cboxTBSEC;
	}
}


