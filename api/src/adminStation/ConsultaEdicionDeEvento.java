package adminStation;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultCellEditor;
import javax.swing.JInternalFrame;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import logica.controllers.IControllerEvento;
import logica.manejadores.ManejadorEvento;

import javax.swing.ListSelectionModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;




@SuppressWarnings("serial")
public class ConsultaEdicionDeEvento extends JInternalFrame {
	private static ConsultaEdicionDeEvento instance = null;
	public static interface AbrirRegistros {
	    void open(String evento, String edicion, logica.data_types.DTTipoRegistro registro);
	}
	public static interface AbrirPatrocinios {
	    void open(String evento, String edicion, String nivel, java.util.List<logica.data_types.DTPatrocinio> patrociniosDelNivel);
	}

	
	
	
    private static final String PLACEHOLDER_REG_TIPO = "— Seleccione tipo —";
    private static final String PLACEHOLDER_PAT_TIPO = "— Seleccione nivel —";

    private JComboBox<String> cbxListadoDeEdiciones;
    private JComboBox<String> cbxListadoDeEventos;

    // JCombos para embebidos en tabla
    private JComboBox<String> editorTiposRegCombo;  // col 7
    private JComboBox<String> editorTiposPatCombo;  // col 8


    // Tablas
    private JTable tblDetallesDeEdicion;
    private JTable tblDetalleDeRegistro;
    private JTable tblDetalleDePatrocinio;

    
    //Lógica
    private IControllerEvento controllerEvento;
    
    //Para abrir jInternlFrames de patrocinios y registros con tablas
    private AbrirRegistros onOpenTipoRegistro;
    private AbrirPatrocinios onOpenPatrocinio;

    public void setOnOpenTipoRegistro(AbrirRegistros opener) { this.onOpenTipoRegistro = opener; }
    public void setOnOpenPatrocinio(AbrirPatrocinios opener) { this.onOpenPatrocinio = opener; }

