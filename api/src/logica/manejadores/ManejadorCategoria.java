package logica.manejadores;
import java.util.HashSet;
import java.util.Set;

import logica.models.Categoria;

public class ManejadorCategoria {
 	private static ManejadorCategoria instance = null;
 	private Set<Categoria> categorias;
 	
 	private ManejadorCategoria() {
 		categorias = new HashSet<Categoria>();
 	}
	
	public static ManejadorCategoria getInstance() {
		if (instance == null) {
			instance = new ManejadorCategoria();
		}
		return instance;
	}
	
	public Set<Categoria> getCategorias() {
		return categorias;
	}
	
	public void agregarCategoria(Categoria cat) {
		categorias.add(cat);
	}

	public Categoria obtenerCategoria(String cat) {
		for (Categoria c : categorias) {
			if (c.getNombre().equals(cat)) {
				return c;
			}
		}
		return null;
	}


   public Set<String> obtenernombresCategorias() {
	   Set<String> nombres = new HashSet<>();
	   for (Categoria c : categorias) {
		   nombres.add(c.getNombre());
	   }
	   return nombres;
   }
}
   
