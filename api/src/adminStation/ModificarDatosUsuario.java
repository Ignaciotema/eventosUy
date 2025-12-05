package adminStation;

import java.awt.Font;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Set;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import excepciones.UsuarioNoEncontrado;
import logica.controllers.IControllerUsuario;
import logica.data_types.DTAsistente;
import logica.data_types.DTOrganizador;
import logica.data_types.DataUsuario;

public class ModificarDatosUsuario extends JInternalFrame {
    private static ModificarDatosUsuario instance = null;
	private static final long serialVersionUID = 1L;
	// Constante para el placeholder
	private static final String PLACEHOLDER_USUARIO = "— Seleccione usuario —";
	
	private JTextField txtFieldNombreUsuario;
	private JTextField txtFieldNicknameUsuario;
	private JTextField txtFieldEmailUsuario;
	private JTextField txtFieldfNacUsuario;
	private JTextField txtFieldApellidoUsuario;
	JComboBox<String> comboBox;
	
	// Campos para etiquetas comunes
	private JLabel lblNombre;
	private JLabel lblNombre_1;
	private JLabel lblNombre_2;
	private JButton btnConfirmarEdicionUsuario;
	private JButton btnCancelarEdicionUsuario;
	
	// Campos específicos para Organizador
	private JTextField txtFieldDescripcion;
	private JTextField txtFieldWeb;
	private JLabel lblDescripcion;
	private JLabel lblWeb;
	private JLabel lblApellido;
	private JLabel lblFechaDeNacimiento;
	
	
	// Referencia al controlador
	private IControllerUsuario ICU;

