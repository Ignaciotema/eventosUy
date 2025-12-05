package adminStation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Set;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import excepciones.UsuarioNoEncontrado;
import logica.models.Factory;
import logica.models.Usuario;
import logica.controllers.IControllerUsuario;
import logica.data_types.DataUsuario;
import logica.data_types.DataUsuario.TipoUsuario;



//TODO: Implementar llamado a ventana de consulta de edicion al hacer doble click en una edicion
//TODO: Implementar llamado a ventana de consulta de registro al hacer doble click en un registro
public class ConsultaUsuario extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	
	private static ConsultaUsuario instance = null;
	private IControllerUsuario controllerUsr;
	private JPanel panelDetallesUsr;
	private JList<String> listUsuarios;
	private JList<String> listAsociaciones;
	private JTextField txtNombre;
	private JTextField txtEmail;
	private JLabel lblAsociaciones;
	private JScrollPane scrollPane_1;
	private JLabel lblApellido;
	private JTextField txtApellido;
	private JLabel lblFechaNac;
	private JTextField txtFechaNac;
	private JLabel lblDescripcion;
	private JTextArea txtDescripcion;
	private JScrollPane scrollDescripcion;
	private JLabel lblWeb;
	private JTextField txtWeb;
	
	@SuppressWarnings({ "serial", "unchecked" })
	public ConsultaUsuario(IControllerUsuario ICU) {
		controllerUsr = ICU;
	
		setTitle("Consulta de Usuario");
		setClosable(true);
		setBounds(100, 100, 448, 386);
		setMaximizable(true);
		getContentPane().setLayout(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setTitle("Consulta de Usuario");
		setClosable(true);
		setIconifiable(true);
		setMaximizable(true);
		
		
		panelDetallesUsr = new JPanel();
		panelDetallesUsr.setBounds(133, 11, 291, 339);
		getContentPane().add(panelDetallesUsr);
		panelDetallesUsr.setVisible(true);
		GridBagLayout gbl_panelDetallesUsr = new GridBagLayout();
		gbl_panelDetallesUsr.columnWidths = new int[]{92, 138, 0};
		gbl_panelDetallesUsr.rowHeights = new int[]{14, 0, 20, 162, 0, 0};
		gbl_panelDetallesUsr.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_panelDetallesUsr.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		panelDetallesUsr.setLayout(gbl_panelDetallesUsr);
		
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
		
		JLabel lblEmail = new JLabel("Email: ");
		GridBagConstraints gbc_lblEmail = new GridBagConstraints();
		gbc_lblEmail.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmail.gridx = 0;
		gbc_lblEmail.gridy = 2;
		panelDetallesUsr.add(lblEmail, gbc_lblEmail);
		lblEmail.setVisible(true);
		
		lblAsociaciones = new JLabel("Ediciones asociadas:");
		GridBagConstraints gbc_lblAsociaciones = new GridBagConstraints();
		gbc_lblAsociaciones.anchor = GridBagConstraints.NORTH;
		gbc_lblAsociaciones.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblAsociaciones.insets = new Insets(0, 0, 5, 5);
		gbc_lblAsociaciones.gridx = 0;
		gbc_lblAsociaciones.gridy = 3;
		panelDetallesUsr.add(lblAsociaciones, gbc_lblAsociaciones);
		
		scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 1;
		gbc_scrollPane_1.gridy = 3;
		panelDetallesUsr.add(scrollPane_1, gbc_scrollPane_1);
		
		listAsociaciones = new JList<String>();
		scrollPane_1.setViewportView(listAsociaciones);
		listAsociaciones.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					String selected = listAsociaciones.getSelectedValue();
					if (selected == null) {
						return;
					}
					if (lblAsociaciones.getText().equals("Ediciones organizadas:")) {
						ConsultaEdicionDeEvento ce = ConsultaEdicionDeEvento.getInstance(Factory.getInstance().getControllerEvento());
						ce.invocacionDesdeConsultaUsuario(listAsociaciones.getSelectedValue());
						ce.setVisible(true);
						ce.toFront();
					} else {
						ConsultaRegistro cr = ConsultaRegistro.getInstance(Factory.getInstance().getControllerEvento(), Factory.getInstance().getControllerUsuario());
					
						String usuario = listUsuarios.getSelectedValue();
						try {
							DataUsuario dataUser = controllerUsr.infoUsuario(usuario);
							cr.invocacionDesdeConsultaUsuario(dataUser, listAsociaciones.getSelectedValue());
						} catch (UsuarioNoEncontrado e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cr.setVisible(true);
						cr.toFront();
					}
				}
			}
		});
		
		listUsuarios = new JList<String>();
		DefaultListModel<String> model = new DefaultListModel<String>();
		listUsuarios.setModel(model);
		
		JScrollPane scrollPane = new JScrollPane(listUsuarios);
		scrollPane.setBounds(10, 11, 113, 248);
		Set<String> usuarios = ICU.listarUsuarios();
		for (String nomUsuario : usuarios) {
			model.addElement(nomUsuario);
		}
		
		getContentPane().add(scrollPane);
		listUsuarios.setVisible(true);
		listUsuarios.setEnabled(true);
		listUsuarios.addListSelectionListener(e -> {
			try {
				detallesUsuario();
			} catch (UsuarioNoEncontrado e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		listUsuarios.setListData(controllerUsr.listarUsuarios().toArray(new String[0]));
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
		
		// Apellido
		lblApellido = new JLabel("Apellido: ");
		GridBagConstraints gbc_lblApellido = new GridBagConstraints();
		gbc_lblApellido.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblApellido.insets = new Insets(0, 0, 5, 5);
		gbc_lblApellido.gridx = 0;
		gbc_lblApellido.gridy = 4;
		panelDetallesUsr.add(lblApellido, gbc_lblApellido);
		lblApellido.setVisible(false);

		txtApellido = new JTextField();
		GridBagConstraints gbc_txtApellido = new GridBagConstraints();
		gbc_txtApellido.anchor = GridBagConstraints.NORTH;
		gbc_txtApellido.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtApellido.insets = new Insets(0, 0, 5, 0);
		gbc_txtApellido.gridx = 1;
		gbc_txtApellido.gridy = 4;
		panelDetallesUsr.add(txtApellido, gbc_txtApellido);
		txtApellido.setColumns(10);
		txtApellido.setVisible(false);
		txtApellido.setEditable(false);

		// Fecha de nacimiento
		lblFechaNac = new JLabel("Fecha de nacimiento: ");
		GridBagConstraints gbc_lblFechaNac = new GridBagConstraints();
		gbc_lblFechaNac.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblFechaNac.insets = new Insets(0, 0, 5, 5);
		gbc_lblFechaNac.gridx = 0;
		gbc_lblFechaNac.gridy = 5;
		panelDetallesUsr.add(lblFechaNac, gbc_lblFechaNac);
		lblFechaNac.setVisible(false);

		txtFechaNac = new JTextField();
		GridBagConstraints gbc_txtFechaNac = new GridBagConstraints();
		gbc_txtFechaNac.anchor = GridBagConstraints.NORTH;
		gbc_txtFechaNac.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFechaNac.insets = new Insets(0, 0, 5, 0);
		gbc_txtFechaNac.gridx = 1;
		gbc_txtFechaNac.gridy = 5;
		panelDetallesUsr.add(txtFechaNac, gbc_txtFechaNac);
		txtFechaNac.setColumns(10);
		txtFechaNac.setVisible(false);
		txtFechaNac.setEditable(false);

		// Descripcion
		lblDescripcion = new JLabel("Descripción: ");
		GridBagConstraints gbc_lblDescripcion = new GridBagConstraints();
		gbc_lblDescripcion.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblDescripcion.insets = new Insets(0, 0, 5, 5);
		gbc_lblDescripcion.gridx = 0;
		gbc_lblDescripcion.gridy = 4;
		panelDetallesUsr.add(lblDescripcion, gbc_lblDescripcion);
		lblDescripcion.setVisible(false);

		txtDescripcion = new JTextArea();
		txtDescripcion.setLineWrap(true);
		txtDescripcion.setWrapStyleWord(true);
		txtDescripcion.setRows(3);
		txtDescripcion.setEditable(false);
		scrollDescripcion = new JScrollPane(txtDescripcion);
		GridBagConstraints gbc_scrollDescripcion = new GridBagConstraints();
		gbc_scrollDescripcion.anchor = GridBagConstraints.NORTH;
		gbc_scrollDescripcion.fill = GridBagConstraints.BOTH;
		gbc_scrollDescripcion.insets = new Insets(0, 0, 5, 0);
		gbc_scrollDescripcion.gridx = 1;
		gbc_scrollDescripcion.gridy = 4;
		panelDetallesUsr.add(scrollDescripcion, gbc_scrollDescripcion);
		scrollDescripcion.setVisible(false);

		// Web
		lblWeb = new JLabel("Web: ");
		GridBagConstraints gbc_lblWeb = new GridBagConstraints();
		gbc_lblWeb.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblWeb.insets = new Insets(0, 0, 5, 5);
		gbc_lblWeb.gridx = 0;
		gbc_lblWeb.gridy = 5;
		panelDetallesUsr.add(lblWeb, gbc_lblWeb);
		lblWeb.setVisible(false);

		txtWeb = new JTextField();
		GridBagConstraints gbc_txtWeb = new GridBagConstraints();
		gbc_txtWeb.anchor = GridBagConstraints.NORTH;
		gbc_txtWeb.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtWeb.insets = new Insets(0, 0, 5, 0);
		gbc_txtWeb.gridx = 1;
		gbc_txtWeb.gridy = 5;
		panelDetallesUsr.add(txtWeb, gbc_txtWeb);
		txtWeb.setColumns(10);
		txtWeb.setVisible(false);
		txtWeb.setEditable(false);
	}
	
	public void refrescar() {
		listUsuarios.setListData(controllerUsr.listarUsuarios().toArray(new String[0]));
	}

	private void detallesUsuario() throws UsuarioNoEncontrado {
		String selected = listUsuarios.getSelectedValue();
		if (selected == null) {
			return;
		}
		panelDetallesUsr.setVisible(true);
		DataUsuario dataUser = controllerUsr.infoUsuario(selected);
		txtNombre.setText(dataUser.getNombre());
		txtEmail.setText(dataUser.getEmail());
		
		// Hide all extra fields first
		lblApellido.setVisible(false);
		txtApellido.setVisible(false);
		lblFechaNac.setVisible(false);
		txtFechaNac.setVisible(false);
		lblDescripcion.setVisible(false);
		scrollDescripcion.setVisible(false);
		lblWeb.setVisible(false);
		txtWeb.setVisible(false);
		
		if (dataUser.getTipo() == TipoUsuario.ASISTENTE) {
			lblAsociaciones.setText("Registros a ediciones:");
			listAsociaciones.setListData(controllerUsr.listarRegistrosAEventos(selected).toArray(new String[0]));
			
			// Get detailed asistente information using DTAsistente
			logica.data_types.DTAsistente asistenteInfo = controllerUsr.infoAsistente(selected);
			if (asistenteInfo != null) {
				// Show and set apellido and fecha de nacimiento
				lblApellido.setVisible(true);
				txtApellido.setVisible(true);
				lblFechaNac.setVisible(true);
				txtFechaNac.setVisible(true);
				
				txtApellido.setText(asistenteInfo.getApellido() != null ? asistenteInfo.getApellido() : "");
				txtFechaNac.setText(asistenteInfo.getFechaNacimiento() != null ? asistenteInfo.getFechaNacimiento().toString() : "");
			}
		} else {
			lblAsociaciones.setText("Ediciones organizadas:");
			listAsociaciones.setListData(controllerUsr.listarEdicionesOrganizadas(selected).toArray(new String[0]));
			
			// Get detailed organizador information using DTOrganizador
			logica.data_types.DTOrganizador organizadorInfo = controllerUsr.infoOrganizador(selected);
			if (organizadorInfo != null) {
				// Show and set descripcion and web
				lblDescripcion.setVisible(true);
				scrollDescripcion.setVisible(true);
				lblWeb.setVisible(true);
				txtWeb.setVisible(true);
				
				txtDescripcion.setText(organizadorInfo.getDescripcion() != null ? organizadorInfo.getDescripcion() : "");
				txtWeb.setText(organizadorInfo.getWeb() != null ? organizadorInfo.getWeb() : "");
			}
		}
	}
	public static ConsultaUsuario getInstance(IControllerUsuario ICU) {
		if (instance == null) {
			instance = new ConsultaUsuario(ICU);
		}
		return instance;
	}
	
	
	
	
	
}