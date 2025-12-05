package adminStation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextField;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import excepciones.NombreInstiExistente;
import logica.controllers.IControllerUsuario;

public class AltaInstitucion extends JInternalFrame{
	private static AltaInstitucion instance = null;
	private TextField txtNombre;
	private TextField txtDescripcion;
	private TextField txtweb;
	private JLabel lblNombre;
	private JLabel lblDescripcion;
	private JLabel lblweb;
	private JButton btnAceptar;
	private JButton btnCancelar;
	private IControllerUsuario ICU;
	
	
	public AltaInstitucion(IControllerUsuario icu) {
		ICU = icu;
		setTitle("Alta Institución");
		setClosable(true);
		setBounds(100, 100, 450, 300);
		setResizable(true);
		setIconifiable(true);
		setMaximizable(true);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(new EmptyBorder(12, 12, 12, 12));
        setContentPane(content);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        int y = 0;
        
        lblNombre = new JLabel("Nombre:");
        content.add(lblNombre, gbc(0, y, 1, 1, 0.0, 0.0, GridBagConstraints.NONE));
        txtNombre = new TextField();
        content.add(txtNombre, gbc(1, y++, 2, 1, 1.0, 0.0, GridBagConstraints.HORIZONTAL));
        lblDescripcion = new JLabel("Descripción:");
        content.add(lblDescripcion, gbc(0, y, 1, 1, 0.0, 0.0, GridBagConstraints.NONE));
        txtDescripcion = new TextField();
        content.add(txtDescripcion, gbc(1, y++, 2, 1, 1.0, 0.0, GridBagConstraints.HORIZONTAL));
        lblweb = new JLabel("Web:");
        content.add(lblweb, gbc(0, y, 1, 1, 0.0, 0.0, GridBagConstraints.NONE));
        txtweb = new TextField();
        content.add(txtweb, gbc(1, y++, 2, 1, 1.0, 0.0, GridBagConstraints.HORIZONTAL));
        
        btnAceptar = new JButton("Aceptar");
        content.add(btnAceptar, gbc(1, y, 1, 1, 0.0, 0.0, GridBagConstraints.NONE));
        btnCancelar = new JButton("Cancelar");
        content.add(btnCancelar, gbc(2, y, 1, 1, 0.0, 0.0, GridBagConstraints.NONE));
        
        btnAceptar.addActionListener(e -> {
			try {
				String nombre = txtNombre.getText().trim();
				String descripcion = txtDescripcion.getText().trim();
				String web = txtweb.getText().trim();
				
				if(nombre.isEmpty()){
					JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				if(descripcion.isEmpty()) {
					JOptionPane.showMessageDialog(this, "La descripción no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				if(web.isEmpty()) {
					JOptionPane.showMessageDialog(this, "La web no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				
				ICU.altaInstitucion(nombre, descripcion, web);
				JOptionPane.showMessageDialog(this, "Institución registrada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
				limpiarCampos();
			}
			catch (NombreInstiExistente ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
			catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error al registrar la institución: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
        
        btnCancelar.addActionListener(e -> {
			limpiarCampos();
			setVisible(false);
		});
        
        
       
        
        
        
        
	}
	
	public static AltaInstitucion getInstance(IControllerUsuario icu) {
		if (instance == null) {
			instance = new AltaInstitucion(icu);
		}
		return instance;
	}
	
	private void limpiarCampos() {
		txtNombre.setText("");
		txtDescripcion.setText("");
		txtweb.setText("");
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

}