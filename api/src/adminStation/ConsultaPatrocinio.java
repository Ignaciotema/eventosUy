package adminStation;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;

import logica.controllers.IControllerEvento;
import logica.data_types.DTPatrocinio;

@SuppressWarnings("serial")
public class ConsultaPatrocinio extends JInternalFrame {

    private static ConsultaPatrocinio instance;
    public static ConsultaPatrocinio getInstance(IControllerEvento ice) {
        if (instance == null) instance = new ConsultaPatrocinio(ice);
        return instance;
    }

    private final IControllerEvento controller;

    private final JComboBox<String> cbEventos   = new JComboBox<>();
    private final JComboBox<String> cbEdiciones = new JComboBox<>();
    private final DefaultListModel<String> modeloPat = new DefaultListModel<>();
    private final JList<String> lstPatrocinios  = new JList<>(modeloPat);

    private final JTextField txtInstitucion   = roField();
    private final JTextField txtNivel         = roField();
    private final JTextField txtAporte        = roField();
    private final JTextField txtTipoGratis    = roField();
    private final JTextField txtCantGratis    = roField();
    private final JTextField txtCodigo        = roField();
    private final JTextField txtFechaAlta     = roField();

    private ConsultaPatrocinio(IControllerEvento ice) {
        super("Consulta de Patrocinio", true, true, true, true);
        this.controller = ice;

        JPanel root = new JPanel(new GridBagLayout());
        root.setBorder(new EmptyBorder(10, 12, 12, 12));
        setContentPane(root);
        
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setIconifiable(true);
		setMaximizable(true);

        root.add(new JLabel("Eventos"),         gbc(0, 0, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL));
        root.add(cbEventos,                     gbc(0, 1, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL));
        root.add(new JLabel("Ediciones"),       gbc(0, 2, 1, 1, 0, 0, GridBagConstraints.HORIZONTAL));
        root.add(cbEdiciones,                   gbc(0, 3, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL));

        lstPatrocinios.setVisibleRowCount(8);
        lstPatrocinios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane spList = new JScrollPane(lstPatrocinios);
        spList.setPreferredSize(new Dimension(240, 180));
        root.add(spList, gbc(0, 4, 1, 2, 1, 1, GridBagConstraints.BOTH));

        JPanel detalle = new JPanel(new GridBagLayout());
        detalle.setBorder(BorderFactory.createTitledBorder("Detalle del patrocinio"));
        int r = 0;
        detalle.add(new JLabel("Institución:"),    gbc2(0, r)); detalle.add(txtInstitucion, gbc2(1, r++));
        detalle.add(new JLabel("Nivel:"),          gbc2(0, r)); detalle.add(txtNivel,       gbc2(1, r++));
        detalle.add(new JLabel("Aporte (UYU):"),   gbc2(0, r)); detalle.add(txtAporte,      gbc2(1, r++));
        detalle.add(new JLabel("Tipo reg. gratis:"),gbc2(0, r)); detalle.add(txtTipoGratis, gbc2(1, r++));
        detalle.add(new JLabel("Cant. gratis:"),   gbc2(0, r)); detalle.add(txtCantGratis,  gbc2(1, r++));
        detalle.add(new JLabel("Código:"),         gbc2(0, r)); detalle.add(txtCodigo,      gbc2(1, r++));
        detalle.add(new JLabel("Fecha alta:"),     gbc2(0, r)); detalle.add(txtFechaAlta,   gbc2(1, r++));
        root.add(detalle, gbc(1, 0, 1, 5, 1, 1, GridBagConstraints.BOTH));

        setBounds(40, 40, 780, 420);

        cargaEventos();
        cbEventos.addActionListener(e -> onEventoChange());
        cbEdiciones.addActionListener(e -> onEdicionChange());
        lstPatrocinios.addListSelectionListener(this::onPatrocinioSeleccionado);
    }

