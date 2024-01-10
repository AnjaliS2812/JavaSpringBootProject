package com.omnie.claim.discount.engine.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.omnie.claim.discount.engine.config.DataSourceFactory;
import com.omnie.claim.discount.engine.constants.Constants;
import com.omnie.claim.discount.engine.dto.PortBean;

public class PortBeanRepository {
	
    private DataSource dataSource= DataSourceFactory.getFactory().getDataSource();
	
	public PortBean getPortDetail(String pBinNo, String pDateOfService, String pPcn) {
		List<PortBean> portBeanList= new ArrayList<PortBean>();
        try(Connection con = dataSource.getConnection()) {
        	try(CallableStatement cstmt = con.prepareCall(Constants.SQL_GET_PORT_DETAILS)){
				cstmt.setString(1, pBinNo);
				cstmt.setString(2, pDateOfService);
				cstmt.setString(3, pPcn);
				try(ResultSet rs = cstmt.executeQuery()){
					while (rs.next()) {
						PortBean portBean = new PortBean();
						portBean.setHost(rs.getString(2));
						portBean.setPort(rs.getString(3));
						portBean.setDiscountType(rs.getString(4));
						portBeanList.add(portBean);
					}
		        }
        	}
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return portBeanList.size()>0 ? portBeanList.get(0) :  null;
	}
	
	
	public List<PortBean> getPortDetailsforCache(String pBinNo) {
		List<PortBean> portBeanList= new ArrayList<PortBean>();
        try(Connection con = dataSource.getConnection()) {
        	try(CallableStatement cstmt = con.prepareCall(Constants.SQL_GET_PORT_DETAILS_FOR_CACHE)){
				cstmt.setString(1, pBinNo);
				try(ResultSet rs = cstmt.executeQuery()){
					while (rs.next()) {
						PortBean portBean = new PortBean();
		            	portBean.setBin(rs.getString(1));
		            	portBean.setPcn(rs.getString(2)==null||(rs.getString(2)!=null&&rs.getString(2).equalsIgnoreCase("null"))?"":rs.getString(2));
		            	portBean.setGroupNum(rs.getString(3)==null||(rs.getString(3)!=null&&rs.getString(3).equalsIgnoreCase("null"))?"":rs.getString(3));
		            	portBean.setHost(rs.getString(4));
		            	portBean.setPort1(rs.getString(5));
		            	portBean.setPort2(rs.getString(6));
		            	portBean.setDiscountType(rs.getString(7));
		            	portBean.setStartDate(rs.getString(8));
		            	portBean.setTerminationDate(rs.getString(9));
						portBeanList.add(portBean);
					}
		        }
        	}
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return portBeanList;
	}

}
