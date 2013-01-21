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
import entidades.Clien;
import entidades.Ender;
import entidades.Fabri;
import entidades.Forne;
import entidades.Grcli;

public class App extends JFrame {

	private static final long serialVersionUID = 1L;

	private JCheckBox cboxPRODU;
	private JCheckBox cboxFABRI;
	private JTextField txtPGBanco;
	private JTextField txtVmdServidor;
	private JTextField txtVmdBanco;
	private JButton btn_limpa_dados;
	private JProgressBar progressBar;
	private JButton btn_processa;
	public JProgressBar progressBar2;
	private JCheckBox cboxFORNE;
	private JCheckBox cboxCLIEN;
	private JCheckBox cboxENDER;
	private JCheckBox cboxTBNCM;
	private JCheckBox cboxTBSEC;
	private JCheckBox cboxPRXLJ;
	private JCheckBox cboxGRCLI;
	

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
		
		txtPGBanco= new JTextField();
		txtPGBanco.setText("povo_brasileiro");
		panelTop.add(txtPGBanco, "cell 1 0,growx");
		txtPGBanco.setColumns(10);

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
					Connection pg = Conexao.getPostgresConnection();
					Connection vmd = Conexao.getSqlConnection();
					
					Forne forne = new Forne();
					Fabri fabri = new Fabri();
					Grcli grcli = new Grcli();
					Clien clien = new Clien();
					Ender ender = new Ender();
						
					if (cboxTBNCM.isSelected() && cboxTBSEC.isSelected()
						 && cboxFABRI.isSelected() && cboxPRODU.isSelected() 
						 && cboxCLIEN.isSelected() && cboxFORNE.isSelected() 
						 && cboxENDER.isSelected() && cboxGRCLI.isSelected()) {

						// APAGANDO DADOS
						// PRODUTO
						try (Statement stmt = vmd.createStatement()) {
							stmt.executeUpdate("DELETE FROM PRODU");
							stmt.close();
							System.out.println("Deletou PRODU");
							progressBar.setValue(1);
						}

						// FABRI
						fabri.deleta();

						// FORNE					
						forne.deleta();
						
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
						clien.deleta();
						
						// GRCLI
						grcli.deleta();
						
						// CLXED
						ender.deleta_clxed();
						
						// ENDER
						ender.deleta_ender();
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
							 PreparedStatement pMs = pg.prepareStatement(msGRPRC)) {
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
							 PreparedStatement pMs = pg.prepareStatement(msTBSEC)) {
							
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
						
						fabri.deleta();
						progressBar.setValue(14);
						
						fabri.importa();
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
							 PreparedStatement pMs = pg.prepareStatement(MsPRODU);) {
							
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
							 PreparedStatement pMs = pg.prepareStatement(mProdu)) {
							
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
							pg.close();
							rs.close();
							
							progressBar2.setValue(0);
					}
						
						progressBar.setValue(19);
					}
					
					//FORNE					
					if (cboxFORNE.isSelected()) {
						System.out.println("COMEÇOU FORNE");
						forne.deleta();
						progressBar2.setValue(0);
						forne.importa();

					}
					
					// GRCLI
					if (cboxGRCLI.isSelected()) {
						System.out.println("COMEÇOU GRCLI");
						grcli.deleta();
						progressBar2.setValue(0);
						grcli.importa();
					}
					
					//CLIEN
					if (cboxCLIEN.isSelected()) {
						System.out.println("COMEÇOU CLIEN");
						clien.deleta();
						progressBar2.setValue(0);
						clien.importa();
						
					}
					
					//ENDER
					if (cboxENDER.isSelected()) {
						System.out.println("COMEÇOU ENDER");
						ender.deleta_clxed();
						
						ender.deleta_ender();

						progressBar2.setValue(0);

						ender.importa();
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
				Conexao.PostGres_BANCO = txtPGBanco.getText();
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
				Conexao.PostGres_BANCO = txtPGBanco.getText();
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
		
		cboxGRCLI = new JCheckBox("7-GRCLI");
		cboxGRCLI.setSelected(true);
		panel_1.add(cboxGRCLI, "cell 3 0");
		
		cboxENDER = new JCheckBox("9-ENDER");
		cboxENDER.setSelected(true);
		panel_1.add(cboxENDER, "cell 4 0");
		
		cboxTBSEC = new JCheckBox("2-TBSEC");
		cboxTBSEC.setSelected(true);
		panel_1.add(cboxTBSEC, "cell 0 1");

		cboxPRODU = new JCheckBox("4-PRODU");
		cboxPRODU.setSelected(true);
		panel_1.add(cboxPRODU, "cell 1 1");

		cboxFORNE = new JCheckBox("6-FORNE");
		cboxFORNE.setSelected(true);
		panel_1.add(cboxFORNE, "cell 2 1");
				
				cboxCLIEN = new JCheckBox("8-CLIEN");
				cboxCLIEN.setSelected(true);
				panel_1.add(cboxCLIEN, "cell 3 1");

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
	
	public int getCod_Clien(int clien, int empre) throws SQLException {
		
			String sql = "SELECT Cod_Client FROM Clien WHERE clien_aux = "+clien+"and empre_aux ="+empre;
			try (PreparedStatement ps = Conexao.getSqlConnectionAux().prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
				while(rs.next()) {
				return rs.getInt("Cod_Client");
			}
		}
			return -1;
	}

	public int contaRegistros(String tabela) throws SQLException {
		String sql = "SELECT count(*) qtde FROM " + tabela;
			try (PreparedStatement ps = Conexao.getPostgresConnectionAux().prepareStatement(sql);
					ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
			}
			return 0;
		}
	}

	public boolean existeCod_EndFon(String Cod_EndFon) throws SQLException {
		String sql = "SELECT CAST(CASE WHEN EXISTS(SELECT * FROM ENDER where Cod_EndFon = '"+Cod_EndFon+"') THEN 1 ELSE 0 END AS BIT)";
		try (PreparedStatement ps = Conexao.getSqlConnectionAux().prepareStatement(sql);
			ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getBoolean(1);
				} else
					return false;
		}
	}
	
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
	public JCheckBox getCboxGRCLI() {
		return cboxGRCLI;
	}
}


