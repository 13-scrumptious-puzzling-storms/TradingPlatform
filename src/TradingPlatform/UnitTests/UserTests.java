package TradingPlatform.UnitTests;

import TradingPlatform.NetworkProtocol.DBConnection;
import TradingPlatform.JDBCDataSources.JDBCUserDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

public class UserTests {
    static Connection connection;

    @BeforeAll
    public static void init(){
        connection = DBConnection.getInstance();
    }

    @Test
    public void TestGetUsername(){
        JDBCUserDataSource user = new JDBCUserDataSource(1, connection);
        assert (user.getUsername() != null);
    }

    @Test
    public void TestGetAccountType(){
        JDBCUserDataSource user = new JDBCUserDataSource(1, connection);
        assert(user.getAccountType() != null);
    }

    @Test
    public void TestGetOrganisationalUnit(){
        JDBCUserDataSource user = new JDBCUserDataSource(1, connection);
        var orgUnit = user.getOrganisationalUnit();
        assert(orgUnit != null);
    }
}
