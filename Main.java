import java.sql.*;
import java.util.*;


public class Main{
    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventory";
    private static final String USER = "root";
    private static final String PASS = "@Root!";
    private Connection conn = null;
    Scanner scanner = new Scanner(System.in);

    @SuppressWarnings("CallToPrintStackTrace")
    public Main(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected to database successfully!");
        } catch (SQLException se) {
            se.printStackTrace();
            System.err.println("Database connection failed. Check your DB_URL, USER, PASS, and ensure MySQL is running.");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            System.err.println("JDBC Driver not found. Make sure mysql-connector-j-x.x.x.jar is in your classpath.");
        }
    }

    public void start(){
        int choice;
        if(conn == null) {
            System.err.println("Connection to database is not established. Exiting application.");
            return;
        }

        do { 
            display();
            System.out.println("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); 
            switch (choice) {
                case 1:
                    add();
                    break;
                case 2:
                    view();
                    break;
                case 3:
                    update();
                    break;
                case 4:
                    delete();
                    break;
                case 5:
                    System.out.println("Thank you for using the Inventory Management System!");
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
        
        closeResources();
    }

    public void display(){
        System.out.println("--------------Inventory Management System-------------");
        System.out.println("1. Add Item");
        System.out.println("2. View Items");
        System.out.println("3. Update Item");
        System.out.println("4. Delete Item");
        System.out.println("5. Exit");
      
    }

    public void add() {
    String insertsql = "INSERT INTO in_management (product_name, product_quantity, product_price) VALUES (?, ?, ?)";
    try (PreparedStatement pstmt = conn.prepareStatement(insertsql)) {
        System.out.println("Enter item name: ");
        String product_name = scanner.nextLine();
        System.out.println("Enter item quantity: ");
        int product_quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("Enter item price: ");
        double product_price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        pstmt.setString(1, product_name);
        pstmt.setInt(2, product_quantity);
        pstmt.setDouble(3, product_price);
        int rowsAffected = pstmt.executeUpdate();
        System.out.println(rowsAffected + " row(s) inserted successfully!");
    } catch (SQLException e) {
        e.printStackTrace();
        System.err.println("Error inserting item into database.");
    }
}


    public void view(){
        String selectsql = "SELECT * FROM in_management";
        try {
            PreparedStatement pstmt = conn.prepareStatement(selectsql);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Inventory Items:");
            while(rs.next()){
                int product_id = rs.getInt("product_id");
                String product_name = rs.getString("product_name");
                int product_quantity = rs.getInt("product_quantity");
                double product_price = rs.getDouble("product_price");
                System.out.println("ID: " + product_id + ", Name: " + product_name + ", Quantity: " + product_quantity + ", Price: " + product_price);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error Viewing item.");
        }
        

    }

    public void update() {
    String updatesql = "UPDATE in_management SET product_name=?, product_quantity=?, product_price=? WHERE product_id=?";
    try (PreparedStatement pstmt = conn.prepareStatement(updatesql)) {
        System.out.println("Enter item ID to update: ");
        int product_id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("Enter new item name: ");
        String product_name = scanner.nextLine();
         System.out.println("Enter new item quantity: ");
        int product_quantity = scanner.nextInt();
        System.out.println("Enter new item price: ");
        double product_price = scanner.nextDouble();
         scanner.nextLine(); // Consume newline
        pstmt.setString(1, product_name);
        pstmt.setDouble(2, product_price);
        pstmt.setInt(3, product_quantity);
        pstmt.setInt(4, product_id);
        int rowsAffected = pstmt.executeUpdate();
        System.out.println(rowsAffected + " row(s) updated successfully!");
    } catch (SQLException e) {
        e.printStackTrace();
        System.err.println("Error updating item in database.");
    }
}

    public void delete(){
        String deletesql = "DELETE FROM in_management WHERE product_id=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(deletesql);
        System.out.println("Enter item ID to delete: ");
        int product_id = scanner.nextInt();
        pstmt.setInt(1, product_id);
        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Item deleted successfully!");
        } else {
            System.out.println("No item found with the given ID.");
        }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error deleting item from database.");
        }
        
    }

 private void closeResources() {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("Database connection closed.");
            }
            if (scanner != null) {
                scanner.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
    }
   }
    
 public static void main(String[] args) throws Exception{
    Main app = new Main();
    app.start();
}

}