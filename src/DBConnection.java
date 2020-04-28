import java.sql.*;

public class DBConnection {
    public static Connection connection;
    public static PreparedStatement preparedStatement;


    //    private static String dbName = "learn";
//    private static String dbUser = "root";
//    private static String dbPass = "ya78yrc8n4w3984";
    private static String dbName = "skillbox";
    private static String dbUser = "root";
    private static String dbPass = "123456789";

    private static StringBuilder insertQuery = new StringBuilder();

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/" + dbName +
                                "?user=" + dbUser + "&password=" + dbPass + "&useSSL=false" + "&serverTimezone=UTC" + "&useUnicode=true&characterEncoding=UTF-8" + "&max_allowed_packet=8388608");
//                SET GLOBAL max_allowed_packet=16777216
                connection.createStatement().execute("DROP TABLE IF EXISTS voter_count");

//                connection.createStatement().execute("CREATE TABLE voter_count(" +
//                        "id INT NOT NULL AUTO_INCREMENT, " +
//                        "name TINYTEXT NOT NULL, " +
//                        "birthDate DATE NOT NULL, " +
//                        "`count` INT NOT NULL, " +
//                        "PRIMARY KEY(id), " +
//                        "UNIQUE KEY name_date(name(50), birthDate))");

                connection.createStatement().execute("CREATE TABLE voter_count(" +
                        "id INT NOT NULL AUTO_INCREMENT, " +
                        "name TINYTEXT NOT NULL, " +
                        "birthDate DATE NOT NULL, " +
//                        "`count` INT NOT NULL, " +
//                        "PRIMARY KEY(id), " +
//                        "KEY name_date(name(50), birthDate))");

                        "PRIMARY KEY(id))");

//                preparedStatement = connection.prepareStatement("INSERT INTO voter_count(name, birthDate, `count`) VALUES  ?");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

//    public static void executeMultiInsert(String insertQuery) throws SQLException {
//        String sql = "INSERT INTO voter_count(name, birthDate, `count`) " +
//                "VALUES " + insertQuery +
//                "ON DUPLICATE KEY UPDATE count=count + 1";

        public static void executeMultiInsert (String insertQuery) throws SQLException {
            String sql = "INSERT INTO voter_count(name, birthDate) " +
                    "VALUES " + insertQuery ;
//            String sql = "INSERT INTO voter_count(name, birthDate, `count`) " +

            DBConnection.getConnection().createStatement().execute(sql);

        }

        public static void executePreparedStatement (String name, String birthDate) throws SQLException {

            preparedStatement.setString(1,  name);
            preparedStatement.setString(2,  birthDate);
            int resultSet = preparedStatement.executeUpdate();

        }

        public static void executeMultiInsert () throws SQLException {
            String sql = "INSERT INTO voter_count(name, birthDate, `count`) " +
                    "VALUES " + insertQuery.toString() +
                    "ON DUPLICATE KEY UPDATE count=count + 1";

            DBConnection.getConnection().createStatement().execute(sql);

        }

        public static void countVoter (String name, String birthDay) throws SQLException
        {
            birthDay = birthDay.replace('.', '-');

            insertQuery.append((insertQuery.length() == 0 ? "" : ", ") + "('" + name + "', '" + birthDay + "', 1) ");

        }

//        public static void printVoterCountsO () throws SQLException
//        {
//            String sql = "SELECT name, birthDate, `count` FROM voter_count WHERE `count` > 1";
//            ResultSet rs = DBConnection.getConnection().createStatement().executeQuery(sql);
//            while (rs.next()) {
//                System.out.println("\t" + rs.getString("name") + " (" +
//                        rs.getString("birthDate") + ") - " + rs.getInt("count"));
//            }
//
//        }

        public static void printVoterCounts () throws SQLException
        {
//            String sql = "SELECT v.id, v.name, v.birthDate, v.count, count(*) cnt FROM skillbox.voter_count v  group by concat(name, birthDate) having cnt>1 order by name";
            String sql = "SELECT v.id, v.name, v.birthDate, count(*) cnt FROM skillbox.voter_count v  group by concat(name, birthDate) having cnt>1 order by name";
//            ResultSet rs = DBConnection.getConnection().createStatement().executeQuery(sql);
            ResultSet rs = DBConnection.connection.createStatement().executeQuery(sql);
            while (rs.next()) {
                System.out.println("\t" + rs.getString("name") + " (" +
                        rs.getString("birthDate") + ") - " + rs.getInt("cnt"));
            }

        }
    }
