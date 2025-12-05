package webservices;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Properties;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.ws.Endpoint;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class publicadorImagenes {
    private Endpoint endpoint = null;
    private String uploadsPath;
    
    //Constructor
    public publicadorImagenes(){
        // Inicializar la ruta de uploads
        this.uploadsPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "webservices" + File.separator + "uploads";
        // Crear directorios si no existen
        crearDirectoriosUpload();
    }
    
    @WebMethod(exclude = true)
    private void crearDirectoriosUpload() {
        String[] carpetas = {"usuarios", "eventos", "ediciones"};
        for (String carpeta : carpetas) {
            File dir = new File(uploadsPath + File.separator + carpeta);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }

    @WebMethod(exclude = true)
    public void publicar(){
        Properties props = new Properties();
        FileInputStream fis;
        try {
            // Buscar application.properties en el home del usuario (según Sección 7.9)
            String userHome = System.getProperty("user.home");
            String configPath = userHome + "/application.properties";
            fis = new FileInputStream(configPath);
            props.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        endpoint = Endpoint.publish(props.getProperty("server.url") + ":" + props.getProperty("server.port") + "/publicadorImagenes", this);
    }
    
    @WebMethod(exclude = true)
    public Endpoint getEndpoint() {
        return endpoint;
    }
    
    /**
     * Guarda una imagen en Base64 en el servidor
     * @param imagenBase64 La imagen codificada en Base64
     * @param nombreArchivo El nombre del archivo (sin extensión)
     * @param carpeta La carpeta donde guardar (usuarios, eventos, ediciones)
     * @return true si se guardó correctamente, false en caso contrario
     */
    @WebMethod
    public boolean guardarImagen(String imagenBase64, String nombreArchivo, String carpeta) {
        try {
            // Validar carpeta
            if (!validarCarpeta(carpeta)) {
                return false;
            }
            
            // Decodificar la imagen Base64
            byte[] imagenBytes = Base64.getDecoder().decode(imagenBase64);
            
            // Crear la ruta completa del archivo
            String rutaCompleta = uploadsPath + File.separator + carpeta + File.separator + nombreArchivo.toLowerCase() + ".jpg";
            File archivoDestino = new File(rutaCompleta);
            
            // Guardar la imagen
            try (FileOutputStream fos = new FileOutputStream(archivoDestino)) {
                fos.write(imagenBytes);
                fos.flush();
            }
            
            System.out.println("Imagen guardada en: " + rutaCompleta);
            return true;
            
        } catch (Exception e) {
            System.err.println("Error al guardar imagen: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Obtiene una imagen del servidor y la devuelve en Base64
     * @param nombreArchivo El nombre del archivo a buscar (sin extensión)
     * @param carpeta La carpeta donde buscar (usuarios, eventos, ediciones)
     * @return La imagen en formato Base64, o null si no se encuentra
     */
    @WebMethod
    public String obtenerImagen(String nombreArchivo, String carpeta) {
        try {
            // Validar carpeta
            if (!validarCarpeta(carpeta)) {
                return null;
            }
            
            // Buscar el archivo con diferentes extensiones
            String archivoEncontrado = buscarArchivo(nombreArchivo, carpeta);
            if (archivoEncontrado == null) {
                archivoEncontrado = "default.jpg"; // Imagen por defecto
            }
            
            // Leer el archivo
            String rutaCompleta = uploadsPath + File.separator + carpeta + File.separator + archivoEncontrado;
            File archivo = new File(rutaCompleta);
            
            if (!archivo.exists() || !archivo.isFile()) {
                return null;
            }
            
            // Convertir a Base64
            byte[] imagenBytes = Files.readAllBytes(archivo.toPath());
            String imagenBase64 = Base64.getEncoder().encodeToString(imagenBytes);
            
            System.out.println("Imagen obtenida: " + rutaCompleta);
            return imagenBase64;
            
        } catch (Exception e) {
            System.err.println("Error al obtener imagen: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Verifica si existe una imagen en el servidor
     * @param nombreArchivo El nombre del archivo a buscar (sin extensión)
     * @param carpeta La carpeta donde buscar
     * @return true si existe, false en caso contrario
     */
    @WebMethod
    public boolean existeImagen(String nombreArchivo, String carpeta) {
        if (!validarCarpeta(carpeta)) {
            return false;
        }
        
        String archivoEncontrado = buscarArchivo(nombreArchivo, carpeta);
        return archivoEncontrado != null;
    }
    
    /**
     * Elimina una imagen del servidor
     * @param nombreArchivo El nombre del archivo a eliminar (sin extensión)
     * @param carpeta La carpeta donde buscar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    @WebMethod
    public boolean eliminarImagen(String nombreArchivo, String carpeta) {
        try {
            if (!validarCarpeta(carpeta)) {
                return false;
            }
            
            String archivoEncontrado = buscarArchivo(nombreArchivo, carpeta);
            if (archivoEncontrado == null) {
                return false;
            }
            
            String rutaCompleta = uploadsPath + File.separator + carpeta + File.separator + archivoEncontrado;
            File archivo = new File(rutaCompleta);
            
            if (archivo.exists() && archivo.delete()) {
                System.out.println("Imagen eliminada: " + rutaCompleta);
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            System.err.println("Error al eliminar imagen: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @WebMethod(exclude = true)
    private boolean validarCarpeta(String carpeta) {
        return carpeta != null && 
               (carpeta.equals("usuarios") || carpeta.equals("eventos") || carpeta.equals("ediciones"));
    }
    
    @WebMethod(exclude = true)
    private String buscarArchivo(String nombreArchivo, String carpeta) {
        File dir = new File(uploadsPath + File.separator + carpeta);
        if (!dir.exists() || !dir.isDirectory()) {
            return null;
        }

        for (File f : dir.listFiles()) {
            String baseName = f.getName();
            int dot = baseName.lastIndexOf(".");
            if (dot > 0) {
                baseName = baseName.substring(0, dot); // quita extensión
                if (baseName.equalsIgnoreCase(nombreArchivo)) {
                    return f.getName(); // devuelve el nombre con extensión
                }
            }
        }
        return null; // no encontrado
    }
}