package adminStation;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import excepciones.UsuarioNoEncontrado;
import logica.models.Factory;
import logica.models.Usuario;
import logica.controllers.IControllerEvento;
import logica.controllers.IControllerUsuario;
import logica.data_types.DTRegistro;
import logica.data_types.DataUsuario;
import logica.data_types.DataUsuario.TipoUsuario;

import javax.swing.AbstractListModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;



//TODO: Implementar llamado a ventana de consulta de edicion al hacer doble click en una edicion
//TODO: Implementar llamado a ventana de consulta de registro al hacer doble click en un registro
public class ConsultaRegistro extends JInternalFrame {
	private static ConsultaRegistro instance = null;
	private IControllerUsuario controllerUsr;
	private IControllerEvento controllerEv = Factory.getInstance().getControllerEvento();
	private JPanel panelDetallesUsr;
	private JList<String> listUsuarios;
	private JList<String> listRegistros;
	private JTextField txtNombre;
	private JTextField txtEmail;
	private JLabel lblAsociaciones;
	private JScrollPane scrollPane_1;
	private JLabel lblFecha;
	private JTextField Fecha;
	private JLabel lblCosto;
	private JTextField Costo;
	
	@SuppressWarnings({ "serial", "unchecked" })
	public ConsultaRegistro(IControllerUsuario ICU, IControllerEvento controllerEvento) {
		controllerEv = controllerEvento;
		controllerUsr = ICU;
	
		setTitle("Consulta de Registro");
		setClosable(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setTitle("Consulta de Registro");
		setClosable(true);
		setIconifiable(true);
		setMaximizable(true);
		
		
		panelDetallesUsr = new JPanel();
		panelDetallesUsr.setBounds(133, 11, 291, 181);
		getContentPane().add(panelDetallesUsr);
		panelDetallesUsr.setVisible(true);
		GridBagLayout gbl_panelDetallesUsr = new GridBagLayout();
		gbl_panelDetallesUsr.columnWidths = new int[]{92, 138, 0};
		gbl_panelDetallesUsr.rowHeights = new int[]{14, 0, 20, 162, 0};
		gbl_panelDetallesUsr.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panelDetallesUsr.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panelDetallesUsr.setLayout(gbl_panelDetallesUsr);
		
		JPanel panelRegistro = new JPanel();
		panelRegistro.setBounds(133, 202, 291, 57);
		getContentPane().add(panelRegistro);
		panelDetallesUsr.setVisible(true);
		GridBagLayout gbl_panelRegistro = new GridBagLayout();
		gbl_panelRegistro.columnWidths = new int[]{92, 138, 0};
		gbl_panelRegistro.rowHeights = new int[]{0, 20, 0};
		gbl_panelRegistro.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panelRegistro.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panelRegistro.setLayout(gbl_panelRegistro);
		
		JLabel lblDetalles = new JLabel("Detalles del Usuario");
		GridBagConstraints gbc_lblDetalles = new GridBagConstraints();
		gbc_lblDetalles.anchor = GridBagConstraints.NORTH;
		gbc_lblDetalles.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblDetalles.insets = new Insets(0, 0, 5, 0);
		gbc_lblDetalles.gridwidth = 2;
		gbc_lblDetalles.gridx = 0;
		gbc_lblDetalles.gridy = 0;
		panelDetallesUsr.add(lblDetalles, gbc_lblDetalles);
		lblDetalles.setVisible(true);
		
		JLabel lblNombre = new JLabel("Nombre: ");
		GridBagConstraints gbc_lblNombre = new GridBagConstraints();
		gbc_lblNombre.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNombre.insets = new Insets(0, 0, 5, 5);
		gbc_lblNombre.gridx = 0;
		gbc_lblNombre.gridy = 1;
		panelDetallesUsr.add(lblNombre, gbc_lblNombre);
		lblNombre.setVisible(true);
		
		txtNombre = new JTextField();
		GridBagConstraints gbc_txtNombre = new GridBagConstraints();
		gbc_txtNombre.anchor = GridBagConstraints.NORTH;
		gbc_txtNombre.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNombre.insets = new Insets(0, 0, 5, 0);
		gbc_txtNombre.gridx = 1;
		gbc_txtNombre.gridy = 1;
		panelDetallesUsr.add(txtNombre, gbc_txtNombre);
		txtNombre.setColumns(10);
		txtNombre.setVisible(true);
		txtNombre.setEditable(false);
		
		JLabel lblEmail = new JLabel("Email: ");
		GridBagConstraints gbc_lblEmail = new GridBagConstraints();
		gbc_lblEmail.anchor = GridBagConstraints.WEST;
		gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmail.gridx = 0;
		gbc_lblEmail.gridy = 2;
		panelDetallesUsr.add(lblEmail, gbc_lblEmail);
		lblEmail.setVisible(true);
		
		txtEmail = new JTextField();
		GridBagConstraints gbc_txtEmail = new GridBagConstraints();
		gbc_txtEmail.anchor = GridBagConstraints.NORTH;
		gbc_txtEmail.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEmail.insets = new Insets(0, 0, 5, 0);
		gbc_txtEmail.gridx = 1;
		gbc_txtEmail.gridy = 2;
		panelDetallesUsr.add(txtEmail, gbc_txtEmail);
		txtEmail.setColumns(10);
		txtEmail.setVisible(true);
		txtEmail.setEditable(false);
		
		JLabel lblFecha = new JLabel("Fecha");
		GridBagConstraints gbc_lblFecha = new GridBagConstraints();
		gbc_lblFecha.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblFecha.insets = new Insets(0, 0, 5, 5);
		gbc_lblFecha.gridx = 0;
		gbc_lblFecha.gridy = 0;
		panelRegistro.add(lblFecha, gbc_lblFecha);
		lblFecha.setVisible(true);
		
		Fecha = new JTextField();
		GridBagConstraints gbc_txtFecha = new GridBagConstraints();
		gbc_txtFecha.anchor = GridBagConstraints.NORTH;
		gbc_txtFecha.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFecha.insets = new Insets(0, 0, 5, 0);
		gbc_txtFecha.gridx = 1;
		gbc_txtFecha.gridy = 0;
		panelRegistro.add(Fecha, gbc_txtFecha);
		Fecha.setColumns(10);
		Fecha.setVisible(true);
		Fecha.setEditable(false);
		
		JLabel lblCosto = new JLabel("Costo");
		GridBagConstraints gbc_lblCosto = new GridBagConstraints();
		gbc_lblCosto.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblCosto.insets = new Insets(0, 0, 0, 5);
		gbc_lblCosto.gridx = 0;
		gbc_lblCosto.gridy = 1;
		panelRegistro.add(lblCosto, gbc_lblCosto);
		lblCosto.setVisible(true);
		
		Costo = new JTextField();
		GridBagConstraints gbc_txtCosto = new GridBagConstraints();
		gbc_txtCosto.anchor = GridBagConstraints.NORTH;
		gbc_txtCosto.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCosto.gridx = 1;
		gbc_txtCosto.gridy = 1;
		panelRegistro.add(Costo, gbc_txtCosto);
		Costo.setColumns(10);
		Costo.setVisible(true);
		Costo.setEditable(false);
		
		lblAsociaciones = new JLabel("Ediciones asociadas:");
		GridBagConstraints gbc_lblAsociaciones = new GridBagConstraints();
		gbc_lblAsociaciones.anchor = GridBagConstraints.NORTH;
		gbc_lblAsociaciones.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblAsociaciones.insets = new Insets(0, 0, 0, 5);
		gbc_lblAsociaciones.gridx = 0;
		gbc_lblAsociaciones.gridy = 3;
		panelDetallesUsr.add(lblAsociaciones, gbc_lblAsociaciones);
		
		scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 1;
		gbc_scrollPane_1.gridy = 3;
		panelDetallesUsr.add(scrollPane_1, gbc_scrollPane_1);
		
		listRegistros = new JList<String>();
		scrollPane_1.setViewportView(listRegistros);
	
		
		//---implementación---
		
		listRegistros.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					String selected = listRegistros.getSelectedValue();
					if (selected == null) {
						return;
					}
					/*if (lblAsociaciones.getText().equals("Ediciones organizadas:")) {
						ConsultaEdicionDeEvento ce = ConsultaEdicionDeEvento.getInstance(Factory.getInstance().getControllerEvento());
						ce.setVisible(true);
						ce.toFront();
						ce.invocacionDesdeConsultaUsuario(listRegistros.getSelectedValue());
					} else {

					}*/
					
					detallesRegistro(selected);
				}
			}
		});
		
		
		
		listUsuarios = new JList<String>();
		JScrollPane scrollPane = new JScrollPane(listUsuarios);
		scrollPane.setBounds(10, 11, 113, 248);
		getContentPane().add(scrollPane);
		listUsuarios.setVisible(true);
		listUsuarios.setEnabled(true);
		listUsuarios.addListSelectionListener(e -> {
			Fecha.setText("");
			Costo.setText("");
			try {
				detallesUsuario();
			} catch (UsuarioNoEncontrado e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		listUsuarios.setListData(controllerUsr.listarAsistentes().toArray(new String[0]));
		listUsuarios.setModel(new AbstractListModel() {
			String[] values = new String[] {};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		listUsuarios.setSelectedIndex(0);
		
	}
	
	public void refrescar() {
		listUsuarios.setListData(controllerUsr.listarAsistentes().toArray(new String[0]));
		listUsuarios.setSelectedIndex(0);
		Fecha.setText("");
		Costo.setText("");
		txtNombre.setText("");
		txtEmail.setText("");
		listRegistros.setListData(new String[] {});

	}

	private void detallesUsuario() throws UsuarioNoEncontrado {
		String selected = listUsuarios.getSelectedValue();
		if (selected == null) {
			return;
		}
		
		System.out.println("Selected user: " + selected);
		panelDetallesUsr.setVisible(true);
		DataUsuario dataUser = controllerUsr.infoUsuario(selected);
		txtNombre.setText(dataUser.getNombre());
		txtEmail.setText(dataUser.getEmail());
		
		lblAsociaciones.setText("Registros a ediciones:");
		System.out.println(controllerUsr.listarRegistrosAEventos(selected).toArray(new String[0]).length);
		listRegistros.setListData(controllerUsr.listarRegistrosAEventos(selected).toArray(new String[0]));
		
	}
	
	//TODO: checkear como convertirlo a lista
	private void detallesRegistro(String edicion) {
		
		
		String usuario = listUsuarios.getSelectedValue();
		if (edicion == null) {
			
			return;
		}
		//panelRegistro.setVisible(true);
		DTRegistro dataReg = controllerEv.infoRegistro(edicion, usuario);
		Fecha.setText((dataReg.getFechaRegistro()).toString());
		
		Costo.setText(String.valueOf(dataReg.getCosto()));
		
		//lblAsociaciones.setText("Registros a ediciones:");
		//listRegistros.setListData(controllerUsr.listarRegistrosAEventos(registro).toArray(new String[0]));
		
	}
	
	//TODO: Implementar esto:
	public void invocacionDesdeConsultaUsuario(DataUsuario user, String edicion) throws UsuarioNoEncontrado {
		refrescar();
		listUsuarios.setSelectedValue(user.getNickname(), true);
		listRegistros.setListData(controllerUsr.listarRegistrosAEventos(user.getNickname()).toArray(new String[0]));
		listRegistros.setSelectedValue(edicion, true);
		detallesUsuario();
		detallesRegistro(edicion);
		//cbxListadoDeEdiciones.setSelectedItem(edicion);
		//detallesRegistro(edicion);
		//actualizarTablasParaEdicion(edicion);
    }

	public static ConsultaRegistro getInstance(IControllerEvento controllerEvento, IControllerUsuario controllerUsuario) {
		if (instance == null) {
			instance = new ConsultaRegistro(controllerUsuario, controllerEvento);
		}
		return instance;
	}
	
	
}