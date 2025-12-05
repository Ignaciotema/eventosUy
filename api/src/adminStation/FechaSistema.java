package adminStation;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.SwingConstants;

import logica.controllers.IControllerEvento;

import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.awt.event.ActionEvent;

public class FechaSistema extends JInternalFrame{

	
	private static final long serialVersionUID = 1L;
	private static FechaSistema instance = null;
	private JLabel lblFechaActualSistema;
	private JTextField txtFechaSistema;
	private JTextField textFieldNuevaFecha;
	private JLabel lblFechaActualSistema_1;
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	
	
	
	public FechaSistema(IControllerEvento ICE) {
		
		setTitle("Cambiar fecha del sistema");
		setClosable(true);
		setResizable(true);
		setIconifiable(true);
		setMaximizable(true);
		setBounds(100, 100, 460, 226);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 250, 182, 0, 0};
		gridBagLayout.rowHeights = new int[]{19, 43, 68, 48, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		lblFechaActualSistema = new JLabel("Fecha actual del sistema (AAAA-MM-DD):");
		GridBagConstraints gbc_lblFechaActualSistema = new GridBagConstraints();
		gbc_lblFechaActualSistema.fill = GridBagConstraints.BOTH;
		gbc_lblFechaActualSistema.insets = new Insets(0, 0, 5, 5);
		gbc_lblFechaActualSistema.gridx = 1;
		gbc_lblFechaActualSistema.gridy = 1;
		getContentPane().add(lblFechaActualSistema, gbc_lblFechaActualSistema);
		
		txtFechaSistema = new JTextField();
		txtFechaSistema.setEditable(false);
		txtFechaSistema.setHorizontalAlignment(SwingConstants.CENTER);
		txtFechaSistema.setText(ICE.getFechaSistema().toString());
		GridBagConstraints gbc_txtFechaSistema = new GridBagConstraints();
		gbc_txtFechaSistema.insets = new Insets(0, 0, 5, 5);
		gbc_txtFechaSistema.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFechaSistema.gridx = 2;
		gbc_txtFechaSistema.gridy = 1;
		getContentPane().add(txtFechaSistema, gbc_txtFechaSistema);
		
		lblFechaActualSistema_1 = new JLabel("Nueva fecha del sistema (AAAA-MM-DD):");
		GridBagConstraints gbc_lblFechaActualSistema_1 = new GridBagConstraints();
		gbc_lblFechaActualSistema_1.fill = GridBagConstraints.BOTH;
		gbc_lblFechaActualSistema_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblFechaActualSistema_1.gridx = 1;
		gbc_lblFechaActualSistema_1.gridy = 2;
		getContentPane().add(lblFechaActualSistema_1, gbc_lblFechaActualSistema_1);
		
		textFieldNuevaFecha = new JTextField();
		textFieldNuevaFecha.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 2;
		getContentPane().add(textFieldNuevaFecha, gbc_textField);
		textFieldNuevaFecha.setColumns(10);
		
		btnNewButton = new JButton("Aceptar");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ICE.setFechaSistema(LocalDate.parse(textFieldNuevaFecha.getText()));
					txtFechaSistema.setText(ICE.getFechaSistema().toString());
					textFieldNuevaFecha.setText("");
					JOptionPane.showMessageDialog(null, "Fecha del sistema actualizada correctamente", "Info", JOptionPane.INFORMATION_MESSAGE);
				} catch (DateTimeParseException e1) {
					JOptionPane.showMessageDialog(null, "Error: Formato de fecha invalido. Use AAAA-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.anchor = GridBagConstraints.NORTH;
		gbc_btnNewButton.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 3;
		getContentPane().add(btnNewButton, gbc_btnNewButton);
		
		btnNewButton_1 = new JButton("Cancelar");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				textFieldNuevaFecha.setText("");
			}
		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.anchor = GridBagConstraints.NORTH;
		gbc_btnNewButton_1.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton_1.gridx = 2;
		gbc_btnNewButton_1.gridy = 3;
		getContentPane().add(btnNewButton_1, gbc_btnNewButton_1);
	}


	public static FechaSistema getInstance(IControllerEvento iCE) {
		if (instance == null) {
			instance = new FechaSistema(iCE);
		}
		return instance;
	}
	
	public void refrescar(IControllerEvento ICE) {
		txtFechaSistema.setText(ICE.getFechaSistema().toString());
		textFieldNuevaFecha.setText("");
	}

}
