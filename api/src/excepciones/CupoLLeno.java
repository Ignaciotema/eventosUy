package excepciones;


public class CupoLLeno extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7792510568167373899L;

	public CupoLLeno(String mensaje) {
		super(mensaje);
	}
}