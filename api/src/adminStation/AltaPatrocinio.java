package adminStation;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.util.Set;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import logica.controllers.IControllerEvento;
import logica.controllers.IControllerUsuario;
import logica.enumerators.NivelPatrocinio;




public class AltaPatrocinio extends JInternalFrame {

    private static final long serialVersionUID = 1L;
    private static AltaPatrocinio instance = null;
	private final IControllerEvento ice;
	private final IControllerUsuario icu;


    private final JComboBox<String> cbEvento = new JComboBox<>();
    private final JComboBox<String> cbEdicion = new JComboBox<>();
    private final JComboBox<String> cbTipoLista = new JComboBox<>();
    private final JComboBox<String> cbTipoGratis = new JComboBox<>();
    private final JComboBox<String> cbTipo = new JComboBox<>();
    private final JComboBox<String> cbInstitucion = new JComboBox<>();
    private final JComboBox<NivelPatrocinio> cbNivel = new JComboBox<>(NivelPatrocinio.values());
    private final JTextField tfAporte = new JTextField(10);
    private final JTextField tfCodigo = new JTextField(10);
    private final JSpinner spCantGratis = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
    private final JLabel lblCostoTipo = new JLabel("Costo tipo: -");
    private final JLabel lblRegla20 = new JLabel("Regla 20%: costoGratis=0 | 20% aporte=0");
    private final JButton btnAceptar = new JButton("Aceptar");
    private final JButton btnCancelar = new JButton("Cancelar");

    public AltaPatrocinio(IControllerEvento iCE, IControllerUsuario iCU) {
    	super("Alta de Patrocinio", true, true, true, true);
    	setBounds(20, 20, 720, 360);
        this.ice = iCE;
        this.icu = iCU;
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setIconifiable(true);
		setMaximizable(true);

        
        JPanel root = new JPanel(new GridBagLayout());
        setContentPane(root);

        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        
        add(new JLabel("Evento:"), gbc(0, 0, 1, GridBagConstraints.NONE, 0));
        add(cbEvento,              gbc(1, 0, 1, GridBagConstraints.HORIZONTAL, 1));
        add(new JLabel("Edición:"),gbc(2, 0, 1, GridBagConstraints.NONE, 0));
        add(cbEdicion,             gbc(3, 0, 1, GridBagConstraints.HORIZONTAL, 1));

        add(new JLabel("Tipo de registro:"), gbc(0, 1, 1, GridBagConstraints.NONE, 0));
        add(cbTipo,                           gbc(1, 1, 1, GridBagConstraints.HORIZONTAL, 1));
        add(lblCostoTipo,                     gbc(2, 1, 2, GridBagConstraints.HORIZONTAL, 1));

        add(new JLabel("Institución:"),       gbc(0, 2, 1, GridBagConstraints.NONE, 0));
        add(cbInstitucion,                    gbc(1, 2, 1, GridBagConstraints.HORIZONTAL, 1));
        add(new JLabel("Nivel:"),             gbc(2, 2, 1, GridBagConstraints.NONE, 0));
        add(cbNivel,                          gbc(3, 2, 1, GridBagConstraints.HORIZONTAL, 1));

        add(new JLabel("Aporte económico (UYU):"), gbc(0, 3, 1, GridBagConstraints.NONE, 0));
        add(tfAporte,                               gbc(1, 3, 1, GridBagConstraints.HORIZONTAL, 1));
        add(new JLabel("Código:"),                  gbc(2, 3, 1, GridBagConstraints.NONE, 0));
        add(tfCodigo,                               gbc(3, 3, 1, GridBagConstraints.HORIZONTAL, 1));

        add(new JLabel("Registros gratis para tipo:"), gbc(0, 4, 1, GridBagConstraints.NONE, 0));
        add(cbTipo,                                       gbc(1, 4, 1, GridBagConstraints.HORIZONTAL, 1)); // mismo combo
        add(new JLabel("Cantidad:"),                      gbc(2, 4, 1, GridBagConstraints.NONE, 0));
        add(spCantGratis,                                 gbc(3, 4, 1, GridBagConstraints.HORIZONTAL, 1));

        lblRegla20.setForeground(new Color(0x33, 0x33, 0x33));
        add(lblRegla20, gbc(0, 5, 4, GridBagConstraints.HORIZONTAL, 1));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.add(btnAceptar);
        btns.add(btnCancelar);
        add(btns, gbc(0, 6, 4, GridBagConstraints.HORIZONTAL, 1));

        cargaData();
        eventos();
    }

