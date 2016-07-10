package cn.tf.fy.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import cn.tf.fy.entity.Article;
import cn.tf.util.LuceneUtil;

//持久层
public class ArticleDao {
	
	
	//根据关键字，获取总记录数,返回总的记录数
	
	public int getAllRecord(String keywords) throws Exception{	
		List<Article> atl=new ArrayList<Article>();
		
		QueryParser queryParser=new QueryParser(LuceneUtil.getVersion(),"content",LuceneUtil.getAnalyzer());
		Query query=queryParser.parse(keywords);
		IndexSearcher  is=new IndexSearcher(LuceneUtil.getD());
		TopDocs td=is.search(query,5);
		
		
		//返回符合条件的真实总记录数，不受search(query,5)中定义的5的影响
		//return td.totalHits;  //17条
		return td.scoreDocs.length;   //5条
		
	}
	
	//根据关键字，批量查询记录
	/*
	 * start:从第几条记录的索引号开始查询，索引号从0开始
	 * size:最多查询几条记录，不满足最多数目时，以实际为准
	 * 返回集合
	 */
	public List<Article>  findAll(String keywords,int start,int size) throws Exception{
		List<Article>  atl=new ArrayList<Article>();
		QueryParser queryParser=new QueryParser(LuceneUtil.getVersion(),"content",LuceneUtil.getAnalyzer());
		Query query=queryParser.parse(keywords);
		IndexSearcher  is=new IndexSearcher(LuceneUtil.getD());
		TopDocs td=is.search(query,100);

		int middle=Math.min(start+size,td.totalHits);
		for(int i=start;i<middle;i++){
			ScoreDoc  sd=td.scoreDocs[i];
			int no=sd.doc;
			Document document=is.doc(no);
			Article at=(Article) LuceneUtil.document2Javabean(document, Article.class);
			atl.add(at); 
		}
		return atl;
	  
	}
	
	
	
	public static void main(String[] args) throws Exception {
		ArticleDao  dao=new ArticleDao();
		System.out.println(dao.getAllRecord("是"));
		
		System.out.println("第一页");
		List<Article> list=dao.findAll("是", 0, 4);
		for (Article a : list) {
			System.out.println(a);
		}
		
		System.out.println("第二页");
		list=dao.findAll("是", 4, 4);
		for (Article a : list) {
			System.out.println(a);
		}
		
		System.out.println("第三页");
		list=dao.findAll("是", 8, 4);
		for (Article a : list) {
			System.out.println(a);
		}
		
		System.out.println("第四页");
		list=dao.findAll("是", 12, 4);
		for (Article a : list) {
			System.out.println(a);
		}
		
	}
	
}
