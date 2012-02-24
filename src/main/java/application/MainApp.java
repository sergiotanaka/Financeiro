package application;

import gui.GuiFactory;
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
import javax.swing.JTextField;

import com.jgoodies.binding.adapter.AbstractTableAdapter;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.forms.builder.ButtonBarBuilder2;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationResult;
import com.pinguin.validation.SelfValidatorPresentationModel;

import domain.conta.CentroCusto;
import domain.conta.Conta;
import domain.conta.ContaAnalitica;
import domain.conta.ContaSintetica;
import domain.conta.PlanoDeContas;
import domain.conta.RepositorioPlano;
import domain.conta.validator.ContaAnaliticaValidator;
import domain.conta.validator.ContaSinteticaValidator;
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
 * 11. Mudar o relacionamento conta historico.
 */
public class MainApp {

	private JFrame mainFrame;
	private Exercicio exercicio;
	private GuiFactory guiFactory = new GuiFactory();

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
			
			RepositorioPlano repositorioPlano = new RepositorioPlano();
			PlanoDeContas plano = repositorioPlano.retrievePlano();

			exercicio = new Exercicio(inicio, fim, plano);
//			exercicio = new Exercicio(inicio, fim, PlanoDeContas.buildPlano());
			exercicio.getCentrosCusto().addAll(
					Arrays.asList(new CentroCusto("Sergio"), new CentroCusto(
							"Suiani")));
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
		final HistoricoPresentation model = new HistoricoPresentation(
				historico);

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

		public HistoricoAction(HistoricoPresentation model,
				boolean novo) {
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

	public void manterHistorico(final HistoricoPresentation model,
			Action action) {
		JPanel historicoPanel = guiFactory.createHistoricoPanel(model,
				exercicio, action);

		final JDialog lancarDialog = new JDialog(mainFrame, "Histórico", true);
		if (action instanceof DisposeContainerAction) {
			((DisposeContainerAction) action).setContainer(lancarDialog);
		}
		lancarDialog.getContentPane().add(historicoPanel);
		lancarDialog.pack();
		lancarDialog.setResizable(false);
		lancarDialog.setLocationRelativeTo(mainFrame);
		lancarDialog.setVisible(true);
	}

	public void verHistorico() {
		final JDialog seletorDialog = new JDialog(mainFrame,
				"Selecione a conta", true);

		List<ContaAnalitica> contasAnaliticas = exercicio.getPlano()
				.getContasAnaliticas();
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

//		exercicio.processarSaldos(null);
		final List<Conta> todasContas = exercicio.getPlano().getTodasContas();
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
					return formatDinheiro(todasContas.get(rowIndex).getValorSaldo());
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
				exercicio.processarSaldos((CentroCusto) evt.getNewValue());
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
		final JDialog saldoDialog = new JDialog(mainFrame, "Manter contas",
				true);

		final List<Conta> todasContas = exercicio.getPlano().getTodasContas();
		final SelectionInList<Conta> selectionInList = new SelectionInList<Conta>(
				todasContas);
		final AbstractTableAdapter<Historico> tableAdapter = new AbstractTableAdapter<Historico>(
				"Conta") {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch (columnIndex) {
				case 0:
					return todasContas.get(rowIndex).toString();
				default:
					return null;
				}
			}
		};
		final JTable table = BasicComponentFactory.createTable(selectionInList,
				tableAdapter);

		DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout(
				"p:g"));
		builder.setDefaultDialogBorder();
		builder.append(new JScrollPane(table));

		ButtonBarBuilder2 bbBuilder = ButtonBarBuilder2
				.createLeftToRightBuilder();
		ButtonBarBuilder2 bbBuilder2 = ButtonBarBuilder2
				.createLeftToRightBuilder();

