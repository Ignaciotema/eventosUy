
import java.nio.file.Files;
import java.nio.file.Path;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class InicializadorServidor implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
    	System.out.println("Servidor iniciado — cargando recursos...");}
	

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Servidor detenido — liberando recursos..."); 
    }
}
