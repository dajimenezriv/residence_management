package centro.document;

import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;

public abstract class Word extends Docs {

	private XWPFRun run;

	public void add_text(XWPFParagraph par, int size, boolean bold, boolean underline, String text) {

		run = par.createRun();

		run.setFontSize(size);
		run.setFontFamily("Times New Roman");
		run.setBold(bold);

		if (underline) {
			run.setUnderline(UnderlinePatterns.SINGLE); }

		run.setText(text);

	}

	public void add_text(XWPFParagraph par, String type, int size, boolean bold, boolean underline, String text) {

		run = par.createRun();

		run.setFontSize(size);
		run.setFontFamily(type);
		run.setBold(bold);

		if (underline) {
			run.setUnderline(UnderlinePatterns.SINGLE); }

		run.setText(text);

	}

	public void new_line() {

		run.addBreak();

	}

}