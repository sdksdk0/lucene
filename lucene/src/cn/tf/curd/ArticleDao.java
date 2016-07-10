package cn.tf.curd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.LockObtainFailedException;
import org.junit.Test;

import cn.tf.entity.Article;
import cn.tf.util.LuceneUtil;

public class ArticleDao {

	@Test
	public void add() throws Exception{
		Article  at=new Article(1, "lucene","Lucene是apache软件基金会发布的一个开放源代码的全文检索引擎工具包");
		Document document=LuceneUtil.javabean2Document(at);
		IndexWriter  iw=new IndexWriter(LuceneUtil.getD(), LuceneUtil.getAnalyzer(), LuceneUtil.getMfl()); 
		iw.addDocument(document);
		iw.close();
	}
	
	@Test
	public void addAll() throws Exception{
		IndexWriter  iw=new IndexWriter(LuceneUtil.getD(), LuceneUtil.getAnalyzer(), LuceneUtil.getMfl()); 
		
		
		Article  at1=new Article(1, "lucene","Lucene是apache软件基金会发布的一个开放源代码的全文检索引擎工具包");
		Document document1=LuceneUtil.javabean2Document(at1);
		iw.addDocument(document1);
		
		Article  at2=new Article(2, "lucene","Lucene是根据关健字来搜索的文本搜索工具");
		Document document2=LuceneUtil.javabean2Document(at2);
		iw.addDocument(document2);
		
		Article  at3=new Article(3, "lucene","Lucene在全文检索领域是一个经典的祖先，现在很多检索引擎都是在其基础上创建的，思想是相通的");
		Document document3=LuceneUtil.javabean2Document(at3);
		iw.addDocument(document3);
		
		Article  at4=new Article(4, "lucene","Lucene它是一个全文检索引擎的架构，提供了完整的创建索引和查询索引");
		Document document4=LuceneUtil.javabean2Document(at4);
		iw.addDocument(document4);
		
		iw.close();
		
	}
	@Test
	public void update() throws Exception{
		Article  newArticle=new Article(4, "lucene","4是一个全文检索引擎的架构，提供了完整的创建索引和查询索引");
		Document document=LuceneUtil.javabean2Document(newArticle);
		IndexWriter  iw=new IndexWriter(LuceneUtil.getD(), LuceneUtil.getAnalyzer(), LuceneUtil.getMfl()); 
		
		iw.updateDocument(new Term("id","4"), document);
		iw.close();
	}
	@Test
	public void delete() throws  LockObtainFailedException, IOException{
		IndexWriter  iw=new IndexWriter(LuceneUtil.getD(), LuceneUtil.getAnalyzer(), LuceneUtil.getMfl()); 
		
		iw.deleteDocuments(new Term("id","2"));
		iw.close();
	}
	
	
	public void deleteAll(){
		
	}
	
	@Test
	public void findAll() throws Exception{
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
