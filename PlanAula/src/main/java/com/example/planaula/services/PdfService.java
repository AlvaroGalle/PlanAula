/*
package com.example.planaula.services;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.util.Locale;
import java.util.Map;

@Service
public class PdfService {

    private final TemplateEngine templateEngine;

    public PdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] renderFromTemplate(String templateName, Map<String, Object> model) {
        var ctx = new Context(Locale.forLanguageTag("es-ES"));
        ctx.setVariables(model);

        String html = templateEngine.process(templateName, ctx);

        try (var out = new ByteArrayOutputStream()) {
            var builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null); // si usas imágenes/CSS externos, pon baseUrl
            // fuente embebida para acentos/€:
            builder.useFont(
                    () -> new ClassPathResource("fonts/DejaVuSans.ttf").getInputStream(),
                    "DejaVu Sans"
            );
            builder.toStream(out);
            builder.run();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF", e);
        }
    }
}
*/
