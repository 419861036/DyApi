package kkd.common.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import kkd.common.exception.KKDException;

/** 
* 事务管理器 
*/ 
public class TransactionManager { 

	
    private Connection conn;
    private String dsName;
       
    protected TransactionManager(ConnectionHandle ch) {
    	this.conn = ch.getConn();
        this.dsName=ch.getDsName();
	}
	/**
     * 设置事物隔离等级
     * @param level
     */
    public void setTransactionIsolation(int level){
    	try {
			conn.setTransactionIsolation(level);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    /** 开启事务 */ 
    public void beginTransaction() {   
        try {
            conn.setAutoCommit(false); //把事务提交方式改为手工提交   
        } catch (SQLException e) {   
            throw new KKDException("开启动事务时出现异常",e);   
        }   
    }
    /**
     * 设置保存点
     * @return
     */
    public Savepoint savePoint(){
    	try {
			return conn.setSavepoint();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
    }   
    /**
     * 释放保存点
     * @return
     */
    public void releaseSavepoint(Savepoint savePoint){
    	try {
    		if(savePoint!=null){
    		 conn.releaseSavepoint(savePoint);
    		}
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }   
    /** 提交事务并关闭连接 */ 
    public void commit() {   
        try {
        	if(conn!=null){
        		conn.commit(); //提交事务   
        	}
        } catch (SQLException e) {   
            throw new KKDException("提交事务时出现异常",e);   
        }  
    }   
    /**回滚*/
    public void rollback(Savepoint savePoint){
    	 try {
    		 if(savePoint!=null){
    			 conn.rollback(savePoint);
    		 }
         } catch (SQLException e) {   
             throw new KKDException("回滚事务时出现异常",e);   
         }
    }
    /** 回滚并关闭连接 */ 
    public void rollback(){   
        try {
            conn.rollback(); 
        } catch (SQLException e) {   
            throw new KKDException("回滚事务时出现异常",e);   
        }
    }  
    /**关闭*/
    public void close(){
    	DbUtils.close(conn,dsName); 
    }
} 