	public ModificarDatosUsuario(IControllerUsuario ICU) {
		this.ICU = ICU;
		setTitle("Modificar datos de Usuario");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		setClosable(true);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setIconifiable(true);
		setMaximizable(true);
		
		
		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.setBounds(0, 0, 1, 1);
		getContentPane().add(horizontalBox);
		
		JLabel lblUsuario = new JLabel("Seleccionar Usuario:");
		lblUsuario.setFont(new Font("Dialog", Font.BOLD, 15));
		lblUsuario.setBounds(58, 16, 156, 17);
		getContentPane().add(lblUsuario);
		
		comboBox = new JComboBox<String>();
		// Añadir primero el placeholder
		comboBox.addItem(PLACEHOLDER_USUARIO);
		// Luego añadir los usuarios
		Set<String> usuarios = ICU.listarUsuarios();
		for (String nomUsuario : usuarios) {
			comboBox.addItem(nomUsuario);
		}
		comboBox.setBounds(214, 12, 124, 26);
		comboBox.setSelectedIndex(0); // Seleccionar el placeholder por defecto
		getContentPane().add(comboBox);
		
		// Añadir el evento al combobox para cargar los datos del usuario seleccionado
		comboBox.addActionListener(e -> {
			int selectedIndex = comboBox.getSelectedIndex();
			// Si es el placeholder, no cargar datos
			if (selectedIndex > 0) {
				String nickname = (String) comboBox.getSelectedItem();
				try {
					cargarDatosUsuario(nickname);
				} catch (UsuarioNoEncontrado e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// Mostrar todos los campos comunes
				mostrarCamposComunes(true);
			} else if (selectedIndex == 0) {
				// Limpiar formulario si se selecciona el placeholder
				limpiarFormulario();
				// Ocultar todos los campos
				mostrarCamposComunes(false);
			}
		});
		
		lblNombre = new JLabel("Nombre: ");
		lblNombre.setBounds(107, 69, 60, 17);
		getContentPane().add(lblNombre);
		
		lblNombre_1 = new JLabel("nickname:");
		lblNombre_1.setBounds(107, 93, 68, 17);
		getContentPane().add(lblNombre_1);
		
		lblNombre_2 = new JLabel("Email:");
		lblNombre_2.setBounds(107, 122, 60, 17);
		getContentPane().add(lblNombre_2);
		
		// Asignar las referencias a los campos de clase en lugar de crear variables locales
		lblFechaDeNacimiento = new JLabel("Fecha de nacimiento(yyyy-mm-dd:");
		lblFechaDeNacimiento.setBounds(107, 155, 133, 17);
		getContentPane().add(lblFechaDeNacimiento);
		
		lblApellido = new JLabel("Apellido:");
		lblApellido.setBounds(107, 173, 60, 17);
		getContentPane().add(lblApellido);
		
		txtFieldNombreUsuario = new JTextField();
		txtFieldNombreUsuario.setBounds(247, 69, 114, 17);
		getContentPane().add(txtFieldNombreUsuario);
		txtFieldNombreUsuario.setColumns(10);
		
		txtFieldNicknameUsuario = new JTextField();
		txtFieldNicknameUsuario.setEditable(false);
		txtFieldNicknameUsuario.setColumns(10);
		txtFieldNicknameUsuario.setBounds(247, 93, 114, 17);
		getContentPane().add(txtFieldNicknameUsuario);
		
		txtFieldEmailUsuario = new JTextField();
		txtFieldEmailUsuario.setEditable(false);
		txtFieldEmailUsuario.setColumns(10);
		txtFieldEmailUsuario.setBounds(247, 122, 114, 17);
		getContentPane().add(txtFieldEmailUsuario);
		
		txtFieldfNacUsuario = new JTextField();
		txtFieldfNacUsuario.setColumns(10);
		txtFieldfNacUsuario.setBounds(247, 153, 114, 17);
		getContentPane().add(txtFieldfNacUsuario);
		
		txtFieldApellidoUsuario = new JTextField();
		txtFieldApellidoUsuario.setColumns(10);
		txtFieldApellidoUsuario.setBounds(247, 173, 114, 17);
		getContentPane().add(txtFieldApellidoUsuario);
		
		// Campos específicos para Organizador
		lblDescripcion = new JLabel("Descripción:");
		lblDescripcion.setBounds(107, 155, 80, 17);
		getContentPane().add(lblDescripcion);
		
		txtFieldDescripcion = new JTextField();
		txtFieldDescripcion.setColumns(10);
		txtFieldDescripcion.setBounds(247, 153, 114, 17);
		getContentPane().add(txtFieldDescripcion);
		
		lblWeb = new JLabel("Web:");
		lblWeb.setBounds(107, 173, 60, 17);
		getContentPane().add(lblWeb);
		
		txtFieldWeb = new JTextField();
		txtFieldWeb.setColumns(10);
		txtFieldWeb.setBounds(247, 173, 114, 17);
		getContentPane().add(txtFieldWeb);
		
		btnConfirmarEdicionUsuario = new JButton("Aceptar");
		btnConfirmarEdicionUsuario.setBounds(109, 229, 105, 27);
		getContentPane().add(btnConfirmarEdicionUsuario);
		
		btnCancelarEdicionUsuario = new JButton("Cancelar");
		btnCancelarEdicionUsuario.setBounds(226, 229, 105, 27);
		getContentPane().add(btnCancelarEdicionUsuario);
		
		btnConfirmarEdicionUsuario.addActionListener(e -> {
		    String nombre = txtFieldNombreUsuario.getText();
		    String nickname = txtFieldNicknameUsuario.getText();
		    
		    try {
		        // Validar que el nombre no esté vacío
		        if (nombre.trim().isEmpty()) {
		            JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
		            return;
		        }
		        
		        // Determinar si es un asistente u organizador y usar el método adecuado
		        if (esAsistente(nickname)) {
		            String apellido = txtFieldApellidoUsuario.getText();
		            String fNac = txtFieldfNacUsuario.getText();
		            
		            // Validar campos específicos de asistente
		            if (apellido.trim().isEmpty()) {
		                JOptionPane.showMessageDialog(this, "El apellido no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
		                return;
		            }
		            
		            if (fNac.trim().isEmpty()) {
		                JOptionPane.showMessageDialog(this, "La fecha de nacimiento no puede estar vacía", "Error", JOptionPane.ERROR_MESSAGE);
		                return;
		            }
		            
		            LocalDate fechaNac = LocalDate.parse(fNac);
		            ICU.editarAsistente(nickname,nombre, apellido, fechaNac);
		        } else {
		            // Es organizador
		            String descripcion = txtFieldDescripcion.getText();
		            String web = txtFieldWeb.getText();
		            
		            // Validar campos específicos de organizador
		            if (descripcion.trim().isEmpty()) {
		                JOptionPane.showMessageDialog(this, "La descripción no puede estar vacía", "Error", JOptionPane.ERROR_MESSAGE);
		                return;
		            }
		            
		            ICU.editarOrganizador(nickname,nombre, descripcion, web);
		        }
		        
		        setVisible(false);
		        limpiarFormulario();
		        JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente", "Modificar Usuario", JOptionPane.INFORMATION_MESSAGE);
		    } catch (DateTimeException ex) {
		        JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Use yyyy-mm-dd.", "Error", JOptionPane.ERROR_MESSAGE);
		    } catch (Exception ex) {
		        JOptionPane.showMessageDialog(this, "Error al actualizar usuario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		    }
		});
		
		btnCancelarEdicionUsuario.addActionListener(e -> {
		    setVisible(false);
		    limpiarFormulario();
		});
		
		// Inicialmente ocultar los campos específicos
		mostrarCamposAsistente(false);
		mostrarCamposOrganizador(false);
		// Inicialmente ocultar también los campos comunes ya que el placeholder está seleccionado
		mostrarCamposComunes(false);
	}
	
	private void cargarDatosUsuario(String nickname) throws UsuarioNoEncontrado {
		// Obtener datos del usuario seleccionado
		DataUsuario dataUsuario = ICU.infoUsuario(nickname);
		
		// Cargar datos comunes
		txtFieldNicknameUsuario.setText(dataUsuario.getNickname());
		txtFieldNombreUsuario.setText(dataUsuario.getNombre());
		txtFieldEmailUsuario.setText(dataUsuario.getEmail());
		
		// Determinar si es asistente u organizador y mostrar campos específicos
		if (esAsistente(nickname)) {
			DTAsistente dataAsistente = ICU.infoAsistente(nickname);
			mostrarCamposAsistente(true);
			mostrarCamposOrganizador(false);
			
			// Cargar datos específicos de asistente
			txtFieldApellidoUsuario.setText(dataAsistente.getApellido());
			LocalDate fechaNac = dataAsistente.getFechaNacimiento();
			if (fechaNac != null) {
				txtFieldfNacUsuario.setText(fechaNac.toString());
			} else {
				txtFieldfNacUsuario.setText("");
			}
			
		} else {
			// Es organizador
			DTOrganizador dataOrganizador = ICU.infoOrganizador(nickname);
			
			mostrarCamposAsistente(false);
			mostrarCamposOrganizador(true);
			
			// Cargar datos específicos de organizador
			txtFieldDescripcion.setText(dataOrganizador.getDescripcion());
			txtFieldWeb.setText(dataOrganizador.getWeb());
		}
	}
	
	private boolean esAsistente(String nickname) {
		Set<String> asistentes = ICU.listarAsistentes();
		return asistentes.contains(nickname);
	}
	
	private void mostrarCamposAsistente(boolean mostrar) {
		lblApellido.setVisible(mostrar);
		txtFieldApellidoUsuario.setVisible(mostrar);
		lblFechaDeNacimiento.setVisible(mostrar);
		txtFieldfNacUsuario.setVisible(mostrar);
	}
	
	private void mostrarCamposOrganizador(boolean mostrar) {
		lblDescripcion.setVisible(mostrar);
		txtFieldDescripcion.setVisible(mostrar);
		lblWeb.setVisible(mostrar);
		txtFieldWeb.setVisible(mostrar);
	}
	
	private void mostrarCamposComunes(boolean mostrar) {
		// Labels
		lblNombre.setVisible(mostrar);
		lblNombre_1.setVisible(mostrar);
		lblNombre_2.setVisible(mostrar);
		
		// TextFields
		txtFieldNombreUsuario.setVisible(mostrar);
		txtFieldNicknameUsuario.setVisible(mostrar);
		txtFieldEmailUsuario.setVisible(mostrar);
		
		// Botones de confirmar y cancelar (opcional, pero mejor ocultarlos también)
		btnConfirmarEdicionUsuario.setVisible(mostrar);
		btnCancelarEdicionUsuario.setVisible(mostrar);
	}
	
	private void limpiarFormulario() {
		comboBox.setSelectedIndex(-1);
		txtFieldNombreUsuario.setText("");
		txtFieldNicknameUsuario.setText("");
		txtFieldfNacUsuario.setText("");
		txtFieldEmailUsuario.setText("");
		txtFieldApellidoUsuario.setText("");
		txtFieldDescripcion.setText("");
		txtFieldWeb.setText("");
		
		// Ocultar campos específicos
		mostrarCamposAsistente(false);
		mostrarCamposOrganizador(false);
		// Ocultar campos comunes
		mostrarCamposComunes(false);
	}
	
	public static ModificarDatosUsuario getInstance(IControllerUsuario ICU) {
		if (instance == null) {
			instance = new ModificarDatosUsuario(ICU);
		}
		return instance;
	}
	
	public void refrescar(IControllerUsuario ICU) {
		comboBox.removeAllItems();
		// Añadir primero el placeholder
		comboBox.addItem(PLACEHOLDER_USUARIO);
		// Luego añadir los usuarios
		Set<String> usuarios = ICU.listarUsuarios();
		for (String nomUsuario : usuarios) {
			comboBox.addItem(nomUsuario);
		}
		// Seleccionar el placeholder por defecto
		comboBox.setSelectedIndex(0);
		
		// Limpiar los campos
		txtFieldNombreUsuario.setText("");
		txtFieldNicknameUsuario.setText("");
		txtFieldfNacUsuario.setText("");
		txtFieldEmailUsuario.setText("");
		txtFieldApellidoUsuario.setText("");
		txtFieldDescripcion.setText("");
		txtFieldWeb.setText("");
		
		// Ocultar campos específicos
		mostrarCamposAsistente(false);
		mostrarCamposOrganizador(false);
		// Ocultar campos comunes
		mostrarCamposComunes(false);
	}
	
	
	
	
}