package centro.document;

import java.awt.Dimension;
import java.awt.Toolkit;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import javax.swing.border.EmptyBorder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import centro.document.WordGeneral;
import centro.document.WordDoc;
import centro.document.PdfDoc;

public class Informe extends JFrame {

	private Dimension dimension;
	private JPanel box_panel;
	private JTextField text_mes, text_year;
	private JButton button;

	public Informe(ImageIcon icon) {

		interfaz(icon);

	}

	public void interfaz(ImageIcon icon) {

		dimension = Toolkit.getDefaultToolkit().getScreenSize();

		/* Paneles */

		box_panel = new javax.swing.JPanel();
		box_panel.setLayout(new BoxLayout(box_panel, BoxLayout.PAGE_AXIS));
		box_panel.setBorder(new EmptyBorder(10, 10, 10, 10));

		/* Otros Elementos */

		String date = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.now());
		
		int mes = Integer.parseInt(date.split("/")[1]);

		String[] meses = new String[] {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

		text_mes = new JTextField(meses[mes - 1]);
		text_mes.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

		text_year = new JTextField(date.split("/")[2]);
		text_year.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

		box_panel.add(text_mes);
		box_panel.add(text_year);

		/* Word */

		button = new JButton("Word Individual");
		button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				iniciar("word individual");
			}
		});

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		panel.add(button);

		box_panel.add(panel);

		button = new JButton("Word Total");
		button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				iniciar("word total");
			}
		});

		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		panel.add(button);

		box_panel.add(panel);

		/* Pdf */

		button = new JButton("Pdf Individual");
		button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				iniciar("pdf individual");
			}
		});

		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		panel.add(button);

		box_panel.add(panel);

		button = new JButton("Pdf Total");
		button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				iniciar("pdf total");
			}
		});

		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
		panel.add(button);

		box_panel.add(panel);

		/* Frame */

		add(box_panel);
		setIconImage(icon.getImage());
		setBounds(dimension.width / 2 - 200, dimension.height / 2 - 200, 400, 400);
		setResizable(false);
		setTitle("Informe");
		setAlwaysOnTop(true);
		requestFocus();
		setVisible(true);

	}

	public void iniciar(String document) {

		dispose();

		try {

			String mes = text_mes.getText();
			String year = text_year.getText();

			switch (document) {

				case "word individual":
					new WordDoc("individual", mes, year);
					new WordGeneral(mes, year);
					break;

				case "pdf individual":
					new PdfDoc("individual", mes, year);
					new PdfGeneral(mes, year);
					break;

				case "word total":
					new WordDoc("total", mes, year);
					new WordGeneral(mes, year);
					break;

				case "pdf total":
					new PdfDoc("total", mes, year);
					new PdfGeneral(mes, year);
					break;

			}

			JOptionPane.showMessageDialog(null, "Informes Realizados");

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Completa todo correctamente"); }

	}

}
