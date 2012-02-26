package gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ConverterFactory;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;

import domain.conta.CentroCusto;
import domain.conta.ContaAnalitica;
import domain.conta.PlanoDeContas;
import domain.conta.RepositorioPlano;
import domain.exercicio.Exercicio;
import domain.exercicio.presentation.HistoricoPresentation;

public class GuiFactory {
	public JPanel createHistoricoPanel(
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

}
