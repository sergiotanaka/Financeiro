package examples;

import java.awt.Color;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.util.ValidationUtils;
import com.jgoodies.validation.view.ValidationComponentUtils;
import com.jgoodies.validation.view.ValidationResultViewFactory;

public final class InputHints {

	private final JFrame frame = new JFrame("InputHints");
	private final JLabel hintLabel = new JLabel();
	private final JTextField name = new JTextField(30);
	private final JTextField username = new JTextField(30);
	private final JTextField phoneNumber = new JTextField(30);
	private final ValidationResultModel validationResultModel = new DefaultValidationResultModel();
	private final Pattern phonePattern = Pattern
			.compile("\\b\\d{3}-\\d{3}-\\d{4}");

	public InputHints() {
		// create a hint for each of the three validated fields
		ValidationComponentUtils.setInputHint(name, "Enter a name.");
		ValidationComponentUtils.setInputHint(username,
				"Enter a username with 6-12 characters.");
		ValidationComponentUtils.setInputHint(phoneNumber,
				"Enter a phone number like 314-555-1212.");
		// update the hint based on which field has focus
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addPropertyChangeListener(new FocusChangeHandler());

		this.frame.add(createPanel());
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.pack();
	}

	private JPanel createPanel() {
		FormLayout layout = new FormLayout("pref, 2dlu, pref:grow");
		DefaultFormBuilder builder = new DefaultFormBuilder(layout);
		int columnCount = builder.getColumnCount();
		builder.setDefaultDialogBorder();

		// add the label that will show validation hints, with an icon
		hintLabel.setIcon(ValidationResultViewFactory.getInfoIcon());
		builder.append(this.hintLabel, columnCount);

		// add the three differently-decorated text fields
		builder.append(buildLabelForegroundPanel(), columnCount);
		builder.append(buildComponentBackgroundPanel(), columnCount);
		builder.append(buildComponentBorderPanel(), columnCount);

		JComponent validationResultsComponent = ValidationResultViewFactory
				.createReportList(this.validationResultModel);
		builder.appendUnrelatedComponentsGapRow();
		builder.appendRow("fill:50dlu:grow");
		builder.nextLine(2);
		builder.append(validationResultsComponent, columnCount);

		JPanel buttonBar = ButtonBarFactory.buildOKCancelBar(new JButton(
				new OkAction()), new JButton(new CancelAction()));
		builder.append(buttonBar, columnCount);

		return builder.getPanel();
	}

	// mark name as mandatory by changing the label's foreground color
	private JComponent buildLabelForegroundPanel() {
		FormLayout layout = new FormLayout("50dlu, 2dlu, pref:grow");

		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		JLabel orderNoLabel = new JLabel("Name");
		Color foreground = ValidationComponentUtils.getMandatoryForeground();
		orderNoLabel.setForeground(foreground);
		builder.append(orderNoLabel, this.name);
		return builder.getPanel();
	}

	// mark username as mandatory by changing the field's background color
	private JComponent buildComponentBackgroundPanel() {
		FormLayout layout = new FormLayout("50dlu, 2dlu, pref:grow");

		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		ValidationComponentUtils.setMandatory(this.username, true);
		builder.append("Username", this.username);

		ValidationComponentUtils.updateComponentTreeMandatoryBackground(builder
				.getPanel());

		return builder.getPanel();
	}

	// mark phoneNumber as mandatory by changing the field's border's color
	private JComponent buildComponentBorderPanel() {
		FormLayout layout = new FormLayout("50dlu, 2dlu, pref:grow");

		DefaultFormBuilder builder = new DefaultFormBuilder(layout);

		ValidationComponentUtils.setMandatory(this.phoneNumber, true);
		builder.append("Phone Number", this.phoneNumber);

		ValidationComponentUtils.updateComponentTreeMandatoryBorder(builder
				.getPanel());

		return builder.getPanel();
	}

	private void show() {
		this.frame.setVisible(true);
	}

	private ValidationResult validate() {
		ValidationResult validationResult = new ValidationResult();

		// validate the name field
		if (!ValidationUtils.hasMinimumLength(this.name.getText(), 1)) {
			validationResult.addError("The Name field can not be blank.");
		}

		// validate the username field
		if (!ValidationUtils.hasMinimumLength(this.username.getText(), 1)) {
			validationResult.addError("The Username field can not be blank.");
		} else if (!ValidationUtils.hasBoundedLength(this.username.getText(),
				6, 12)) {
			validationResult
					.addError("The Username field must be between 6 and 12 characters.");
		}

		// validate the phoneNumber field
		String phone = this.phoneNumber.getText();
		if (!ValidationUtils.hasMinimumLength(phone, 1)) {
			validationResult
					.addError("The Phone Number field can not be blank.");
		} else {
			Matcher matcher = this.phonePattern.matcher(phone);
			if (!matcher.matches()) {
				validationResult
						.addWarning("The phone number must be a legal American number.");
			}
		}

		return validationResult;
	}

	private final class OkAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		private OkAction() {
			super("OK");
		}

		public void actionPerformed(ActionEvent e) {
			// don't close the frame on OK unless it validates
			ValidationResult validationResult = validate();
			validationResultModel.setResult(validationResult);
			if (!validationResultModel.hasErrors()) {
				frame.dispose();
			}
		}
	}

	private final class CancelAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		private CancelAction() {
			super("Cancel");
		}

		public void actionPerformed(ActionEvent e) {
			frame.dispose();
		}
	}

	// update the hint label's text based on which component has focus
	private final class FocusChangeHandler implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			String propertyName = evt.getPropertyName();
			if ("permanentFocusOwner".equals(propertyName)) {
				Component focusOwner = KeyboardFocusManager
						.getCurrentKeyboardFocusManager().getFocusOwner();

				if (focusOwner instanceof JTextField) {
					JTextField field = (JTextField) focusOwner;
					String focusHint = (String) ValidationComponentUtils
							.getInputHint(field);
					hintLabel.setText(focusHint);
				} else {
					hintLabel.setText("");
				}
			}
		}
	}

	public static void main(String[] args) {
		InputHints example = new InputHints();
		example.show();
	}
}
