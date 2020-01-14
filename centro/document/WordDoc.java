package centro.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.util.Units;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;

public class WordDoc extends Word {

	private XWPFDocument document;
	private FileOutputStream gen;
	private XWPFRun run;

	private String mes_total, year_total;

	public WordDoc(String type, String mes_total, String year_total) {

		super();

		this.mes_total = mes_total;
		this.year_total = year_total;

		new File(mes_total + "-" + year_total).mkdirs();
		
		if (type.equals("individual")) {

			for (int i = 0; i < array_personas.size(); i++) {

				try {

					calcular_coste(i);
					document = new XWPFDocument();
					gen = new FileOutputStream(mes_total + "-" + year_total + "/" + nombre + ".docx");

					header();
					word(i);

					document.write(gen);
					document.close();

				} catch (Exception e) {
					e.printStackTrace(); }

			}
				
		} else {

			try {

				document = new XWPFDocument();
				gen = new FileOutputStream(mes_total + "-" + year_total + "/Informe.docx");

				header();

				for (int i = 0; i < array_personas.size(); i++) {

					calcular_coste(i);
					word(i);

				}

				document.write(gen);
				document.close();
				
			} catch (Exception e) {
				e.printStackTrace(); }

		}		

	}
	
	public void header() {
		
		try {

			/* Imagen */

			XWPFHeaderFooterPolicy policy = document.createHeaderFooterPolicy();
			XWPFHeader header = policy.createHeader(policy.DEFAULT);
			XWPFParagraph image = header.getListParagraph().get(0);

			image.setAlignment(ParagraphAlignment.LEFT);

			this.run = image.createRun();

			this.run.addPicture(new FileInputStream("Imagen.png"), XWPFDocument.PICTURE_TYPE_PNG, "Imagen.png", Units.toEMU(500), Units.toEMU(70));

		} catch (Exception error) {
			System.out.println(error.toString()); }

	}

	public void word(int i) {

		try {

			/* Titulo */
			
			XWPFParagraph title = document.createParagraph();
			
			title.setPageBreak(true);
			title.setAlignment(ParagraphAlignment.LEFT);
			
			add_text(title, "Calibri", 13, true, false, "SERVICIO DE ESTANCIAS DIURNAS CENTRO DE MAYORES \"CUENCA II\"");
			new_line();
			new_line();

			XWPFParagraph temp = document.createParagraph();

			temp.setAlignment(ParagraphAlignment.CENTER);
			add_text(temp, 13, false, true, "LIQUIDACIÓN DE ESTANCIAS");
			new_line();

			add_text(title, 14, false, false, "Correspondientes al mes de        ");
			add_text(title, 13, true, true, mes_total.toUpperCase() + " de " + year_total);
			new_line();

			add_text(title, 14, false, false, "D./Dña.:           ");
			add_text(title, 13, true, false, array_personas.get(i).get("nombre").toUpperCase());
		
			/* Table */

			XWPFTable table = document.createTable();

			table.setCellMargins(50, 0, 50, 300);
			table.getCTTbl().getTblPr().unsetTblBorders();

			add_text(table.getRow(0).getCell(0).getParagraphs().get(0), 13, false, false, "Base imponible:");

			table.getRow(0).createCell();
			add_text(table.getRow(0).getCell(1).getParagraphs().get(0), 13, true, false, String.format("%.2f", coste_base).replace(".", ",") + " €");

			table.createRow();
			add_text(table.getRow(1).getCell(0).getParagraphs().get(0), 13, false, false, "Número de estancias posibles en el mes:");	
			add_text(table.getRow(1).getCell(1).getParagraphs().get(0), 13, true, false, "" + (dias_asistidos + dias_faltados));
			
			table.createRow();
			add_text(table.getRow(2).getCell(0).getParagraphs().get(0), 13, false, false, "N.º estancias ordinarias producidas:");
			add_text(table.getRow(2).getCell(1).getParagraphs().get(0), 13, true, false, "" + dias_asistidos);
			
			table.createRow();
			add_text(table.getRow(3).getCell(0).getParagraphs().get(0), 13, false, false, "Base diaria de estancias (%):");
			add_text(table.getRow(3).getCell(1).getParagraphs().get(0), 13, true, false, porcentaje + " %");

			table.createRow();
			add_text(table.getRow(4).getCell(0).getParagraphs().get(0), 13, false, false, "Número de días reservas de plaza:");

			if (dias_faltados > 4) {
				add_text(table.getRow(4).getCell(1).getParagraphs().get(0), 13, true, false, "" + (dias_faltados - 4)); }

			table.createRow();
			add_text(table.getRow(5).getCell(0).getParagraphs().get(0), 13, false, false, "Base diaria:");

			double base_diaria = (coste_base * porcentaje / 100) / dias_mes;

			add_text(table.getRow(5).getCell(1).getParagraphs().get(0), 13, true, false, String.format("%.2f", base_diaria).replace(".", ",") + " €");

			table.createRow();
			add_text(table.getRow(6).getCell(0).getParagraphs().get(0), 13, false, false, "Aportación reserva:");

			if (dias_faltados > 4) {
				double aportacion_reserva = coste_base / dias_mes * 0.13333 * (dias_faltados - 4);
				add_text(table.getRow(6).getCell(1).getParagraphs().get(0), 13, true, false, String.format("%.2f", aportacion_reserva).replace(".", ",") + " €"); }

			table.createRow();
			add_text(table.getRow(7).getCell(0).getParagraphs().get(0), 13, false, false, "Cálculo del importe total de la liquidación:");
			add_text(table.getRow(7).getCell(1).getParagraphs().get(0), 13, true, false, String.format("%.2f", coste_total).replace(".", ",") + " €");

			new_line();

			/* Text */

			XWPFParagraph text = document.createParagraph();
			
			text.setAlignment(ParagraphAlignment.LEFT);

			add_text(text, 12, true, false, "NOTA: LOS INGRESOS SE REALIZARÁN A MES VENCIDO, DENTRO DE LOS CINCO PRIMEROS DEL MES SIGUIENTE.");
			new_line();
			add_text(text, 12, true, false, "N.º DE CUENTA BANCARIA: ES91 2048 3648 5134 0000 5309");
			new_line();
			add_text(text, 12, false, false, "Nota* Concepto: SED \"CUENCA II\" " + mes + " de " + year + ", NOMBRE DE USUARIO SED");

			/* Final */

			XWPFParagraph fin = document.createParagraph();

			fin.setAlignment(ParagraphAlignment.CENTER);

			add_text(fin, 13, false, false, "Cuenca, " + dia + " de " + mes + " de " + year);
			new_line();
			add_text(fin, 13, false, false, "DIRECTOR DEL CENTRO");
			new_line();
			new_line();
			new_line();
			new_line();
			new_line();
			add_text(fin, 13, false, false, "Fdo. Roberto Rivera García");

		} catch (Exception error) {
			System.out.println(error.toString()); }
		
	}

}