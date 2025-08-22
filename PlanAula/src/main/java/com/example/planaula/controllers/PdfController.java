package com.example.planaula.controllers;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;

@RestController
public class PdfController {

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> generarPdf() {
        try (PDDocument doc = new PDDocument();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PDPage pagina = new PDPage();
            doc.addPage(pagina);

            PDPageContentStream contenido = new PDPageContentStream(doc, pagina);
            contenido.beginText();
            contenido.setFont(PDType1Font.HELVETICA_BOLD, 16);
            contenido.newLineAtOffset(100, 700);
            contenido.showText("Hola, mundo con PDFBox y Spring Boot!");
            contenido.endText();
            contenido.close();

            doc.save(baos);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ejemplo.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(baos.toByteArray());

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(("Error al generar PDF: " + e.getMessage()).getBytes());
        }
    }
}
