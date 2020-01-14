package centro.document;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;

import java.util.List;
import java.util.HashMap;

import centro.connection.Database;

public abstract class Docs {

	List<HashMap<String, String>> array_personas;

	String nombre;
	double coste_base, coste_total;
	int transporte, dias_asistidos, dias_faltados, dias_mes, porcentaje;

	int year, dia;
	String mes;

	public Docs() {

		array_personas = Database.get_personas();

		String[] meses = new String[] {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
		String date = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.now());

		dia = Integer.parseInt(date.split("/")[0]);
		mes = meses[Integer.parseInt(date.split("/")[1]) - 1];
		year = Integer.parseInt(date.split("/")[2]);


	}

	public void calcular_coste(int i) {

		nombre = array_personas.get(i).get("nombre");
	   	coste_base = Double.parseDouble(array_personas.get(i).get("coste_base"));
	    transporte = Integer.parseInt(array_personas.get(i).get("transporte"));
	    dias_asistidos = Integer.parseInt(array_personas.get(i).get("dias_asistidos"));
	    dias_faltados = Integer.parseInt(array_personas.get(i).get("dias_faltados"));
	    dias_mes = Integer.parseInt(array_personas.get(i).get("dias_mes"));

	    if (transporte == 1) porcentaje = 35;
		else porcentaje = 25;

		double coste_dia = coste_base / dias_mes;

		if (dias_faltados > 4) {

			double resultado = coste_dia * (dias_asistidos + 4) * porcentaje / 100;
			double resultado2 = coste_dia * (dias_faltados - 4) * 0.1333;

			coste_total = resultado + resultado2;

		} else {

			coste_total = coste_dia * (dias_asistidos + dias_faltados) * porcentaje / 100; }

	}

}