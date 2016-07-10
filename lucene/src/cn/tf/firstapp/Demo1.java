package cn.tf.firstapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import cn.tf.entity.Article;

public class Demo1 {

	/**
	 * 创建索引库
	 * 将article对象放入索引库中的原始记录表中，从而形成词汇表
	 * @throws IOException 
	 * 
	 */
	//@Test
	public void createIndexDB() throws IOException{
		//创建article对象
		Article  at=new Article(1, "lucene","Lucene是apache软件基金会发布的一个开放源代码的全文检索引擎工具包");
		//创建Document对象
		Document doc=new Document();
		//将article对象中的3个属性分别绑定到document对象中
		
		/*
		 * 
		 * 参数1：document对象中的属性名叫xid，article对象中的属性名叫id
		 * 参数2：document对象中的xid的值，与article对象中相同
		 * 参数3：是否将xid属性值存入词汇表,YES表示会存入，NO表示不会存入
		 * 参数4：是否将xid属性值进行分词，ANALYZED表示会进行词汇拆分 ，NOT_ANALYZED不会进行拆分
		 * 
		 * 项目中提倡非id值进行词汇拆分，提倡非id值存入词汇表
		 */
		
		doc.add(new Field("xid",at.getId().toString(),Store.YES,Index.ANALYZED));
		doc.add(new Field("xtitle",at.getTitle().toString(),Store.YES,Index.ANALYZED));
		doc.add(new Field("xcontent",at.getContent().toString(),Store.YES,Index.ANALYZED));
		
		//将document对象写入lucene索引库
		/*
		 * 参数1:lucene索引库最终对应于硬盘中的目录
		 * 参数2:将文本拆分的策略,一个策略就是一个具体的实现类
		 * 参数3:最多将文本拆分的词汇，LIMITED表示1万个，只取前1万个
		 * 
		 */
		Directory d=FSDirectory.open(new File("D:/IndexDB"));
		Analyzer a=new StandardAnalyzer(Version.LUCENE_30);
		MaxFieldLength mfl=MaxFieldLength.LIMITED;
		
		IndexWriter iw=new IndexWriter(d, a, mfl);
		iw.addDocument(doc);
		//关闭字符流对象
		iw.close();
		
	}
	
	/**
	 * 根据关键字从索引库中搜索符合条件的记录
	 * 
	 * @throws IOException 
	 * @throws ParseException 
	 * 
	 */
	@Test
	public void findIndexDB() throws IOException, ParseException{
		//搜索"源"这个字
		String keyword="apache";
		List<Article>  atl=new ArrayList<Article>();
		
		Directory d=FSDirectory.open(new File("D:/IndexDB"));
		Analyzer a=new StandardAnalyzer(Version.LUCENE_30);
		MaxFieldLength mfl=MaxFieldLength.LIMITED;
		
		//创建IndexSearch字符流对象
		IndexSearcher  is=new IndexSearcher(d);
		
		/*
		 * 参数1:使用分词器的版本,qitqt
		 * 参数2:针对document对象中的哪个属性进行搜索
		 */
		Version version=Version.LUCENE_30;
		Analyzer analyzer=new StandardAnalyzer(version);
		QueryParser queryParser=new QueryParser(version,"xcontent",analyzer);
		//封装查询关键字
		Query query=queryParser.parse(keyword);
		/*
		 * 参数1:查询对象及封装关键字的对象
		 * 参数2:MAX_RECORD表示如果根据关键字搜索出来的内容教多，只取前MAX_RECORD个
		 * 
		 */
		int MAX_RECORD=100;
		TopDocs td=is.search(query,MAX_RECORD);
		//迭代词汇表中符合条件的编号
		for(int i=0;i<td.scoreDocs.length;i++){
			//取出每一个编号和分数scoreDoc对象
			ScoreDoc scoreDoc=td.scoreDocs[i];
			//取出每一个编号
			int no=scoreDoc.doc; 
			//根据编号去索引库中的原始记录表中查询对应的document对象
			Document document=is.doc(no);
			String xid=document.get("xid");
			String xtitle=document.get("xtitle");
			String xcontent=document.get("xcontent");
			//封装到article对象中
			Article article=new Article(Integer.parseInt(xid),xtitle,xcontent);
			//将article对象加入到list集合中
			atl.add(article);
		
		}
		//迭代结果集
		for(Article a1:atl){
			System.out.println(a1);
		}
	}

}
