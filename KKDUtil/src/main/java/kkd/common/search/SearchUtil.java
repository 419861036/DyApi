package kkd.common.search;


public class SearchUtil {
	
	public static void main(String[] args) {
		int ab=searchNoResult(1,20);
		System.out.println(ab);
	}
	
	public static int halfSearchLoop(int[] sortedData,int findValue){  
        int start=0;  
        int end=sortedData.length-1;  
          
        while(start<=end){  
//            lCount++;  
            //中间位置  
            int middle=(start+end)>>1;    //相当于(start+end)/2  
            //中值  
            int middleValue=sortedData[middle];  
              
            if(findValue==middleValue){  
                //等于中值直接返回  
                return middle;  
            }else if(findValue<middleValue) {  
                //小于中值时在中值前面找  
                end=middle-1;  
            }else{  
                //大于中值在中值后面找  
                start=middle+1;  
            }  
        }  
        //找不到  
        return -1;  
    }  
	
	/**
	 * 猜大小
	 * @param start
	 * @param end
	 * @return
	 */
	public static int searchNoResult(int start,int end){  
        int c=0;
        while(start<end){  
            c++;  
            //中间位置  
            int middle=(start+end)>>1;    //相当于(start+end)/2  
        	boolean big=true;//猜大小
//        	if(c%2==0){
//        		big=false;
//        	}
	        if(start==middle){
	        	if(big){
	        		return start;
	        	}
			}
	    	if(end==middle){
	    		if(!big){
	        		return end;
	        	}
    		}
        	if(big){//大了 就减
        		end=middle-1;
        	}else{//小了就加
        		start=middle+1;  
        	}
        }  
//        System.out.println(c);
        //找不到  
        if(start==end){
        	 return start;  
        }
        return -1;  
    }  
	
	
	
}