		JButton editarButton = new JButton("Editar");
		builder.append(editarButton);
		editarButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Conta conta = selectionInList.getSelection();
				MainApp.this.editConta(conta);
				reconstruir(todasContas);
				table.repaint();
			}
		});

		JButton addCtSinteticaButton = new JButton("Add conta sintética");
		addCtSinteticaButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ContaSintetica conta = new ContaSintetica();
				MainApp.this.editConta(conta);
				reconstruir(todasContas);
				table.repaint();
			}
		});

		JButton addCtAnaliticaButton = new JButton("Add conta analítica");
		addCtAnaliticaButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ContaAnalitica conta = new ContaAnalitica();
				MainApp.this.editConta(conta);
				reconstruir(todasContas);
				table.repaint();
			}
		});

		JButton removerCtButton = new JButton("Remover");
		removerCtButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Conta conta = selectionInList.getSelection();
				ContaSintetica contaPai = conta.getContaPai();
				if (contaPai == null) {
					JOptionPane.showMessageDialog(mainFrame,
							"Contas pais não podem ser excluídas.");
					return;
				}

				if (conta instanceof ContaSintetica) {
					if (((ContaSintetica) conta).getSubContas().size() > 0) {
						JOptionPane.showMessageDialog(mainFrame,
								"Subcontas devem ser excluídas antes.");
						return;
					}
				}

				int ret = JOptionPane.showConfirmDialog(mainFrame,
						"Confirma a exclusão da conta?", "Confirmação",
						JOptionPane.YES_NO_OPTION);
				if (ret == JOptionPane.YES_OPTION) {
					contaPai.removerSubConta(conta);
					reconstruir(todasContas);
					table.repaint();
				}
			}
		});
		JButton subirButton = new JButton("Subir");
		subirButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Conta conta = selectionInList.getSelection();
				ContaSintetica contaPai = conta.getContaPai();
				contaPai.subirConta(conta);
				reconstruir(todasContas);
				table.repaint();
			}
		});
		JButton descerButton = new JButton("Descer");
		descerButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Conta conta = selectionInList.getSelection();
				ContaSintetica contaPai = conta.getContaPai();
				contaPai.descerConta(conta);
				reconstruir(todasContas);
				table.repaint();
			}
		});

		bbBuilder.addButton(editarButton, addCtSinteticaButton,
				addCtAnaliticaButton);
		bbBuilder2.addButton(removerCtButton, subirButton, descerButton);
		builder.append(bbBuilder.getPanel());
		builder.append(bbBuilder2.getPanel());

		saldoDialog.getContentPane().add(builder.getPanel());
		saldoDialog.pack();
		saldoDialog.setResizable(false);
		saldoDialog.setLocationRelativeTo(mainFrame);
		saldoDialog.setVisible(true);
	}

	private void reconstruir(final List<Conta> todasContas) {
		todasContas.clear();
		todasContas.addAll(exercicio.getPlano().getTodasContas());
	}

	protected void editConta(Conta conta) {
		if (conta instanceof ContaAnalitica) {
			this.editConta((ContaAnalitica) conta);
		} else if (conta instanceof ContaSintetica) {
			this.editConta((ContaSintetica) conta);
		}
	}

	protected void editConta(final ContaAnalitica conta) {
		final JDialog editarDialog = new JDialog(mainFrame, "Conta analítica",
				true);

		final SelfValidatorPresentationModel<ContaAnalitica> model = new SelfValidatorPresentationModel<ContaAnalitica>(
				conta, new ContaAnaliticaValidator());

		JTextField nomeTextField = BasicComponentFactory.createTextField(model
				.getBufferedModel("nome"));
		List<ContaSintetica> contasSinteticas = exercicio.getPlano()
				.getContasSinteticas();
		JComboBox contasSinteticasComboBox = BasicComponentFactory
				.createComboBox(new SelectionInList<ContaSintetica>(
						contasSinteticas, model.getBufferedModel("contaPai")));

		DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout(
				"p:g"));
		builder.setDefaultDialogBorder();
		builder.append("Conta pai:", contasSinteticasComboBox);
		builder.append("Nome:", nomeTextField);
		builder.appendRelatedComponentsGapRow();
		builder.appendRow("p");

		builder.nextRow(2);
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ValidationResult result = model.getValidationResultModel().getResult();
				if (result.getErrors().size() > 0) {
					JOptionPane.showMessageDialog(mainFrame,
							result.getMessagesText());
					return;
				}
				model.triggerCommit();
				editarDialog.dispose();
			}
		});
		builder.append(okButton);

		editarDialog.getContentPane().add(builder.getPanel());
		editarDialog.pack();
		editarDialog.setResizable(false);
		editarDialog.setLocationRelativeTo(mainFrame);
		editarDialog.setVisible(true);
	}

	protected void editConta(final ContaSintetica conta) {

		final JDialog editarDialog = new JDialog(mainFrame, "Conta sintética",
				true);
		final SelfValidatorPresentationModel<ContaSintetica> model = new SelfValidatorPresentationModel<ContaSintetica>(
				conta, new ContaSinteticaValidator());

		JTextField nomeTextField = BasicComponentFactory.createTextField(model
				.getBufferedModel("nome"));
		List<ContaSintetica> contasSinteticas = exercicio.getPlano()
				.getContasSinteticas();
		JComboBox contasSinteticasComboBox = BasicComponentFactory
				.createComboBox(new SelectionInList<ContaSintetica>(
						contasSinteticas, model.getBufferedModel("contaPai")));

		DefaultFormBuilder builder = new DefaultFormBuilder(new FormLayout(
				"p:g"));
		builder.setDefaultDialogBorder();
		builder.append("Conta pai:", contasSinteticasComboBox);
		builder.append("Nome:", nomeTextField);
		builder.appendRelatedComponentsGapRow();
		builder.appendRow("p");

		builder.nextRow(2);
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ValidationResult result = model.getValidationResultModel().getResult();
				if (result.getErrors().size() > 0) {
					JOptionPane.showMessageDialog(mainFrame,
							result.getMessagesText());
					return;
				}
				model.triggerCommit();
				editarDialog.dispose();
			}
		});
		builder.append(okButton);

		editarDialog.getContentPane().add(builder.getPanel());
		editarDialog.pack();
		editarDialog.setResizable(false);
		editarDialog.setLocationRelativeTo(mainFrame);
		editarDialog.setVisible(true);
	}

	public String formatDinheiro(double dinheiro) {
		return String.format("%.2f", dinheiro);
	}

	public Exercicio getExercicio() {
		return exercicio;
	}
}
