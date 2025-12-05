package casosPrueba;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import excepciones.FechaInicioPOSTFINAL;
import excepciones.FechaInicioPREALTA;
import excepciones.NombreEdicionExistenteExcepcion;
import logica.controllers.IControllerEvento;
import logica.controllers.IControllerUsuario;
import logica.enumerators.NivelPatrocinio;
import logica.manejadores.ManejadorCategoria;
import logica.manejadores.ManejadorEvento;
import logica.manejadores.ManejadorInstitucion;
import logica.manejadores.ManejadorUsuario;
import logica.models.Asistente;
import logica.models.Evento;
import logica.models.Factory;
import logica.models.Organizador;
import logica.models.RastreadorVisitasEvento;
import logica.models.Usuario;

public class CargaDatos {
	public static void cargarDatos() throws Exception {
		
		cargarInstituciones();
		cargarUsuarios();
		cargarCategorias();
		cargarEventos();
		cargarEdiciones();
		cargarTiposRegistro();
		cargarPatrocinios();
		cargarRegistros();
		cargarSeguidores();
		
		System.out.println("Carga de datos finalizada");
	    imprimirDatosCargados();
		
	}
	
	
	





	//CARGAS
	public static void cargarUsuarios() throws Exception {
		BufferedReader brUsuarios = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/datosPrueba/2025Usuarios.csv"));

		IControllerUsuario ICU = Factory.getInstance().getControllerUsuario();

		String linea;
		brUsuarios.readLine(); // Saltear la primer linea (headers)
		while ((linea = brUsuarios.readLine()) != null) {
			if (linea.isBlank())
				continue;
			String[] campos = linea.split(";");

			String idUsr = campos[0];
			String tipoUsr = campos[1];
			String nickname = campos[2];
			String nombre = campos[3];
			String email = campos[4];
			String password = campos[5];
			if (tipoUsr.equals("A")) {
				String[] lineaAsist = buscarLinea(idUsr, "/datosPrueba/2025Usuarios-Asistentes.csv");
				String[] fechaNac = lineaAsist[2].split("/");
				String strFechaNac = new String(fechaNac[2] + "-" + fechaNac[1] + "-" + fechaNac[0]);
				ICU.ingresarAsistente(nickname, nombre, email,password,lineaAsist[1], LocalDate.parse(strFechaNac));
				
				if (lineaAsist.length == 4) {
					String[] lineaInst = buscarLinea(lineaAsist[3], "/datosPrueba/2025Instituciones.csv");
					ICU.agregarAsistente(nickname, lineaInst[1]);
				}
				
			} else {
				String[] lineaOrg = buscarLinea(idUsr, "/datosPrueba/2025Usuarios-Organizadores.csv");
				ICU.ingresarOrganizador(nickname, nombre, email,password, lineaOrg[1], lineaOrg.length == 3 ? lineaOrg[2] : "");
			}
			
		}
		System.out.println("Usuarios cargados");
		brUsuarios.close();
	}
	
	
	
	
	
	
	public static void cargarInstituciones() throws Exception {
		BufferedReader brInstituciones = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/datosPrueba/2025Instituciones.csv"));

		IControllerUsuario ICU = Factory.getInstance().getControllerUsuario();
		
		String linea;
		brInstituciones.readLine(); // Saltear la primer linea (headers)
		while ((linea = brInstituciones.readLine()) != null) {
			if (linea.isBlank())
				continue;
			String[] campos = linea.split(";");
			System.out.println(campos[1]);
			System.out.println(campos[2]);
			System.out.println(campos[3]);
			ICU.altaInstitucion(campos[1], campos[2], campos[3]);
		}
		System.out.println("Instituciones cargadas");
		brInstituciones.close();
	}
	
	
	
	
	
	
	public static void cargarCategorias() throws Exception {
		BufferedReader brCategorias = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/datosPrueba/2025Categorias.csv"));

		IControllerEvento ICE = Factory.getInstance().getControllerEvento();
		
		String linea;
		brCategorias.readLine(); // Saltear la primer linea (headers)
		while ((linea = brCategorias.readLine()) != null) {
			if (linea.isBlank())
				continue;
			String[] campos = linea.split(";");
			ICE.ingresarCategoria(campos[1]);
		}
		System.out.println("Categorias cargadas");
		brCategorias.close();
	}
	
	
	
	
	
	
	public static void cargarEventos() throws Exception {
		BufferedReader brEventos = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/datosPrueba/2025Eventos.csv"));

		IControllerEvento ICE = Factory.getInstance().getControllerEvento();
		RastreadorVisitasEvento rastreador = RastreadorVisitasEvento.obtenerInstancia();
		
		String linea;
		brEventos.readLine(); // Saltear la primer linea (headers)
		while ((linea = brEventos.readLine()) != null) {
			if (linea.isBlank())
				continue;
			String[] campos = linea.split(";");
			
			String idEv = campos[0];
			String nombre= campos[1];
			String descripcion = campos[2];
			String sigla = campos[3];
			String fechaAlta = campos[4];
			// Formatear fecha de dd/mm/yyyy a yyyy-mm-dd
			String[] fechaParts = fechaAlta.split("/");
			fechaAlta = new String(fechaParts[2] + "-" + fechaParts[1] + "-" + fechaParts[0]);
			
			HashSet<String> idCategorias = new HashSet<String>(Arrays.asList(campos[5].split(",")));
			
			HashSet<String> categorias = new HashSet<String>();
			for (String cat : idCategorias) {
				categorias.add(buscarLinea(cat.stripLeading(), "/datosPrueba/2025Categorias.csv")[1]);
			}
			
			ICE.altaEvento(nombre, sigla, LocalDate.parse(fechaAlta), descripcion, categorias,"");
			
			// Manejar el estado finalizado (campos[7])
			String finalizado = campos[7];
			if (finalizado.equals("Si")) {
				// Acceder al manejador para establecer el estado finalizado del evento
				ManejadorEvento mE = ManejadorEvento.getInstance();
				Evento evento = mE.obtenerEvento(nombre);
				if (evento != null) {
					evento.setFinalizado(true);
				}
			}
			
			// Cargar contador de visitas desde la última columna (campos[8])
			if (campos.length > 8 && !campos[8].trim().isEmpty()) {
				try {
					int visitas = Integer.parseInt(campos[8].trim());
					// Registrar las visitas en el rastreador
					for (int i = 0; i < visitas; i++) {
						rastreador.registrarVisita(nombre);
					}
				} catch (NumberFormatException e) {
					System.out.println("Error al parsear visitas para evento " + nombre + ": " + campos[8]);
				}
			}
		}
		System.out.println("Eventos cargados");
		brEventos.close();
	}
	
	
	
	
	
