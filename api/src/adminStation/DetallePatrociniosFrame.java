package adminStation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class DetallePatrociniosFrame extends JInternalFrame {
    private static final long serialVersionUID = 1L;

    private final String evento;
    private final String edicion;
    private final String nivel; // String elegido en el combo (p.ej. "ORO")
    private final List<logica.data_types.DTPatrocinio> patrocinios;

    private JTable table;
    private JLabel lblResumen;

    public DetallePatrociniosFrame(String evento, String edicion, String nivel,
                                   List<logica.data_types.DTPatrocinio> patrociniosDelNivel) {
        super("Patrocinios - Nivel " + nivel, true, true, true, true);
        this.evento = evento;
        this.edicion = edicion;
        this.nivel = nivel;
        this.patrocinios = patrociniosDelNivel;

        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(640, 360);
        setLocation(60, 60);
        setIconifiable(true);
		setMaximizable(true);

        JPanel content = new JPanel(new BorderLayout(8, 8));
        content.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(content);

        // Encabezado
        JLabel lblHeader = new JLabel(
                "<html><b>Evento:</b> " + safe(evento) +
                " &nbsp;&nbsp;|&nbsp;&nbsp; <b>Edición:</b> " + safe(edicion) +
                " &nbsp;&nbsp;|&nbsp;&nbsp; <b>Nivel:</b> " + safe(nivel) + "</html>"
        );
        content.add(lblHeader, BorderLayout.NORTH);

        // Tabla
        table = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Fecha", "Monto", "Código", "Nivel"}
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        });
        configurarTabla(table);
        JScrollPane sp = new JScrollPane(table);
        content.add(sp, BorderLayout.CENTER);

        // Pie con resumen + botón cerrar
        JPanel south = new JPanel(new BorderLayout());
        lblResumen = new JLabel(" ");
        south.add(lblResumen, BorderLayout.WEST);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> setVisible(false));
        buttons.add(btnCerrar);
        south.add(buttons, BorderLayout.EAST);

        content.add(south, BorderLayout.SOUTH);

        // Cargar datos
        cargarTabla();
    }

    private void configurarTabla(JTable t) {
        t.setFillsViewportHeight(true);
        t.setRowHeight(22);
        t.setAutoCreateRowSorter(true);
        t.getTableHeader().setReorderingAllowed(false);
        t.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setPreferredScrollableViewportSize(new Dimension(600, 200));
    }

    private void cargarTabla() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("es", "UY"));

        double total = 0.0;
        int count = 0;

        if (patrocinios != null) {
            for (logica.data_types.DTPatrocinio p : patrocinios) {
                String fecha = p.getFecha() != null ? p.getFecha().format(df) : "";
                String monto = nf.format(p.getMonto());
                String codigo = safe(p.getCodigo());
                String nivelTxt = (p.getNivelPatrocinio() != null) ? p.getNivelPatrocinio().name() : "";

                model.addRow(new Object[]{fecha, monto, codigo, nivelTxt});

                total += p.getMonto();
                count++;
            }
        }

        lblResumen.setText("Registros: " + count + "   |   Total: " + nf.format(total));
    }

    private static String safe(Object o) { return o == null ? "" : String.valueOf(o); }
}
