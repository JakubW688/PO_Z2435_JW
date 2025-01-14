package com.myapp.database;
import java.sql.*;



public class DatabaseHelper {
    private static final String DATABASE_URL = "jdbc:sqlite:database.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    public static void initializeDatabase() {
        String sql = """
                CREATE TABLE IF NOT EXISTS products (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    price REAL NOT NULL
                );
                """;
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Błąd podczas inicjalizacji bazy danych: " + e.getMessage());
        }
    }
    public static String getAllProducts() {
        StringBuilder results = new StringBuilder();
        String sql = "SELECT * FROM products";  // Zapytanie, które pobiera wszystkie produkty z tabeli

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                results.append("ID: ").append(rs.getInt("id"))
                        .append(", Nazwa: ").append(rs.getString("name"))
                        .append(", Cena: ").append(rs.getDouble("price"))
                        .append("\n");
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania danych: " + e.getMessage());
        }

        return results.toString();
    }


    public static void saveProduct(String name, double price) {
        String sql = "INSERT INTO products(name, price) VALUES(?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Błąd podczas zapisu do bazy danych: " + e.getMessage());
        }
    }

    public static String searchProducts(String searchTerm) {
        StringBuilder results = new StringBuilder();
        String sql = "SELECT * FROM products WHERE name LIKE ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                results.append("ID: ").append(rs.getInt("id"))
                        .append(", Nazwa: ").append(rs.getString("name"))
                        .append(", Cena: ").append(rs.getDouble("price"))
                        .append("\n");
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas wyszukiwania: " + e.getMessage());
        }
        return results.toString();
    }
}