	public static void cargarEdiciones() {
		BufferedReader brEdiciones;
		try {
			brEdiciones = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/datosPrueba/2025EdicionesEventos.csv"));

			IControllerEvento ICE = Factory.getInstance().getControllerEvento();
			
			String linea;
			brEdiciones.readLine(); // Saltear la primer linea (headers)
			while ((linea = brEdiciones.readLine()) != null) {
				if (linea.isBlank())
					continue;
				System.out.println(linea);
				
				String[] campos = linea.split(";");
				
				String idEdi = campos[0];
				String idEve = campos[1];
				String idOrg = campos[2];
				String nombre = campos[3];
				String sigla = campos[4];
				String ciudad = campos[5];
				String pais = campos[6];
				String fechaIni = campos[7];
				String fechaFin = campos[8];
				String fechaAlta = campos[9];
				// campos[11] es Imagen - lo ignoramos
				String videoUrl = campos.length > 12 ? campos[12] : ""; // Video URL
				
				String nombreEvento = buscarLinea(idEve, "/datosPrueba/2025Eventos.csv")[1];
				String nicknameOrganizador = buscarLinea(idOrg, "/datosPrueba/2025Usuarios.csv")[2];
				
				String[] fechaIniParts = fechaIni.split("/");
				fechaIni = new String(fechaIniParts[2] + "-" + fechaIniParts[1] + "-" + fechaIniParts[0]);
				
				String[] fechaFinParts = fechaFin.split("/");
				fechaFin = new String(fechaFinParts[2] + "-" + fechaFinParts[1] + "-" + fechaFinParts[0]);
				
				String[] fechaAltaParts = fechaAlta.split("/");
				fechaAlta = new String(fechaAltaParts[2] + "-" + fechaAltaParts[1] + "-" + fechaAltaParts[0]);
				
				ICE.altaEdicionDeEvento(nombreEvento, nicknameOrganizador, nombre, sigla, LocalDate.parse(fechaIni), LocalDate.parse(fechaFin), LocalDate.parse(fechaAlta), ciudad, pais, videoUrl);
			
				if (campos[10].equals("Aceptada")) {
					ICE.aceptarEdicion(nombre,nombreEvento);
				}
			    if (campos[10].equals("Rechazada")) {
					ICE.rechazarEdicion(nombre,nombreEvento);
				}
			}} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NombreEdicionExistenteExcepcion e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FechaInicioPOSTFINAL e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FechaInicioPREALTA e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	
		System.out.println("Ediciones cargadas");}
			
	
		
		
		
		
		private static void cargarTiposRegistro() {
			BufferedReader brTiposReg;
			try {
				brTiposReg = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/datosPrueba/2025TipoRegistro.csv"));

				IControllerEvento ICE = Factory.getInstance().getControllerEvento();
				
				String linea;
				brTiposReg.readLine(); // Saltear la primer linea (headers)
				while ((linea = brTiposReg.readLine()) != null) {
					if (linea.isBlank())
						continue;
					
					String[] campos = linea.split(";");
								
					String idTip = campos[0];
					String idEdi = campos[1];
					String nombre = campos[2];
					String descripcion = campos[3];
					Float costo = Float.parseFloat(campos[4]);
					int cupo = Integer.parseInt(campos[5]);
					
					String nombreEdi = buscarLinea(idEdi, "/datosPrueba/2025EdicionesEventos.csv")[3];
					
					ICE.altaTipoDeRegistro(nombreEdi, nombre, descripcion, costo, cupo);
				
				}} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		
			System.out.println("Tipos de registro cargados");
			
		}
	
	
	private static void cargarPatrocinios() {
		BufferedReader brPatrocinios;
		try {
			brPatrocinios = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/datosPrueba/2025Patrocinios.csv"));

			IControllerEvento ICE = Factory.getInstance().getControllerEvento();
			
			String linea;
			brPatrocinios.readLine(); // Saltear la primer linea (headers)
			while ((linea = brPatrocinios.readLine()) != null) {
				if (linea.isBlank())
					continue;
				
				String[] campos = linea.split(";");
							
				String idPat = campos[0];
				String idEdi = campos[1];
				String idInst = campos[2];
				String nivel = campos[3];
				String idTipoReg = campos[4];
				double aporte = Double.parseDouble(campos[5]);
				String fechaAlta = campos[6];
				int cantReg = Integer.parseInt(campos[7]);
				String codigo = campos[8];
				
				String nombreEdi = buscarLinea(idEdi, "/datosPrueba/2025EdicionesEventos.csv")[3];
				String nombreInst = buscarLinea(idInst, "/datosPrueba/2025Instituciones.csv")[1];
				String tipoGratis = buscarLinea(idTipoReg, "/datosPrueba/2025TipoRegistro.csv")[2];
				
				NivelPatrocinio nivelEnum;
				if (nivel.equals("Platino")) {
					nivelEnum = NivelPatrocinio.Platino;
				} else if (nivel.equals("Oro")) {
					nivelEnum = NivelPatrocinio.Oro;
				} else if (nivel.equals("Plata")) {
					nivelEnum = NivelPatrocinio.Plata;
				} else {
					nivelEnum = NivelPatrocinio.Bronce;
				}
				
				String[] fechaAltaParts = fechaAlta.split("/");
				fechaAlta = new String(fechaAltaParts[2] + "-" + fechaAltaParts[1] + "-" + fechaAltaParts[0]);
				
				//TODO: SetFechaActual(LocalDate.parse(fechaAlta));	
				Factory.getInstance().getControllerEvento().setFechaSistema(LocalDate.parse(fechaAlta));
				ICE.altaPatrocinio(nombreEdi, nombreInst, nivelEnum, aporte, tipoGratis, cantReg, codigo);
			
			}} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	
		System.out.println("Patrocinios cargados");
		
	}
	
	
	
	
	
