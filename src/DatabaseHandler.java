//  Class setting up database connection and implementing functions based on queries

import java.sql.*;

public class DatabaseHandler {
    String jdbcURL = "jdbc:postgresql://localhost:5432/mazegame_test";
    String username = "postgres";
    String password = "0000";

    private static final int maxRowsAtOnce = 10;

    private static final String INSERT_HIGH_SCORE = "INSERT INTO high_score" +
            "(nickname, total_time, added_on) VALUES " +
            "(?,?,?)";

    private static final String SELECT_HIGH_SCORES = "SELECT nickname, total_time FROM high_score ORDER BY total_time ASC, added_on DESC LIMIT ? OFFSET ?";

    public void insertRecord(String nickname, int totalTime) throws SQLException{
        //set up connection
        try(Connection connection = DriverManager.getConnection(jdbcURL,username,password)){
            //prepare the statement
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_HIGH_SCORE);

            //fill the question marks - id is auto incremented, hence not included in sql query
            preparedStatement.setString(1,nickname);    //name and time
            preparedStatement.setInt(2,totalTime);      //from arguments
            preparedStatement.setDate(3,java.sql.Date.valueOf(java.time.LocalDate.now()));  //current time

            //execute statement
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            printSQLException(e);
        }
    }

    //show some of the best high scores
    // - once show maxRowsAtOnce
    // - offset = index of table (ex: for zero we get the best maxRowsAtOnce players)
    // - returns a string - to be displayed by the respective label
    public String showTable(int offset) throws SQLException {
        //set up the connection
        Connection connection = DriverManager.getConnection(jdbcURL,username,password);

        //prepare the statement
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_HIGH_SCORES);

        //set parameters
        preparedStatement.setInt(1,maxRowsAtOnce);              //limit
        preparedStatement.setInt(2,offset*maxRowsAtOnce);    //offset

        //execute statement - we get an iterator
        ResultSet resultSet = preparedStatement.executeQuery();

        //start with some space
        String scores="<html>&ensp;&ensp;&ensp;&ensp;";

        while(resultSet.next()){
            //append name
            scores = scores.concat(resultSet.getString(1));

            //append some dots, even for very large names
            for (int i=0; i<3; i++) scores = scores.concat(".");

            //get length of name
            int len = resultSet.getString(1).length();

            //append more dots, so scores line up
            while (len < 30){
                scores = scores.concat(".");
                len++;
            }

            //append score
            scores = scores.concat(resultSet.getString(2));

            //append next line and spaces
            scores = scores.concat(" <br> &ensp;&ensp;&ensp;&ensp;");
        }

        //close up the html tag
        scores = scores.concat(" </html>");

        return scores;
    }

    //method from internet - prints the exception nicely
    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
