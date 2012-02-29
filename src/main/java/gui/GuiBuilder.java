package gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.jgoodies.binding.adapter.AbstractTableAdapter;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.forms.builder.ButtonBarBuilder2;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;

import domain.conta.CentroCusto;
import domain.conta.Conta;
import domain.conta.ContaAnalitica;
import domain.conta.PlanoDeContas;
import domain.conta.RepositorioPlano;
import domain.exercicio.Exercicio;
import domain.exercicio.Historico;
import domain.exercicio.presentation.HistoricoPresentation;

public class GuiBuilder {
	
	public static JPanel buildHistoricoPanel(
			HistoricoPresentation historicoPresentation, Exercicio exercicio,
			Action okAction) {
		JFormattedTextField dataHoraField = BasicComponentFactory
				.createDateField(historicoPresentation
						.getBufferedModel("dataHora"));
		RepositorioPlano repositorioPlano = new RepositorioPlano();
		PlanoDeContas plano = repositorioPlano.retrievePlano();
		List<ContaAnalitica> contasAnaliticas = plano.getContasAnaliticas();
		JComboBox debitoComboBox = BasicComponentFactory
				.createComboBox(new SelectionInList<ContaAnalitica>(
						contasAnaliticas, historicoPresentation
								.getContaDebitoHolder()));
		JComboBox creditoComboBox = BasicComponentFactory
				.createComboBox(new SelectionInList<ContaAnalitica>(
						contasAnaliticas, historicoPresentation
								.getContaCreditoHolder()));
		JTextField valorTextField = BasicComponentFactory
				.createTextField(ConverterFactory.createStringConverter(
						historicoPresentation.getBufferedModel("valor"),
						new DecimalFormat("#####0.00")));
		final JComboBox centroCustoComboBox = BasicComponentFactory
				.createComboBox(new SelectionInList<CentroCusto>(exercicio
						.getCentrosCusto(), historicoPresentation
						.getBufferedModel("centroCusto")));
		centroCustoComboBox.addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == '\u007F') {
					centroCustoComboBox.getModel().setSelectedItem(null);
				}
			}
		});
		JTextField descricaoTextField = BasicComponentFactory
				.createTextField(historicoPresentation
						.getBufferedModel("descricao"));

		final DefaultFormBuilder builder = new DefaultFormBuilder(
				new FormLayout("right:pref, 2dlu, fill:pref:grow"));
		builder.setDefaultDialogBorder();
		builder.append("Data/Hora:", dataHoraField);
		builder.append("Centro custo:", centroCustoComboBox);
		builder.append("Crédito:", creditoComboBox);
		builder.append("Débito:", debitoComboBox);
		builder.append("Valor:", valorTextField);
		builder.append("Descrição:", descricaoTextField);
		builder.appendRelatedComponentsGapRow();
		builder.appendRow("p");
		builder.nextRow(2);

		builder.append(ButtonBarFactory.buildOKBar(new JButton(okAction)),
				builder.getColumnCount());

		return builder.getPanel();
	}

	public static JPanel buildPlanoContaPanel(final List<Conta> todasContas,
			final JFrame mainFrame) {
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
		JButton addCtSinteticaButton = new JButton("Add conta sintética");
		JButton addCtAnaliticaButton = new JButton("Add conta analítica");
		JButton removerCtButton = new JButton("Remover");
		JButton subirButton = new JButton("Subir");
		JButton descerButton = new JButton("Descer");

		bbBuilder.addButton(editarButton, addCtSinteticaButton,
				addCtAnaliticaButton);
		bbBuilder2.addButton(removerCtButton, subirButton, descerButton);
		builder.append(bbBuilder.getPanel());
		builder.append(bbBuilder2.getPanel());

		return builder.getPanel();
	}

}
