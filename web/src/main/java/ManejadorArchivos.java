import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Part;
import webservices.PublicadorImagenes;
import webservices.PublicadorImagenesService;

public class ManejadorArchivos {
    
    private static PublicadorImagenes getPublicadorImagenes() {
        try {
            PublicadorImagenesService service = new PublicadorImagenesService();
            return service.getPublicadorImagenesPort();
        } catch (Exception e) {
            System.err.println("Error conectando al servidor central: " + e.getMessage());
            return null;
        }
    }

    /**
     * Busca un archivo y devuelve la imagen como data URI completa
     * @param nombreArchivo nombre del archivo sin extensión
     * @param carpeta carpeta donde buscar (usuarios, eventos, ediciones)
     * @return data URI completa lista para usar en HTML o imagen por defecto
     */
    public static String buscarArchivo(String nombreArchivo, String carpeta) {
        try {
            PublicadorImagenes publicador = getPublicadorImagenes();
            if (publicador == null) {
                System.err.println("No se pudo conectar al publicador de imágenes");
                return "uploads/" + carpeta + "/default.jpg";
            }
            
            // Obtener imagen Base64 del servidor central
            String imagenBase64 = publicador.obtenerImagen(nombreArchivo, carpeta);
            
            if (imagenBase64 != null && !imagenBase64.trim().isEmpty()) {
                // Construir data URI completa
                return "data:image/jpeg;base64," + imagenBase64;
            } else {
                System.out.println("Imagen no encontrada: " + nombreArchivo + " en " + carpeta);
                return "uploads/" + carpeta + "/default.jpg"; // Imagen por defecto específica por carpeta
            }
            
        } catch (Exception e) {
            System.err.println("Error buscando archivo " + nombreArchivo + ": " + e.getMessage());
            return "uploads/" + carpeta + "/default.jpg";
        }
    }
    
    public static void guardarArchivo(Part imagen, String nombre, String carpeta, ServletContext context) throws IOException {
        if (imagen != null && imagen.getSize() > 0) {
            try {
                // Convertir Part a Base64
                byte[] imagenBytes;
                try (InputStream input = imagen.getInputStream()) {
                    imagenBytes = input.readAllBytes();
                }
                
                String imagenBase64 = Base64.getEncoder().encodeToString(imagenBytes);
                
                // Llamar al web service del servidor central
                PublicadorImagenes publicador = getPublicadorImagenes();
                if (publicador == null) {
                    throw new IOException("No se pudo conectar al servidor central");
                }
                
                boolean guardado = publicador.guardarImagen(imagenBase64, nombre.toLowerCase(), carpeta);
                
                if (guardado) {
                    System.out.println("Imagen guardada en servidor central: " + nombre + " en " + carpeta);
                } else {
                    throw new IOException("Error guardando imagen en servidor central");
                }
                
            } catch (Exception e) {
                System.err.println("Error guardando archivo: " + e.getMessage());
                throw new IOException("Error guardando imagen: " + e.getMessage(), e);
            }
        }
    }
}