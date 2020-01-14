package centro.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

public class Database {

	private static Connection connection;

	public static void init() {

		connection = null;

		try {

			/* Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/" + "trabajo", "root", ""); */

			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			connection = java.sql.DriverManager.getConnection("jdbc:ucanaccess://bbdd/trabajo.accdb");

		} catch (Exception error) {
			JOptionPane.showMessageDialog(null, error.toString()); }

	}

	public static void close() {

		try {

			if (connection != null) {
				connection.close(); }

		} catch (SQLException error) { }

	}

	public static List<HashMap<String, String>> get_personas() {

		Database.init();
		List<HashMap<String, String>> array_personas = new ArrayList<HashMap<String, String>>();

		try {

			String sql = "SELECT * FROM personas";
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet result = statement.executeQuery();

			while (result.next()) {

				try {

			        HashMap<String, String> hm = new HashMap<String, String>();

			        hm.put("id", Integer.toString(result.getInt("id")));
			        hm.put("nombre", result.getString("nombre"));
			        hm.put("fecha_registro", result.getString("fecha_registro"));
			        hm.put("coste_base", Double.toString(result.getDouble("coste_base")));
			        hm.put("transporte", Integer.toString(result.getInt("transporte")));
			        hm.put("dias_asistidos", Integer.toString(result.getInt("dias_asistidos")));
			        hm.put("dias_faltados", Integer.toString(result.getInt("dias_faltados")));
			        hm.put("dias_mes", Integer.toString(result.getInt("dias_mes")));

			        array_personas.add(hm);

				} catch (NumberFormatException error) {
					System.out.println(error.toString()); }

			}

		} catch (SQLException error) {
			System.out.println(error.toString()); }

		Database.close();

		return array_personas;

	}

	public static int new_persona(String nombre, String fecha_registro, double coste_base, int transporte, int dias_asistidos, int dias_faltados, int dias_mes) {

		Database.init();

		int id = -1;

		try {

			String sql = "INSERT INTO personas (nombre, fecha_registro, coste_base, transporte, dias_asistidos, dias_faltados, dias_mes) VALUES (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			statement.setString(1, nombre);
			statement.setString(2, fecha_registro);
			statement.setDouble(3, coste_base);		
			statement.setInt(4, transporte);
			statement.setInt(5, dias_asistidos);
			statement.setInt(6, dias_faltados);
			statement.setInt(7, dias_mes);

			statement.executeUpdate();

			ResultSet keys = statement.getGeneratedKeys();
			keys.next();
			id = keys.getInt(1);

			keys.close();
			statement.close();

		} catch (SQLException error) {
			System.out.println(error.toString()); }

		Database.close();

		return id;

	}

	public static int delete_persona(int id) {

		Database.init();

		int count = 0;

		try {

			String sql = "DELETE FROM personas WHERE id = ?";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, id);

			count = statement.executeUpdate();
			statement.close();

		} catch (SQLException error) {
			System.out.println(error.toString()); }

		Database.close();

		return count;

	}

	public static int update_persona(int id, String nombre, String fecha_registro, double coste_base, int transporte, int dias_asistidos, int dias_faltados) {

		Database.init();

		int count = 0;

		try {

			String sql = "UPDATE personas SET nombre = ?, fecha_registro = ?, coste_base = ?, transporte = ?, dias_asistidos = ?, dias_faltados = ? WHERE id = ?";

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, nombre);
			statement.setString(2, fecha_registro);
			statement.setDouble(3, coste_base);
			statement.setInt(4, transporte);
			statement.setInt(5, dias_asistidos);
			statement.setInt(6, dias_faltados);
			statement.setInt(7, id);

			count = statement.executeUpdate();
			statement.close();

		} catch (SQLException error) {
			System.out.println(error.toString()); }

		Database.close();

		return count;

	}

	public static int update_nombre(int id, String nombre) {

		Database.init();

		int count = 0;

		try {

			String sql = "UPDATE personas SET nombre = ? WHERE id = ?";

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, nombre);
			statement.setInt(2, id);

			count = statement.executeUpdate();
			statement.close();

		} catch (SQLException error) {
			System.out.println(error.toString()); }

		Database.close();

		return count;

	}

	public static int update_fecha_registro(int id, String fecha_registro) {

		Database.init();

		int count = 0;

		try {

			String sql = "UPDATE personas SET fecha_registro = ? WHERE id = ?";

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, fecha_registro);
			statement.setInt(2, id);

			count = statement.executeUpdate();
			statement.close();

		} catch (SQLException error) {
			System.out.println(error.toString()); }

		Database.close();

		return count;

	}

	public static int update_coste_base(int id, double coste_base) {

		Database.init();

		int count = 0;

		try {

			String sql = "UPDATE personas SET coste_base = ? WHERE id = ?";

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setDouble(1, coste_base);
			statement.setInt(2, id);

			count = statement.executeUpdate();
			statement.close();

		} catch (SQLException error) {
			System.out.println(error.toString()); }

		Database.close();

		return count;

	}

	public static int update_transporte(int id, int transporte) {

		Database.init();

		int count = 0;

		try {

			String sql = "UPDATE personas SET transporte = ? WHERE id = ?";

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, transporte);
			statement.setInt(2, id);

			count = statement.executeUpdate();
			statement.close();

		} catch (SQLException error) {
			System.out.println(error.toString()); }

		Database.close();

		return count;

	}

	public static int update_dias_asistidos(int id, int dias_asistidos) {

		Database.init();

		int count = 0;

		try {

			String sql = "UPDATE personas SET dias_asistidos = ? WHERE id = ?";

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, dias_asistidos);
			statement.setInt(2, id);

			count = statement.executeUpdate();
			statement.close();

		} catch (SQLException error) {
			System.out.println(error.toString()); }

		Database.close();

		return count;

	}

	public static int update_dias_faltados(int id, int dias_faltados) {

		Database.init();

		int count = 0;

		try {

			String sql = "UPDATE personas SET dias_faltados = ? WHERE id = ?";

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, dias_faltados);
			statement.setInt(2, id);

			count = statement.executeUpdate();
			statement.close();

		} catch (SQLException error) {
			System.out.println(error.toString()); }

		Database.close();

		return count;

	}

	public static int update_dias_mes(int id, int dias_mes) {

		Database.init();

		int count = 0;

		try {

			String sql = "UPDATE personas SET dias_mes = ? WHERE id = ?";

			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, dias_mes);
			statement.setInt(2, id);

			count = statement.executeUpdate();
			statement.close();

		} catch (SQLException error) {
			System.out.println(error.toString()); }

		Database.close();

		return count;

	}

	public static int update_all_dias_asistidos(int dias_asistidos) {

		Database.init();

		int count = 0;

		try {

			String sql = "UPDATE personas SET dias_asistidos = ?";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, dias_asistidos);

			count = statement.executeUpdate();
			statement.close();

		} catch (SQLException error) {

			System.out.println(error.toString()); }

		return count;

	}

	public static int update_all_dias_faltados(int dias_faltados) {

		Database.init();

		int count = 0;

		try {

			String sql = "UPDATE personas SET dias_faltados = ?";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, dias_faltados);

			count = statement.executeUpdate();
			statement.close();

		} catch (SQLException error) {

			System.out.println(error.toString()); }

		return count;

	}

	public static int update_all_dias_mes(int dias_mes) {

		Database.init();

		int count = 0;

		try {

			String sql = "UPDATE personas SET dias_mes = ?";
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, dias_mes);

			count = statement.executeUpdate();
			statement.close();

		} catch (SQLException error) {
			System.out.println(error.toString()); }

		Database.close();

		return count;

	}

}