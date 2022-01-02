import java.sql.*;

public class TransactionManager {

    private static Connection flyBookingDbConnection;
    private static Connection hotelBookingDbConnection;
    private static Connection accountDbConnection;

    // Prepare tx for fly_booking db
    private static String PREPARE_FLY_BOOKING_TX =
            "BEGIN;\n" +
                    "insert into fly_booking(client_name, fly_number, from_airport, to_airport, departure_date) " +
                    "values ('Nik', 'KLM 1382', 'KBP', 'AMS', '2015-05-01');\n" +
                    "PREPARE TRANSACTION 'fly_booking_tx';\n";
    private static String ROLLBACK_FLY_BOOKING_TX = "ROLLBACK PREPARED 'fly_booking_tx';";
    private static String COMMIT_FLY_BOOKING_TX = "COMMIT PREPARED 'fly_booking_tx';";

    // Prepare tx for hotel_booking db
    private static String PREPARE_HOTEL_BOOKING_TX =
            "BEGIN;\n" +
                    "insert into hotel_booking(client_name, hotel_name, arrival_date, departure_date) " +
                    "values ('Nik', 'Hilton', '2015-05-01', '2015-05-02');\n" +
                    "PREPARE TRANSACTION 'hotel_booking_tx';\n";
    private static String ROLLBACK_HOTEL_BOOKING_TX = "ROLLBACK PREPARED 'hotel_booking_tx';";
    private static String COMMIT_HOTEL_BOOKING_TX = "COMMIT PREPARED 'hotel_booking_tx';";

    // Prepare tx for account db
    private static String PREPARE_ACCOUNT_TX =
            "BEGIN;\n" +
                    "update account set amount = amount - 115 where account_id = 1;\n" +
                    "PREPARE TRANSACTION 'account_tx';\n";
    private static String ROLLBACK_ACCOUNT_TX = "ROLLBACK PREPARED 'account_tx';";
    private static String COMMIT_ACCOUNT_TX = "COMMIT PREPARED 'account_tx';";

    private static boolean proceedTx(Connection connection, String sql) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
            return true;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) throws SQLException {

        // initialize db connections
        flyBookingDbConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/fly_booking?user=postgres&password=postgres");
        hotelBookingDbConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/hotel_booking?user=postgres&password=postgres");
        accountDbConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/account?user=postgres&password=postgres");

        // 1st phase
        boolean isFlyBookingPrepared = proceedTx(flyBookingDbConnection, PREPARE_FLY_BOOKING_TX);
        boolean isHotelBookingPrepared = proceedTx(hotelBookingDbConnection, PREPARE_HOTEL_BOOKING_TX);
        boolean isAccountPrepared = proceedTx(accountDbConnection, PREPARE_ACCOUNT_TX);

        // 2nd phase: COMMIT or ROLLBACK
        if (isFlyBookingPrepared && isHotelBookingPrepared && isAccountPrepared) {
            proceedTx(flyBookingDbConnection, COMMIT_FLY_BOOKING_TX);
            proceedTx(hotelBookingDbConnection, COMMIT_HOTEL_BOOKING_TX);
            proceedTx(accountDbConnection, COMMIT_ACCOUNT_TX);
        } else {
            proceedTx(flyBookingDbConnection, ROLLBACK_FLY_BOOKING_TX);
            proceedTx(hotelBookingDbConnection, ROLLBACK_HOTEL_BOOKING_TX);
            proceedTx(accountDbConnection, ROLLBACK_ACCOUNT_TX);
        }
    }

}


