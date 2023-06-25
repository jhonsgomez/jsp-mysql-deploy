package app.demomysql;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        Dotenv dotenv = Dotenv.load();
        Connection connection = null;
        final String DB_HOST = (dotenv.get("DB_HOST") != null) ? dotenv.get("DB_HOST") : "localhost";
        final String DB_NAME = (dotenv.get("DB_NAME") != null) ? dotenv.get("DB_NAME") : "prueba";
        final String DB_PORT = (dotenv.get("DB_PORT") != null) ? dotenv.get("DB_PORT") : "3306";
        final String DB_USER = (dotenv.get("DB_USER") != null) ? dotenv.get("DB_USER") : "root";
        final String DB_PASSWORD = (dotenv.get("DB_PASSWORD") != null) ? dotenv.get("DB_PASSWORD") : "";
        final String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            Statement statement;
            statement = connection.createStatement();
            ResultSet resultSet;
            resultSet = statement.executeQuery("select * from _users");

            PrintWriter out = response.getWriter();
            out.println("<html><body>");
            out.println("<h1>" + message + "</h1>");
            out.println("<ul>");
            while (resultSet.next()) {
                int id = resultSet.getInt("user_id");
                String firstname = resultSet.getString("user_firstname").trim();
                String lastname = resultSet.getString("user_lastname").trim();
                out.println("<li><b>" + id + "</b>&nbsp;" + firstname + " " + lastname + "</li>");
            }
            out.println("</ul>");
            out.println("</body></html>");

            resultSet.close();
            statement.close();
            connection.close();
        }
        catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}