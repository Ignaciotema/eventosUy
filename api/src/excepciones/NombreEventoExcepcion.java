package excepciones;


public class NombreEventoExcepcion extends Exception {
		private static final long serialVersionUID = 1L;

		public NombreEventoExcepcion(String mensaje) {
		super(mensaje);
	}
}