    private GridBagConstraints gbc(int x, int y, int w, int fill, double wx) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x; c.gridy = y; c.gridwidth = w;
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = fill;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = wx;
        return c;
    }

    private void cargaData() {
        cbEvento.setModel(new DefaultComboBoxModel<>(ice.listarEventos().toArray(new String[0])));
        if (cbEvento.getItemCount() > 0) cbEvento.setSelectedIndex(0);
        refreshEdiciones();
        refreshTipos();
        refreshInstituciones();
        updateCostoYRegla();
    }

    private void eventos() {
        cbEvento.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                refreshEdiciones();
            }
        });
        cbEdicion.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                refreshTipos();
                updateCostoYRegla();
            }
        });
        cbTipo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) updateCostoYRegla();
        });
        spCantGratis.addChangeListener(e -> updateCostoYRegla());
        tfAporte.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateCostoYRegla(); }
            public void removeUpdate(DocumentEvent e) { updateCostoYRegla(); }
            public void changedUpdate(DocumentEvent e) { updateCostoYRegla(); }
        });
        cbInstitucion.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) checkDuplicado();
        });

        btnAceptar.addActionListener(e -> onAceptar());
        btnCancelar.addActionListener(e -> onCancelar());
    }

    private void refreshEdiciones() {
        String evento = (String) cbEvento.getSelectedItem();
        Set<String> eds = (evento == null) ? Set.of() : ice.listarEdiciones(evento);
        cbEdicion.setModel(new DefaultComboBoxModel<>(eds.toArray(new String[0])));
        if (cbEdicion.getItemCount() > 0) cbEdicion.setSelectedIndex(0);
        refreshTipos();
    }

    private void refreshTipos() {
        String edicion = (String) cbEdicion.getSelectedItem();
        Set<String> trs = (edicion == null) ? Set.of() : ice.listarTiposDeRegistro(edicion);
        cbTipo.setModel(new DefaultComboBoxModel<>(trs.toArray(new String[0])));
        if (cbTipo.getItemCount() > 0) cbTipo.setSelectedIndex(0);
        updateCostoYRegla();
    }

    private void refreshInstituciones() {
        cbInstitucion.setModel(new DefaultComboBoxModel<>(icu.listarInstituciones().toArray(new String[0])));
        if (cbInstitucion.getItemCount() > 0) cbInstitucion.setSelectedIndex(0);
    }

    private void updateCostoYRegla() {
        String edicion = (String) cbEdicion.getSelectedItem();
        String tipo = (String) cbTipo.getSelectedItem();
        double costoTipo = (edicion == null || tipo == null) ? 0.0 : ice.verDetalleTRegistro(edicion, tipo).getCosto();
        lblCostoTipo.setText(String.format("Costo tipo: UYU %.2f", costoTipo));

        double aporte = parseDouble(tfAporte.getText());
        int cant = (int) ((SpinnerNumberModel) spCantGratis.getModel()).getNumber();
        double costoGratis = costoTipo * cant;
        double veinte = aporte * 0.20;
        lblRegla20.setText(String.format("Regla 20%% → costoGratis=%.2f | 20%% aporte=%.2f", costoGratis, veinte));
        lblRegla20.setForeground(costoGratis > veinte ? new Color(0xB7, 0x1C, 0x1C) : new Color(0x33, 0x66, 0x33));
    }

    private void checkDuplicado() {
        String edicion = (String) cbEdicion.getSelectedItem();
        String inst = (String) cbInstitucion.getSelectedItem();
        if (edicion != null && inst != null && ice.obtenerPatrocinio(edicion, inst) != null) {
            JOptionPane.showMessageDialog(this,
                "Ya existe un patrocinio para la institución en esta edición.",
                "Patrocinio existente", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onAceptar() {
    	
      try {
        String edicion = (String) cbEdicion.getSelectedItem();
        String institucion = (String) cbInstitucion.getSelectedItem();
        String tipo = (String) cbTipo.getSelectedItem();
        NivelPatrocinio nivel = (NivelPatrocinio) cbNivel.getSelectedItem();
        double aporte = parseDouble(tfAporte.getText());
        int cant = (int) ((SpinnerNumberModel) spCantGratis.getModel()).getNumber();
        String codigo = tfCodigo.getText().trim();
     
        if (edicion == null || institucion == null || tipo == null || nivel == null||tfAporte.getText().trim().isEmpty()|| codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos obligatorios.", "Datos incompletos", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        
        
        
        
        if(aporte<=0) {
			JOptionPane.showMessageDialog(this, "El aporte económico debe ser un número positivo.", "Aporte inválido", JOptionPane.ERROR_MESSAGE);
			return;
		}
        if(cant<0 ) {
			JOptionPane.showMessageDialog(this, "La cantidad de registros gratis debe ser un número positivo o cero.", "Cantidad inválida", JOptionPane.ERROR_MESSAGE);
			return;
        }
        
    
        if (ice.obtenerPatrocinio(edicion, institucion) != null) {
            // Según el caso de uso: informar y permitir editar/cancelar (no continuar).
            JOptionPane.showMessageDialog(this,
                "Ya existe un patrocinio de esa institución para la edición seleccionada.\nEdita los datos o cancela.",
                "Patrocinio duplicado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double costoUnit = ice.verDetalleTRegistro(edicion, tipo).getCosto();
        double costoGratis = costoUnit * cant;
        double limite = aporte * 0.20;
        if (costoGratis > limite) {
            JOptionPane.showMessageDialog(this,
                String.format("El costo de los registros gratuitos (%.2f) supera el 20%% del aporte (%.2f).\nEdita los valores o cancela.", costoGratis, limite),
                "Regla 20% incumplida", JOptionPane.ERROR_MESSAGE);
            return;
        }
        ice.setFechaSistema(LocalDate.now());
        ice.altaPatrocinio(edicion, institucion, nivel, aporte, tipo, cant, codigo);
        JOptionPane.showMessageDialog(this, "Patrocinio registrado con éxito.", "OK", JOptionPane.INFORMATION_MESSAGE);
        clearForm();}
        catch (NumberFormatException nfe) {
      	  JOptionPane.showMessageDialog(this, "El aporte económico y la cantidad de registros gratuitos deben ser un número válido.", "Aporte inválido", JOptionPane.ERROR_MESSAGE);
        }}

    private void onCancelar() {
    	clearForm();
		setVisible(false);
    }

    private void clearForm() {
        tfAporte.setText("");
        tfCodigo.setText("");
        spCantGratis.setValue(0);
        if (cbEvento.getItemCount() > 0) cbEvento.setSelectedIndex(0);
        if (cbInstitucion.getItemCount() > 0) cbInstitucion.setSelectedIndex(0);
        if (cbNivel.getItemCount() > 0) cbNivel.setSelectedIndex(0);
        updateCostoYRegla();
    }

    private static double parseDouble(String s) {
        try { return Double.parseDouble(s.trim().replace(",", ".")); }
        catch (Exception e) { return 0.0; }
    }

    public void refrescar() {
        cargaData();
    }

    public static AltaPatrocinio getInstance(IControllerEvento ICE, IControllerUsuario ICU) {
		if (instance == null) {
			instance = new AltaPatrocinio(ICE, ICU);
		}
		return instance;
	
    
    
    
    
    
}}