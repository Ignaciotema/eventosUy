package excepciones;


public class NombreUsuarioExistente extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NombreUsuarioExistente(String mensaje) {
		super(mensaje);
	}
}