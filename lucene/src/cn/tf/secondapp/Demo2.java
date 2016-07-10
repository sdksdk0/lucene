package cn.tf.secondapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
import org.junit.Test;

import cn.tf.entity.Article;
import cn.tf.util.LuceneUtil;

public class Demo2 {
	
	
	@Test
	//创建索引库
     public void createIndexDB() throws Exception{
    	 Article  at=new Article(1, "lucene","Lucene是apache软件基金会发布的一个开放源代码的全文检索引擎工具包");
 		//创建Document对象
 		Document doc=LuceneUtil.javabean2Document(at);
    	IndexWriter iw=new IndexWriter(LuceneUtil.getD(), LuceneUtil.getAnalyzer(), LuceneUtil.getMfl()) ;
    	iw.addDocument(doc);
    	iw.close();
     }
	
	//根据关键字查询
	@Test
	public void findIndexDB() throws Exception {
		String keywords="是";
		List<Article> atl=new ArrayList<Article>();
		
		QueryParser queryParser=new QueryParser(LuceneUtil.getVersion(),"content",LuceneUtil.getAnalyzer());
		Query query=queryParser.parse(keywords);
		IndexSearcher  is=new IndexSearcher(LuceneUtil.getD());
		TopDocs td=is.search(query,100);
		
		for(int i=0;i<td.scoreDocs.length;i++){
			ScoreDoc sd=td.scoreDocs[i];
			
			int no=sd.doc;
			Document document=is.doc(no);
			Article article=(Article) LuceneUtil.document2Javabean(document, Article.class);
			atl.add(article);
			
		}
		for (Article article : atl) {
			System.out.println(article);
		}
		
	}
	

}