	private static void cargarRegistros() {
		BufferedReader brRegistros;
		try {
			brRegistros = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/datosPrueba/2025Registros.csv"));

			IControllerEvento ICE = Factory.getInstance().getControllerEvento();
			
			String linea;
			brRegistros.readLine(); // Saltear la primer linea (headers)
			while ((linea = brRegistros.readLine()) != null) {
				if (linea.isBlank())
					continue;
				
				String[] campos = linea.split(";");
							
				String idReg = campos[0];
				String idUsu = campos[1];
				String idEdi = campos[2];
				String idTipoReg = campos[3];
				String fechaAlta = campos[4];
				float costo = Float.parseFloat(campos[5]);
				
				String nombreEdi = buscarLinea(idEdi, "/datosPrueba/2025EdicionesEventos.csv")[3];
				String nickAsistente = buscarLinea(idUsu, "/datosPrueba/2025Usuarios.csv")[2];
				String tipoReg = buscarLinea(idTipoReg, "/datosPrueba/2025TipoRegistro.csv")[2];
				
				fechaAlta = fechaAlta.split("/")[2] + "-" + fechaAlta.split("/")[1] + "-" + fechaAlta.split("/")[0];
				
				ICE.setFechaSistema(LocalDate.parse(fechaAlta));
				ICE.elegirAsistenteYTipoRegistro(nickAsistente, tipoReg, nombreEdi,false);
				
				
			} } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		System.out.println("Registros cargados");
	}
	
