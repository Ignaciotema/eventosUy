package excepciones;

public class AsistenteYaRegistrado extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AsistenteYaRegistrado(String mensaje) {
		super(mensaje);
	}
}