package adminStation;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import excepciones.AsistenteYaRegistrado;
import excepciones.CupoLLeno;
import excepciones.FechaRegPREALTA;
import logica.controllers.IControllerEvento;
import logica.controllers.IControllerUsuario;

public class RegistroEdicion extends JInternalFrame {
	private static RegistroEdicion instance = null;
	private JLabel lblEvento;
	private JComboBox<String> comboBoxEvento;
	
	private JLabel lblEdicion;
	private JComboBox<String> comboBoxEdicion;
	
	private JLabel lblAsistente;
	private JComboBox<String> comboBoxAsistente;
	
	private JLabel lblTipoReg;
	private JComboBox<String> comboBoxTipoReg;
	
	private JButton btnAceptar;
	private JButton btnCancelar;

	
	public RegistroEdicion(IControllerEvento ICE, IControllerUsuario ICU) {
		setTitle("Registro a Edición de Evento");
		setClosable(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setIconifiable(true);
		setMaximizable(true);
		
		lblEvento = new JLabel("Evento:");
		
		
		lblEvento.setBounds(45, 33, 100, 14);
		getContentPane().add(lblEvento);
		
		comboBoxEvento = new JComboBox<String>();
		comboBoxEvento.setBounds(185, 30, 200, 20);
		getContentPane().add(comboBoxEvento);
		comboBoxEvento.addActionListener(e -> {
			refrescarEdiciones(ICE);
			comboBoxTipoReg.removeAllItems();
			comboBoxTipoReg.setSelectedIndex(-1);
		});
		
		lblEdicion = new JLabel("Edición:");
		lblEdicion.setBounds(45, 72, 100, 14);
		getContentPane().add(lblEdicion);
		
		comboBoxEdicion = new JComboBox<String>();
		comboBoxEdicion.setBounds(185, 69, 200, 20);
		getContentPane().add(comboBoxEdicion);
		comboBoxEdicion.addActionListener(e -> {
			refrescarTiposRegistro(ICE);
		});
		
		lblAsistente = new JLabel("Asistente:");
		lblAsistente.setBounds(45, 112, 100, 14);
		getContentPane().add(lblAsistente);
		
		comboBoxAsistente = new JComboBox<String>();
		comboBoxAsistente.setBounds(185, 109, 200, 20);
		getContentPane().add(comboBoxAsistente);
		
		lblTipoReg = new JLabel("Tipo de Registro:");
		lblTipoReg.setBounds(45, 152, 100, 14);
		getContentPane().add(lblTipoReg);
		
		comboBoxTipoReg = new JComboBox<String>();
		comboBoxTipoReg.setBounds(185, 149, 200, 20);
		getContentPane().add(comboBoxTipoReg);
		
		btnAceptar = new JButton("Aceptar");
		btnAceptar.setBounds(77, 200, 90, 25);
		getContentPane().add(btnAceptar);
		btnAceptar.addActionListener(e -> {
			// Lógica para registrar al asistente en la edición del evento
			// Usar ICE e ICU según sea necesario
			try {
		    ICE.setFechaSistema(java.time.LocalDate.now());
			ICE.elegirAsistenteYTipoRegistro(
				(String) comboBoxAsistente.getSelectedItem(),
				(String) comboBoxTipoReg.getSelectedItem(),
				(String) comboBoxEdicion.getSelectedItem(),false

			);
			
			JOptionPane.showMessageDialog(this, "Asistente registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
			limpiarFormulario();
			
			}catch(AsistenteYaRegistrado ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}catch(CupoLLeno ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}catch(FechaRegPREALTA ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
			
			catch (Exception ex) {
				ex.printStackTrace();
			}
			limpiarFormulario();
			
		});
		
		btnCancelar = new JButton("Cancelar");
		btnCancelar.setBounds(260, 200, 90, 25);
		getContentPane().add(btnCancelar);
		btnCancelar.addActionListener(e -> {
			limpiarFormulario();
			setVisible(false);
		});
	}

	private void limpiarFormulario() {
		comboBoxEvento.setSelectedIndex(-1);
		comboBoxEdicion.setSelectedIndex(-1);
		comboBoxAsistente.setSelectedIndex(-1);
		comboBoxTipoReg.setSelectedIndex(-1);
	}
	
	public void refrescarEventos(IControllerEvento ICE) {
		comboBoxEvento.removeAllItems();
		for (String evento : ICE.listarEventos()) {
			comboBoxEvento.addItem(evento);
		}
		comboBoxEvento.setSelectedIndex(-1);
		
	}
	
	public void refrescarAsistentes(IControllerUsuario ICU) {
		comboBoxAsistente.removeAllItems();
		for (String asistente : ICU.listarAsistentes()) {
			comboBoxAsistente.addItem(asistente);
		}
		comboBoxAsistente.setSelectedIndex(-1);
		
	}
	
	public void refrescarEdiciones(IControllerEvento ICE) {
		comboBoxEdicion.removeAllItems();
		if (comboBoxEvento.getSelectedItem() != null) {
			for (String edicion : ICE.listarEdicionesConfirmadas((String) comboBoxEvento.getSelectedItem())) {
				comboBoxEdicion.addItem(edicion);
			}
		}
		comboBoxEdicion.setSelectedIndex(-1);
	}
		
    public void refrescarTiposRegistro(IControllerEvento ICE) {
		comboBoxTipoReg.removeAllItems();
		if (comboBoxEdicion.getSelectedItem() != null) {
			for (String tipoReg : ICE.listarTiposDeRegistro((String) comboBoxEdicion.getSelectedItem())) {
				comboBoxTipoReg.addItem(tipoReg);
			}
		}
		comboBoxTipoReg.setSelectedIndex(-1);
	}
	
	public static RegistroEdicion getInstance(IControllerEvento ICE, IControllerUsuario ICU) {
		if (instance == null) {
			instance = new RegistroEdicion(ICE, ICU);
		}
		return instance;
	}
	
	
	
	
}