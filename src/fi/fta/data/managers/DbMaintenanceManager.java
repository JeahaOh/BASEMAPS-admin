package fi.fta.data.managers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import fi.fta.beans.Pair;
import fi.fta.data.dao.SimpleDAO;
import fi.fta.utils.HibernateUtil;


public class DbMaintenanceManager
{
	
	public static void main(String[] args)
	{
		List<String> vacuum = new ArrayList<String>();
		
		List<Pair<String, String>> cluster = new ArrayList<Pair<String, String>>();
		
		try
    	{
    		HibernateUtil.create();
    		SimpleDAO<Object> dao = new SimpleDAO<>(Object.class);
    		for (String table : vacuum)
    		{
    			try
    			{
    				Logger.getRootLogger().info("DbMaintenanceManager.main > analyzing " + table);
    				dao.executeCustomSQLUpdateQuery("VACUUM ANALYZE " + table);
    			}
    			catch (HibernateException ex)
    			{
    				Logger.getRootLogger().error("DbMaintenanceManager.main.vacuum " + table + " Fatal error", ex);
    				ex.printStackTrace();
    			}
    		}
    		for (Pair<String, String> table : cluster)
    		{
    			try
    			{
    				Logger.getRootLogger().info("DbMaintenanceManager.main > clustering " + table.getFirst());
    				dao.executeCustomSQLUpdateQuery("CLUSTER " + table.getFirst() + " USING " + table.getSecond());
    			}
    			catch (HibernateException ex)
    			{
    				Logger.getRootLogger().error("DbMaintenanceManager.main.vacuum " + table + " Fatal error", ex);
    				ex.printStackTrace();
    			}
    		}
    		try
			{
				Logger.getRootLogger().info("DbMaintenanceManager.main > refresh");
			}
			catch (HibernateException ex)
			{
				Logger.getRootLogger().error("DbMaintenanceManager.main.refresh > Fatal error", ex);
				ex.printStackTrace();
			}
    		
    	}
    	catch (HibernateException ex)
    	{
    		Logger.getRootLogger().error("DbMaintenanceManager.main.getConnection > Fatal error", ex);
    		ex.printStackTrace();
    	}
    	finally
    	{
    		try
			{
				HibernateUtil.close();
			}
			catch (HibernateException ex)
			{
				Logger.getRootLogger().error("DbMaintenanceManager.main.finalize > Fatal error", ex);
			}
    	}
		
	}
	
}