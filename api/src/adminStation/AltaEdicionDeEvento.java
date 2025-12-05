package adminStation;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import logica.controllers.IControllerEvento;
import logica.controllers.IControllerUsuario;


public class AltaEdicionDeEvento extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	private static AltaEdicionDeEvento instance = null;

	private IControllerUsuario controllerUsuario;
	private JComboBox<String> seleccionarEvento;
	private JComboBox<String> seleccionarOrganizador;
	private JLabel lbl_seleccionarEvento;
	private JLabel lbl_seleccionarOrganizador;
	private JLabel lbl_nuevaEdicion;
	private JLabel lbl_nombre;
	private JLabel lbl_sigla;
	private JLabel lbl_ciudad;
	private JLabel lbl_pais;
	private JLabel lbl_fIni;
	private JLabel lbl_fFin;
	private JLabel lbl_fAlta;
	private JTextField tf_nombre;
	private JTextField tf_sigla;
	private JTextField tf_ciudad;
	private JTextField tf_pais;
	private JTextField tf_fIni;
	private JTextField tf_fFin;
	private JTextField tf_fAlta;
	
	private JButton btn_aceptar;
	private JButton btn_cancelar;

	public AltaEdicionDeEvento(IControllerEvento ice,IControllerUsuario icu) {
		controllerUsuario = icu;
		setTitle("Alta de Edicion");
		setClosable(true);
		setBounds(100, 100, 800, 400);
		getContentPane().setLayout(new GridBagLayout());
		setResizable(false);
		setIconifiable(true);
		setMaximizable(true);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		Container ventana = getContentPane();
		int y = 0;

		// Fila seleccionar evento
		lbl_seleccionarEvento = new JLabel("Seleccione un evento: ");
		GridBagConstraints gbcLblEvento = new GridBagConstraints();
		gbcLblEvento.insets = new Insets(5, 5, 5, 5);
		gbcLblEvento.fill = GridBagConstraints.HORIZONTAL;
		gbcLblEvento.weightx = 1.0;
		gbcLblEvento.gridx = 0; gbcLblEvento.gridy = y; gbcLblEvento.gridwidth = 1;
		ventana.add(lbl_seleccionarEvento, gbcLblEvento);

		seleccionarEvento = new JComboBox<>();
		for (String ev : ice.listarEventos()) {
			
		    seleccionarEvento.addItem(ev);
		}
		
		seleccionarEvento.addActionListener(e -> {
		    String eventoSeleccionado = (String) seleccionarEvento.getSelectedItem();
		    if (eventoSeleccionado != null) {
		        activarTextFields();
		        lbl_nuevaEdicion.setText("Nueva edicion de " + eventoSeleccionado);
		    } else {
		        desactivarTextFields();
		        lbl_nuevaEdicion.setText("Nueva edicion de ");
		    }
		});
		GridBagConstraints gbcComboEvento = new GridBagConstraints();
		gbcComboEvento.insets = new Insets(5, 5, 5, 5);
		gbcComboEvento.fill = GridBagConstraints.HORIZONTAL;
		gbcComboEvento.weightx = 1.0;
		gbcComboEvento.gridx = 1; gbcComboEvento.gridy = y; gbcComboEvento.gridwidth = 2;
		ventana.add(seleccionarEvento, gbcComboEvento);
		y++;

		// Fila seleccionar organizador
		lbl_seleccionarOrganizador = new JLabel("Seleccionar Organizador: ");
		GridBagConstraints gbcLblOrganizador = new GridBagConstraints();
		gbcLblOrganizador.insets = new Insets(5, 5, 5, 5);
		gbcLblOrganizador.fill = GridBagConstraints.HORIZONTAL;
		gbcLblOrganizador.weightx = 1.0;
		gbcLblOrganizador.gridx = 0; gbcLblOrganizador.gridy = y; gbcLblOrganizador.gridwidth = 1;
		ventana.add(lbl_seleccionarOrganizador, gbcLblOrganizador);

		seleccionarOrganizador = new JComboBox<>();
		Set<String> organizadores = controllerUsuario.listarOrganizadores();
		for (String nomOrganizador : organizadores) {
		    seleccionarOrganizador.addItem(nomOrganizador);
		}
		GridBagConstraints gbcComboOrganizador = new GridBagConstraints();
		gbcComboOrganizador.insets = new Insets(5, 5, 5, 5);
		gbcComboOrganizador.fill = GridBagConstraints.HORIZONTAL;
		gbcComboOrganizador.weightx = 1.0;
		gbcComboOrganizador.gridx = 1; gbcComboOrganizador.gridy = y; gbcComboOrganizador.gridwidth = 2;
		ventana.add(seleccionarOrganizador, gbcComboOrganizador);
		y++;

		// Fila nueva edicion (título)
		lbl_nuevaEdicion = new JLabel("Nueva edicion de");
		GridBagConstraints gbcLblTitulo = new GridBagConstraints();
		gbcLblTitulo.insets = new Insets(5, 5, 5, 5);
		gbcLblTitulo.fill = GridBagConstraints.HORIZONTAL;
		gbcLblTitulo.weightx = 1.0;
		gbcLblTitulo.gridx = 0; gbcLblTitulo.gridy = y; gbcLblTitulo.gridwidth = 3;
		ventana.add(lbl_nuevaEdicion, gbcLblTitulo);
		y++;

		// Fila nombre
		lbl_nombre = new JLabel("Nombre: ");
		GridBagConstraints gbcLblNombre = new GridBagConstraints();
		gbcLblNombre.insets = new Insets(5, 5, 5, 5);
		gbcLblNombre.fill = GridBagConstraints.HORIZONTAL;
		gbcLblNombre.weightx = 1.0;
		gbcLblNombre.gridx = 0; gbcLblNombre.gridy = y; gbcLblNombre.gridwidth = 1;
		ventana.add(lbl_nombre, gbcLblNombre);

		tf_nombre = new JTextField();
		GridBagConstraints gbcTfNombre = new GridBagConstraints();
		gbcTfNombre.insets = new Insets(5, 5, 5, 5);
		gbcTfNombre.fill = GridBagConstraints.HORIZONTAL;
		gbcTfNombre.weightx = 1.0;
		gbcTfNombre.gridx = 1; gbcTfNombre.gridy = y; gbcTfNombre.gridwidth = 2;
		ventana.add(tf_nombre, gbcTfNombre);
		y++;

		// Fila sigla
		lbl_sigla = new JLabel("Sigla: ");
		GridBagConstraints gbcLblSigla = new GridBagConstraints();
		gbcLblSigla.insets = new Insets(5, 5, 5, 5);
		gbcLblSigla.fill = GridBagConstraints.HORIZONTAL;
		gbcLblSigla.weightx = 1.0;
		gbcLblSigla.gridx = 0; gbcLblSigla.gridy = y; gbcLblSigla.gridwidth = 1;
		ventana.add(lbl_sigla, gbcLblSigla);

		tf_sigla = new JTextField();
		GridBagConstraints gbcTfSigla = new GridBagConstraints();
		gbcTfSigla.insets = new Insets(5, 5, 5, 5);
		gbcTfSigla.fill = GridBagConstraints.HORIZONTAL;
		gbcTfSigla.weightx = 1.0;
		gbcTfSigla.gridx = 1; gbcTfSigla.gridy = y; gbcTfSigla.gridwidth = 2;
		ventana.add(tf_sigla, gbcTfSigla);
		y++;

		// Fila ciudad
		lbl_ciudad = new JLabel("Ciudad: ");
		GridBagConstraints gbcLblCiudad = new GridBagConstraints();
		gbcLblCiudad.insets = new Insets(5, 5, 5, 5);
		gbcLblCiudad.fill = GridBagConstraints.HORIZONTAL;
		gbcLblCiudad.weightx = 1.0;
		gbcLblCiudad.gridx = 0; gbcLblCiudad.gridy = y; gbcLblCiudad.gridwidth = 1;
		ventana.add(lbl_ciudad, gbcLblCiudad);

		tf_ciudad = new JTextField();
		GridBagConstraints gbcTfCiudad = new GridBagConstraints();
		gbcTfCiudad.insets = new Insets(5, 5, 5, 5);
		gbcTfCiudad.fill = GridBagConstraints.HORIZONTAL;
		gbcTfCiudad.weightx = 1.0;
		gbcTfCiudad.gridx = 1; gbcTfCiudad.gridy = y; gbcTfCiudad.gridwidth = 2;
		ventana.add(tf_ciudad, gbcTfCiudad);
		y++;

		// Fila pais
		lbl_pais = new JLabel("Pais: ");
		GridBagConstraints gbcLblPais = new GridBagConstraints();
		gbcLblPais.insets = new Insets(5, 5, 5, 5);
		gbcLblPais.fill = GridBagConstraints.HORIZONTAL;
		gbcLblPais.weightx = 1.0;
		gbcLblPais.gridx = 0; gbcLblPais.gridy = y; gbcLblPais.gridwidth = 1;
		ventana.add(lbl_pais, gbcLblPais);

		tf_pais = new JTextField();
		GridBagConstraints gbcTfPais = new GridBagConstraints();
		gbcTfPais.insets = new Insets(5, 5, 5, 5);
		gbcTfPais.fill = GridBagConstraints.HORIZONTAL;
		gbcTfPais.weightx = 1.0;
		gbcTfPais.gridx = 1; gbcTfPais.gridy = y; gbcTfPais.gridwidth = 2;
		ventana.add(tf_pais, gbcTfPais);
		y++;
		
		// Fila fecha de inicio
		lbl_fIni = new JLabel("Fecha de inicio (yyyy-mm-dd): ");
		GridBagConstraints gbcLblFIni = new GridBagConstraints();
		gbcLblFIni.insets = new Insets(5, 5, 5, 5);
		gbcLblFIni.fill = GridBagConstraints.HORIZONTAL;
		gbcLblFIni.weightx = 1.0;
		gbcLblFIni.gridx = 0; gbcLblFIni.gridy = y; gbcLblFIni.gridwidth = 1;
		ventana.add(lbl_fIni, gbcLblFIni);

		tf_fIni = new JTextField();
		GridBagConstraints gbcTfFini= new GridBagConstraints();
		gbcTfFini.insets = new Insets(5, 5, 5, 5);
		gbcTfFini.fill = GridBagConstraints.HORIZONTAL;
		gbcTfFini.weightx = 1.0;
		gbcTfFini.gridx = 1; gbcTfFini.gridy = y; gbcTfFini.gridwidth = 2;
		ventana.add(tf_fIni, gbcTfFini);
		y++;
		
		// Fila fecha de fin
		lbl_fFin = new JLabel("Fecha de Fin (yyyy-mm-dd): ");
		GridBagConstraints gbcLblFFin = new GridBagConstraints();
		gbcLblFFin.insets = new Insets(5, 5, 5, 5);
		gbcLblFFin.fill = GridBagConstraints.HORIZONTAL;
		gbcLblFFin.weightx = 1.0;
		gbcLblFFin.gridx = 0; gbcLblFFin.gridy = y; gbcLblFFin.gridwidth = 1;
		ventana.add(lbl_fFin, gbcLblFFin);

		tf_fFin = new JTextField();
		GridBagConstraints gbcTfFFin= new GridBagConstraints();
		gbcTfFFin.insets = new Insets(5, 5, 5, 5);
		gbcTfFFin.fill = GridBagConstraints.HORIZONTAL;
		gbcTfFFin.weightx = 1.0;
		gbcTfFFin.gridx = 1; gbcTfFFin.gridy = y; gbcTfFFin.gridwidth = 2;
		ventana.add(tf_fFin, gbcTfFFin);
		y++;
		
		// Fila fecha de alta
		lbl_fAlta = new JLabel("Fecha de Alta (yyyy-mm-dd): ");
		GridBagConstraints gbcLblFAlta = new GridBagConstraints();
		gbcLblFAlta.insets = new Insets(5, 5, 5, 5);
		gbcLblFAlta.fill = GridBagConstraints.HORIZONTAL;
		gbcLblFAlta.weightx = 1.0;
		gbcLblFAlta.gridx = 0; gbcLblFAlta.gridy = y; gbcLblFAlta.gridwidth = 1;
		ventana.add(lbl_fAlta, gbcLblFAlta);

		tf_fAlta = new JTextField();
		GridBagConstraints gbcTfFAlta= new GridBagConstraints();
		gbcTfFAlta.insets = new Insets(5, 5, 5, 5);
		gbcTfFAlta.fill = GridBagConstraints.HORIZONTAL;
		gbcTfFAlta.weightx = 1.0;
		gbcTfFAlta.gridx = 1; gbcTfFAlta.gridy = y; gbcTfFAlta.gridwidth = 2;
		ventana.add(tf_fAlta, gbcTfFAlta);
		y++;

		// Si no hay evento seleccionado, desactivamos todos los textfields
		if(seleccionarEvento.getSelectedItem() == null || seleccionarOrganizador.getSelectedItem() == null) {
			desactivarTextFields();
		} else {
			activarTextFields();
		}

		// Fila botones
		btn_aceptar = new JButton("Aceptar");
		btn_cancelar = new JButton("Cancelar");
		JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filaBotones.add(btn_aceptar);
		filaBotones.add(btn_cancelar);
		GridBagConstraints gbcBotones = new GridBagConstraints();
		gbcBotones.insets = new Insets(5, 5, 5, 5);
		gbcBotones.fill = GridBagConstraints.HORIZONTAL;
		gbcBotones.weightx = 1.0;
		gbcBotones.gridx = 0; gbcBotones.gridy = y; gbcBotones.gridwidth = 3;
		ventana.add(filaBotones, gbcBotones);

		// Logica botones
		btn_aceptar.addActionListener(e -> {
		    if (seleccionarOrganizador.getSelectedItem() != null) {
		    	try {
		            String ev = (String) seleccionarEvento.getSelectedItem();
		            String org = (String) seleccionarOrganizador.getSelectedItem();
		            String nombre = tf_nombre.getText();
		            String sigla = tf_sigla.getText();
		            String ciudad = tf_ciudad.getText();
		            String pais = tf_pais.getText();
		            
		            if (nombre.isEmpty()) {
		                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
		                return;
		            }
		            
		            if (sigla.isEmpty()) {
		                JOptionPane.showMessageDialog(this, "La sigla no puede estar vacía.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
		                return;
		            }
		            if (ciudad.isEmpty()) {
		                JOptionPane.showMessageDialog(this, "La ciudad no puede estar vacía.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
		                return;
		            }
		            if (pais.isEmpty()) { 
		                JOptionPane.showMessageDialog(this, "El país no puede estar vacío.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
		                return;
		            }
		            
		            

		            LocalDate fIni;
		            try {
		                fIni = LocalDate.parse(tf_fIni.getText());
		            } catch (java.time.format.DateTimeParseException ex) {
		                JOptionPane.showMessageDialog(this, "Fecha de inicio inválida. Formato esperado: yyyy-mm-dd", "Error de Fecha", JOptionPane.ERROR_MESSAGE);
		                return;
		            }

		            LocalDate fFin;
		            try {
		                fFin = LocalDate.parse(tf_fFin.getText());
		            } catch (java.time.format.DateTimeParseException ex) {
		                JOptionPane.showMessageDialog(this, "Fecha de fin inválida. Formato esperado: yyyy-mm-dd", "Error de Fecha", JOptionPane.ERROR_MESSAGE);
		                return;
		            }

		            LocalDate fAlta;
		            try {
		                fAlta = LocalDate.parse(tf_fAlta.getText());
		            } catch (java.time.format.DateTimeParseException ex) {
		                JOptionPane.showMessageDialog(this, "Fecha de alta inválida. Formato esperado: yyyy-mm-dd", "Error de Fecha", JOptionPane.ERROR_MESSAGE);
		                return;
		            }

		          
		            ice.altaEdicionDeEvento(ev, org, nombre, sigla, fIni, fFin, fAlta, ciudad, pais,"");
		            JOptionPane.showMessageDialog(this, "La edicion se ha registrado con exito", "Alta Edicion",
		                        JOptionPane.INFORMATION_MESSAGE);
		            limpiarFormulario();
		        } catch(excepciones.FechaInicioPOSTFINAL ex) {
		            JOptionPane.showMessageDialog(this, ex.getMessage(),"Alta de Edicion", JOptionPane.ERROR_MESSAGE);
		            tf_fIni.setText("");
		        } catch (excepciones.FechaInicioPREALTA ex) {
		            JOptionPane.showMessageDialog(this, ex.getMessage(),"Alta de Edicion", JOptionPane.ERROR_MESSAGE);
		            tf_fIni.setText("");
		        }
		    	catch (excepciones.NombreEdicionExistenteExcepcion ex) {
		            JOptionPane.showMessageDialog(this, ex.getMessage(),"Alta de Edicion", JOptionPane.ERROR_MESSAGE);
		            tf_nombre.setText("");
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }
		    }
		});

		btn_cancelar.addActionListener(e -> {
		    setVisible(false);
		    limpiarFormulario();
		});
	}

	private void limpiarFormulario() {
		seleccionarEvento.setSelectedIndex(-1);
		seleccionarOrganizador.setSelectedIndex(-1);
		tf_nombre.setText("");
		tf_sigla.setText("");
		tf_ciudad.setText("");
		tf_pais.setText("");
		tf_fAlta.setText("");
		tf_fFin.setText("");
		tf_fIni.setText("");
		
	}


	private void activarTextFields() {
		seleccionarOrganizador.setEnabled(true);
		tf_ciudad.setEnabled(true);
		tf_pais.setEnabled(true);
		tf_sigla.setEnabled(true);
		tf_nombre.setEnabled(true);
		tf_fAlta.setEnabled(true);
		tf_fFin.setEnabled(true);
		tf_fIni.setEnabled(true);
		
	}

	private void desactivarTextFields() {
		seleccionarOrganizador.setEnabled(false);
		tf_ciudad.setEnabled(false);
		tf_pais.setEnabled(false);
		tf_sigla.setEnabled(false);
		tf_nombre.setEnabled(false);
		tf_fAlta.setEnabled(false);
		tf_fFin.setEnabled(false);
		tf_fIni.setEnabled(false);
		
	}
	
	public void refrescar(IControllerEvento ice, IControllerUsuario controllerUsuario) {
		seleccionarEvento.removeAllItems();
		for (String evento : ice.listarEventos()) {
			seleccionarEvento.addItem(evento);
		}
		seleccionarEvento.setSelectedIndex(-1);
		
		seleccionarOrganizador.removeAllItems();
	    Set<String> orgs = controllerUsuario.listarOrganizadores();
	    for (String string : orgs) {
			seleccionarOrganizador.addItem(string);
		}
	    seleccionarOrganizador.setSelectedIndex(-1);
		
		
		if (seleccionarEvento.getSelectedItem() == null) {
			seleccionarOrganizador.setEnabled(false);
			desactivarTextFields();
		} else {
			seleccionarOrganizador.setEnabled(true);
			activarTextFields();
		}
	}
	
	public static AltaEdicionDeEvento getInstance(IControllerEvento ice,IControllerUsuario icu) {
		if (instance == null) {
			instance = new AltaEdicionDeEvento(ice,icu);
		}
		return instance;
	}
	
	
	
	
}

