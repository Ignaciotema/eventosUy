package adminStation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import logica.controllers.IControllerEvento;
import logica.data_types.DTDetalleEvento;


@SuppressWarnings("serial")
public class ConsultaDeEvento extends JInternalFrame {
    private static ConsultaDeEvento instance = null;
	// Java
	
    private static final String PLACEHOLDER_EVENTO = "Seleccionar evento";
    private static final String PLACEHOLDER_EDICION = "Seleccionar edición";
    private JComboBox<String> cbxListadoDeEventos;
    private JComboBox<String> cbxListadoDeEdiciones;
    
    
    private IControllerEvento controllerEvento;
 
    private JTextField txtNombreEvento = new JTextField(20);
    private JTextField txtSiglaEvento = new JTextField(20);
    


    private JTextArea textAreaDescripcion;
    private DefaultListModel<String> modeloCategorias = new DefaultListModel<>();
    private JList<String> listCategorias = new JList<>(modeloCategorias);
    
    private javax.swing.JButton btnFinalizarEvento;
    
   
    

    public ConsultaDeEvento(IControllerEvento ice) {
    	controllerEvento = ice;
    	setTitle("Consulta de evento");
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setBounds(10, 10, 700, 410); 
        txtNombreEvento.setEditable(false);
        txtSiglaEvento.setEditable(false);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(new EmptyBorder(8, 10, 10, 10));
        getContentPane().add(content, BorderLayout.CENTER);
        int y = 0;

        // Combo de eventos
        JLabel lblEventos = new JLabel("Listado de eventos");
        content.add(lblEventos, gbc(0, y++, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL));

        cbxListadoDeEventos = new JComboBox<>();
        cbxListadoDeEventos.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        content.add(cbxListadoDeEventos, gbc(0, y++, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL));

        JPanel panelNombre = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Etiqueta "Nombre:"
        JLabel lblNombre = new JLabel("Nombre:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 5); // margen derecho
        gbc.fill = GridBagConstraints.NONE;
        panelNombre.add(lblNombre, gbc);

        // Campo de texto
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelNombre.add(txtNombreEvento, gbc);

        // Botón "Finalizar evento"
        btnFinalizarEvento = new javax.swing.JButton("Finalizar evento");
        gbc.gridx = 2;
        gbc.weightx = 0; // no expandir
        gbc.insets = new Insets(0, 5, 0, 0); // margen izquierdo
        gbc.fill = GridBagConstraints.NONE;
        panelNombre.add(btnFinalizarEvento, gbc);
        btnFinalizarEvento.setEnabled(false);

        GridBagConstraints gbcNombre = gbc(0, y, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL);
        gbcNombre.anchor = GridBagConstraints.WEST; // alinear a la izquierda
        content.add(panelNombre, gbcNombre);
        y++;

        JPanel panelSigla = new JPanel(new BorderLayout(5, 0)); // 5px de separación
        panelSigla.add(new JLabel("Sigla:"), BorderLayout.WEST);
        panelSigla.add(txtSiglaEvento , BorderLayout.CENTER);

        // Agregar al GridBagLayout solo en la columna 0
        content.add(panelSigla, gbc(0, y, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL));
        y++;

;

        // Panel horizontal para descripción y categorías
        textAreaDescripcion = new JTextArea(6, 40);
        textAreaDescripcion.setLineWrap(true);
        textAreaDescripcion.setWrapStyleWord(true);
        textAreaDescripcion.setEditable(false);
        JScrollPane spDesc = new JScrollPane(textAreaDescripcion);
        spDesc.setPreferredSize(new Dimension(400, 120));

        DefaultListModel<String> modeloCategorias = new DefaultListModel<>();

     // lista cats
     
     listCategorias.setVisibleRowCount(6); // cantidad de filas visibles a la vez
     listCategorias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
     JScrollPane spCats = new JScrollPane(listCategorias);
     spCats.setPreferredSize(new Dimension(200, 120));
     
     
        JLabel lbldes = new JLabel("Descripcion");
        JLabel lblcat = new JLabel("Categorias");
        content.add(lbldes, gbc(0, y++, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL));
        content.add(lblcat, gbc(1, y-1, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL));
        content.add(spDesc, gbc(0, y, 1, 1, 1, 1, GridBagConstraints.BOTH));
        content.add(spCats, gbc(1, y++, 1, 1, 0.5, 1, GridBagConstraints.BOTH));
      
        // Combo de ediciones
        JLabel lblEdiciones = new JLabel("Listado de Ediciones");
        content.add(lblEdiciones, gbc(0, y++, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL));

        cbxListadoDeEdiciones = new JComboBox<>();
        cbxListadoDeEdiciones.addItem(PLACEHOLDER_EDICION);
        cbxListadoDeEdiciones.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        content.add(cbxListadoDeEdiciones, gbc(0, y++, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL));

     


        // Vincular con el BackEnd
        
        Set<String> eventos = controllerEvento.listarEventosConFinalizados();
        cbxListadoDeEventos.removeAllItems();
        cbxListadoDeEventos.addItem(PLACEHOLDER_EVENTO);
        if(eventos != null) {for (String ev : eventos) cbxListadoDeEventos.addItem(ev);};
        
        // Listeners
        cbxListadoDeEventos.addActionListener(e -> actualizarEventoSeleccionado());
        cbxListadoDeEdiciones.addActionListener(e -> {
        	String edicion = (String) cbxListadoDeEdiciones.getSelectedItem();
        	String evento = (String) cbxListadoDeEventos.getSelectedItem();
        	System.out.println(edicion);
            if (edicion!=null && !edicion.equals(PLACEHOLDER_EDICION)) {
        		llamarAConsultaEdicion(edicion,evento);
            }
        });
        
        
        
        btnFinalizarEvento.addActionListener(e -> {
            String eventoSeleccionado = (String) cbxListadoDeEventos.getSelectedItem();
            
            
            int resultado = JOptionPane.showConfirmDialog(
                this,
                "¿Desea finalizar el evento \"" + eventoSeleccionado + "\"?\nEsta acción no se puede deshacer.",
                "Confirmar finalización",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (resultado == JOptionPane.YES_OPTION) {
                try {
                    // Llamar al controlador para finalizar el evento
                    controllerEvento.finalizarEvento(eventoSeleccionado);
                    
                    // Mostrar mensaje de éxito
                    JOptionPane.showMessageDialog(
                        this,
                        "El evento ha sido finalizado correctamente.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    
                    // Opcional: refrescar la interfaz
                    refrescar();
                    
                } catch (Exception ex) {
                    // Manejar errores (por ejemplo, si el evento ya está finalizado)
                    JOptionPane.showMessageDialog(
                        this,
                        "No se pudo finalizar el evento:\n" + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    ex.printStackTrace();
                }
            }
        });
        

        if (cbxListadoDeEventos.getItemCount() > 0) cbxListadoDeEventos.setSelectedIndex(0);
    }

    private void actualizarEventoSeleccionado() {
        String evento = (String) cbxListadoDeEventos.getSelectedItem();
        if (evento != null && !evento.equals(PLACEHOLDER_EVENTO)) {
            // Labels de Nombre y Sigla
        	DTDetalleEvento dtde = null;
        	try {
        		dtde = controllerEvento.verDetalleEvento(evento);
        	}
        	catch (Exception ex) {
        		ex.printStackTrace();
        		return;
        	}
            
           
            	txtNombreEvento.setText(dtde.getNombre());
            	txtSiglaEvento.setText(dtde.getSigla());
                textAreaDescripcion.setText(dtde.getDescripcion());
                
                if(!dtde.getFinalizado()) {
                	btnFinalizarEvento.setEnabled(true);
                } else {
                	btnFinalizarEvento.setEnabled(false);
                }
                
           
            modeloCategorias.clear(); 
            Set<String> cats = dtde.getCategorias();
                for (String c : cats) {
                    modeloCategorias.addElement(c);
                }
            }

            // Combo de ediciones
            cbxListadoDeEdiciones.removeAllItems();
            cbxListadoDeEdiciones.addItem(PLACEHOLDER_EDICION);
            Set<String> eds = controllerEvento.listarEdiciones(evento);
            if (eds != null) {
                for (String ed : eds) cbxListadoDeEdiciones.addItem(ed);
                if (!eds.isEmpty()) cbxListadoDeEdiciones.setSelectedIndex(0);
            }
         else {
            cbxListadoDeEdiciones.removeAllItems();
            cbxListadoDeEdiciones.addItem(PLACEHOLDER_EDICION);
        }
    }

    
    
    //Metodo para refrescar el combo de eventos
    public void refrescar() {
        Set<String> eventos = controllerEvento.listarEventosConFinalizados();
        cbxListadoDeEventos.removeAllItems();
        cbxListadoDeEventos.addItem(PLACEHOLDER_EVENTO);
        if (eventos != null) {
            for (String ev : eventos) cbxListadoDeEventos.addItem(ev);
        }
        cbxListadoDeEventos.setSelectedIndex(0);

        // Limpiar campos
        txtNombreEvento.setText("");
        txtSiglaEvento.setText("");
        textAreaDescripcion.setText("");
        modeloCategorias.clear();
        cbxListadoDeEdiciones.removeAllItems();
        cbxListadoDeEdiciones.addItem(PLACEHOLDER_EDICION);
        btnFinalizarEvento.setEnabled(false);
    }
       
    
    
    private void llamarAConsultaEdicion(String edicion, String evento) {
        ConsultaEdicionDeEvento frmConsultaEdicionDeEvento = ConsultaEdicionDeEvento.getInstance(controllerEvento);
        JDesktopPane desktop = getDesktopPane();
        if (desktop != null) {
			frmConsultaEdicionDeEvento.invocacionDesdeConsultaDeEvento(edicion, evento);
			frmConsultaEdicionDeEvento.setVisible(true);
		    frmConsultaEdicionDeEvento.toFront();
		}
			
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
    public static ConsultaDeEvento getInstance(IControllerEvento ice) {
    	 if (instance == null) {
			 instance = new ConsultaDeEvento(ice);
		 }
		 return instance;
    }
    


}