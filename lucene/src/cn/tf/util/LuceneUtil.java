package cn.tf.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import cn.tf.entity.Article;

public class LuceneUtil {
	
	private static Directory d;
	private static Version version;
	private static Analyzer analyzer;
	private static  MaxFieldLength mfl;
	
	
	//防止外界new该帮助类
	static{
		try {
			d=FSDirectory.open(new File("D:/IndexDB"));
			version=Version.LUCENE_30;
			analyzer=new StandardAnalyzer(version);
			mfl=MaxFieldLength.LIMITED;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}
	private LuceneUtil(){}

	public static Directory getD() {
		return d;
	}

	public static void setD(Directory d) {
		LuceneUtil.d = d;
	}


	public static Version getVersion() {
		return version;
	}



	public static void setVersion(Version version) {
		LuceneUtil.version = version;
	}

	public static Analyzer getAnalyzer() {
		return analyzer;
	}

	public static void setAnalyzer(Analyzer analyzer) {
		LuceneUtil.analyzer = analyzer;
	}

	public static MaxFieldLength getMfl() {
		return mfl;
	}



	public static void setMfl(MaxFieldLength mfl) {
		LuceneUtil.mfl = mfl;
	}


	//将javabean转成document对象
	public static Document javabean2Document(Object obj) throws Exception{
		
		Document document=new Document();
		
		//获取obj引用的对象字节码
		Class  clazz=obj.getClass();
		java.lang.reflect.Field[] reflectFields=clazz.getDeclaredFields();
		//迭代
		for(java.lang.reflect.Field reflectField:reflectFields){
			//反射
			reflectField.setAccessible(true);
			//获取属性名,
			String name=reflectField.getName();
			String methodName="get"+name.substring(0,1).toUpperCase()+name.substring(1);
			//获取方法
			Method method=clazz.getMethod(methodName, null);
			//执行方法
			String value=method.invoke(obj, null).toString();
			//加入到document对象中去
			document.add(new Field(name,value,Store.YES,Index.ANALYZED));
			
			
		}
		//返回document对象
		return document;
	}
	//将document转成avabean对象
	public static Object document2Javabean(Document document, Class clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException{
		Object obj=clazz.newInstance();
		
		java.lang.reflect.Field[] reflectFields = clazz.getDeclaredFields();
		for(java.lang.reflect.Field field : reflectFields){
			field.setAccessible(true);
			String name= field.getName();
			String value = document.get(name);
			BeanUtils.setProperty(obj,name,value);
		}	

		return obj;
	
	}
	
	public static void main(String[] args) throws Exception {
		Article  at=new Article(1, "lucene","Lucene是apache软件基金会发布的一个开放源代码的全文检索引擎工具包");
		Document document=LuceneUtil.javabean2Document(at);
		
		System.out.println(document.get("id"));
		System.out.println(document.get("title"));
		System.out.println(document.get("content"));
		
		
		Article  article=(Article) LuceneUtil.document2Javabean(document,Article.class);
		
		System.out.println(article);
	}
	

}
