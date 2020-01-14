package centro.interfaz;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JButton;

import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;

import javax.swing.table.DefaultTableModel;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import centro.connection.Database;
import centro.document.Informe;

public class Interfaz extends JFrame {

	private ImageIcon icon;
	private Dimension dimension;

	private JTable table;
	private JScrollPane scroll_pane;
	DefaultTableModel table_model;

	private JButton button;

	List<HashMap<String, String>> array_personas;

	public Interfaz(ImageIcon icon) {

		super();

		/* Funcionalidad */

		this.icon = icon;
		array_personas = Database.get_personas();
		dimension = Toolkit.getDefaultToolkit().getScreenSize();

		/* Tabla */

		table = new JTable();
		table_model = new DefaultTableModel();
		scroll_pane = new JScrollPane(table);
		scroll_pane.setPreferredSize(new Dimension(dimension.width - 200, dimension.height - 300));

		table_model.addColumn("Nombre");
		table_model.addColumn("Fecha de Registro");
		table_model.addColumn("Base Imponible");
		table_model.addColumn("Transporte");
		table_model.addColumn("Días Asistidos");
		table_model.addColumn("Días Faltados");
		table_model.addColumn("Días del Mes");
		table_model.addColumn("Eliminar Persona");

		table.setModel(table_model);
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		table.setSelectionBackground(Color.decode("#FFCCEE"));

		/* Personas */

		for (int i = 0; i < array_personas.size(); i++) {

			table_model.addRow(new Object[] {
				array_personas.get(i).get("nombre"),
				array_personas.get(i).get("fecha_registro"),
				array_personas.get(i).get("coste_base"),
				array_personas.get(i).get("transporte"),
				array_personas.get(i).get("dias_asistidos"),
				array_personas.get(i).get("dias_faltados"),
				array_personas.get(i).get("dias_mes"),
				0});

			table.setRowHeight(i, 30);

		}

		/* Acciones */

		button = new JButton("Nueva Persona");
		button.setPreferredSize(new Dimension((dimension.width - 200) / 3, 40));
		button.setContentAreaFilled(false);

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				int id = Database.new_persona("Nombre", dtf.format(LocalDate.now()), 0, 0, 0, 0, 0);

				HashMap<String, String> hm = new HashMap<String, String>();

		        hm.put("id", Integer.toString(id));
		        hm.put("nombre", "Nombre");
		        hm.put("fecha_registro", dtf.format(LocalDate.now()));
		        hm.put("coste_base", "0");
		        hm.put("transporte", "0");
		        hm.put("dias_asistidos", "0");
		        hm.put("dias_faltados", "0");
		        hm.put("dias_mes", "0");

		        array_personas.add(hm);

		        table_model.addRow(new Object[] {"Nombre", dtf.format(LocalDate.now()), 0, 0, 0, 0, 0, 0});
		        table.setRowHeight(table.getModel().getRowCount() - 1, 30);

			}

		});

		add(button, BorderLayout.WEST);

		button = new JButton("Actualizar Días del Mes");
		button.setPreferredSize(new Dimension((dimension.width - 200) / 3, 40));
		button.setContentAreaFilled(false);

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String value = JOptionPane.showInputDialog(null, "Días del mes");
					if (value != null) {
						int dias_mes = Integer.parseInt(value);
						if (dias_mes >= 0) {
							Database.update_all_dias_mes(dias_mes);
							for (int i = 0; i < array_personas.size(); i++) {
		            			table_model.setValueAt(dias_mes, i, 6);
		            		}

							if (JOptionPane.showConfirmDialog(null, "Resetear días asistidos", "Días asistidos", JOptionPane.YES_NO_OPTION) == 0) {
		            			Database.update_all_dias_asistidos(dias_mes);
		            			Database.update_all_dias_faltados(0);
		            			for (int i = 0; i < array_personas.size(); i++) {
			            			table_model.setValueAt(dias_mes, i, 4);
			            			table_model.setValueAt(0, i, 5);
			            		}
		            		}

						} else {
							JOptionPane.showMessageDialog(null, "Debe ser un número mayor o igual que 0");
						}
					}
				} catch (NumberFormatException error) { JOptionPane.showMessageDialog(null, "Debe ser un número natural"); }
			}
		});

		add(button, BorderLayout.CENTER);

		button = new JButton("Crear Informe");
		button.setPreferredSize(new Dimension((dimension.width - 200) / 3, 40));
		button.setContentAreaFilled(false);

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Informe(icon);
			}
		});

		add(button, BorderLayout.EAST);

		/* Interfaz */

		setSize(dimension.width - 200, dimension.height - 200);
		setLocation((dimension.width / 2) - (dimension.width - 200) / 2, (dimension.height / 2) - (dimension.height - 200) / 2);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("Información");
		setIconImage(icon.getImage());
		add(scroll_pane, BorderLayout.NORTH);
		setVisible(true);

		/* Listener */

		table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
            	if (e.getType() == TableModelEvent.UPDATE) {

            		DefaultTableModel model = (DefaultTableModel) e.getSource();
		            int row = e.getFirstRow();
		            int column = e.getColumn();

		            int id = Integer.parseInt(array_personas.get(row).get("id"));

	            	switch (column) {	            		

	            		/* Nombre */
	            		case 0:
	            			String nombre = model.getValueAt(row, 0).toString();
	            			if (nombre != "") {
	            				Database.update_nombre(id, nombre);
	            			} else {
	            				JOptionPane.showMessageDialog(null, "No debe estar vacío");
	            			}
	            			break;

	            		/* Fecha Registro */
	            		case 1:
	            			String fecha_registro = model.getValueAt(row, 1).toString();
	            			if (fecha_registro != "") {
	            				Database.update_fecha_registro(id, model.getValueAt(row, 1).toString());
	            			} else {
	            				JOptionPane.showMessageDialog(null, "No debe estar vacío");
	            			}
	            			break;

	            		/* Coste Base */
	            		case 2:
	            			try {
	            				double coste_base = Double.parseDouble(model.getValueAt(row, 2).toString());
	            				if (coste_base >= 0) {
	            					Database.update_coste_base(id, coste_base);
	            				} else {
									JOptionPane.showMessageDialog(null, "Debe ser un número mayor o igual que 0");
								}
	            			} catch (NumberFormatException error) { JOptionPane.showMessageDialog(null, "Debe ser un número"); }
	            			break;

	            		/* Transporte */
	            		case 3:
	            			try {
	            				int transporte = Integer.parseInt(model.getValueAt(row, 3).toString());
	            				if (transporte == 0 || transporte == 1) {
	            					Database.update_transporte(id, transporte);
	            				} else {
	            					JOptionPane.showMessageDialog(null, "Debe ser un valor 0 o 1");
	            				}
	            			} catch (NumberFormatException error) { JOptionPane.showMessageDialog(null, "Debe ser un valor 0 o 1"); }
	            			break;

	            		/* Dias Asistidos */
	            		case 4:
	            			try {
	            				int dias_asistidos = Integer.parseInt(model.getValueAt(row, 4).toString());
	            				if (dias_asistidos >= 0) {
	            					Database.update_dias_asistidos(id, dias_asistidos);
	            				} else {
									JOptionPane.showMessageDialog(null, "Debe ser un número mayor o igual que 0");
								}
	            			} catch (NumberFormatException error) { JOptionPane.showMessageDialog(null, "Debe ser un número natural"); }
	            			break;

	            		/* Dias Faltados */
	            		case 5:
	            			try {
	            				int dias_faltados = Integer.parseInt(model.getValueAt(row, 5).toString());
	            				if (dias_faltados >= 0) {
									Database.update_dias_faltados(id, dias_faltados);
								} else {
									JOptionPane.showMessageDialog(null, "Debe ser un número mayor o igual que 0");
								}
	            			} catch (NumberFormatException error) { JOptionPane.showMessageDialog(null, "Debe ser un número natural"); }
	            			break;

	            		/* Dias del Mes */
	            		case 6:
	            			try {
	            				int dias_mes = Integer.parseInt(model.getValueAt(row, 4).toString());
	            				if (dias_mes >= 0) {
									Database.update_dias_mes(id, dias_mes);
								} else {
									JOptionPane.showMessageDialog(null, "Debe ser un número mayor o igual que 0");
								}
	            			} catch (NumberFormatException error) { JOptionPane.showMessageDialog(null, "Debe ser un número natural"); }
		            		break;

		            	/* Eliminar Persona */
	            		case 7:
	            			try {
	            				int eliminar_persona = Integer.parseInt(model.getValueAt(row, 7).toString());
			            		if (eliminar_persona == 1) {
			            			if (JOptionPane.showConfirmDialog(null, "¿Estás seguro?", "Eliminar", JOptionPane.YES_NO_OPTION) == 0) {
				            			Database.delete_persona(Integer.parseInt(array_personas.get(row).get("id")));
				            			array_personas.remove(row);
				            			model.removeRow(row);
				            		} else {
				            			table_model.setValueAt(0, row, 7);
				            		}
				            	} else if (eliminar_persona != 0) {
				            		JOptionPane.showMessageDialog(null, "Debe ser un valor 0 o 1");
									table_model.setValueAt(0, row, 7);
				            	}
			            	} catch (NumberFormatException error) { JOptionPane.showMessageDialog(null, "Debe ser un 1 para eliminar"); }
	            			break;

	           		}

		        }
		    }
        });

	}

}