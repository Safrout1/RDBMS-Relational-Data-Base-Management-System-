package src.Datei;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Hashtable;

public class Main {

	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws DBAppException
	 */

	public static void emptyFile(String path) throws IOException {
		PrintWriter out = new PrintWriter(new FileWriter(path));
		out.close();
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException, DBAppException, ClassNotFoundException {
		// TODO Auto-generated method stub

		/*
		 * DBApp a = new DBApp();
		 * 
		 * a.init();
		 * 
		 * Hashtable<String, String> h1 = new Hashtable<String, String>();
		 * h1.put("ID", "Integer"); h1.put("name", "String"); h1.put("email",
		 * "String"); a.createTable("Employee", h1, null, "ID");
		 */

		/*
		 * emptyFile("src/data/metadata.csv"); File x = new
		 * File("src/data/Employee.txt"); x.delete();
		 */

		/*
		 * Page p = new Page(5, 324, "sdgs"); File pagesFile = new
		 * File("src/classes/Datei/" + "sdgs" + ".class"); FileOutputStream
		 * fileOut = new FileOutputStream(pagesFile); ObjectOutputStream
		 * outObject = new ObjectOutputStream(fileOut);
		 * outObject.writeObject(p); outObject.close(); fileOut.close();
		 */

		/*
		 * File x = new File("src/classes/Datei/" + "sdgs" + ".class");
		 * x.delete();
		 */
		/*
		 * FileInputStream fileIn = new FileInputStream(new
		 * File("src/classes/Datei/" + "sdgs" + ".class")); ObjectInputStream in
		 * = new ObjectInputStream(fileIn);
		 * System.out.println(fileIn.available()); while (fileIn.available() >
		 * 0) { Page p = (Page) in.readObject();
		 * 
		 * System.out.println(p.tableName); }
		 */

		// System.out.println(Float.parseFloat("0.75f"));

		// Page p = new Page(200, 5, "student");
		// Page p2 = new Page(300, 54, "lol");
		// File f = new File("hello" + ".txt");
		/*
		 * FileOutputStream fileOut = new FileOutputStream(f);
		 * ObjectOutputStream outObject = new ObjectOutputStream(fileOut);
		 * outObject.writeObject(p); outObject.writeObject(p2);
		 * outObject.close(); fileOut.close();
		 */

		/*
		 * FileInputStream fileIn = new FileInputStream(f); ObjectInputStream in
		 * = new ObjectInputStream(fileIn);
		 * System.out.println(fileIn.available()); while (fileIn.available() >
		 * 0) { Page p = (Page) in.readObject();
		 * 
		 * System.out.println(p.tableName); }
		 */

		/*
		 * Page p = new Page(200, 5, "student"); p.rows[0][0] = "khalid";
		 * 
		 * File f = new File("hello" + ".class"); FileOutputStream fileOut = new
		 * FileOutputStream(f); ObjectOutputStream outObject = new
		 * ObjectOutputStream(fileOut); outObject.writeObject(p);
		 * outObject.close(); fileOut.close();
		 */

		/*
		 * File f = new File("hello" + ".class"); FileInputStream fileIn = new
		 * FileInputStream(f); ObjectInputStream in = new
		 * ObjectInputStream(fileIn); Page p = (Page) in.readObject();
		 * 
		 * System.out.println(p.tableName); System.out.println(p.rows[0][0]);
		 */
		
		/*Hashtable<String, String> h1 = new Hashtable<String, String>();
		h1.put("ID", "Integer");
		h1.put("name", "String");
		h1.put("email", "String");
		
		Table x = new Table("Employee", h1, null, "ID");
		
		DBApp.writeObj(x, x.tableName + ".class");
		
		x = null;*/
		
		//Table x = (Table) DBApp.readObj("Employee.class");
		//System.out.println(x.tableName);
		
		emptyFile("src/data/metadata.csv");
	}

}
