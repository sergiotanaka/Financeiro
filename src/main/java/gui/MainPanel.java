package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;

import application.MainApp;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.BeanAdapter;

import domain.exercicio.Exercicio;
import domain.exercicio.Periodo;

public class MainPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private MainApp mainApp;

	public MainPanel(MainApp mainApp) {
		this.mainApp = mainApp;

		initialize();
	}

	private void initialize() {
		setPreferredSize(new Dimension(432, 250));

		JButton btnLancar = new JButton("Lan\u00E7ar");

		btnLancar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				btnLancarActionPerformed(e);
			}
		});

		JButton btnPersistir = new JButton("Persistir");
		btnPersistir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPersistirActionPerformed(e);
			}
		});

		JButton btnVerHistrico = new JButton("Ver hist\u00F3rico");
		btnVerHistrico.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainApp.verHistorico();
			}
		});

		JButton btnVerSaldo = new JButton("Ver saldo");
		btnVerSaldo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainApp.verSaldo();
			}
		});

		JButton btnManterContas = new JButton("Manter contas");
		btnManterContas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainPanel.this.mainApp.manterContas();
			}
		});

		BeanAdapter<Exercicio> exercAdapter = new BeanAdapter<Exercicio>(this.mainApp
				.getExercicio());
		BeanAdapter<Periodo> periodAdapter = new BeanAdapter<Periodo>(this.mainApp
				.getExercicio().getFiltro());

		JFormattedTextField txtInicio = BasicComponentFactory.createDateField(periodAdapter
				.getValueModel("inicio"));
		txtInicio.setColumns(10);

		JFormattedTextField txtFim = BasicComponentFactory.createDateField(periodAdapter
				.getValueModel("fim"));
		txtFim.setColumns(10);
		
		JCheckBox chckbxAtivado = BasicComponentFactory.createCheckBox(exercAdapter.getValueModel("filtroAtivado"), "ativado");

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(btnVerSaldo, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnVerHistrico, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnLancar, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnManterContas, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnPersistir, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(txtInicio, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtFim, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(chckbxAtivado))
					.addGap(38))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnLancar)
						.addComponent(btnPersistir)
						.addComponent(txtInicio, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtFim, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnVerHistrico)
						.addComponent(btnManterContas)
						.addComponent(chckbxAtivado))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnVerSaldo)
					.addContainerGap(158, Short.MAX_VALUE))
		);
		setLayout(groupLayout);
	}

	protected void btnPersistirActionPerformed(ActionEvent e) {
		mainApp.persistExercicio();
	}

	protected void btnLancarActionPerformed(ActionEvent evt) {
		mainApp.lancarHistorico();
	}
}
