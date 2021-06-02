package kkd.common.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 *                       
 * @Filename PageList.java
 * @Description  带分页信息的List
 *
 *
 */
public class PageList<T> implements Serializable{
	
	/** Comment for <code>serialVersionUID</code> */
	private static final long serialVersionUID = 1L;
	
	private  Collection<T>  collection;
	
	private  Paginator  paginator;
	
	public  PageList(){
		paginator = new Paginator();
	}
	
	
	public PageList(Collection<T> c,Paginator pg){
		collection = c==null?new ArrayList<T>():c;
		paginator = pg==null?new Paginator():pg;
	}
	
	/***
	 * @deprecation 总条数
	 * @return
	 */
	public  Long  getTotalNum(){		
		return this.paginator.getItems();
	}
	
	/**
	 * @deprecation 总页码
	 * @return
	 */
	public  int  getTotalPages(){
		return this.paginator.getPages();
	}
	
	/**
	 * @deprecation 当前页
	 * @return
	 */
	public  int  getCurrPage(){
		return this.paginator.getPage();
	}

	/**
	 * @deprecation 页面大小
	 * @return
	 */
	public  int   getPageSize(){
		return paginator.getItemsPerPage();
	}
	
	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}


	public Collection<T> getCollection() {
		return collection;
	}


	public void setCollection(Collection<T> collection) {
		this.collection = collection;
	}
	
	
	
}
