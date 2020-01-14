package centro.document;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;

public class WordGeneral extends Word {

	private XWPFDocument document;
	private XWPFTable table;

	private double ganancias_totales;

	public WordGeneral(String mes_total, String year_total) {

		super();

		new File(mes_total + "-" + year_total).mkdirs();

		document = new XWPFDocument();
		table = document.createTable();
		ganancias_totales = 0;
		
		try {

			FileOutputStream gen = new FileOutputStream(mes_total + "-" + year_total + "/General.docx");

			columns();
			general();
			ganancias_totales();

			document.write(gen);
			document.close();
			
		} catch (Exception e) {
			e.printStackTrace(); }

	}

	public void columns() {

		table.setCellMargins(30, 0, 30, 150);
		table.getCTTbl().getTblPr().unsetTblBorders();

		table.getRow(0).createCell();
		table.getRow(0).createCell();
		table.getRow(0).createCell();
		table.getRow(0).createCell();

		add_text(table.getRow(0).getCell(0).getParagraphs().get(0), 13, false, false, "Nombre");
		add_text(table.getRow(0).getCell(1).getParagraphs().get(0), 13, false, false, "Base imponible");
		add_text(table.getRow(0).getCell(2).getParagraphs().get(0), 13, false, false, "Días asistidos");
		add_text(table.getRow(0).getCell(3).getParagraphs().get(0), 13, false, false, "Días faltados");
		add_text(table.getRow(0).getCell(4).getParagraphs().get(0), 13, false, false, "Coste total");

	}

	public void general() {

		for (int i = 0; i < array_personas.size(); i++) {

			try {

				table.createRow();

				calcular_coste(i);

				add_text(table.getRow(i + 1).getCell(0).getParagraphs().get(0), 13, false, false, nombre);
				add_text(table.getRow(i + 1).getCell(1).getParagraphs().get(0), 13, false, false, String.format("%.2f", coste_base).replace(".", ",") + "€");
				add_text(table.getRow(i + 1).getCell(2).getParagraphs().get(0), 13, false, false, "" + dias_asistidos);
				add_text(table.getRow(i + 1).getCell(3).getParagraphs().get(0), 13, false, false, "" + dias_faltados);
				add_text(table.getRow(i + 1).getCell(4).getParagraphs().get(0), 13, false, false, String.format("%.2f", coste_total).replace(".", ",") + "€");

				ganancias_totales += coste_total;

			} catch (Exception error) {
				System.out.println(error.toString()); }

		}

	}

	public void ganancias_totales() {

		try {

			table.createRow();

			add_text(table.getRow(array_personas.size() + 1).getCell(3).getParagraphs().get(0), 13, false, false, "Ganancias: ");
			add_text(table.getRow(array_personas.size() + 1).getCell(4).getParagraphs().get(0), 13, false, false, String.format("%.2f", ganancias_totales).replace(".", ",") + "€");

		} catch (Exception error) {
			System.out.println(error.toString()); }

	}

}