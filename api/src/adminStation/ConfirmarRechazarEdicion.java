package adminStation;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import logica.controllers.IControllerEvento;
import logica.controllers.IControllerUsuario;
import logica.manejadores.ManejadorEvento;
import logica.models.Edicion;
import logica.models.Evento;

public class ConfirmarRechazarEdicion extends JInternalFrame {
	private static ConfirmarRechazarEdicion instance = null;
	
	private JComboBox<String> comboBoxEventos;
	private JList<String> listaEdicionesPendientes;
	private JButton btnConfirmar;
	private JButton btnRechazar;
	private JButton btnCancelar;
	private JLabel lblEventos;
	private JLabel lblEdicionesPendientes;

	
	public ConfirmarRechazarEdicion(IControllerEvento ice, IControllerUsuario icu) {
		IControllerEvento ICE = ice;
		setBounds(100, 100, 800, 400);
		setTitle("Confirmar/Rechazar Edicion");
		setClosable(true);
		setIconifiable(true);
		setMaximizable(true);
		getContentPane().setLayout(new GridBagLayout());
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		int y = 0;
		
		Container ventana = getContentPane();
		
		//Fila seleccionar evento
		lblEventos = new JLabel("Seleccione un evento:");
		GridBagConstraints gbcLblEvento = new GridBagConstraints();
		gbcLblEvento.insets = new Insets(5, 5, 5, 5);
		gbcLblEvento.fill = GridBagConstraints.HORIZONTAL;
		gbcLblEvento.weightx = 1.0;
		gbcLblEvento.gridx = 0; gbcLblEvento.gridy = y; gbcLblEvento.gridwidth = 1;
		ventana.add(lblEventos, gbcLblEvento);
		
		comboBoxEventos = new JComboBox<String>();
		for(Evento ev: ManejadorEvento.getInstance().obtenerEventos().values()) {
				comboBoxEventos.addItem(ev.getNombre());
		}
		comboBoxEventos.addActionListener(e -> {
			refrescarEdiciones();
		});
		
		GridBagConstraints gbcComboBoxEventos = new GridBagConstraints();
		gbcComboBoxEventos.insets = new Insets(5, 5, 5,5);
		gbcComboBoxEventos.fill = GridBagConstraints.HORIZONTAL;
		gbcComboBoxEventos.weightx = 1.0;
		gbcComboBoxEventos.gridx = 1; gbcComboBoxEventos.gridy = y; gbcComboBoxEventos.gridwidth = 2;
		ventana.add(comboBoxEventos, gbcComboBoxEventos);
		y++;
		
		//Fila listar ediciones pendientes
		lblEdicionesPendientes = new JLabel("Ediciones pendientes:");
		GridBagConstraints gbcLblEdicionesPendientes = new GridBagConstraints();
		gbcLblEdicionesPendientes.insets = new Insets(5, 5, 5,5);
		gbcLblEdicionesPendientes.fill = GridBagConstraints.HORIZONTAL;
		gbcLblEdicionesPendientes.weightx = 1.0;
		gbcLblEdicionesPendientes.gridx = 0; gbcLblEdicionesPendientes.gridy = y; 
		gbcLblEdicionesPendientes.gridwidth = 1;
		ventana.add(lblEdicionesPendientes, gbcLblEdicionesPendientes);
		
		listaEdicionesPendientes = new JList<String>();
		GridBagConstraints gbcListaEdicionesPendientes = new GridBagConstraints();
		gbcListaEdicionesPendientes.insets = new Insets(5, 5, 5,5);
		gbcListaEdicionesPendientes.fill = GridBagConstraints.BOTH;
		gbcListaEdicionesPendientes.weightx = 1.0;
		gbcListaEdicionesPendientes.gridx = 1; gbcListaEdicionesPendientes.gridy = y;
		gbcListaEdicionesPendientes.gridwidth = 2; 
		ventana.add(listaEdicionesPendientes, gbcListaEdicionesPendientes);
		
		String nomEvento = (String) comboBoxEventos.getSelectedItem();
		Evento evento = ManejadorEvento.getInstance().obtenerEvento(nomEvento);
		
		if(evento != null) {
			List<Edicion> edicionesPendientes = evento.getColEdicionesIngresadas();
			DefaultListModel<String> model = new DefaultListModel<>();
			
			for (Edicion edicion : edicionesPendientes) {
			    model.addElement(edicion.getNombre());
			}
			listaEdicionesPendientes.setModel(model);
		}
		
		y++;
		
		//Fila botones
		btnConfirmar = new JButton("Confirmar");
		GridBagConstraints gbcBtnConfirmar = new GridBagConstraints();
		gbcBtnConfirmar.insets = new Insets(5, 5, 5,5);
		gbcBtnConfirmar.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnConfirmar.weightx = 1.0;
		gbcBtnConfirmar.gridx = 3; gbcBtnConfirmar.gridy = y; 
		gbcBtnConfirmar.gridwidth = 1;
		ventana.add(btnConfirmar, gbcBtnConfirmar);
		
		btnRechazar = new JButton("Rechazar");
		GridBagConstraints gbcBtnRechazar = new GridBagConstraints();
		gbcBtnRechazar.insets = new Insets(5, 5, 5,5);
		gbcBtnRechazar.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnRechazar.weightx = 1.0;
		gbcBtnRechazar.gridx = 4; gbcBtnRechazar.gridy = y;
		gbcBtnRechazar.gridwidth = 1;
		ventana.add(btnRechazar, gbcBtnRechazar);
		
		
		btnCancelar = new JButton("Cancelar");
		GridBagConstraints gbcBtnCancelar = new GridBagConstraints();
		gbcBtnCancelar.insets = new Insets(5, 5, 5,5);
		gbcBtnCancelar.fill = GridBagConstraints.HORIZONTAL;
		gbcBtnCancelar.weightx = 1.0;
		gbcBtnCancelar.gridx = 5; gbcBtnCancelar.gridy = y;
		gbcBtnCancelar.gridwidth = 1;
		ventana.add(btnCancelar, gbcBtnCancelar);
		
		btnCancelar.addActionListener(e -> {
			this.setVisible(false);
		});
		
		btnConfirmar.addActionListener(e -> {
			String nombreEvento = (String) comboBoxEventos.getSelectedItem();	
			String nombreEdicion = listaEdicionesPendientes.getSelectedValue();
			if (nombreEdicion != null) {
			    ICE.aceptarEdicion(nombreEdicion,nombreEvento);
			    JOptionPane.showMessageDialog(this, 
			    	"Edición '" + nombreEdicion + "' confirmada con éxito", 
			    	"Confirmación exitosa", 
			    	JOptionPane.INFORMATION_MESSAGE);
			    refrescarEdiciones(); // Actualizar la lista después de confirmar
			} else {
			    JOptionPane.showMessageDialog(this, 
			    	"Por favor seleccione una edición para confirmar", 
			    	"Error", 
			    	JOptionPane.WARNING_MESSAGE);
			}
		});
		
		btnRechazar.addActionListener(e -> {
			String nombreEvento = (String) comboBoxEventos.getSelectedItem();
			String nombreEdicion = listaEdicionesPendientes.getSelectedValue();
			if (nombreEdicion != null) {
			    ICE.rechazarEdicion(nombreEdicion,nombreEvento);
			    JOptionPane.showMessageDialog(this, 
			    	"Edición '" + nombreEdicion + "' rechazada con éxito", 
			    	"Rechazo exitoso", 
			    	JOptionPane.INFORMATION_MESSAGE);
			    refrescarEdiciones(); // Actualizar la lista después de rechazar
			} else {
			    JOptionPane.showMessageDialog(this, 
			    	"Por favor seleccione una edición para rechazar", 
			    	"Error", 
			    	JOptionPane.WARNING_MESSAGE);
			}
		});
				
				
		
		
		
		

	}
	private static GridBagConstraints gbc(int x, int y, int w, int h, double wx, double wy, int fill) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x; c.gridy = y;
        c.gridwidth = w; c.gridheight = h;
        c.weightx = wx; c.weighty = wy;
        c.fill = fill;
        c.insets = new Insets(3, 3, 3, 3);
        c.anchor = GridBagConstraints.LINE_START;
        return c;
    }
	public static ConfirmarRechazarEdicion getInstance(IControllerEvento ice, IControllerUsuario icu) {
		if (instance == null) {
			instance = new ConfirmarRechazarEdicion(ice, icu);
		}
		return instance;
	}
	
	private void limpiarCampos() {
		comboBoxEventos.setSelectedIndex(-1);
		listaEdicionesPendientes.setModel(new DefaultListModel<String>());
	}
	
	public void refrescarEdiciones() {
	
	    if (comboBoxEventos.getItemCount() > 0) {
	        String nomEvento = (String) comboBoxEventos.getSelectedItem();
	        Evento evento = ManejadorEvento.getInstance().obtenerEvento(nomEvento);
	        if (evento != null) {
	            List<Edicion> edicionesPendientes = evento.getColEdicionesIngresadas();
	            DefaultListModel<String> model = new DefaultListModel<>();
	            for (Edicion edicion : edicionesPendientes) {
	                model.addElement(edicion.getNombre());
	            }
	            listaEdicionesPendientes.setModel(model);
	        }
	    } else {
	        listaEdicionesPendientes.setModel(new DefaultListModel<>());
	    }
	}
	
	public void refrescar() {
	    limpiarCampos();
	    comboBoxEventos.removeAllItems();
	    Collection<Evento> eventos = ManejadorEvento.getInstance().obtenerEventos().values();
	    if (eventos != null) {
	        for (Evento evento : eventos) {
	            comboBoxEventos.addItem(evento.getNombre());
	        }
	    }
	    
	}
}