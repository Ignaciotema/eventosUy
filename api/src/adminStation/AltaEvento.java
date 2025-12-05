package adminStation;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import excepciones.NombreEventoExcepcion;
import logica.controllers.IControllerEvento;


public class AltaEvento extends JInternalFrame{
	private static final long serialVersionUID = 1L;
	private static AltaEvento instance = null;
	private  IControllerEvento controllerEvento;
	private JTextField NombreEvento;
    private JTextField Sigla;
	private JTextField DescripcionEvento;
	private JTextField FechaEvento;
	private JLabel lblNombreEvento;
	private JLabel lblSigla;
	private JLabel lblDescripcionEvento;
	private JLabel lblFechaEvento;
	private JLabel lblCategorias;
	private JLabel lblCategoriasSelec;
	private JComboBox<String> cbxCategorias;
	private DefaultListModel<String> listModelCategorias;
	private JList<String> listCategorias;
	private JButton btnAceptar;
	private JButton btnCancelar;
	
	
	
	public AltaEvento(IControllerEvento ice) {
		controllerEvento = ice;
    	setTitle("Alta De Evento");
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setBounds(100, 100, 450, 300); 
       
        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(new EmptyBorder(8, 10, 10, 10));
        getContentPane().add(content, BorderLayout.CENTER);
        int y = 0;
        lblNombreEvento = new JLabel("Nombre:");
        content.add(lblNombreEvento, gbc(0, y, 1, 1, 0, 0, GridBagConstraints.NONE));
        NombreEvento = new JTextField(20);
        content.add(NombreEvento, gbc(1, y, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL));
        y++;
        lblSigla = new JLabel("Sigla:");
        content.add(lblSigla, gbc(0, y, 1, 1,0,0, GridBagConstraints.NONE));
        
  	    Sigla = new JTextField(20);
		content.add(Sigla, gbc(1, y, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL));
		y++;
		lblDescripcionEvento = new JLabel("Descripcion:");
		content.add(lblDescripcionEvento, gbc(0, y, 1, 1,0,0, GridBagConstraints.NONE));
		DescripcionEvento = new JTextField(20);
		content.add(DescripcionEvento, gbc(1, y, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL));
		y++;
		lblFechaEvento = new JLabel("Fecha(yyyy-mm-dd):");
		content.add(lblFechaEvento, gbc(0, y, 1, 1, 0,0, GridBagConstraints.NONE));
		FechaEvento = new JTextField(20);
		content.add(FechaEvento, gbc(1, y, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL));
		y++;
		
       lblCategorias = new JLabel("Categorias:");
       content.add(lblCategorias, gbc(0, y, 1, 1, 0,0, GridBagConstraints.NONE));
       cbxCategorias = new JComboBox<String>();
       cbxCategorias.addItem("Agregar categoría"); // Placeholder
       content.add(cbxCategorias, gbc(1, y, 2, 1, 1, 0, GridBagConstraints.HORIZONTAL));
	   y++;
	   Set<String> categorias = controllerEvento.listarCategorias();
	   for (String categoria : categorias) {
			cbxCategorias.addItem(categoria);
		}
		
		
		listModelCategorias = new DefaultListModel<>();
		lblCategoriasSelec = new JLabel("Seleccionadas:");
		content.add(lblCategoriasSelec, gbc(0, y, 1, 1 , 0,0, GridBagConstraints.NONE));
		listCategorias = new JList<>(listModelCategorias);
		content.add(listCategorias, gbc(1, y, 2, 1, 1, 1, GridBagConstraints.HORIZONTAL));
		y++;
		cbxCategorias.addActionListener(e -> {
			String categoriaSeleccionada = (String) cbxCategorias.getSelectedItem();
			if (categoriaSeleccionada != null &&
				!categoriaSeleccionada.equals("Agregar categoría") &&
				!listModelCategorias.contains(categoriaSeleccionada)) {
				listModelCategorias.addElement(categoriaSeleccionada);
			}
			// Siempre vuelve a mostrar el placeholder
			cbxCategorias.setSelectedIndex(0);
		});
		listCategorias.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = listCategorias.locationToIndex(e.getPoint());
					if (index != -1) {
						listModelCategorias.remove(index);
					}
				}
			}
		});
		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new GridLayout(1,2,10,0));
		GridBagConstraints gbcBotones = gbc(1, y, 3, 1, 1, 0, GridBagConstraints.CENTER);
		content.add(panelBotones, gbcBotones);
		btnAceptar = new JButton("Aceptar");
		panelBotones.add(btnAceptar);
		btnAceptar.addActionListener(a -> { 
            // Validación de campos vacíos
            if (NombreEvento.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El campo 'Nombre' está vacío", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (Sigla.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El campo 'Sigla' está vacío", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (DescripcionEvento.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El campo 'Descripción' está vacío", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (FechaEvento.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El campo 'Fecha' está vacío", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (listModelCategorias.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar al menos una categoría", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Validación de formato de fecha
            try {
                LocalDate date = LocalDate.parse(FechaEvento.getText());
            } catch (Exception DateTimeParseException) {
                JOptionPane.showMessageDialog(this, "formato de fecha invalido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Set<String> nomscats= new HashSet<>();
            for (int i = 0; i < listModelCategorias.size(); i++) {
                String nombreCategoria = listModelCategorias.getElementAt(i);
                nomscats.add(nombreCategoria);
            }
            try {
                controllerEvento.altaEvento(NombreEvento.getText(), Sigla.getText(),LocalDate.parse(FechaEvento.getText()), DescripcionEvento.getText() , nomscats,"");
                JOptionPane.showMessageDialog(this, "El evento se ha registrado con éxito", "Alta de Evento",JOptionPane.INFORMATION_MESSAGE);
				NombreEvento.setText("");
				Sigla.setText("");
				DescripcionEvento.setText("");
				FechaEvento.setText("");
				listModelCategorias.clear();
            
            
            } catch (NombreEventoExcepcion e) {
                JOptionPane.showMessageDialog(this, "ya existe un evento con el nombre ingresado", "Error", JOptionPane.ERROR_MESSAGE);
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            
		});
		btnCancelar = new JButton("Cancelar");
		panelBotones.add(btnCancelar);
		btnCancelar.addActionListener(a -> {
	
			NombreEvento.setText("");
			Sigla.setText("");
			DescripcionEvento.setText("");
			FechaEvento.setText("");
			listModelCategorias.clear();
			setVisible(false);
		
		});
	}
	
	public static AltaEvento getInstance(IControllerEvento I) {
		if (instance == null) {
			instance = new AltaEvento(I);
		}
		return instance;
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
	
	public void resfrescar() {
		//cbxCategorias.removeAllItems();
		Set<String> categorias = controllerEvento.listarCategorias();
		   for (String categoria : categorias) {
				cbxCategorias.addItem(categoria);
			}
	}
	
	
	
}