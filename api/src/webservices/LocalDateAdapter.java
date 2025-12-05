package webservices;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adaptador para convertir entre String y java.time.LocalDate
 * para el correcto funcionamiento con JAX-WS
 */
public class LocalDateAdapter extends XmlAdapter<String, java.time.LocalDate> {

    @Override
    public java.time.LocalDate unmarshal(String dateString) throws Exception {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        try {
            return java.time.LocalDate.parse(dateString);
        } catch (Exception e) {
            // Log the error and return null instead of throwing exception
            System.err.println("Error parsing date: " + dateString + " - " + e.getMessage());
            return null;
        }
    }

    @Override
    public String marshal(java.time.LocalDate localDate) throws Exception {
        if (localDate == null) {
            return "";  // Return empty string instead of null
        }
        try {
            return localDate.toString();
        } catch (Exception e) {
            // Log the error and return empty string instead of throwing exception
            System.err.println("Error marshalling date: " + localDate + " - " + e.getMessage());
            return "";
        }
    }
}