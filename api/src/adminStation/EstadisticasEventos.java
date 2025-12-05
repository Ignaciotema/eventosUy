package adminStation;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import logica.controllers.IControllerEvento;

public class EstadisticasEventos extends JInternalFrame {
    
    private static final long serialVersionUID = 1L;
    private static EstadisticasEventos instancia;
    private JTable tablaEventos;
    private DefaultTableModel modeloTabla;
    private JLabel lblTotalVisitas;
    private JLabel lblTotalEventos;
    private IControllerEvento controladorEvento;
    
    public static EstadisticasEventos obtenerInstancia(IControllerEvento controladorEvento) {
        if (instancia == null) {
            instancia = new EstadisticasEventos(controladorEvento);
        }
        return instancia;
    }
    
    private EstadisticasEventos(IControllerEvento controladorEvento) {
        this.controladorEvento = controladorEvento;
        setTitle("Estadísticas de Eventos Más Visitados");
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setClosable(true);
        setBounds(10, 10, 600, 400);
        
        inicializarComponentes();
       
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        
        // Panel superior con estadísticas generales
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Resumen General"));
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.X_AXIS));
        
        lblTotalVisitas = new JLabel("Total de Visitas: 0");
        lblTotalVisitas.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalEventos = new JLabel("Eventos con Visitas: 0");
        lblTotalEventos.setFont(new Font("Arial", Font.BOLD, 14));
        
        panelSuperior.add(Box.createHorizontalGlue());
        panelSuperior.add(lblTotalVisitas);
        panelSuperior.add(Box.createHorizontalStrut(30));
        panelSuperior.add(lblTotalEventos);
        panelSuperior.add(Box.createHorizontalGlue());
        
        add(panelSuperior, BorderLayout.NORTH);
        
        // Tabla de eventos más visitados
        String[] columnas = {"#", "Nombre del Evento", "Cantidad de Visitas"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaEventos = new JTable(modeloTabla);
        tablaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEventos.getTableHeader().setReorderingAllowed(false);
        
        // Configurar renderizado de celdas
        DefaultTableCellRenderer renderizadorCentrado = new DefaultTableCellRenderer();
        renderizadorCentrado.setHorizontalAlignment(JLabel.CENTER);
        tablaEventos.getColumnModel().getColumn(0).setCellRenderer(renderizadorCentrado);
        tablaEventos.getColumnModel().getColumn(2).setCellRenderer(renderizadorCentrado);
        
        // Ajustar anchos de columnas
        tablaEventos.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaEventos.getColumnModel().getColumn(1).setPreferredWidth(300);
        tablaEventos.getColumnModel().getColumn(2).setPreferredWidth(150);
        
        JScrollPane scrollPane = new JScrollPane(tablaEventos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Top 5 Eventos Más Visitados"));
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel inferior con botones
        JPanel panelInferior = new JPanel();
        JButton btnActualizar = new JButton("Actualizar Datos");
        btnActualizar.addActionListener(e -> cargarDatos());
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> setVisible(false));
        
        panelInferior.add(btnActualizar);
        panelInferior.add(Box.createHorizontalStrut(10));
        panelInferior.add(btnCerrar);
        
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    public void cargarDatos() {
        try {
            // Obtener estadísticas usando el controlador
            List<Map<String, Object>> top5 = controladorEvento.obtenerTop5EventosMasVisitados();
            Map<String, Long> todasLasVisitas = controladorEvento.obtenerEstadisticasVisitas();
            
            if (top5.isEmpty()) {
                mostrarMensajeSinDatos();
                return;
            }
            
            // Limpiar tabla
            modeloTabla.setRowCount(0);
            
            // Llenar tabla
            int ranking = 1;
            for (Map<String, Object> evento : top5) {
                Object[] fila = {
                    ranking,
                    evento.get("nombre"),
                    evento.get("visitas")
                };
                modeloTabla.addRow(fila);
                ranking++;
            }
            
            // Actualizar estadísticas generales - SIN STREAMS
            long totalVisitas = 0;
            for (Long visitas : todasLasVisitas.values()) {
                totalVisitas += visitas;
            }
            int totalEventos = todasLasVisitas.size();
            
            lblTotalVisitas.setText("Total de Visitas: " + totalVisitas);
            lblTotalEventos.setText("Eventos con Visitas: " + totalEventos);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar estadísticas: " + e.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mostrarMensajeSinDatos() {
        modeloTabla.setRowCount(0);
        lblTotalVisitas.setText("Total de Visitas: 0");
        lblTotalEventos.setText("Eventos con Visitas: 0");
        
        JOptionPane.showMessageDialog(this,
            "Aún no hay registros de visitas a eventos.\n" +
            "Las estadísticas aparecerán cuando los usuarios visiten los detalles de eventos.",
            "Sin Datos",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void refrescar() {
        cargarDatos();
    }
}