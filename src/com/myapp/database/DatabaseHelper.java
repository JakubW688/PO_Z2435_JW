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
                netto_price REAL NOT NULL,
                gross_price REAL NOT NULL
            );
            """;
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Błąd podczas inicjalizacji bazy danych: " + e.getMessage());
        }
    }


    public static void checkAndAddColumns() {
        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("PRAGMA table_info(products);");

            boolean hasNettoPrice = false;
            boolean hasGrossPrice = false;

            while (rs.next()) {
                String columnName = rs.getString("name");
                if ("netto_price".equals(columnName)) {
                    hasNettoPrice = true;
                } else if ("gross_price".equals(columnName)) {
                    hasGrossPrice = true;
                }
            }

            if (!hasNettoPrice) {
                stmt.executeUpdate("ALTER TABLE products ADD COLUMN netto_price REAL NOT NULL;");
            }

            if (!hasGrossPrice) {
                stmt.executeUpdate("ALTER TABLE products ADD COLUMN gross_price REAL NOT NULL;");
            }

        } catch (SQLException e) {
            System.err.println("Błąd podczas sprawdzania i dodawania kolumn: " + e.getMessage());
        }
    }

    public static String getAllProducts() {
        StringBuilder results = new StringBuilder();
        String sql = "SELECT * FROM products";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                results.append("ID: ").append(rs.getInt("id"))
                        .append(", Nazwa: ").append(rs.getString("name"))
                        .append(", Cena netto: ").append(rs.getDouble("netto_price"))
                        .append(", Cena brutto: ").append(rs.getDouble("gross_price"))
                        .append("\n");
            }
        } catch (SQLException e) {
            System.err.println("Błąd podczas pobierania danych: " + e.getMessage());
        }

        return results.toString();
    }

    public static void saveProductWithGrossPrice(String name, double nettoPrice, double grossPrice) {
        String sql = "INSERT INTO products(name, netto_price, gross_price) VALUES(?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, nettoPrice);
            pstmt.setDouble(3, grossPrice);
            pstmt.executeUpdate();
            System.out.println("Produkt dodany: " + name + ", Netto: " + nettoPrice + ", Brutto: " + grossPrice);
        } catch (SQLException e) {
            System.err.println("Błąd podczas zapisu do bazy danych: " + e.getMessage());
        }
    }


    public static boolean deleteProduct(String productName) {
        String sql = "DELETE FROM products WHERE name = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
        try (Connection conn = connect();
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
