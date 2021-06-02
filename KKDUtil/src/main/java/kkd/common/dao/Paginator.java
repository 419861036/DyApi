package kkd.common.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 *                       
 * @Filename Paginator.java
 * @Description 分页器
 *
 *
 */
public class Paginator implements Serializable{
	private static final long serialVersionUID = 1L;
	public static final     	int first = -1; //首页标识
	 public static final  	int before =-2; //上一页标识
	 public static final  	int after = -3;//下一页标识
	public static final    	int last =-4;//尾页标识
	public static final 	    int beforAndAfter = 4; //间隔页
	public static final       int homePage = 1;//首页页码
	/**当前页数**/
	private int		page;			
	
	/**总条数**/
	private long		items;			
	
	/**每页项数**/
	private int		itemsPerPage;
	
	/**总页码*/
	private int     totalPages;
	
	/**默认大小*/
	public  static final  int  DEFAULT_ITEMSPERPAGE = 10;
	
	private OutPut outPut;
	
	private Map<String,String> headers; 
	
	@SuppressWarnings("unused")
	private String html=getOutPut();
	//分页页面编码为UTF-8，通过ajax返回无法直接写中文，通过request返回
	
	
	public Paginator(){
		this(1,DEFAULT_ITEMSPERPAGE);
	}
	
	/**
	 * @deprecation 默认每项为10的分页器
	 * @param page 当前页面
	 */
	public  Paginator(int page){
		this(page,DEFAULT_ITEMSPERPAGE);
	}

	
	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	/**
	 * 
	 * @param page 当前页面
	 * @param itemsPerPage 每页项数
	 */
	public  Paginator(Integer page,Integer itemsPerPage){
		if(page==null){
			this.page = homePage;
		}else{
			this.page = page;
		}
		itemsPerPage = itemsPerPage==null?DEFAULT_ITEMSPERPAGE:itemsPerPage;
		this.itemsPerPage = itemsPerPage;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public Long getItems() {
		return items;
	}

	public void setItems(Long items) {
		this.items = items;
		this.totalPages = getPages();
	}

	public int getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(int itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}
	
	/**
	 * @deprecation 取得总页数
	 * @return
	 */
	public int getPages(){
		return (int)Math.ceil((double)items/itemsPerPage);
	}

	
	/***
	 * @return
	 */
	public int  getStartIndex(){
		return getEndIndex()-itemsPerPage;		
	}
	
	/***
	 * @return
	 */
	public  int  getEndIndex(){
		return  itemsPerPage*page;
	}
	public interface OutPut{
		public String print();
	}
	public String getOutPut() {
		if(outPut!=null){
			return outPut.print();
		}
		return "";
	}

	public void setOutPut(OutPut outPut) {
		this.outPut = outPut;
	}
	
	/**
	 *  分页标签集合
	 * @param page 当前页
	 * @param totalPages 总页数
	 * @return
	 */
	public static List<Integer> getPagNumber(int page ,int totalPages){
		return getPagNumber( homePage, beforAndAfter, totalPages , page, first,
				 before, after, last);
	}
	/**
	 * 分页标签集合
	 * @param page 当前页
	 * @param beforAndAfter 前后间隔页
	 * @param totalPages 总页数
	 * @return
	 */
	public static List<Integer> getPagNumber(int page ,int totalPages,int beforAndAfter){
		 
		return getPagNumber( homePage, beforAndAfter, totalPages , page, first,
				 before, after, last);
	}
	/**
	 * 分页标签集合获取
	 * @param homePage 首页
	 * @param beforAndAfter 间隔页码
	 * @param totalPages 总页数
	 * @param page 当前页
	 * @param first 首页标识
	 * @param before 上一页标识
	 * @param after 下一页标识
	 * @param last 尾页标识
	 * @return
	 */
	public static List<Integer> getPagNumber(int homePage,int beforAndAfter,int totalPages ,int page,int first,
			int before,int after,int last){
		int startPage ;//显示的的第一个页码
		int endPage ;//显示的最后一个页码
		page = getAccuratePage(page,totalPages);
		List<Integer> list = new ArrayList<Integer>();
		
		//根据当前页获取起始页
		if(page-beforAndAfter>=homePage){
			startPage = page-beforAndAfter;
		}else{
			startPage = homePage;
		}
		
		if(page+beforAndAfter<=totalPages){
			endPage = page+beforAndAfter;
		}else{
			endPage = totalPages;
		}
		//首页
		if(startPage > homePage){
			list.add(first);
		}
		//上一页(当前页只要大于起始页，则一定存在上一页标签)
		if(page>startPage){
			list.add(before);
		}
		
		//遍历页码(起始到结尾)
		for(int i=startPage;i<=endPage;i++){
			list.add(i);
		}
		
		//下一页(当前页小于endpage，则一定存在下一页)
		if(page < endPage){
			list.add(after);
		}
		//尾页(结束页码一定要小于总页码，才可存在尾页标签)
		if(endPage < totalPages){
			list.add(last);
		}
		return list;
	}
	
	public static int getAccuratePage(int page,int totalPages){
		if(page > totalPages){
			page = totalPages+1;
		}
		if(page<homePage){
			page = homePage;
		}
		return page;
	}
	
	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
}
