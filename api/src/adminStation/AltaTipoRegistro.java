package adminStation;

import java.awt.Container;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import logica.controllers.IControllerEvento;
import logica.controllers.IControllerUsuario;
import logica.manejadores.ManejadorEdicion;
import logica.manejadores.ManejadorEvento;
import logica.models.Edicion;
import logica.models.Evento;

public class AltaTipoRegistro extends JInternalFrame {
    private static AltaTipoRegistro instance=null;
	private static final long serialVersionUID = 1L;

	private JComboBox<String> seleccionarEvento;
	private JComboBox<String> seleccionarEdicion;
	private JLabel lbl_seleccionarEvento;
	private JLabel lbl_seleccionarEdicion;
	private JLabel lbl_nuevoTipoRegistro;
	private JLabel lbl_nombre;
	private JLabel lbl_descripcion;
	private JLabel lbl_costo;
	private JLabel lbl_cupo;
	private JTextField tf_nombre;
	private JTextField tf_descripcion;
	private JTextField tf_costo;
	private JTextField tf_cupo;
	private JButton btn_aceptar;
	private JButton btn_cancelar;

	public AltaTipoRegistro(IControllerEvento ice) {
		setTitle("Alta de Tipo Registro");
		setClosable(true);
		setBounds(100, 100, 450, 300);
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
		for (Evento ev : ManejadorEvento.getInstance().obtenerEventos().values()) {
		    seleccionarEvento.addItem(ev.getNombre());
		}
		
		seleccionarEvento.addActionListener(e -> {
		    String eventoSeleccionado = (String) seleccionarEvento.getSelectedItem();
		    actualizarEdiciones(ice, eventoSeleccionado);
		    if (eventoSeleccionado != null) {
		        activarTextFields();
		    } else {
		        desactivarTextFields();
		    }
		});
		GridBagConstraints gbcComboEvento = new GridBagConstraints();
		gbcComboEvento.insets = new Insets(5, 5, 5, 5);
		gbcComboEvento.fill = GridBagConstraints.HORIZONTAL;
		gbcComboEvento.weightx = 1.0;
		gbcComboEvento.gridx = 1; gbcComboEvento.gridy = y; gbcComboEvento.gridwidth = 2;
		ventana.add(seleccionarEvento, gbcComboEvento);
		y++;

		// Fila seleccionar edición
		lbl_seleccionarEdicion = new JLabel("Seleccionar Edicion: ");
		GridBagConstraints gbcLblEdicion = new GridBagConstraints();
		gbcLblEdicion.insets = new Insets(5, 5, 5, 5);
		gbcLblEdicion.fill = GridBagConstraints.HORIZONTAL;
		gbcLblEdicion.weightx = 1.0;
		gbcLblEdicion.gridx = 0; gbcLblEdicion.gridy = y; gbcLblEdicion.gridwidth = 1;
		ventana.add(lbl_seleccionarEdicion, gbcLblEdicion);

		seleccionarEdicion = new JComboBox<>();
		Set<String>ediciones=ice.listarEdicionesTodas();
		for (String nomEdicion : ediciones) {
		    seleccionarEdicion.addItem(nomEdicion);
		}
		GridBagConstraints gbcComboEdicion = new GridBagConstraints();
		gbcComboEdicion.insets = new Insets(5, 5, 5, 5);
		gbcComboEdicion.fill = GridBagConstraints.HORIZONTAL;
		gbcComboEdicion.weightx = 1.0;
		gbcComboEdicion.gridx = 1; gbcComboEdicion.gridy = y; gbcComboEdicion.gridwidth = 2;
		ventana.add(seleccionarEdicion, gbcComboEdicion);
		y++;

		// Fila nuevo tipo registro (título)
		lbl_nuevoTipoRegistro = new JLabel("Nuevo tipo de registro");
		GridBagConstraints gbcLblTitulo = new GridBagConstraints();
		gbcLblTitulo.insets = new Insets(5, 5, 5, 5);
		gbcLblTitulo.fill = GridBagConstraints.HORIZONTAL;
		gbcLblTitulo.weightx = 1.0;
		gbcLblTitulo.gridx = 0; gbcLblTitulo.gridy = y; gbcLblTitulo.gridwidth = 3;
		ventana.add(lbl_nuevoTipoRegistro, gbcLblTitulo);
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

		// Fila descripción
		lbl_descripcion = new JLabel("Descripcion: ");
		GridBagConstraints gbcLblDesc = new GridBagConstraints();
		gbcLblDesc.insets = new Insets(5, 5, 5, 5);
		gbcLblDesc.fill = GridBagConstraints.HORIZONTAL;
		gbcLblDesc.weightx = 1.0;
		gbcLblDesc.gridx = 0; gbcLblDesc.gridy = y; gbcLblDesc.gridwidth = 1;
		ventana.add(lbl_descripcion, gbcLblDesc);

		tf_descripcion = new JTextField();
		GridBagConstraints gbcTfDesc = new GridBagConstraints();
		gbcTfDesc.insets = new Insets(5, 5, 5, 5);
		gbcTfDesc.fill = GridBagConstraints.HORIZONTAL;
		gbcTfDesc.weightx = 1.0;
		gbcTfDesc.gridx = 1; gbcTfDesc.gridy = y; gbcTfDesc.gridwidth = 2;
		ventana.add(tf_descripcion, gbcTfDesc);
		y++;

		// Fila costo
		lbl_costo = new JLabel("Costo: ");
		GridBagConstraints gbcLblCosto = new GridBagConstraints();
		gbcLblCosto.insets = new Insets(5, 5, 5, 5);
		gbcLblCosto.fill = GridBagConstraints.HORIZONTAL;
		gbcLblCosto.weightx = 1.0;
		gbcLblCosto.gridx = 0; gbcLblCosto.gridy = y; gbcLblCosto.gridwidth = 1;
		ventana.add(lbl_costo, gbcLblCosto);

		tf_costo = new JTextField();
		GridBagConstraints gbcTfCosto = new GridBagConstraints();
		gbcTfCosto.insets = new Insets(5, 5, 5, 5);
		gbcTfCosto.fill = GridBagConstraints.HORIZONTAL;
		gbcTfCosto.weightx = 1.0;
		gbcTfCosto.gridx = 1; gbcTfCosto.gridy = y; gbcTfCosto.gridwidth = 2;
		ventana.add(tf_costo, gbcTfCosto);
		y++;

		// Fila cupo
		lbl_cupo = new JLabel("Cupo: ");
		GridBagConstraints gbcLblCupo = new GridBagConstraints();
		gbcLblCupo.insets = new Insets(5, 5, 5, 5);
		gbcLblCupo.fill = GridBagConstraints.HORIZONTAL;
		gbcLblCupo.weightx = 1.0;
		gbcLblCupo.gridx = 0; gbcLblCupo.gridy = y; gbcLblCupo.gridwidth = 1;
		ventana.add(lbl_cupo, gbcLblCupo);

		tf_cupo = new JTextField();
		GridBagConstraints gbcTfCupo = new GridBagConstraints();
		gbcTfCupo.insets = new Insets(5, 5, 5, 5);
		gbcTfCupo.fill = GridBagConstraints.HORIZONTAL;
		gbcTfCupo.weightx = 1.0;
		gbcTfCupo.gridx = 1; gbcTfCupo.gridy = y; gbcTfCupo.gridwidth = 2;
		ventana.add(tf_cupo, gbcTfCupo);
		y++;

		// Si no hay evento seleccionado, desactivamos todos los textfields
		if(seleccionarEvento.getSelectedItem() == null || seleccionarEdicion.getSelectedItem() == null) {
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
		    if (seleccionarEdicion.getSelectedItem() != null) {
		        try {
		            String edi = (String) seleccionarEdicion.getSelectedItem();
		            String nombre = tf_nombre.getText();
		            String descripcion = tf_descripcion.getText();
		           
		           float costo = Float.parseFloat(tf_costo.getText());
		            int cupo = Integer.parseInt(tf_cupo.getText());
		            
		        if(costo <  0 ) {
		        	JOptionPane.showMessageDialog(this,"el costo no puede ser negativo");
		        	return;
		        }
		        
		        if(cupo <  0 ) {
		        	JOptionPane.showMessageDialog(this,"la cantidad de cupos no puede ser negativa");
		        	return;
		        }
		      
		            Edicion edicion = ManejadorEdicion.getInstance().encontrarEdicion(edi);
		            if (edicion != null && edicion.existeTipoRegistro(nombre)) {
		                throw new excepciones.TipoRegistroExistenteExcepcion(
		                    "El tipo de registro '" + nombre + "' ya existe en la edición '" + edi);
		            }
		            ice.altaTipoDeRegistro(edi, nombre, descripcion, costo, cupo);
		            JOptionPane.showMessageDialog(this, "El tipo de registro se ha registrado con éxito", "Alta de Tipo Registro",
		                        JOptionPane.INFORMATION_MESSAGE);
		            limpiarFormulario();
		        } catch(NumberFormatException nfe) {
		        	JOptionPane.showMessageDialog(this, "Costo y cupo deben ser numéricos","Alta de Tipo Registro", JOptionPane.ERROR_MESSAGE);
		        }
		        
		        catch (excepciones.TipoRegistroExistenteExcepcion ex) {
		        	JOptionPane.showMessageDialog(this, ex.getMessage(),"Alta de Tipo Registro", JOptionPane.ERROR_MESSAGE);
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
		seleccionarEdicion.setSelectedIndex(-1);
		tf_nombre.setText("");
		tf_descripcion.setText("");
		tf_costo.setText("");
		tf_cupo.setText("");
		
	}

	private void activarTextFields() {
		tf_costo.setEnabled(true);
		tf_cupo.setEnabled(true);
		tf_descripcion.setEnabled(true);
		tf_nombre.setEnabled(true);
		
	}

	private void desactivarTextFields() {
		tf_costo.setEnabled(false);
		tf_cupo.setEnabled(false);
		tf_descripcion.setEnabled(false);
		tf_nombre.setEnabled(false);
		
	}
	
	
	
	
	private void actualizarEdiciones(IControllerEvento ice, String eventoSeleccionado) {
	    seleccionarEdicion.removeAllItems();
	    if (eventoSeleccionado != null) {
	        for (String edicion : ice.listarEdiciones(eventoSeleccionado)) {
	            seleccionarEdicion.addItem(edicion);
	        }
	        seleccionarEdicion.setEnabled(true);
	    } else {
	        seleccionarEdicion.setEnabled(false);
	    }
	}
	
	public void refrescar(IControllerEvento ice) {
		seleccionarEvento.removeAllItems();
		for (String evento : ice.listarEventos()) {
			seleccionarEvento.addItem(evento);
		}
		seleccionarEvento.setSelectedIndex(-1);
		
		if (seleccionarEvento.getSelectedItem() == null) {
			seleccionarEdicion.setEnabled(false);
			desactivarTextFields();
		} else {
			seleccionarEdicion.setEnabled(true);
			activarTextFields();
		}
	}
	
	
	public static AltaTipoRegistro getInstance(IControllerEvento ICE) {
		if (instance == null) {
			instance = new AltaTipoRegistro(ICE);
		}
		return instance;
	
	
	
	
}}