    private static GridBagConstraints gbc(int x, int y, int w, int h, double wx, double wy, int fill) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x; c.gridy = y; c.gridwidth = w; c.gridheight = h;
        c.weightx = wx; c.weighty = wy; c.insets = new Insets(6, 6, 6, 6);
        c.fill = fill; c.anchor = GridBagConstraints.NORTHWEST;
        return c;
    }
    private static GridBagConstraints gbc2(int x, int y) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x; c.gridy = y; c.insets = new Insets(4, 6, 4, 6);
        c.fill = (x==1) ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE;
        c.weightx = (x==1) ? 1 : 0; c.anchor = GridBagConstraints.WEST;
        return c;
    }
    private static JTextField roField() { JTextField t = new JTextField(22); t.setEditable(false); return t; }

    public void refrescar() {
        cargaEventos();
        limpiarDetalle();
        modeloPat.clear();
    }

    private void cargaEventos() {
        cbEventos.removeAllItems();
        Set<String> eventos = controller.listarEventos();
        if (eventos != null) for (String ev : eventos) cbEventos.addItem(ev);
        if (cbEventos.getItemCount() > 0) cbEventos.setSelectedIndex(0);
        onEventoChange();
    }

    private void onEventoChange() {
        String evento = (String) cbEventos.getSelectedItem();
        cbEdiciones.removeAllItems();
        if (evento != null) {
            for (String ed : controller.listarEdiciones(evento)) cbEdiciones.addItem(ed);
        }
        if (cbEdiciones.getItemCount() > 0) cbEdiciones.setSelectedIndex(0);
        onEdicionChange();
    }

    private void onEdicionChange() {
        String edicion = (String) cbEdiciones.getSelectedItem();
        modeloPat.clear();
        limpiarDetalle();
        if (edicion != null) {
            Set<String> instituciones = controller.listarPatrocinios(edicion);
            if (instituciones != null) {
                for (String inst : instituciones) modeloPat.addElement(inst);
            }
        }
    }

    private void onPatrocinioSeleccionado(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        String edicion = (String) cbEdiciones.getSelectedItem();
        String institucion = lstPatrocinios.getSelectedValue();
        if (edicion == null || institucion == null) { limpiarDetalle(); return; }

        DTPatrocinio p = controller.obtenerPatrocinio(edicion, institucion);
        if (p == null) { limpiarDetalle(); return; }

        txtInstitucion.setText(institucion);
        txtNivel.setText(p.getNivelPatrocinio() != null ? p.getNivelPatrocinio().name() : "");
        txtAporte.setText(String.valueOf(p.getMonto()));
        try { txtTipoGratis.setText(p.getTipoRegistroGratis()); } catch (Exception ex) { txtTipoGratis.setText(""); }
        try { txtCantGratis.setText(String.valueOf(p.getCantRegsGratis())); } catch (Exception ex) { txtCantGratis.setText(""); }
        try { txtCodigo.setText(p.getCodigo()); } catch (Exception ex) { txtCodigo.setText(""); }
        try {
            txtFechaAlta.setText(p.getFecha().toString());
        } catch (Exception ex) {
            txtFechaAlta.setText("");
        }
    }

    private void limpiarDetalle() {
        txtInstitucion.setText("");
        txtNivel.setText("");
        txtAporte.setText("");
        txtTipoGratis.setText("");
        txtCantGratis.setText("");
        txtCodigo.setText("");
        txtFechaAlta.setText("");
    }

    
	public void invocacionDesdeConsultaDeEdicion(String nivel, String edicion, String evento) {
		// Refrescar y seleccionar evento/edición
		refrescar();
		if (evento != null) cbEventos.setSelectedItem(evento);
		if (edicion != null) cbEdiciones.setSelectedItem(edicion);
		// Asegurarse de que el modelo de lista esté vacío antes de rellenar con el filtro
		modeloPat.clear();
		if (edicion != null) {
			Set<String> instituciones = controller.listarPatrocinios(edicion);
			if (instituciones != null) {
				for (String inst : instituciones) {
					try {
						DTPatrocinio p = controller.obtenerPatrocinio(edicion, inst);
						if (p != null && p.getNivelPatrocinio() != null && p.getNivelPatrocinio().name().equalsIgnoreCase(nivel)) {
							modeloPat.addElement(inst);
						}
					} catch (Exception ex) {
						// ignore malformed entry
					}
				}
			}
		}
		// Si hay resultados, seleccionar el primero para que se muestren los detalles
		if (modeloPat.getSize() > 0) {
			lstPatrocinios.setSelectedIndex(0);
			// onPatrocinioSeleccionado será invocado por el listener de selección
		} else {
			limpiarDetalle();
		}
	}
}