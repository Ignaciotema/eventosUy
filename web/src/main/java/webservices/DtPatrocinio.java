
package webservices;

import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para dtPatrocinio complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>{@code
 * <complexType name="dtPatrocinio">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="institucion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="fecha" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         <element name="monto" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *         <element name="codigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="nivelPatrocinio" type="{http://webservices/}nivelPatrocinio" minOccurs="0"/>
 *         <element name="tipoRegistroGratis" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="cantRegsGratis" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dtPatrocinio", propOrder = {
    "institucion",
    "fecha",
    "monto",
    "codigo",
    "nivelPatrocinio",
    "tipoRegistroGratis",
    "cantRegsGratis"
})
public class DtPatrocinio {

    protected String institucion;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar fecha;
    protected float monto;
    protected String codigo;
    @XmlSchemaType(name = "string")
    protected NivelPatrocinio nivelPatrocinio;
    protected String tipoRegistroGratis;
    protected int cantRegsGratis;

    /**
     * Obtiene el valor de la propiedad institucion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstitucion() {
        return institucion;
    }

    /**
     * Define el valor de la propiedad institucion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstitucion(String value) {
        this.institucion = value;
    }

    /**
     * Obtiene el valor de la propiedad fecha.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFecha() {
        return fecha;
    }

    /**
     * Define el valor de la propiedad fecha.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFecha(XMLGregorianCalendar value) {
        this.fecha = value;
    }

    /**
     * Obtiene el valor de la propiedad monto.
     * 
     */
    public float getMonto() {
        return monto;
    }

    /**
     * Define el valor de la propiedad monto.
     * 
     */
    public void setMonto(float value) {
        this.monto = value;
    }

    /**
     * Obtiene el valor de la propiedad codigo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Define el valor de la propiedad codigo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigo(String value) {
        this.codigo = value;
    }

    /**
     * Obtiene el valor de la propiedad nivelPatrocinio.
     * 
     * @return
     *     possible object is
     *     {@link NivelPatrocinio }
     *     
     */
    public NivelPatrocinio getNivelPatrocinio() {
        return nivelPatrocinio;
    }

    /**
     * Define el valor de la propiedad nivelPatrocinio.
     * 
     * @param value
     *     allowed object is
     *     {@link NivelPatrocinio }
     *     
     */
    public void setNivelPatrocinio(NivelPatrocinio value) {
        this.nivelPatrocinio = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoRegistroGratis.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoRegistroGratis() {
        return tipoRegistroGratis;
    }

    /**
     * Define el valor de la propiedad tipoRegistroGratis.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoRegistroGratis(String value) {
        this.tipoRegistroGratis = value;
    }

    /**
     * Obtiene el valor de la propiedad cantRegsGratis.
     * 
     */
    public int getCantRegsGratis() {
        return cantRegsGratis;
    }

    /**
     * Define el valor de la propiedad cantRegsGratis.
     * 
     */
    public void setCantRegsGratis(int value) {
        this.cantRegsGratis = value;
    }

}
