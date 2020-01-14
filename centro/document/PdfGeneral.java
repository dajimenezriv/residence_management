package centro.document;

import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;

import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;

public class PdfGeneral extends Word {

	private Document document;
	private PdfPTable table;
	private Font font;
	private double ganancias_totales;

	public PdfGeneral(String mes_total, String year_total) {

		super();

		new File(mes_total + "-" + year_total).mkdirs();

		ganancias_totales = 0;

		try {

			float[] widths = {2, 1, 1, 1, 1};

			document = new Document();
			font = new Font(Font.FontFamily.TIMES_ROMAN, 11);
			table = new PdfPTable(widths);

			PdfWriter.getInstance(document, new FileOutputStream(mes_total + "-" + year_total + "/General.pdf"));

			document.open();
			document.addTitle("Informe de " + mes + "-" + year);
	        document.addSubject("Informe");

	        columns();
            general();
            ganancias_totales();

            document.add(table);
			document.close();
			
		} catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
        	e.printStackTrace();
        }

	}

	public void columns() {

		PdfPCell cell = new PdfPCell(new Phrase("Nombre", font));
		cell.setFixedHeight(30f);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("Base imponible", font));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("Días asistidos", font));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("Días faltados", font));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("Coste total", font));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

	}

	public void general() {

		for (int i = 0; i < array_personas.size(); i++) {

			calcular_coste(i);

			PdfPCell cell = new PdfPCell(new Phrase(nombre, font));
			cell.setFixedHeight(30f);
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase(String.format("%.2f", coste_base).replace(".", ",") + "€", font));
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("" + dias_asistidos, font));
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase("" + dias_faltados, font));
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);
			cell = new PdfPCell(new Phrase(String.format("%.2f", coste_total).replace(".", ",") + "€", font));
			cell.setBorder(Rectangle.NO_BORDER);
			table.addCell(cell);

			ganancias_totales += coste_total;

		}

	}

	public void ganancias_totales() {

		PdfPCell cell = new PdfPCell(new Phrase("Ganancias", font));
		cell.setFixedHeight(30f);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(""));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(""));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(""));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);
		cell = new PdfPCell(new Phrase(String.format("%.2f", ganancias_totales).replace(".", ",") + "€"));
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

	}

}