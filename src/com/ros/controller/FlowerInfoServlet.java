package com.ros.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.ros.entity.FlowerInfo;
import com.ros.entity.FlowerType;
import com.ros.service.FlowerInfoService;
import com.ros.service_impl.FlowerInfoServiceImpl;
import com.ros.test.FlowerTest;
import com.ros.util.BaseDao;
import com.ros.util.PageData;

/**
 *   鲜花业务控制器
 */
@WebServlet("/flower.do")
public class FlowerInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	FlowerInfoService fIS=new FlowerInfoServiceImpl();//实例化鲜花业务层对象
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FlowerInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 
		String op=request.getParameter("op");//判断请求方式
		//统一设置编码格式
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		 PrintWriter out=response.getWriter();
		 //最新鲜花
		 if(op.equals("getFlowerInfoByLimit")) {
			 ArrayList<FlowerInfo> list=fIS.getFlowerInfoByLimit();
			  request.getSession().setAttribute("list", list);
			 response.sendRedirect("/Rosemary/index/index.jsp");
			
		 }
		 //顶级鲜花
		 else if(op.equals("getFlowerInfoByPrice")) {
			 ArrayList<FlowerInfo> list=fIS.getFlowerInfoByPrice();
			  request.getSession().setAttribute("list2", list);
			 response.sendRedirect("/Rosemary/index/index.jsp");
			 
		 }//所有花的类别
		 else if(op.equals("queryFlowerType")) {
			 
			 ArrayList<FlowerType> list=fIS.queryFlowerType();
			 request.getSession().setAttribute("flowerType", list);
			 response.sendRedirect("/Rosemary/index/shop.jsp");
			 
		 }
		 //主页分页显示所有鲜花，固定每页6条记录。（包含模糊查询）
		 else if(op.equals("queryFlowerInfoByPage")) {
			 //初始化分页对象
			 PageData<FlowerInfo> FlowerInfo=null;
		    int page=1;//默认第一页
		    int pageSize=6;//默认每页显示6条
		    String keyword="";//默认关键词搜索为空字符串（查询所有）
		    int priceStart=0;//默认查询的鲜花价格从0开始
		    int priceEnd=1000;//默认查询的鲜花价格在1000结束
		    
		    //获取鲜花类别最小编号
		    String sql1="select min(typeId) as typeId  from flower_type";
			ArrayList<FlowerType> typeId1=(ArrayList<FlowerType>) BaseDao.select(sql1, FlowerType.class);
		    int typeIdMin=typeId1.get(0).getTypeId();
			//获取鲜花类别最大编号
			String sql2="select max(typeId) as typeId  from flower_type";
			ArrayList<FlowerType> typeId2=(ArrayList<FlowerType>) BaseDao.select(sql2, FlowerType.class);
		    int typeIdMax=typeId2.get(0).getTypeId();
		    //初始化排序方式（按照什么来排序）
		    String sort="createTime";//默认按照创建时间排序
		    //初始化排序方法（降序还是升序）
		    String  sortType="desc";//默认按照创建时间降序查询（即最新的产品排在最前面）
		    //接下来进入判断（参数是否有传进来，否则使用我们初始化参数的值）
		    if(request.getParameter("page")!=null) {
		    	page=Integer.valueOf(request.getParameter("page"));
		    }
		    if(request.getParameter("pageSize")!=null) {
		    	pageSize=Integer.valueOf(request.getParameter("pageSize"));
		    } 
		    if(request.getParameter("keyword")!=null) {
		    	keyword=request.getParameter("keyword");
		    } 
		    if(request.getParameter("priceStart")!=null) {
		    	priceStart=Integer.valueOf(request.getParameter("priceStart"));
		    }
		    if(request.getParameter("priceEnd")!=null) {
		    	priceEnd=Integer.valueOf(request.getParameter("priceEnd"));
		    }
		    if(request.getParameter("sort")!=null) {
		    	sort=request.getParameter("sort");
		    } 
		    if(request.getParameter("sortType")!=null) {
		    	sortType=request.getParameter("sortType");
		    } 
		    //如果用户没有传入花的类别编号，即默认查询所有类别
		    if(request.getParameter("typeId")==null) {
		    	 FlowerInfo=fIS.queryFlowerInfoByPage(page, pageSize, "%"+keyword+"%", priceStart, priceEnd, typeIdMin, typeIdMax, sort, sortType);
		    	
		    }
		    //用户点击左侧花的类别触发的模糊查询事件
		    else {
		    	int typeId=Integer.valueOf(request.getParameter("typeId"));//初始化花的类别编号
		    	FlowerInfo=fIS.queryFlowerInfoByPage(page, pageSize, "%"+keyword+"%", priceStart, priceEnd,typeId , typeId, sort, sortType);
		    	
		    }
		   /* //设置session对象存储花的数据
		    request.getSession().setAttribute("FlowerInfo", FlowerInfo);
		     //最后重定向到商品展示页面
		    response.sendRedirect("/Rosemary/index/shop.jsp");
		    */
		    
		    Gson gson = new Gson();
		    
		    String jsonStr=gson.toJson(FlowerInfo);
		    out.print(jsonStr);
		  
		    
		 }
		
		 


		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
