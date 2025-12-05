package adminStation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

import logica.controllers.IControllerEvento;
import logica.data_types.DTTipoRegistro;

import java.util.Set;

public class  ConsultaDeTipoDeRegistro extends JInternalFrame {
    private static final long serialVersionUID = 1L;
    private static ConsultaDeTipoDeRegistro instance = null;
    
    private JComboBox<String> cbEventos;
    private JComboBox<String> cbEdiciones;
    private JComboBox<String> cbTipoRegistro;

    private IControllerEvento controllerEvento;
    private JPanel panelDetalle;

    
    private static final String PLACEHOLDER_EVENTO = "— Seleccione evento —";
    private static final String PLACEHOLDER_EDICION = "— Seleccione edición —";
    private static final String PLACEHOLDER_TIPO = "— Seleccione tipo —";

    public ConsultaDeTipoDeRegistro(IControllerEvento ICE) {
        super("Consulta de Tipo de Registro", true, true, true, true);
        this.controllerEvento = ICE;
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(520, 380);
        setLocation(40, 40);
        setIconifiable(true);
		setMaximizable(true);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(content);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        int y = 0;

        // ComboBox eventos
        cbEventos = new JComboBox<>();
        cbEventos.setModel(new DefaultComboBoxModel<>(new String[] { PLACEHOLDER_EVENTO }));
        cbEventos.setEnabled(true);
        content.add(new JLabel("Evento:"), gbc(0, y, 1));
        content.add(cbEventos, gbc(1, y++, 1));

        // ComboBox ediciones
        cbEdiciones = new JComboBox<>();
        cbEdiciones.setModel(new DefaultComboBoxModel<>(new String[] { PLACEHOLDER_EDICION }));
        cbEdiciones.setEnabled(false);
        content.add(new JLabel("Edición:"), gbc(0, y, 1));
        content.add(cbEdiciones, gbc(1, y++, 1));

        // ComboBox tipo registro
        cbTipoRegistro = new JComboBox<>();
        cbTipoRegistro.setModel(new DefaultComboBoxModel<>(new String[] { PLACEHOLDER_TIPO }));
        cbTipoRegistro.setEnabled(false);
        content.add(new JLabel("Tipo de Registro:"), gbc(0, y, 1));
        content.add(cbTipoRegistro, gbc(1, y++, 1));

        // Panel for details (initially empty)
        panelDetalle = new JPanel(new GridBagLayout());
        panelDetalle.setVisible(false);
        c = gbc(0, y++, 2);
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        content.add(panelDetalle, c);

        // Listeners (después de inicializar todos los combos)
        cbEventos.addActionListener(e -> AlSeleccionarEvento());
        cbEdiciones.addActionListener(e -> AlSeleccionarEdicion());
        cbTipoRegistro.addActionListener(e -> AlSeleccionarTipoRegistro());

        // Limpiar campos
        limpiarCampos();

        // Poblar cbEventos
        cbEventos.removeAllItems();
        Set<String> eventos = controllerEvento.listarEventos();
        cbEventos.addItem(PLACEHOLDER_EVENTO);
        if(eventos != null) {
	        for (String evento : eventos) {
	            cbEventos.addItem(evento);
	        }
        }
    }

    
    private void AlSeleccionarEvento() {
        DefaultComboBoxModel<String> edicionModel = new DefaultComboBoxModel<>();
        edicionModel.addElement(PLACEHOLDER_EDICION);
        cbEdiciones.setModel(edicionModel);
        cbEdiciones.setEnabled(false);
        DefaultComboBoxModel<String> tipoModel = new DefaultComboBoxModel<>();
        tipoModel.addElement(PLACEHOLDER_TIPO);
        cbTipoRegistro.setModel(tipoModel);
        cbTipoRegistro.setEnabled(false);
        panelDetalle.setVisible(false);
        String eventoSeleccionado = (String) cbEventos.getSelectedItem();
        if (eventoSeleccionado != null && !eventoSeleccionado.equals(PLACEHOLDER_EVENTO)) {
			Set<String> ediciones = controllerEvento.listarEdiciones(eventoSeleccionado);
			for (String edicion : ediciones) {
				edicionModel.addElement(edicion);
			}
			cbEdiciones.setEnabled(true);
		}
    }

    
    private void AlSeleccionarEdicion() {
        DefaultComboBoxModel<String> tipoModel = new DefaultComboBoxModel<>();
        tipoModel.addElement(PLACEHOLDER_TIPO);
        cbTipoRegistro.setModel(tipoModel);
        cbTipoRegistro.setEnabled(false);
        panelDetalle.setVisible(false);
        String edicionSeleccionada = (String) cbEdiciones.getSelectedItem();
        if (edicionSeleccionada != null && !edicionSeleccionada.equals(PLACEHOLDER_EDICION)) {
			Set<String> tipos = controllerEvento.listarTiposDeRegistro(edicionSeleccionada);
			for (String tipo : tipos) {
				tipoModel.addElement(tipo);
			}
			cbTipoRegistro.setEnabled(true);
    }}

