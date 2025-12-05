
package webservices;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para estadoEdicion.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <pre>{@code
 * <simpleType name="estadoEdicion">
 *   <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     <enumeration value="Ingresada"/>
 *     <enumeration value="Confirmada"/>
 *     <enumeration value="Rechazada"/>
 *   </restriction>
 * </simpleType>
 * }</pre>
 * 
 */
@XmlType(name = "estadoEdicion")
@XmlEnum
public enum EstadoEdicion {

    @XmlEnumValue("Ingresada")
    INGRESADA("Ingresada"),
    @XmlEnumValue("Confirmada")
    CONFIRMADA("Confirmada"),
    @XmlEnumValue("Rechazada")
    RECHAZADA("Rechazada");
    private final String value;

    EstadoEdicion(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EstadoEdicion fromValue(String v) {
        for (EstadoEdicion c: EstadoEdicion.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
