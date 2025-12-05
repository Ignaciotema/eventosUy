package excepciones;


public class UsuarioNoEncontrado extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UsuarioNoEncontrado(String mensaje) {
		super(mensaje);
	}
}