    public ConsultaEdicionDeEvento(IControllerEvento ice) {
    	controllerEvento = ice;
    	
        setTitle("Consulta edición de evento");
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setBounds(30, 30, 627, 365);

        getContentPane().setLayout(new BorderLayout());

        // Contenedor principal (Swing puro)
        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(new EmptyBorder(8, 10, 10, 10));
        getContentPane().add(content, BorderLayout.CENTER);

        int y = 0;

        // Eventos: label + combo
        JLabel lblEventos = new JLabel("Listado de eventos");
        content.add(lblEventos, gbc(0, y++, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL));

        cbxListadoDeEventos = new JComboBox<>();
        cbxListadoDeEventos.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        content.add(cbxListadoDeEventos, gbc(0, y++, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL));

        // Ediciones: label + combo
        JLabel lblEdiciones = new JLabel("Listado de Ediciones");
        content.add(lblEdiciones, gbc(0, y++, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL));

        cbxListadoDeEdiciones = new JComboBox<>();
        cbxListadoDeEdiciones.setPrototypeDisplayValue("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        content.add(cbxListadoDeEdiciones, gbc(0, y++, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL));

        // Tabla de detalles de edición
        JLabel lblDetallesEd = new JLabel("Detalles de edición");
        content.add(lblDetallesEd, gbc(0, y++, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL));

        tblDetallesDeEdicion = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Nombre", "Sigla", "Fecha inicio", "Fecha fin", "Ciudad", "País",
                        "Organizador", "Estado", "Tipo de registros", "Patrocinios"}
        ) {
            @Override public boolean isCellEditable(int r, int c) { return c == 8 || c == 9; }
        });
        configurarTablaBasica(tblDetallesDeEdicion);
        JScrollPane spDetEd = new JScrollPane(tblDetallesDeEdicion);
        spDetEd.setPreferredSize(new Dimension(860, 170));
        content.add(spDetEd, gbc(0, y++, 1, 1, 1, 1, GridBagConstraints.BOTH));

        // Editor combo en columna "Tipo de registros"
        editorTiposRegCombo = new JComboBox<>();
        editorTiposRegCombo.setToolTipText("Elegí un tipo de registro");
        editorTiposRegCombo.addActionListener(e -> {
            if (tblDetallesDeEdicion.isEditing()) tblDetallesDeEdicion.getCellEditor().stopCellEditing();

            String tipo = (String) editorTiposRegCombo.getSelectedItem();
            String evento = (String) cbxListadoDeEventos.getSelectedItem();
            String edicion = (String) cbxListadoDeEdiciones.getSelectedItem();

            if (tipo == null || PLACEHOLDER_REG_TIPO.equals(tipo)) {
                return;
            }

           // reset placeholder in the "Tipo de registros" column (index 8)
           tblDetallesDeEdicion.setValueAt(PLACEHOLDER_REG_TIPO, 0, 8);
           llamarAConsultaDeTipoDeRegistro(tipo, edicion, evento);

        });

        TableColumn colTipoReg = tblDetallesDeEdicion.getColumnModel().getColumn(8);
        DefaultCellEditor regEditor = new DefaultCellEditor(editorTiposRegCombo);
        regEditor.setClickCountToStart(1); // abrir con un click
        colTipoReg.setCellEditor(regEditor);

        // Editor combo en columna "Patrocinios"
        editorTiposPatCombo = new JComboBox<>();
        editorTiposPatCombo.setToolTipText("Elegí un nivel de patrocinio");
        editorTiposPatCombo.addActionListener(e -> {
            if (tblDetallesDeEdicion.isEditing()) tblDetallesDeEdicion.getCellEditor().stopCellEditing();

            String nivel = (String) editorTiposPatCombo.getSelectedItem();
            String evento = (String) cbxListadoDeEventos.getSelectedItem();
            String edicion = (String) cbxListadoDeEdiciones.getSelectedItem();

            if (nivel == null || PLACEHOLDER_PAT_TIPO.equals(nivel)) {
                return;
            }

            try {
                java.util.List<logica.data_types.DTPatrocinio> lista = new java.util.ArrayList<>();
                for (String inst : controllerEvento.listarPatrocinios(edicion)) {
                    logica.data_types.DTPatrocinio p = controllerEvento.obtenerPatrocinio(edicion, inst);
                    if (p != null && p.getNivelPatrocinio().name().equalsIgnoreCase(nivel)) {
                        lista.add(p);
                    }
                }
                if (onOpenPatrocinio != null) {
                    onOpenPatrocinio.open(evento, edicion, nivel, lista);
                } else {

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            // reset placeholder in the "Patrocinios" column (index 9)
            tblDetallesDeEdicion.setValueAt(PLACEHOLDER_PAT_TIPO, 0, 9);
            llamarAConsultaDePatrocinio(nivel, edicion, evento);

            
        });
        

        
        

        TableColumn colPat = tblDetallesDeEdicion.getColumnModel().getColumn(9);
        DefaultCellEditor patEditor = new DefaultCellEditor(editorTiposPatCombo);
        patEditor.setClickCountToStart(1); // abrir con un click
        colPat.setCellEditor(patEditor);

        //listeners
        alCambiarEvento();
        cargarEventosDesdeLogica();

        if (cbxListadoDeEventos.getItemCount() > 0) cbxListadoDeEventos.setSelectedIndex(0);
    }
    
    

    private static GridBagConstraints gbc(int x, int y, int w, int h, double wx, double wy, int fill) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x; c.gridy = y;
        c.gridwidth = w; c.gridheight = h;
        c.weightx = wx; c.weighty = wy;
        c.fill = fill;
        c.insets = new Insets(3, 0, 3, 0);
        c.anchor = GridBagConstraints.LINE_START;
        return c;
    }

    private void configurarTablaBasica(JTable t) {
        t.setFillsViewportHeight(true);
        t.setRowHeight(22);
        t.setAutoCreateRowSorter(true);
        t.getTableHeader().setReorderingAllowed(false);
        t.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    

    /* ---------- listeners ---------- */
    private void alCambiarEvento() {
        cbxListadoDeEventos.addActionListener(e -> {
            String evento = (String) cbxListadoDeEventos.getSelectedItem();
            actualizarEdicionesPara(evento);
        });
        cbxListadoDeEdiciones.addActionListener(e -> {
            String edicion = (String) cbxListadoDeEdiciones.getSelectedItem();
            actualizarTablasParaEdicion(edicion);
        });
    }

    private void actualizarEdicionesPara(String evento) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        try {
            if (evento != null) {
                for (String ed : controllerEvento.listarEdiciones(evento)) {
                    model.addElement(ed);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        cbxListadoDeEdiciones.setModel(model);
        cbxListadoDeEdiciones.setEnabled(model.getSize() > 0);

        limpiarTablas();
        prepararEditorTipoRegistros(null);
        prepararEditorTipoPatrocinios(null);


        if (model.getSize() > 0) cbxListadoDeEdiciones.setSelectedIndex(0);
    }


	private void actualizarTablasParaEdicion(String edicion) {
	    DefaultTableModel detModel = (DefaultTableModel) tblDetallesDeEdicion.getModel();
	    detModel.setRowCount(0);
	
	    if (edicion == null) {
	        prepararEditorTipoRegistros(null);
	        prepararEditorTipoPatrocinios(null);
	        return;
	    }
	
	    try {
	        logica.data_types.DTDetalleEdicion dt = controllerEvento.mostrarDetallesEdicion(edicion);
	        if (dt != null) {
	            Object[] fila = new Object[]{
	                dt.getNombre(),
	                dt.getSigla(),
	                String.valueOf(dt.getFechaInicio()),
	                String.valueOf(dt.getFechaFin()),
	                dt.getCiudad(),
	                dt.getPais(),
	                dt.getOrganizador(),
	                dt.getEstado() != null ? dt.getEstado().name() : "",
	                PLACEHOLDER_REG_TIPO,
	                PLACEHOLDER_PAT_TIPO
	            };
	            detModel.addRow(fila);
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	
	    prepararEditorTipoRegistros(edicion);
	    prepararEditorTipoPatrocinios(edicion);
	
	}


	private void prepararEditorTipoRegistros(String edicion) {
	    DefaultComboBoxModel<String> tipoModel = new DefaultComboBoxModel<>();
	    tipoModel.addElement(PLACEHOLDER_REG_TIPO);

	    if (edicion != null) {
	        try {
	            for (String tr : controllerEvento.listarTiposDeRegistro(edicion)) { //como hacemos esto
	                tipoModel.addElement(tr);
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
	    editorTiposRegCombo.setModel(tipoModel);
	}


	private void prepararEditorTipoPatrocinios(String edicion) {
	    DefaultComboBoxModel<String> patModel = new DefaultComboBoxModel<>();
	    patModel.addElement(PLACEHOLDER_PAT_TIPO);

	    if (edicion != null) {
	        java.util.Set<String> instituciones = java.util.Collections.emptySet();
	        try {
	            instituciones = controllerEvento.listarPatrocinios(edicion);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        java.util.Set<String> niveles = new java.util.LinkedHashSet<>();
	        
	        if (instituciones !=null){
	        for (String inst : instituciones) {
	            try {
	                logica.data_types.DTPatrocinio p = controllerEvento.obtenerPatrocinio(edicion, inst);
	                if (p != null && p.getNivelPatrocinio().name() != null) niveles.add(p.getNivelPatrocinio().name());
	            } catch (Exception ex) {
	                ex.printStackTrace();
	            }
	        }
	        for (String n : niveles) patModel.addElement(n);
	    }}

	    editorTiposPatCombo.setModel(patModel);
	}


	private void llamarAConsultaDeTipoDeRegistro(String tipoReg, String edicion, String evento) {
        ConsultaDeTipoDeRegistro frmConsultaDeTipoRegistro = ConsultaDeTipoDeRegistro.getInstance(controllerEvento);
        JDesktopPane desktop = getDesktopPane();
        if (desktop != null) {
			frmConsultaDeTipoRegistro.invocacionDesdeConsultaDeEdicion(tipoReg, edicion, evento);
			frmConsultaDeTipoRegistro.setVisible(true);
			frmConsultaDeTipoRegistro.toFront();
			
			}
		}
	
	private void llamarAConsultaDePatrocinio(String nivel, String edicion, String evento) {
		ConsultaPatrocinio frmConsultaPatrocinio = ConsultaPatrocinio.getInstance(controllerEvento);
		JDesktopPane desktop = getDesktopPane();
		if (desktop != null) {
			
			frmConsultaPatrocinio.invocacionDesdeConsultaDeEdicion(nivel, edicion, evento);
			frmConsultaPatrocinio.setVisible(true);
			frmConsultaPatrocinio.toFront();
		}
	}
    
    private void limpiarTablas() {
        ((DefaultTableModel) tblDetallesDeEdicion.getModel()).setRowCount(0);
    }
    public void invocacionDesdeConsultaDeEvento(String edicion,String evento){
    	// Primero seleccionar el evento
    	cbxListadoDeEventos.setSelectedItem(evento);
    	
    	// Actualizar las ediciones para ese evento
    	actualizarEdicionesPara(evento);
    	
    	// Luego seleccionar la edición específica
    	cbxListadoDeEdiciones.setSelectedItem(edicion);
    	
    	// Actualizar las tablas para esa edición
    	actualizarTablasParaEdicion(edicion);
    }
    
    
    public void invocacionDesdeConsultaUsuario(String edicion) {
    	String evento=controllerEvento.nomEvPorEd(edicion);
    	
    	cbxListadoDeEventos.setSelectedItem(evento);
    	actualizarEdicionesPara(evento);
		cbxListadoDeEdiciones.setSelectedItem(edicion);
		actualizarTablasParaEdicion(edicion);
    }

    // Carga de datos en tablas y demás
    private void cargarEventosDesdeLogica() {
    	DefaultComboBoxModel<String> evModel = new DefaultComboBoxModel<>();
    	try {
            for (String ev : controllerEvento.listarEventos()) {
                evModel.addElement(ev);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        cbxListadoDeEventos.setModel(evModel);
        cbxListadoDeEventos.setEnabled(evModel.getSize() > 0);
        if (evModel.getSize() > 0) cbxListadoDeEventos.setSelectedIndex(0);
    }
    public static ConsultaEdicionDeEvento getInstance(IControllerEvento ice) {
    			if (instance == null) {
    				instance = new ConsultaEdicionDeEvento(ice);
    			}
    			return instance;
    }


    public void refrescar() {
		cargarEventosDesdeLogica();
        // forzá limpiar/estado inicial
        ((DefaultTableModel) tblDetallesDeEdicion.getModel()).setRowCount(0);

	}

}