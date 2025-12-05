import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;

import webservices.DtAsistente;
import webservices.DtDetalleEdicion;
import webservices.DtRegistro;
import webservices.PublicadorEvento;
import webservices.PublicadorEventoService;
import webservices.PublicadorUsuario;
import webservices.PublicadorUsuarioService;

@WebServlet("/descargar-asistencia")
public class ServletDescargarAsistencia extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ServletDescargarAsistencia() { super(); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        final String usuario = req.getParameter("usuario");
        final String edicion = req.getParameter("edicion");

        if (usuario == null || usuario.isBlank() || edicion == null || edicion.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Faltan parámetros 'usuario' y/o 'edicion'.");
            return;
        }

        // webservices
        PublicadorEventoService serviceEvento = new PublicadorEventoService();
        PublicadorEvento portEvento = serviceEvento.getPublicadorEventoPort();

        PublicadorUsuarioService serviceUsuario = new PublicadorUsuarioService();
        PublicadorUsuario portUsuario = serviceUsuario.getPublicadorUsuarioPort();

        try {
            // registro y validación de asistencia
            DtRegistro reg = portEvento.infoRegistro(edicion, usuario);
            if (reg == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Registro no encontrado.");
                return;
            }
            if (!reg.isAsistencia()) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "La asistencia no está confirmada.");
                return;
            }

            // más datos
            DtDetalleEdicion dtEdi = portEvento.mostrarDetallesEdicion(edicion);
            String nombreEvento = nullSafe(portEvento.nomEvPorEd(edicion), "Evento");

            // nombre y apellido del asistente
            String nombreAsis = usuario;
            String apellidoAsis = "";
            try {
                DtAsistente dtAsis = portUsuario.infoAsistente(usuario);
                if (dtAsis != null) {
                    if (dtAsis.getNombre() != null) nombreAsis = dtAsis.getNombre();
                    if (dtAsis.getApellido() != null) apellidoAsis = dtAsis.getApellido();
                }
            } catch (Exception ignore) {
                
            }

            String ciudad = (dtEdi != null && dtEdi.getCiudad() != null) ? dtEdi.getCiudad() : "";
            String nombreEdicion = (reg.getNombreEdicion() != null) ? reg.getNombreEdicion() : edicion;
            LocalDate hoy = LocalDate.now();

            
            String filename = String.format("Certificado_%s_%s.pdf",
                    safe(nombreEdicion), safe(usuario));
            resp.setContentType("application/pdf");
            resp.setCharacterEncoding("UTF-8");
            resp.setHeader("Content-Disposition",
                    "attachment; filename*=UTF-8''" + URLEncoder.encode(filename, StandardCharsets.UTF_8));

            // generación pdf con itext
            PdfWriter writer = null;
            PdfDocument pdf = null;
            Document doc = null;
            try {
                writer = new PdfWriter(resp.getOutputStream());
                pdf = new PdfDocument(writer);
                doc = new Document(pdf);

                doc.add(new Paragraph("Certificado de Asistencia")
                        .setBold()
                        .setFontSize(20)
                        .setTextAlignment(TextAlignment.CENTER));
                doc.add(new Paragraph(" "));
                // cuerpo del pdf
                String cuerpo = String.format(
                    "Se certifica que %s %s (usuario: %s) ha asistido a la Edición \"%s\" del evento \"%s\".",
                    nullToEmpty(nombreAsis), nullToEmpty(apellidoAsis), usuario,
                    nullToEmpty(nombreEdicion), nullToEmpty(nombreEvento)
                );
                doc.add(new Paragraph(cuerpo)
                        .setTextAlignment(TextAlignment.JUSTIFIED)
                        .setFontSize(12));

                if (!ciudad.isBlank()) {
                    doc.add(new Paragraph("Ciudad: " + ciudad).setFontSize(12));
                }

                if (dtEdi != null && dtEdi.getFechaInicio() != null && dtEdi.getFechaFin() != null) {
                    doc.add(new Paragraph("Período de la edición: " +
                            dtEdi.getFechaInicio() + " a " + dtEdi.getFechaFin())
                            .setFontSize(12));
                }

                doc.add(new Paragraph("Fecha de emisión: " + hoy).setFontSize(12));
                doc.add(new Paragraph(" ").setFontSize(6));

                doc.add(new Paragraph("_____________________________")
                        .setTextAlignment(TextAlignment.LEFT));
                doc.add(new Paragraph("Organización de " + nullToEmpty(nombreEvento)).setFontSize(10));
                doc.add(new Paragraph("Documento generado automáticamente por Eventos.uy")
                        .setFontSize(9).setItalic());
            } catch (Exception e) {
                throw new ServletException("Error generando PDF", e);
            } finally {
                if (doc != null) {
                    doc.close();
                } else if (pdf != null) {
                    pdf.close();
                } else if (writer != null) {
                    writer.close();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            if (!resp.isCommitted()) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generando el PDF.");
            }
        }
    }

    private static String safe(String s) {
        return (s == null) ? "" : s.replaceAll("[^\\p{L}\\p{Nd}_-]", "_");
    }
    private static String nullToEmpty(String s) {
        return (s == null) ? "" : s;
    }
    private static String nullSafe(String s, String fallback) {
        return (s == null || s.isBlank()) ? fallback : s;
    }
}
