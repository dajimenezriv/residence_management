package centro.document;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Image;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;

public class PdfDoc extends Docs {

	private Document document;
	private PdfPTable table;
	private Font font_bold, font_normal;

	private String mes_total, year_total;

	public PdfDoc(String type, String mes_total, String year_total) {

		super();

		this.mes_total = mes_total;
		this.year_total = year_total;

		new File(mes_total + "-" + year_total).mkdirs();

		if (type.equals("individual")) {

			for (int i = 0; i < array_personas.size(); i++) {

				try {

					document = new Document();

					calcular_coste(i);
					PdfWriter.getInstance(document, new FileOutputStream(mes_total + "-" + year_total + "/" + nombre + ".pdf"));
					
					document.open();

					document.addTitle("Informe de " + nombre);
		            document.addSubject("Informe");

		            image();
		            title();
		            table();
		            nota();

					document.close();
				
				} catch (IOException e) {
		            e.printStackTrace();
		        } catch (DocumentException e) {
		        	e.printStackTrace();
		        }

		    }

		} else {

			try {

				document = new Document();
				PdfWriter.getInstance(document, new FileOutputStream(mes_total + "-" + year_total + "/Informe.pdf"));
				document.open();
				document.addTitle("Informe de " + mes + "-" + year);
		        document.addSubject("Informe");

		        for (int i = 0; i < array_personas.size(); i++) {

					calcular_coste(i);

		            image();
		            title();
		            table();
		            nota();

		            document.newPage();

		        }

				document.close();
				
			} catch (IOException e) {
	            e.printStackTrace();
	        } catch (DocumentException e) {
	        	e.printStackTrace();
	        }

		}

	}

	public void image() {

		try {
			Image img = Image.getInstance("Imagen.png");
			img.scaleToFit(document.getPageSize().getWidth() - 100, 1000);
			document.add(img);
		} catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
        	e.printStackTrace();
        }

	}

	public void title() {

    	try {

            font_bold = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
            font_normal = new Font(Font.FontFamily.TIMES_ROMAN, 13);

            document.add(new Paragraph("SERVICIO DE ESTANCIAS DIURNAS CENTRO DE MAYORES \"CUENCA II\"", font_bold));
            document.add(Chunk.NEWLINE);

            Paragraph p = new Paragraph("Correspondientes al mes de       ", font_normal);
            Chunk c = new Chunk(mes_total.toUpperCase() + " de " + year_total, font_bold);
            c.setUnderline(0.1f, -2f);
            p.add(c);
            document.add(p);
			document.add(new Paragraph(""));

			p = new Paragraph("D./Dña.:        ", font_normal);
			c = new Chunk(this.nombre, font_bold);
            p.add(c);
            document.add(p);
			document.add(Chunk.NEWLINE);

			p = new Paragraph("LIQUIDACIÓN DE ESTANCIAS", font_normal);
			p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);
			document.add(Chunk.NEWLINE);

        } catch (DocumentException e) {
        	e.printStackTrace();
        }

    }

    public void table() {

        try {

			float[] widths = {2, 1};
			table = new PdfPTable(widths);

			PdfPCell cell_normal = new PdfPCell(new Phrase("Base imponible:", font_normal));
			cell_normal.setFixedHeight(30f);
			cell_normal.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell_normal);
			PdfPCell cell_bold = new PdfPCell(new Phrase(String.format("%.2f", coste_base).replace(".", ",") + " €", font_bold));
			cell_bold.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell_bold);

			cell_normal = new PdfPCell(new Phrase("Número de estancias posibles en el mes:", font_normal));
			cell_normal.setFixedHeight(30f);
			cell_normal.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell_normal);
			cell_bold = new PdfPCell(new Phrase("" + (dias_asistidos + dias_faltados), font_bold));
			cell_bold.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell_bold);

			cell_normal = new PdfPCell(new Phrase("N.º estancias ordinarias producidas:", font_normal));
			cell_normal.setFixedHeight(30f);
			cell_normal.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell_normal);
			cell_bold = new PdfPCell(new Phrase("" + dias_asistidos, font_bold));
			cell_bold.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell_bold);

			cell_normal = new PdfPCell(new Phrase("Base diaria de estancias (%):", font_normal));
			cell_normal.setFixedHeight(30f);
			cell_normal.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell_normal);
			cell_bold = new PdfPCell(new Phrase(porcentaje + " %", font_bold));
			cell_bold.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell_bold);

			cell_normal = new PdfPCell(new Phrase("Número de días reservas de plaza:", font_normal));
			cell_normal.setFixedHeight(30f);
			cell_normal.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell_normal);

			if (dias_faltados > 4) {
				cell_bold = new PdfPCell(new Phrase("" + (dias_faltados - 4), font_bold));
				cell_bold.setBorder(Rectangle.NO_BORDER);
			} else {
				cell_bold = new PdfPCell(new Phrase(""));
				cell_bold.setBorder(Rectangle.NO_BORDER);
			}
			
			table.addCell(cell_bold);

			cell_normal = new PdfPCell(new Phrase("Base diaria:", font_normal));
			cell_normal.setFixedHeight(30f);
			cell_normal.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell_normal);

			double base_diaria = (coste_base * porcentaje / 100) / dias_mes;

			cell_bold = new PdfPCell(new Phrase(String.format("%.2f", base_diaria).replace(".", ",") + " €", font_bold));
			cell_bold.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell_bold);

			cell_normal = new PdfPCell(new Phrase("Aportación reserva:", font_normal));
			cell_normal.setFixedHeight(30f);
			cell_normal.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell_normal);

			if (dias_faltados > 4) {
				double aportacion_reserva = coste_base / dias_mes * 0.13333 * (dias_faltados - 4);
				cell_bold = new PdfPCell(new Phrase(String.format("%.2f", aportacion_reserva).replace(".", ",") + " €", font_bold));
			} else {
				cell_bold = new PdfPCell(new Phrase("", font_bold));
			}

			cell_bold.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell_bold);

			cell_normal = new PdfPCell(new Phrase("Cálculo del importe total de la liquidación:", font_normal));
			cell_normal.setFixedHeight(30f);
			cell_normal.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell_normal);
			cell_bold = new PdfPCell(new Phrase(String.format("%.2f", coste_total).replace(".", ",") + " €", font_bold));
			cell_bold.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell_bold);

	        document.add(table);
	        document.add(Chunk.NEWLINE);

        } catch (DocumentException e) {
        	e.printStackTrace();
        }

	}

	public void nota() {

		try {

			font_bold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
            font_normal = new Font(Font.FontFamily.TIMES_ROMAN, 12);

            document.add(new Paragraph("NOTA: LOS INGRESOS SE REALIZARÁN A MES VENCIDO, DENTRO DE LOS CINCO PRIMEROS DEL MES SIGUIENTE", font_bold));
            document.add(new Paragraph("N.o DE CUENTA BANCARIA: ES91 2048 3648 5134 0000 5309", font_bold));
            document.add(new Paragraph("Nota* Concepto: SED \"CUENCA II\" " + mes + " de " + year + ", NOMBRE DE USUARIO SED", font_normal));
            document.add(new Paragraph(""));

            font_bold = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
            font_normal = new Font(Font.FontFamily.TIMES_ROMAN, 13);

            Paragraph p = new Paragraph("Cuenca, " + dia + " de " + mes + " de " + year + "\nDIRECTOR DEL CENTRO", font_normal);
			p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);

			p = new Paragraph("Fdo. Roberto Rivera García", font_normal);
			p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);

        } catch (DocumentException e) {
        	e.printStackTrace();
        }

	}

}