package adminStation;

import java.awt.EventQueue;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import casosPrueba.CargaDatos;
import logica.controllers.IControllerEvento;
import logica.controllers.IControllerUsuario;
import logica.models.Factory;
import webservices.publicadorEvento;
import webservices.publicadorUsuario;
import webservices.publicadorImagenes;

public class Main {

	private static final long serialVersionUID = 1L;
	private JFrame frmMain;
	private JDesktopPane desktopPane;  // Añadir como variable de clase
	
	private AltaUsuario frmAltaUsuario;
	private ConsultaUsuario frmConsultaUsuario;
	private ModificarDatosUsuario frmModificarDatosUsuario;
	private ConsultaEdicionDeEvento frmConsultaEdicion;
	private RegistroEdicion frmRegistroEdicion;
	private AltaPatrocinio frmAltaPatrocinio;
	private ConsultaPatrocinio frmConsultaPatrocinio;
	private AltaEvento frmAltaEvento;
	private ConsultaDeEvento frmConsultaDeEvento;
	private AltaEdicionDeEvento frmAltaEdicion;
	private AltaTipoRegistro frmAltaTipoRegistro;
	private ConsultaDeTipoDeRegistro frmConsultaTipoDeRegistro;
	private ConsultaRegistro frmConsultaRegistro;
	private AltaInstitucion frmAltaInstitucion;
	private FechaSistema frmFechaSistema;
	private ConfirmarRechazarEdicion frmConfirmarRechazarEdicion;
	private EstadisticasEventos frmEstadisticasEventos;
	private IControllerUsuario ICU;
	private IControllerEvento ICE;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.frmMain.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	public Main() {
		publicadorEvento pubEvento = new publicadorEvento();
		publicadorUsuario pubUsuario = new publicadorUsuario();
		publicadorImagenes pubImagenes = new publicadorImagenes();
		pubEvento.publicar();
		pubUsuario.publicar();
		pubImagenes.publicar();
		
		
		frmMain = new JFrame();
		frmMain.setTitle("Main");

		ICU = Factory.getInstance().getControllerUsuario();
		ICE = Factory.getInstance().getControllerEvento();
		
		ICE.setFechaSistema(LocalDate.now());
		frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMain.setBounds(100, 100, 720, 480);
		
		// Crear el JDesktopPane primero
		desktopPane = new JDesktopPane();
		frmMain.setContentPane(desktopPane);
		
	
		// Barra superior
		JMenuBar menuBar = new JMenuBar();
		frmMain.setJMenuBar(menuBar);
		
		JMenu mnSistema = new JMenu("Sistema");
		menuBar.add(mnSistema);
		
		
	
		frmFechaSistema = FechaSistema.getInstance(ICE);
		frmMain.getContentPane().add(frmFechaSistema);
		
		JMenuItem mntmCambiarFecha = new JMenuItem("Cambiar Fecha del Sistema");
		mnSistema.add(mntmCambiarFecha);
		mntmCambiarFecha.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmFechaSistema = FechaSistema.getInstance(ICE);
				frmFechaSistema.refrescar(ICE);
				frmFechaSistema.setVisible(true);
				frmFechaSistema.toFront();
				System.out.println("Fecha del sistema actual: " + ICE.getFechaSistema());
			}
		});
		
		
		JMenuItem mntmCargarDatos = new JMenuItem("Cargar Datos");
		mnSistema.add(mntmCargarDatos);
		mntmCargarDatos.addActionListener(e -> {
		    try {
		        CargaDatos.cargarDatos();
		        // refrescar pantallas que dependen de los datos:
		        JOptionPane.showMessageDialog(mntmCargarDatos, "Los datos se han cargado correctamente.", "Carga de datos", JOptionPane.INFORMATION_MESSAGE);
		        mntmCargarDatos.setVisible(false);
		        if (frmConsultaEdicion != null) frmConsultaEdicion.refrescar();
		        if (frmConsultaDeEvento != null) frmConsultaDeEvento.refrescar(); // si tenés método similar
		        // idem otras vistas
		    } catch (Exception ex) {
		        ex.printStackTrace();
		    }
		});
		
		JMenuItem mntmSalir = new JMenuItem("Salir");
		mnSistema.add(mntmSalir);
		mntmSalir.addActionListener(e-> {
			frmMain.dispose();
			System.exit(0);
		});

		
		JMenu mnFuncionalidades = new JMenu("Funcionalidades");
		menuBar.add(mnFuncionalidades);
		
		
		// Menu Usuario
		JMenu mnUsuario = new JMenu("Usuario");
		mnFuncionalidades.add(mnUsuario);
		
		//Submenus Usuario
			JMenuItem mntmAltaUsuario = new JMenuItem("Alta Usuario");
			mnUsuario.add(mntmAltaUsuario);
			frmAltaUsuario = AltaUsuario.getInstance(ICU);
			frmMain.getContentPane().add(frmAltaUsuario);
			frmAltaUsuario.setVisible(false);
			mntmAltaUsuario.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frmAltaUsuario.setVisible(true);
					frmAltaUsuario.refrescar();
					frmAltaUsuario.toFront();
				}
			});
			
			
			JMenuItem mntmModificarDatos = new JMenuItem("Modificar Datos");
			mnUsuario.add(mntmModificarDatos);
			frmModificarDatosUsuario = ModificarDatosUsuario.getInstance(ICU);
			frmMain.getContentPane().add(frmModificarDatosUsuario);
			frmModificarDatosUsuario.setVisible(false);
			mntmModificarDatos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frmModificarDatosUsuario.refrescar(ICU);
					frmModificarDatosUsuario.setVisible(true);
					frmModificarDatosUsuario.toFront();
				}
			});
			
			JMenuItem mtnmConsultaUsuario = new JMenuItem("Consulta");
			mnUsuario.add(mtnmConsultaUsuario);
			frmConsultaUsuario = ConsultaUsuario.getInstance(ICU);
			frmMain.getContentPane().add(frmConsultaUsuario);
			frmConsultaUsuario.setVisible(false);
			mtnmConsultaUsuario.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frmConsultaUsuario.setVisible(true);
					frmConsultaUsuario.toFront();
					frmConsultaUsuario.refrescar();
				}
			});

		
        //Menu Evento
		JMenu mnEvento = new JMenu("Evento");
		mnFuncionalidades.add(mnEvento);
		
		// Submenus Evento
			JMenuItem mntmAltaEvento = new JMenuItem("Alta");
			mnEvento.add(mntmAltaEvento);
			frmAltaEvento= AltaEvento.getInstance(ICE);
			frmMain.getContentPane().add(frmAltaEvento);
			frmAltaEvento.setVisible(false);
			mntmAltaEvento.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frmAltaEvento.resfrescar();
					frmAltaEvento.setVisible(true);
					frmAltaEvento.toFront();
					
				}
			});
		
			JMenuItem mntmConsultaEvento = new JMenuItem("Consulta");
			mnEvento.add(mntmConsultaEvento);
			frmConsultaDeEvento = ConsultaDeEvento.getInstance(ICE);
			frmMain.getContentPane().add(frmConsultaDeEvento);
			frmConsultaDeEvento.setVisible(false);
			mntmConsultaEvento.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frmConsultaDeEvento.refrescar();
					frmConsultaDeEvento.setVisible(true);
					frmConsultaDeEvento.toFront();
				}
			});
		
		// Edicion
		JMenu mnEdicion = new JMenu("Edicion");
		mnFuncionalidades.add(mnEdicion);
		
		//Submenus Edicion
			JMenuItem mntmAlta = new JMenuItem("Alta");
			mnEdicion.add(mntmAlta);
			frmAltaEdicion =AltaEdicionDeEvento.getInstance(ICE,ICU);
			frmMain.getContentPane().add(frmAltaEdicion);
			frmAltaEdicion.setVisible(false);
			mntmAlta.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frmAltaEdicion.setVisible(true);
					frmAltaEdicion.toFront();
					frmAltaEdicion.refrescar(ICE,ICU);
				}
			});
		
		
		
			JMenuItem mntmRegistro = new JMenuItem("Registro");
			mnEdicion.add(mntmRegistro);
			frmRegistroEdicion =  RegistroEdicion.getInstance(ICE, ICU);
			frmMain.getContentPane().add(frmRegistroEdicion);
			frmRegistroEdicion.setVisible(false);
			mntmRegistro.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frmRegistroEdicion.setVisible(true);
					frmRegistroEdicion.toFront();
					frmRegistroEdicion.refrescarEventos(ICE);
					frmRegistroEdicion.refrescarAsistentes(ICU);
				}
			});
			
			JMenuItem mntmConsultaEdicion = new JMenuItem("Consulta");
			mnEdicion.add(mntmConsultaEdicion);
			frmConsultaEdicion = ConsultaEdicionDeEvento.getInstance(ICE);
			frmMain.getContentPane().add(frmConsultaEdicion);
			frmConsultaEdicion.setVisible(false);
			mntmConsultaEdicion.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			        frmConsultaEdicion.refrescar();    
			        frmConsultaEdicion.setVisible(true);
			        frmConsultaEdicion.toFront();
			    }
			});
			
			// Confirmar/Rechazar Edicion
			JMenuItem mntmConfirmarRechazarEdicion = new JMenuItem("Administrar ediciones pendientes");
			mnEdicion.add(mntmConfirmarRechazarEdicion);
			frmConfirmarRechazarEdicion = ConfirmarRechazarEdicion.getInstance(ICE, ICU);
			frmMain.getContentPane().add(frmConfirmarRechazarEdicion);
			frmConfirmarRechazarEdicion.setVisible(false);
			mntmConfirmarRechazarEdicion.addActionListener(new ActionListener() {
				  public void actionPerformed(ActionEvent e) {
			        frmConfirmarRechazarEdicion.refrescar();
			        frmConfirmarRechazarEdicion.setVisible(true);
			        frmConfirmarRechazarEdicion.toFront();
			    }
			});
			

		
		// Patrocinio
		JMenu mnPatrocinio = new JMenu("Patrocinio");
		mnFuncionalidades.add(mnPatrocinio);
		
		//Submenus patrocinio
			JMenuItem mntmAltaPatrocinio = new JMenuItem("Alta");
			mnPatrocinio.add(mntmAltaPatrocinio);
			frmAltaPatrocinio = AltaPatrocinio.getInstance(ICE, ICU);
			desktopPane.add(frmAltaPatrocinio);
			frmAltaPatrocinio.setVisible(false);
			mntmAltaPatrocinio.addActionListener(e -> {
			    frmAltaPatrocinio.refrescar();
			    frmAltaPatrocinio.setVisible(true);
			    frmAltaPatrocinio.toFront();
			});
		
			JMenuItem mntmConsultaPatrocinio = new JMenuItem("Consulta");
			mnPatrocinio.add(mntmConsultaPatrocinio);

			frmConsultaPatrocinio = ConsultaPatrocinio.getInstance(ICE);
			desktopPane.add(frmConsultaPatrocinio);
			frmConsultaPatrocinio.setVisible(false);

			mntmConsultaPatrocinio.addActionListener(e -> {
			    frmConsultaPatrocinio.refrescar();
			    frmConsultaPatrocinio.setVisible(true);
			    frmConsultaPatrocinio.toFront();
			    try { frmConsultaPatrocinio.setSelected(true); } catch (Exception ignore) {}
			});

		
		
		// Tipo Registro
		JMenu mnTipoRegistro = new JMenu("Tipo Registro");
		mnFuncionalidades.add(mnTipoRegistro);
			//Sub menus TipoRegistro
			JMenuItem mntmAltaTipoRegistro = new JMenuItem("Alta");
			mnTipoRegistro.add(mntmAltaTipoRegistro);
			frmAltaTipoRegistro = AltaTipoRegistro.getInstance(ICE);
			frmMain.getContentPane().add(frmAltaTipoRegistro);
			frmAltaTipoRegistro.setVisible(false);
			mntmAltaTipoRegistro.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frmAltaTipoRegistro.setVisible(true);
					frmAltaTipoRegistro.toFront();
					frmAltaTipoRegistro.refrescar(ICE);
				}
			});
			
			JMenuItem mntmConsultaTipoReg = new JMenuItem("Consulta");
			mnTipoRegistro.add(mntmConsultaTipoReg);
			frmConsultaTipoDeRegistro = ConsultaDeTipoDeRegistro.getInstance(ICE);
			desktopPane.add(frmConsultaTipoDeRegistro);
			frmConsultaTipoDeRegistro.setVisible(false);
			mntmConsultaTipoReg.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frmConsultaTipoDeRegistro.setVisible(true);
					frmConsultaTipoDeRegistro.toFront();
					frmConsultaTipoDeRegistro.refrescar();
				}
			});
		
		//Registro
		JMenu mnRegistro = new JMenu("Registro");
		mnFuncionalidades.add(mnRegistro);
			//Submenus Registro
			JMenuItem mntmConsultaRegistro = new JMenuItem("Consulta");
			mnRegistro.add(mntmConsultaRegistro);
			frmConsultaRegistro = ConsultaRegistro.getInstance(ICE, ICU);
			frmMain.getContentPane().add(frmConsultaRegistro);
			frmConsultaRegistro.setVisible(false);
			mntmConsultaRegistro.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frmConsultaRegistro.setVisible(true);
					frmConsultaRegistro.toFront();
					frmConsultaRegistro.refrescar();
				}
			});
		// Institucion
		JMenu mnInstitucion = new JMenu("Institucion");
		mnFuncionalidades.add(mnInstitucion);
			//Submenus Institucion
			JMenuItem mntmAltaInstitucion = new JMenuItem("Alta");
			mnInstitucion.add(mntmAltaInstitucion);
			frmAltaInstitucion = AltaInstitucion.getInstance(ICU);
			frmMain.getContentPane().add(frmAltaInstitucion);
			frmAltaInstitucion.setVisible(false);
			mntmAltaInstitucion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frmAltaInstitucion.setVisible(true);
					frmAltaInstitucion.toFront();
					
				}});
				
			
			
		// Estadísticas
		JMenu mnEstadisticas = new JMenu("Estadísticas");
		mnFuncionalidades.add(mnEstadisticas);
			//Submenus Estadísticas
			JMenuItem mntmEventosPopulares = new JMenuItem("Eventos Más Visitados");
			mnEstadisticas.add(mntmEventosPopulares);
			frmEstadisticasEventos = EstadisticasEventos.obtenerInstancia(ICE);
			frmMain.getContentPane().add(frmEstadisticasEventos);
			frmEstadisticasEventos.setVisible(false);
			mntmEventosPopulares.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frmEstadisticasEventos.refrescar();
					frmEstadisticasEventos.setVisible(true);
					frmEstadisticasEventos.toFront();
				}
			});
				
		frmMain.getContentPane().setLayout(null);
	}
}