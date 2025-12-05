package excepciones;

public class EventoFinalizadoExcepcion extends Exception {

		public static final long serialVersionUID = 1L;
		
		public EventoFinalizadoExcepcion(String mensaje) {
			super(mensaje);
		}
}
