package com.omnie.claimrouter.repository;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import com.omnie.claimrouter.config.DataSourceFactory;
import com.omnie.claimrouter.constants.Constants;

public class ClaimRepository {
	
    private DataSource dataSource= DataSourceFactory.getFactory().getDataSource();
	
    public boolean parserClaim(String createBy, int clientId, String segments, String requestString) {
        try(Connection con = dataSource.getConnection()) {
        	SQLServerDataTable stuTypeDT=new SQLServerDataTable();
        	stuTypeDT.setTvpName("[NCPDP].[Segments]");
        	stuTypeDT.addColumnMetadata("discription", java.sql.Types.VARCHAR);
        	stuTypeDT.addRow("Transaction Header Segment");
        	try(SQLServerCallableStatement cstmt = (SQLServerCallableStatement) con.prepareCall(Constants.SQL_CLAIM_PARSER)){
				cstmt.setInt(1, 1);
				cstmt.setInt(2, clientId);
				cstmt.setObject(3, stuTypeDT);
				cstmt.setString(4, requestString);
				cstmt.execute();
        	}
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
	}
}
