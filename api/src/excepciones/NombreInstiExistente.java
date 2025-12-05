package excepciones;

public class NombreInstiExistente extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2271276031067310440L;

	public NombreInstiExistente(String mensaje) {
		super(mensaje);
	}
}