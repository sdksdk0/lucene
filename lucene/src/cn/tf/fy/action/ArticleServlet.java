package cn.tf.fy.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.tf.fy.entity.PageBean;
import cn.tf.fy.service.ArticleService;

public class ArticleServlet extends HttpServlet {


	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		try {
		//获取关键字
		String keywords=request.getParameter("keywords");
		if(keywords==null || keywords.trim().length()==0){
			keywords="是";
		}
		//获取当前页号
		String temp=request.getParameter("currPageNO");
		if(temp==null || temp.trim().length()==0){
			temp="1";
		}
		//调用业务层
		ArticleService articleService=new ArticleService();
		PageBean page=articleService.show(keywords,Integer.parseInt(temp));
		
		//将page对象绑定到request域对象中
		request.setAttribute("PAGE", page);
		request.setAttribute("KEYWORDS", keywords);
		
		//转发到list.jsp页面
		request.getRequestDispatcher("/list.jsp").forward(request, response);
		
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}

}
