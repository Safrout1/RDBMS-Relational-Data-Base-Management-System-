package src.Datei;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public class DBAppTest {

	/**
	 * @param args
	 * @throws DBAppException
	 * @throws DBEngineException 
	 */
	public static void main(String[] args) throws DBAppException, DBEngineException {
		DBApp a = new DBApp();

		/**/Hashtable<String, String> h1 = new Hashtable<String, String>();
		h1.put("ID", "Integer");
		h1.put("name", "String");
		h1.put("email", "String");
		a.createTable("Employee", h1, null, "ID");
		
		a.saveAll();/**/
		
		/**/a.CreateIndex("Employee", "name");
		a.saveAll();/**/
		
		/**/ArrayList<String> arr = new ArrayList<String>();
		arr.add("name");
		arr.add("email");
		a.createMultiDimIndex("Employee", arr);
		a.saveAll();/**/
		
		/**/Hashtable<String, String> h2 = new Hashtable<String, String>();
		h2.put("ID", "6420");
		h2.put("name", "khalid");
		h2.put("email", "khalid.abdulnasser1@gmail.com");
		a.insertIntoTable("Employee", h2);
		a.saveAll();/**/
		
		/**/Hashtable<String, String> h3 = new Hashtable<String, String>();
		h3.put("ID", "325");
		h3.put("name", "khefsdalid");
		h3.put("email", "khasdgsdlid.abdulnasser1@gmail.com");
		a.insertIntoTable("Employee", h3);
		a.saveAll();/**/
		
		/**/Hashtable<String, String> h4 = new Hashtable<String, String>();
		h4.put("ID", "6325420");
		h4.put("name", "khasfaarlid");
		h4.put("email", "khaliasfasd.abdulnasser1@gmail.com");
		a.insertIntoTable("Employee", h4);
		a.saveAll();/**/
		
		/**/Hashtable<String, String> h5 = new Hashtable<String, String>();
		h5.put("ID", "6325420");
		
		Iterator<String> sel1 = a.selectFromTable("Employee", h5, "AND");
		
		while (sel1.hasNext())
			System.out.println(sel1.next() + ", ");
		a.saveAll();/**/
		
		/**/Hashtable<String, String> h6 = new Hashtable<String, String>();
		h6.put("ID", "6325420");
		h6.put("name", "khasfaarlid");
		
		Iterator<String> sel2 = a.selectFromTable("Employee", h6, "OR");
		
		while (sel2.hasNext())
			System.out.println(sel2.next() + ", ");
		a.saveAll();/**/
		
		/**/a.deleteFromTable("Employee", h6, "AND");
		
		a.saveAll();/**/

	}

}