	private static void cargarSeguidores() {
		BufferedReader brSeguidores;
		try {
			brSeguidores = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/datosPrueba/2025SeguidoresSeguidos.csv"));

			IControllerUsuario ICU = Factory.getInstance().getControllerUsuario();
			
			String linea;
			brSeguidores.readLine(); // Saltear la primer linea (headers)
			while ((linea = brSeguidores.readLine()) != null) {
				if (linea.isBlank())
					continue;
				
				String[] campos = linea.split(";");
							
				String ref = campos[0];
				String refSeguidor = campos[1];
				String nicknameSeguidor = campos[2];
				String refSeguido = campos[3];
				String nicknameSeguido = campos[4];
				
				ICU.seguirUsuario(nicknameSeguidor, nicknameSeguido);
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		System.out.println("Seguidores cargados");
	}
	
	//UTILS
	private static String[] buscarLinea(String id, String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(System.getProperty("user.dir") + path));
		br.readLine(); // Saltear la primer linea (headers)
		String linea;
		while ((linea = br.readLine()) != null) {
			if (linea.isBlank())
				continue;
			String[] campos = linea.split(";");
			if (campos[0].equals(id)) {
				br.close();
				return campos;
			}
		}
		br.close();
		System.out.println("No se encontro id " + id + " en " + path);
		return null;
	}
	
	
	// ===== DEBUG / DUMP =====
	public static void imprimirDatosCargados() {
	    System.out.println("\n================= RESUMEN DATOS CARGADOS =================");

	    // Factory y controllers
	    Factory fac = Factory.getInstance();
	    IControllerUsuario ICU = fac.getControllerUsuario();
	    IControllerEvento ICE = fac.getControllerEvento();
	    System.out.println("[Factory] ControllerUsuario = " + ICU.getClass().getName());
	    System.out.println("[Factory] ControllerEvento  = " + ICE.getClass().getName());

	    // Manejadores
	    imprimirInstituciones();
	    imprimirUsuarios();
	    imprimirCategorias();
	    imprimirEventosYEdiciones();

	    System.out.println("==========================================================\n");
	}

	private static void imprimirInstituciones() {
	    ManejadorInstitucion mI = ManejadorInstitucion.getInstance();
	    java.util.Set<String> insts = mI.obtenerInstituciones();
	    System.out.println("\n[Instituciones] total = " + (insts == null ? 0 : insts.size()));
	    if (insts != null) {
	        for (String nombre : insts) {
	            System.out.println("  - " + nombre);
	        }
	    }
	}

	private static void imprimirUsuarios() {
	    ManejadorUsuario mU = ManejadorUsuario.getInstance();
	    java.util.Set<String> nicks = mU.obtenerUsuarios();
	    System.out.println("\n[Usuarios] total = " + (nicks == null ? 0 : nicks.size()));
	    if (nicks != null) {
	        for (String nick : nicks) {
	            Usuario u = mU.obtenerUsuario(nick);
	            if (u == null) continue;
	            String tipo = (u instanceof Organizador) ? "Organizador" :
	                          (u instanceof Asistente)   ? "Asistente"   : u.getClass().getSimpleName();
	            System.out.println("  - " + nick + "  (" + tipo + ")  nombre=" + u.getNombre() + " email=" + u.getEmail());

	            // Si es organizador, mostrar qué ediciones declara organizar
	            if (u instanceof Organizador) {
	                java.util.Set<String> eds = ((Organizador) u).getEdiciones();
	                System.out.println("      edicionesOrganizadas=" + (eds == null ? "[]" : eds));
	            }
	        }
	    }
	}

	private static void imprimirCategorias() {
	    // Si tenés ManejadorCategoria with Set<String> o Map<String,Categoria>
	    ManejadorCategoria mC = ManejadorCategoria.getInstance();
	    java.util.Set<String> nombres = mC.obtenernombresCategorias(); // o adaptá según tu API
	    System.out.println("\n[Categorias] total = " + (nombres == null ? 0 : nombres.size()));
	    if (nombres != null) {
	        for (String n : nombres) {
	            System.out.println("  - " + n);
	        }
	    }
	}

	private static void imprimirEventosYEdiciones() {
	    ManejadorEvento mE = ManejadorEvento.getInstance();
	    java.util.Map<String, Evento> eventos = mE.obtenerEventos();
	    System.out.println("\n[Eventos] total = " + (eventos == null ? 0 : eventos.size()));

	    if (eventos == null) return;

	    // Para resolver organizador por edición (Opción B que estabas usando)
	    ManejadorUsuario mU = ManejadorUsuario.getInstance();

	    for (java.util.Map.Entry<String, Evento> entry : eventos.entrySet()) {
	        Evento ev = entry.getValue();
	        if (ev == null) continue;

	        System.out.println("--------------------------------------------------");
	        System.out.println("Evento: " + ev.getNombre());
	        System.out.println("  fechaAlta: " + ev.getFechaAlta());
	        System.out.println("  descripcion: " + ev.getDescripcion());

	        // Categorías
	        HashSet<String> cats = (HashSet<String>) ev.getCategorias(); // asegurate de tener getter en Evento
	        if (cats != null && !cats.isEmpty()) {
	            System.out.print("  categorias: ");
	            for (int i = 0; i < cats.size(); i++) {
	                System.out.print(cats);
	                if (i < cats.size() - 1) System.out.print(", ");
	            }
	            System.out.println();
	        } else {
	            System.out.println("  categorias: (ninguna)");
	        }

	        // Ediciones
	        Set<String> eds = (Set<String>) ev.getEdiciones(); // agregá getter en Evento si no existe
	        System.out.println("  ediciones (" + (eds == null ? 0 : eds.size()) + "):");
	        if (eds != null) {
	            for (String ed : eds) {
	                if (ed == null) continue;
	                System.out.println(eds);
	            }
	        }
	    }
	}

	// Helper para encontrar organizador por nombre de edición (Opción B)
	private static String buscarOrganizadorDeEdicion(ManejadorUsuario mU, String nombreEdicion) {
	    if (mU == null || nombreEdicion == null) return null;
	    java.util.Set<String> nicks = mU.obtenerUsuarios();
	    if (nicks == null) return null;
	    for (String nick : nicks) {
	        Usuario u = mU.obtenerUsuario(nick);
	        if (u instanceof Organizador) {
	            Organizador org = (Organizador) u;
	            if (org.organizaEdicion(nombreEdicion)) {
	                // devolvés nombre visible + nick
	                return org.getNombre() + " (" + org.getNickname() + ")";
	            }
	        }
	    }
	    return null;
	}


}