    public void invocacionDesdeConsultaDeEdicion(String tipo, String edicion, String evento) {
		cbEventos.addItem(evento); // Asegurarse de que el evento esté en el combo
    	cbEventos.setSelectedItem(evento);
		AlSeleccionarEvento();
	    cbEdiciones.addItem(edicion); // Asegurarse de que la edición esté en el combo
		cbEdiciones.setSelectedItem(edicion);
		AlSeleccionarEdicion(); // Cargar tipos de registro de la edición seleccionada
		cbTipoRegistro.addItem(tipo); // Asegurarse de que el tipo esté en el combo
		cbTipoRegistro.setSelectedItem(tipo);
		AlSeleccionarTipoRegistro();
	}
    
    
    
    
    
    
    
    
    
    private void AlSeleccionarTipoRegistro() {
        panelDetalle.removeAll();
        panelDetalle.setVisible(false);
        panelDetalle.revalidate();
        panelDetalle.repaint();
       
        DTTipoRegistro dto = controllerEvento.verDetalleTRegistro(
				
        		(String) cbEdiciones.getSelectedItem(),
				(String) cbTipoRegistro.getSelectedItem()
		);
        
        if (dto != null) {
			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(4, 6, 4, 6);
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1.0;
			int y = 0;

			// Nombre
			panelDetalle.add(new JLabel("Nombre del tipo:"), gbc(0, y, 1));
			panelDetalle.add(new JTextField(safe(dto.getNombre())) {{
				setEditable(false);
			}}, gbc(1, y++, 1));

			// Descripción
			panelDetalle.add(new JLabel("Descripción:"), gbc(0, y, 1));
			JTextArea taDesc = new JTextArea(safe(dto.getDescripcion()), 3, 30);
			taDesc.setLineWrap(true);
			taDesc.setWrapStyleWord(true);
			taDesc.setEditable(false);
			JScrollPane spDesc = new JScrollPane(taDesc);
			c = gbc(1, y++, 1);
			c.fill = GridBagConstraints.BOTH;
			c.weighty = 1.0;
			panelDetalle.add(spDesc, c);

			// Costo
			c = gbc(0, y, 1);
			c.fill = GridBagConstraints.HORIZONTAL; c.weighty = 0;
			panelDetalle.add(new JLabel("Costo:"), c);
			panelDetalle.add(new JTextField(formatMoney(dto.getCosto())) {{
				setEditable(false);
			}}, gbc(1, y++, 1));

			// Cupo
			panelDetalle.add(new JLabel("Cupo:"), gbc(0, y, 1));
			panelDetalle.add(new JTextField(String.valueOf(dto.getCupo())) {{
				setEditable(false);
			}}, gbc(1, y++, 1));

			panelDetalle.setVisible(true);
			panelDetalle.revalidate();
			panelDetalle.repaint();
		}
    }

 
    private static GridBagConstraints gbc(int x, int y, int w) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x; c.gridy = y;
        c.gridwidth = w; c.gridheight = 1;
        c.insets = new Insets(4, 6, 4, 6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = (w == 2 ? 1.0 : (x == 1 ? 1.0 : 0.0));
        return c;
    }

    private static String safe(Object o) { return o == null ? "" : String.valueOf(o); }

    private static String formatMoney(double v) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("es", "UY"));
        return nf.format(v);
    }
    
    public static ConsultaDeTipoDeRegistro getInstance(IControllerEvento ICE) {
		if (instance == null) {
			instance = new ConsultaDeTipoDeRegistro(ICE);
		}
		return instance;
	}

    public void limpiarCampos() {
        cbEventos.setModel(new DefaultComboBoxModel<>(new String[] { PLACEHOLDER_EVENTO }));
        cbEventos.setEnabled(true);
        cbEdiciones.setModel(new DefaultComboBoxModel<>(new String[] { PLACEHOLDER_EDICION }));
        cbEdiciones.setEnabled(false);
        cbTipoRegistro.setModel(new DefaultComboBoxModel<>(new String[] { PLACEHOLDER_TIPO }));
        cbTipoRegistro.setEnabled(false);
        panelDetalle.removeAll();
        panelDetalle.setVisible(false);
        panelDetalle.revalidate();
        panelDetalle.repaint();
    }  
    
    public void refrescar() {
		limpiarCampos();
		cbEventos.removeAllItems();
		cbEventos.addItem(PLACEHOLDER_EVENTO);
		Set<String> eventos = controllerEvento.listarEventos();
		if(eventos != null) {
	        for (String evento : eventos) {
	            cbEventos.addItem(evento);
	        }
		}
	}
    
    
}