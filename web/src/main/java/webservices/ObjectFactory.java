
package webservices;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the webservices package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _EmailRepetido_QNAME = new QName("http://webservices/", "EmailRepetido");
    private final static QName _Exception_QNAME = new QName("http://webservices/", "Exception");
    private final static QName _NombreInstiExistente_QNAME = new QName("http://webservices/", "NombreInstiExistente");
    private final static QName _NombreUsuarioExistente_QNAME = new QName("http://webservices/", "NombreUsuarioExistente");
    private final static QName _UsuarioNoEncontrado_QNAME = new QName("http://webservices/", "UsuarioNoEncontrado");
    private final static QName _HashSetWrapper_QNAME = new QName("http://webservices/", "hashSetWrapper");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: webservices
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link EmailRepetido }
     * 
     * @return
     *     the new instance of {@link EmailRepetido }
     */
    public EmailRepetido createEmailRepetido() {
        return new EmailRepetido();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     * @return
     *     the new instance of {@link Exception }
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link NombreInstiExistente }
     * 
     * @return
     *     the new instance of {@link NombreInstiExistente }
     */
    public NombreInstiExistente createNombreInstiExistente() {
        return new NombreInstiExistente();
    }

    /**
     * Create an instance of {@link NombreUsuarioExistente }
     * 
     * @return
     *     the new instance of {@link NombreUsuarioExistente }
     */
    public NombreUsuarioExistente createNombreUsuarioExistente() {
        return new NombreUsuarioExistente();
    }

    /**
     * Create an instance of {@link UsuarioNoEncontrado }
     * 
     * @return
     *     the new instance of {@link UsuarioNoEncontrado }
     */
    public UsuarioNoEncontrado createUsuarioNoEncontrado() {
        return new UsuarioNoEncontrado();
    }

    /**
     * Create an instance of {@link WrapperHashSet }
     * 
     * @return
     *     the new instance of {@link WrapperHashSet }
     */
    public WrapperHashSet createWrapperHashSet() {
        return new WrapperHashSet();
    }

    /**
     * Create an instance of {@link DataUsuario }
     * 
     * @return
     *     the new instance of {@link DataUsuario }
     */
    public DataUsuario createDataUsuario() {
        return new DataUsuario();
    }

    /**
     * Create an instance of {@link DtOrganizador }
     * 
     * @return
     *     the new instance of {@link DtOrganizador }
     */
    public DtOrganizador createDtOrganizador() {
        return new DtOrganizador();
    }

    /**
     * Create an instance of {@link DtAsistente }
     * 
     * @return
     *     the new instance of {@link DtAsistente }
     */
    public DtAsistente createDtAsistente() {
        return new DtAsistente();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EmailRepetido }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link EmailRepetido }{@code >}
     */
    @XmlElementDecl(namespace = "http://webservices/", name = "EmailRepetido")
    public JAXBElement<EmailRepetido> createEmailRepetido(EmailRepetido value) {
        return new JAXBElement<>(_EmailRepetido_QNAME, EmailRepetido.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}
     */
    @XmlElementDecl(namespace = "http://webservices/", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<>(_Exception_QNAME, Exception.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NombreInstiExistente }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link NombreInstiExistente }{@code >}
     */
    @XmlElementDecl(namespace = "http://webservices/", name = "NombreInstiExistente")
    public JAXBElement<NombreInstiExistente> createNombreInstiExistente(NombreInstiExistente value) {
        return new JAXBElement<>(_NombreInstiExistente_QNAME, NombreInstiExistente.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NombreUsuarioExistente }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link NombreUsuarioExistente }{@code >}
     */
    @XmlElementDecl(namespace = "http://webservices/", name = "NombreUsuarioExistente")
    public JAXBElement<NombreUsuarioExistente> createNombreUsuarioExistente(NombreUsuarioExistente value) {
        return new JAXBElement<>(_NombreUsuarioExistente_QNAME, NombreUsuarioExistente.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UsuarioNoEncontrado }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link UsuarioNoEncontrado }{@code >}
     */
    @XmlElementDecl(namespace = "http://webservices/", name = "UsuarioNoEncontrado")
    public JAXBElement<UsuarioNoEncontrado> createUsuarioNoEncontrado(UsuarioNoEncontrado value) {
        return new JAXBElement<>(_UsuarioNoEncontrado_QNAME, UsuarioNoEncontrado.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WrapperHashSet }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link WrapperHashSet }{@code >}
     */
    @XmlElementDecl(namespace = "http://webservices/", name = "hashSetWrapper")
    public JAXBElement<WrapperHashSet> createHashSetWrapper(WrapperHashSet value) {
        return new JAXBElement<>(_HashSetWrapper_QNAME, WrapperHashSet.class, null, value);
    }

}
