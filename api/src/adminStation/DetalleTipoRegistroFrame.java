package adminStation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class DetalleTipoRegistroFrame extends JInternalFrame {
    private static final long serialVersionUID = 1L;

    private final String evento;
    private final String edicion;
    private final logica.data_types.DTTipoRegistro dto;

    public DetalleTipoRegistroFrame(String evento, String edicion, logica.data_types.DTTipoRegistro dto) {
        super("Detalle de Tipo de Registro", true, true, true, true);
        this.evento = evento;
        this.edicion = edicion;
        this.dto = dto;

        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(520, 280);
        setIconifiable(true);
		setMaximizable(true);
        setLocation(40, 40);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(content);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;

        int y = 0;

        // Encabezado
        JLabel lblHeader = new JLabel(
                "<html><b>Evento:</b> " + safe(evento) + " &nbsp;&nbsp;|&nbsp;&nbsp; <b>Edición:</b> " + safe(edicion) + "</html>"
        );
        c.gridx = 0; c.gridy = y++; c.gridwidth = 2;
        content.add(lblHeader, c);

        c.gridwidth = 1;

        // Nombre
        content.add(new JLabel("Nombre del tipo:"), gbc(0, y, 1));
        content.add(new JTextField(safe(dto.getNombre())) {{
            setEditable(false);
        }}, gbc(1, y++, 1));

        // Descripción
        content.add(new JLabel("Descripción:"), gbc(0, y, 1));
        JTextArea taDesc = new JTextArea(safe(dto.getDescripcion()), 3, 30);
        taDesc.setLineWrap(true);
        taDesc.setWrapStyleWord(true);
        taDesc.setEditable(false);
        JScrollPane spDesc = new JScrollPane(taDesc);
        c = gbc(1, y++, 1);
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        content.add(spDesc, c);

        // Costo
        c = gbc(0, y, 1);
        c.fill = GridBagConstraints.HORIZONTAL; c.weighty = 0;
        content.add(new JLabel("Costo:"), c);
        content.add(new JTextField(formatMoney(dto.getCosto())) {{
            setEditable(false);
        }}, gbc(1, y++, 1));

        // Cupo
        content.add(new JLabel("Cupo:"), gbc(0, y, 1));
        content.add(new JTextField(String.valueOf(dto.getCupo())) {{
            setEditable(false);
        }}, gbc(1, y++, 1));

        // Botón cerrar
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> setVisible(false));
        buttons.add(btnCerrar);

        c = gbc(0, y, 2);
        c.fill = GridBagConstraints.NONE;
        content.add(buttons, c);
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
}
