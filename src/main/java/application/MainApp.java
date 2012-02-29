package application;

import gui.GuiBuilder;
import gui.MainPanel;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import util.database.ConnectionHolder;

import com.jgoodies.binding.adapter.AbstractTableAdapter;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationResult;

import domain.conta.CentroCusto;
import domain.conta.Conta;
import domain.conta.ContaAnalitica;
import domain.conta.PlanoDeContas;
import domain.conta.RepositorioPlano;
import domain.exercicio.Exercicio;
import domain.exercicio.Historico;
import domain.exercicio.RepositorioExercicio;
import domain.exercicio.presentation.HistoricoPresentation;

/**
 * TODO:<br>
 * <p>
 * 8. Refatorar. Fazer o MVC certinho.<br>
 * 9. Estudar o form builder. Melhorar as IHMs.<br>
 * 10. Validações.<br>
 * 11. Mudar o relacionamento conta historico.<br>
 * 12. Repositorio de plano.
 */
public class MainApp {

	private JFrame mainFrame;
	private Exercicio exercicio;

	public MainApp() {
	}

	public void execute() {
		RepositorioExercicio repositorio = new RepositorioExercicio();
		exercicio = null;
		try {
			exercicio = repositorio.recuperar();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (exercicio == null) {
			Calendar calendar = new GregorianCalendar();
			calendar.set(2012, 2, 1);
			Date inicio = calendar.getTime();
			calendar.set(2012, 12, 31);
			Date fim = calendar.getTime();

			exercicio = new Exercicio(inicio, fim);
			exercicio.getCentrosCusto().addAll(
					Arrays.asList(new CentroCusto("Sergio"), new CentroCusto(
							"Suiani")));
		}
		
		try {
			ConnectionHolder.buildConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Falha ao conectar ao banco de dados.", "Erro", JOptionPane.ERROR_MESSAGE);
			
			return;
		}

		initMainFrame();
	}

	public void persistExercicio() {
		RepositorioExercicio repositorio = new RepositorioExercicio();

		try {
			repositorio.inserir(exercicio);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initMainFrame() {
		mainFrame = new JFrame("Financeiro");
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.getContentPane().add(new MainPanel(this));
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}

	public void lancarHistorico() {
		final Historico historico = new Historico(new Date(), null, null, "",
				0.0);
		final HistoricoPresentation model = new HistoricoPresentation(historico);

		manterHistorico(model, new HistoricoAction(model, true));
	}

	private interface DisposeContainerAction {
		public void setContainer(Window container);
	}

	private class HistoricoAction extends AbstractAction implements
			DisposeContainerAction {
		private static final long serialVersionUID = 1L;
		private HistoricoPresentation model;
		private Window container;
		private boolean novo;

		public HistoricoAction(HistoricoPresentation model, boolean novo) {
			super(novo ? "Inserir" : "Ok");
			this.model = model;
			this.novo = novo;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			ValidationResult result = model.validate();
			if (result.getMessages().size() > 0) {
				JOptionPane.showMessageDialog(mainFrame,
						result.getMessagesText());
				return;
			}
			model.triggerCommit();
			if (novo) {
				exercicio.getHistoricos().add(model.getBean());
			}
			if (container != null) {
				container.dispose();
			}
		}

		@Override
		public void setContainer(Window container) {
			this.container = container;
		}
	}

	public void manterHistorico(final HistoricoPresentation model, Action action) {
		JPanel historicoPanel = GuiBuilder.buildHistoricoPanel(model,
				exercicio, action);

		final JDialog manterHistoricoDialog = new JDialog(mainFrame, "Histórico", true);
		if (action instanceof DisposeContainerAction) {
			((DisposeContainerAction) action).setContainer(manterHistoricoDialog);
		}
		manterHistoricoDialog.getContentPane().add(historicoPanel);
		manterHistoricoDialog.pack();
		manterHistoricoDialog.setResizable(false);
		manterHistoricoDialog.setLocationRelativeTo(mainFrame);
		manterHistoricoDialog.setVisible(true);
	}

	public void verHistorico() {
		final JDialog seletorDialog = new JDialog(mainFrame,
				"Selecione a conta", true);

		RepositorioPlano repositorioPlano = new RepositorioPlano();
		PlanoDeContas plano = repositorioPlano.retrievePlano();

		List<ContaAnalitica> contasAnaliticas = plano.getContasAnaliticas();
		final ValueHolder selectionHolder = new ValueHolder();
		JComboBox contasComboBox = BasicComponentFactory
				.createComboBox(new SelectionInList<ContaAnalitica>(
						contasAnaliticas, selectionHolder));

		DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout(
				"r:p, 2dlu, f:p:g"));
		builder.setDefaultDialogBorder();
		builder.append("Contas:", contasComboBox);
		builder.appendRelatedComponentsGapRow();
		builder.appendRow("p");

		builder.nextRow(2);
		JButton okButton = new JButton("Ok");
		builder.append(okButton);
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showHistorico((ContaAnalitica) selectionHolder.getValue());
			}
		});

