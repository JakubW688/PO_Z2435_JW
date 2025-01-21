package com.myapp.database;
import java.sql.*;

public class DatabaseHelper {
    private static final String DATABASE_URL = "jdbc:sqlite:database.db";

    private static Connection connectBase() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }
    public static void initializeDatabase() {
        String tabela = """
            CREATE TABLE IF NOT EXISTS products (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL, 
                netto_price REAL NOT NULL,
                gross_price REAL NOT NULL
            );
            """;
        try (Connection conn = connectBase();
             Statement stmt = conn.createStatement()) {
            stmt.execute(tabela);
        } catch (SQLException e) {
            System.err.println("Błąd podczas inicjalizacji bazy danych: " + e.getMessage());
        }
    }
    public static void saveProductWithGrossPrice(String name, double nettoPrice, double grossPrice) {
        String tabela = "INSERT INTO products(name, netto_price, gross_price) VALUES(?, ?, ?)";
        try (Connection conn = connectBase();
             PreparedStatement zapytanie = conn.prepareStatement(tabela)) {
            zapytanie.setString(1, name);
            zapytanie.setDouble(2, nettoPrice);
            zapytanie.setDouble(3, grossPrice);
            zapytanie.executeUpdate();
            System.out.println("Produkt dodany: " + name + ", Netto: " + nettoPrice + ", Brutto: " + grossPrice);
        } catch (SQLException e) {
            System.err.println("Błąd podczas zapisu do bazy danych: " + e.getMessage());
        }
    }


    public static boolean deleteProduct(String productName) {
        String tabela = "DELETE FROM products WHERE name = ?";
        try (Connection conn = connectBase();
             PreparedStatement pstmt = conn.prepareStatement(tabela)) {
            pstmt.setString(1, productName);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Błąd podczas usuwania produktu: " + e.getMessage());
            return false;
        }
    }

    public static String searchProducts(String searchTerm) {
        StringBuilder results = new StringBuilder();
        String sql = "SELECT * FROM products WHERE name LIKE ?";
        try (Connection conn = connectBase();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + searchTerm + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                results.append("ID: ").append(rs.getInt("id"))
                        .append(", Nazwa: ").append(rs.getString("name"))
                        .append(", Cena netto: ").append(rs.getDouble("netto_price"))
                        .append(", Cena brutto: ").append(rs.getDouble("gross_price"))
                        .append("\n");
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas wyszukiwania: " + e.getMessage());
        }
        return results.toString();
    }
}
