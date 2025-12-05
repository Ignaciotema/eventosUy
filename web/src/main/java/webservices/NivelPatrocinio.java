
package webservices;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para nivelPatrocinio.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <pre>{@code
 * <simpleType name="nivelPatrocinio">
 *   <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     <enumeration value="Platino"/>
 *     <enumeration value="Oro"/>
 *     <enumeration value="Plata"/>
 *     <enumeration value="Bronce"/>
 *   </restriction>
 * </simpleType>
 * }</pre>
 * 
 */
@XmlType(name = "nivelPatrocinio")
@XmlEnum
public enum NivelPatrocinio {

    @XmlEnumValue("Platino")
    PLATINO("Platino"),
    @XmlEnumValue("Oro")
    ORO("Oro"),
    @XmlEnumValue("Plata")
    PLATA("Plata"),
    @XmlEnumValue("Bronce")
    BRONCE("Bronce");
    private final String value;

    NivelPatrocinio(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NivelPatrocinio fromValue(String v) {
        for (NivelPatrocinio c: NivelPatrocinio.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