		seletorDialog.getContentPane().add(builder.getPanel());
		seletorDialog.pack();
		seletorDialog.setResizable(false);
		seletorDialog.setLocationRelativeTo(mainFrame);
		seletorDialog.setVisible(true);
	}

	protected void showHistorico(final ContaAnalitica conta) {
		final JDialog historicoDialog = new JDialog(mainFrame,
				"Historico da conta " + conta.getNome(), true);

		final ArrayList<Historico> historicos = exercicio.getHistoricoDa(conta,
				null, true);
		final Map<Historico, Double> saldosPeriodo = exercicio.processarSaldo(
				conta, historicos);
		final ArrayList<Historico> historicosExerc = exercicio.getHistoricoDa(
				conta, null, false);
		final Map<Historico, Double> saldosExerc = exercicio.processarSaldo(
				conta, historicosExerc);

		final SelectionInList<Historico> selectionInList = new SelectionInList<Historico>(
				historicos);
		final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy hh:mm");
		AbstractTableAdapter<Historico> tableAdapter = new AbstractTableAdapter<Historico>(
				"Data/Hora", "Contrapartida", "Histórico", "Valor",
				"Saldo período", "Saldo exerc.") {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch (columnIndex) {
				case 0:
					return format
							.format(historicos.get(rowIndex).getDataHora());
				case 1:
					return historicos.get(rowIndex).getContrapartida(conta);
				case 2:
					return historicos.get(rowIndex).getDescricao();
				case 3:
					return formatDinheiro(historicos.get(rowIndex)
							.getValorEmRelacaoA(conta));
				case 4:
					return formatDinheiro(saldosPeriodo.get(historicos
							.get(rowIndex)));
				case 5:
					return formatDinheiro(saldosExerc.get(historicos
							.get(rowIndex)));
				default:
					return null;
				}
			}
		};
		final JTable table = BasicComponentFactory.createTable(selectionInList,
				tableAdapter);
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					HistoricoPresentation model = new HistoricoPresentation(
							selectionInList.getSelection());
					manterHistorico(model, new HistoricoAction(model, false));
					table.repaint();
				}
			}
		});

		final ValueHolder selectionHolder = new ValueHolder();
		final JComboBox centroCustoComboBox = BasicComponentFactory
				.createComboBox(new SelectionInList<CentroCusto>(exercicio
						.getCentrosCusto(), selectionHolder));
		selectionHolder.addValueChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// repreencher o historicos e refresh na tabela
				CentroCusto centroCusto = (CentroCusto) evt.getNewValue();
				historicos.clear();
				historicos.addAll(exercicio.getHistoricoDa(conta, centroCusto,
						true));
				saldosPeriodo.clear();
				saldosPeriodo.putAll(exercicio
						.processarSaldo(conta, historicos));
				historicosExerc.clear();
				historicosExerc.addAll(exercicio.getHistoricoDa(conta,
						centroCusto, false));
				saldosExerc.clear();
				saldosExerc.putAll(exercicio.processarSaldo(conta,
						historicosExerc));
				table.repaint();
			}
		});
		centroCustoComboBox.addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == '\u007F') {
					centroCustoComboBox.getModel().setSelectedItem(null);
				}
			}
		});

		DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout(
				"p:g"));
		builder.setDefaultDialogBorder();
		builder.append("Centro de custo:", centroCustoComboBox);
		builder.append(new JScrollPane(table));
		builder.appendRelatedComponentsGapRow();
		builder.appendRow("p");

		builder.nextRow(2);
		JButton removerButton = new JButton("Remover");
		builder.append(removerButton);
		removerButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int ret = JOptionPane.showConfirmDialog(mainFrame,
						"Confirma a exclusão do histórico?", "Confirmação",
						JOptionPane.YES_NO_OPTION);
				if (ret == JOptionPane.YES_OPTION) {
					exercicio.getHistoricos().remove(
							selectionInList.getSelection());
					table.repaint();
				}

			}
		});

		JButton okButton = new JButton("Ok");
		builder.append(okButton);
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				historicoDialog.dispose();
			}
		});

		historicoDialog.getContentPane().add(builder.getPanel());
		historicoDialog.pack();
		historicoDialog.setResizable(false);
		historicoDialog.setLocationRelativeTo(mainFrame);
		historicoDialog.setVisible(true);
	}

	public void verSaldo() {
		final JDialog saldoDialog = new JDialog(mainFrame,
				"Saldo do exercício", true);

		RepositorioPlano repositorioPlano = new RepositorioPlano();
		PlanoDeContas plano = repositorioPlano.retrievePlano();
		final List<Conta> todasContas = plano.getTodasContas();
		final SelectionInList<Conta> selectionInList = new SelectionInList<Conta>(
				todasContas);
		AbstractTableAdapter<Historico> tableAdapter = new AbstractTableAdapter<Historico>(
				"Conta", "Crédito", "Débito", "Saldo") {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch (columnIndex) {
				case 0:
					return todasContas.get(rowIndex).toString();
				case 1:
					return formatDinheiro(todasContas.get(rowIndex).getSaldo()
							.getCredito());
				case 2:
					return formatDinheiro(todasContas.get(rowIndex).getSaldo()
							.getDebito());
				case 3:
					return formatDinheiro(todasContas.get(rowIndex)
							.getValorSaldo());
				default:
					return null;
				}
			}
		};

		final ValueHolder selectionHolder = new ValueHolder();
		final JComboBox centroCustoComboBox = BasicComponentFactory
				.createComboBox(new SelectionInList<CentroCusto>(exercicio
						.getCentrosCusto(), selectionHolder));
		centroCustoComboBox.addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == '\u007F') {
					centroCustoComboBox.getModel().setSelectedItem(null);
				}
			}
		});

		final JTable table = BasicComponentFactory.createTable(selectionInList,
				tableAdapter);

		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Conta selecionada = selectionInList.getSelection();
					if (selecionada instanceof ContaAnalitica) {
						showHistorico((ContaAnalitica) selecionada);
						table.repaint();
					}
				}
			}
		});

		selectionHolder.addValueChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// exercicio.processarSaldos((CentroCusto) evt.getNewValue());
				table.repaint();
			}
		});
		DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout(
				"p:g"));
		builder.setDefaultDialogBorder();
		builder.append("Centro de custo:", centroCustoComboBox);
		builder.append(new JScrollPane(table));
		builder.appendRelatedComponentsGapRow();
		builder.appendRow("p");

		builder.nextRow(2);
		JButton okButton = new JButton("Ok");
		builder.append(okButton);
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saldoDialog.dispose();
			}
		});

		saldoDialog.getContentPane().add(builder.getPanel());
		saldoDialog.pack();
		saldoDialog.setResizable(false);
		saldoDialog.setLocationRelativeTo(mainFrame);
		saldoDialog.setVisible(true);
	}

	public static void main(String[] args) {
		MainApp mainApp = new MainApp();
		mainApp.execute();
	}

	public void manterContas() {
		ManterPlano.executar(mainFrame);
	}

	public String formatDinheiro(double dinheiro) {
		return String.format("%.2f", dinheiro);
	}

	public Exercicio getExercicio() {
		return exercicio;
	}
}
