package TradingPlatform.JDBCDataSources;

import TradingPlatform.AccountType;
import TradingPlatform.Interfaces.UserDataSource;
import TradingPlatform.OrganisationalUnit;

import java.sql.*;

/**
 * The JDBC user datasource is a class used to get a user's details from the database.
 * The static methods in the class are used to interact with a specific user, or to add a user / find a user's id
 */
public class JDBCUserDataSource implements UserDataSource {

    private static final String GET_USER = "SELECT * FROM User WHERE userId=?";
    private static final String GET_NUM_USERS_WITH_USERNAME = "SELECT COUNT(*) FROM User WHERE username=?";
    private static final String GET_USERID_FROM_USERNAME_PASSWORD = "SELECT userId FROM User WHERE username=? and password=?";
    private static final String SET_USER_PASSWORD = "UPDATE User SET password=? WHERE userId=?";
    private static final String SET_USER_PASSWORD_BY_USERNAME = "UPDATE User SET password=? WHERE username=?";
    private static final String INSERT_USER = "INSERT INTO USER (username, password, organisationUnitId, userRole) VALUES (?, ?, ?, ?)";

    private static PreparedStatement setUserPassword;

    private final int userId;
    private String username;
    private String password;
    private AccountType accountType;
    private OrganisationalUnit organisationalUnit;
    private final Connection connection;

    /**
     * This constructor initialises the JDBC user datasource for a specific user, and the
     * non static methods (such as getUsername) will return the user's details
     * @param userId the userid of the user used for this instance of the datasource
     * @param connection the connection to the database
     */
    public JDBCUserDataSource(int userId, Connection connection){
        this.userId = userId;
        this.connection = connection;

        try {
            // Preparing Statements
            PreparedStatement getUser = connection.prepareStatement(GET_USER);
            setUserPassword = connection.prepareStatement(SET_USER_PASSWORD);

            // Getting the user's data
            getUser.setInt(1, userId);
            ResultSet rs = getUser.executeQuery();

            if (rs.next()) {
                username = rs.getString("username");
                password = rs.getString("password");
                accountType = AccountType.getType(rs.getInt("userRole"));
                int orgId = rs.getInt("organisationUnitId");
                organisationalUnit = new JDBCOrganisationalUnit(connection).getOrganisationalUnit(orgId);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Tries to find the user matching the username and password
     * @param username the user's username - case insensitive
     * @param password the user's hashed password - case sensitive
     * @param connection the connection to the database
     * @return the userId of the user, or -1 if not found
     */
    public static int getUserId(String username, String password, Connection connection){
        int userId = -1;
        try {
            PreparedStatement getUserIdFromUsernamePassword = connection.prepareStatement(GET_USERID_FROM_USERNAME_PASSWORD);
            getUserIdFromUsernamePassword.setString(1, username.toLowerCase());
            getUserIdFromUsernamePassword.setString(2, password);
            var rs = getUserIdFromUsernamePassword.executeQuery();
            if (rs.next()){
                userId = rs.getInt(1);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return userId;
    }

    /**
     * Adds a new user to the database if the username does not exist
     * @param username the user's username
     * @param password the user's hashed password
     * @param AccountType the account type of the user
     * @param OrganisationUnitId the organisation unit id of the orgUnit that the user belongs to
     * @param connection the connection to the database
     * @return True if the user was successfully created, false on error (e.g. org doesn't exist, username isn't unique)
     */
    public static boolean addUser(String username, String password, AccountType AccountType, int OrganisationUnitId, Connection connection){
        boolean success;
        try {
            // Check that there are no users with the given username
            PreparedStatement getUsersWithUsername = connection.prepareStatement(GET_NUM_USERS_WITH_USERNAME);
            getUsersWithUsername.setString(1, username.toLowerCase());
            ResultSet rs = getUsersWithUsername.getResultSet();
            if (rs.next())
                if (rs.getInt(1) != 0)
                    return false; // There are users with the given username

            PreparedStatement insertUser = connection.prepareStatement(INSERT_USER);
            insertUser.setString(1, username.toLowerCase());
            insertUser.setString(2, password);
            insertUser.setInt(3, OrganisationUnitId);
            insertUser.setInt(4, AccountType.getValue());
            int numRecords = insertUser.executeUpdate();
            if (numRecords == 0){
                throw new SQLException("Unable to insert new user into database.");
            }
            success = true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            success = false;
        }
        return success;
    }

    /**
     * IT admins change user password function does not require the user's current password.
     * @param username the username of the user who's password is being changed.
     * @param newHashedPassword the new password of the user (already hashed).
     * @param connection the connection to the database.
     * @return Whether the password was successfully updated
     */
    public static boolean adminChangeUserPassword(String username, String newHashedPassword, Connection connection){
        boolean setPassword;
        try {
            var setUserPasswordByUsername = connection.prepareStatement(SET_USER_PASSWORD_BY_USERNAME);
            setUserPasswordByUsername.setString(1, newHashedPassword);
            setUserPasswordByUsername.setString(2, username);
            int numRecords = setUserPasswordByUsername.executeUpdate();
            if (numRecords == 0) {
                throw new SQLDataException("Unable to update password for user " + username + ".");
            }
            setPassword = true;
        } catch (SQLException exception) {
            exception.printStackTrace();
            setPassword = false;
        }
        return setPassword;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public AccountType getAccountType() {
        return accountType;
    }

    @Override
    public OrganisationalUnit getOrganisationalUnit() {
        return organisationalUnit;
    }

    @Override
    public boolean ChangePassword(String currentHashedPassword, String newHashedPassword) {
        if (currentHashedPassword.equals(password)){
            try {
                setUserPassword = connection.prepareStatement(SET_USER_PASSWORD);
                setUserPassword.setString(1, newHashedPassword);
                setUserPassword.setInt(2, userId);
                int numRecords = setUserPassword.executeUpdate();
                if (numRecords == 0) {
                    throw new SQLDataException("Unable to update password for user " + userId + ".");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
            return true;
        }
        else {
            return false;
        }
    }

}
