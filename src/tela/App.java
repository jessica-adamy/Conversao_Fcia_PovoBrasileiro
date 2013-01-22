package tela;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import entidades.Produ;

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
	private JCheckBox cboxGRCLI;
	private JLabel lblVmdDeConsulta;
	private JTextField txtVmdServidorConsulta;
	private JLabel lblBanco;
	private JTextField txtVmdBancoConsulta;

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
		
		lblVmdDeConsulta = new JLabel("VMD de Consulta Servidor");
		panelTop.add(lblVmdDeConsulta, "cell 0 2,alignx trailing");
		
		txtVmdServidorConsulta = new JTextField();
		txtVmdServidorConsulta.setText("localhost");
		panelTop.add(txtVmdServidorConsulta, "cell 1 2,growx");
		txtVmdServidorConsulta.setColumns(10);
		
		lblBanco = new JLabel("Banco");
		panelTop.add(lblBanco, "cell 2 2,alignx trailing");
		
		txtVmdBancoConsulta = new JTextField();
		txtVmdBancoConsulta.setText("VMD_ALDESUL");
		panelTop.add(txtVmdBancoConsulta, "cell 3 2,growx");
		txtVmdBancoConsulta.setColumns(10);

		JLabel lblNewLabel_3 = new JLabel("Converte uma base para o Varejo");
		panelTop.add(lblNewLabel_3, "cell 0 3 4 1");

		final JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);

		class ProcessaWorker extends SwingWorker<Void, Void> {

			@Override
			protected Void doInBackground() throws Exception {
				progressBar.setValue(0);
				progressBar.setMaximum(14);
				btn_limpa_dados.setEnabled(false);
				btn_processa.setEnabled(false);
				
				int resp = JOptionPane.showConfirmDialog(panel, "Confirma?", "Processar Dados", JOptionPane.YES_NO_OPTION);
				Forne forne = new Forne();
				Fabri fabri = new Fabri();
				Grcli grcli = new Grcli();
				Clien clien = new Clien();
				Ender ender = new Ender();
				Produ produ = new Produ();											
				if (resp == 0) {
					if (cboxFABRI.isSelected() && cboxPRODU.isSelected() 
						 && cboxCLIEN.isSelected() && cboxFORNE.isSelected() 
						 && cboxENDER.isSelected() && cboxGRCLI.isSelected()) {
						
						int i = 1;
						
						// APAGANDO DADOS
						produ.deleta_prxlj();
						produ.deleta_produ();
						fabri.deleta();
						forne.deleta();
						clien.deleta();
						grcli.deleta();
						ender.deleta_clxed();
						ender.deleta_ender();
						progressBar.setValue(i);
					}

					//IMPORTAÇÃO
					//FABRI
					if (cboxFABRI.isSelected()) {
						System.out.println("COMEÇOU FABRI");
						fabri.deleta();
						progressBar.setValue(progressBar.getValue() + 1);
						fabri.importa(progressBar2);
						progressBar.setValue(progressBar.getValue() + 1);
					}
						
					//PRODU
					if (cboxPRODU.isSelected()) {
						System.out.println("COMEÇOU PRODU");
						produ.deleta_prxlj();
						produ.deleta_produ();
						progressBar.setValue(progressBar.getValue() + 1);
						produ.importa(progressBar2);
						progressBar.setValue(progressBar.getValue() + 1);
					}
					
					//FORNE					
					if (cboxFORNE.isSelected()) {
						System.out.println("COMEÇOU FORNE");
						forne.deleta();
						progressBar.setValue(progressBar.getValue() + 1);
						forne.importa(progressBar2);
						progressBar.setValue(progressBar.getValue() + 1);
					}
					
					// GRCLI
					if (cboxGRCLI.isSelected()) {
						System.out.println("COMEÇOU GRCLI");
						grcli.deleta();
						progressBar.setValue(progressBar.getValue() + 1);
						grcli.importa(progressBar2);
						progressBar.setValue(progressBar.getValue() + 1);
					}
					
					//CLIEN
					if (cboxCLIEN.isSelected()) {
						System.out.println("COMEÇOU CLIEN");
						clien.deleta();
						progressBar.setValue(progressBar.getValue() + 1);
						clien.importa(progressBar2);
						progressBar.setValue(progressBar.getValue() + 1);
					}
					
					//ENDER
					if (cboxENDER.isSelected()) {
						System.out.println("COMEÇOU ENDER");
						ender.deleta_clxed();
						progressBar.setValue(progressBar.getValue() + 1);
						ender.deleta_ender();
						progressBar.setValue(progressBar.getValue() + 1);
						ender.importa(progressBar2);
						progressBar.setValue(progressBar.getValue() + 1);
					}
					
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
				Conexao.SQL_SERVIDOR_CONSULTA = txtVmdServidorConsulta.getText();
				Conexao.SQL_BANCO_CONSULTA = txtVmdBancoConsulta.getText();
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
					Produ produ = new Produ();
					produ.deleta_prxlj();
					produ.deleta_produ();
					progressBar.setValue(1);
				}

				// FABRI
				if (cboxFABRI.isSelected()) {
					Fabri fabri = new Fabri();
					fabri.deleta();
					progressBar.setValue(2);
				}

				//FORNE					
				if (cboxFORNE.isSelected()) {
					Forne forne = new Forne();
					forne.deleta();
					progressBar.setValue(3);
				}
				
				//CLIEN					
				if (cboxCLIEN.isSelected()) {
					Clien clien = new Clien();
					clien.deleta();
					progressBar.setValue(4);
				}
				
				//GRCLI					
				if (cboxGRCLI.isSelected()) {
					Grcli grcli = new Grcli();
					grcli.deleta();
					progressBar.setValue(5);
				}
				
				//ENDER
				if (cboxENDER.isSelected()) {
					Ender ender = new Ender();
					ender.deleta_clxed();
					ender.deleta_ender();
					progressBar.setValue(6);
				}
				
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
		panel_1.setLayout(new MigLayout("", "[][][][][][][][grow,fill]", "[][][][][][][][][][][][]"));
		
		cboxFABRI = new JCheckBox("1-FABRI");
		cboxFABRI.setSelected(true);
		panel_1.add(cboxFABRI, "cell 0 0");
		
		cboxFORNE = new JCheckBox("3-FORNE");
		cboxFORNE.setSelected(true);
		panel_1.add(cboxFORNE, "cell 1 0");
		
		cboxCLIEN = new JCheckBox("5-CLIEN");
		cboxCLIEN.setSelected(true);
		panel_1.add(cboxCLIEN, "cell 2 0");

		cboxPRODU = new JCheckBox("2-PRODU");
		cboxPRODU.setSelected(true);
		panel_1.add(cboxPRODU, "cell 0 1");
		
		cboxGRCLI = new JCheckBox("4-GRCLI");
		cboxGRCLI.setSelected(true);
		panel_1.add(cboxGRCLI, "cell 1 1");
		
		cboxENDER = new JCheckBox("6-ENDER");
		cboxENDER.setSelected(true);
		panel_1.add(cboxENDER, "cell 2 1");

		progressBar = new JProgressBar();
		progressBar.setMaximum(14);
		panel_1.add(progressBar, "cell 0 10 8 1,growx");

		progressBar2 = new JProgressBar();
		panel_1.add(progressBar2, "cell 0 11 8 1,growx");

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
	
	public JCheckBox getCboxPRXLJ() {
		return cboxCLIEN;
	}
	public JCheckBox getCboxENDER() {
		return cboxENDER;
	}
	public JCheckBox getCboxGRCLI() {
		return cboxGRCLI;
	}
	public JCheckBox getCboxFABRI() {
		return cboxFABRI;
	}
	public JCheckBox getCboxPRODU() {
		return cboxPRODU;
	}
	public JCheckBox getCboxFORNE() {
		return cboxFORNE;
	}
}