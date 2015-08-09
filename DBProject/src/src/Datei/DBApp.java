package src.Datei;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.StringTokenizer;

public class DBApp implements DBAppInterface {

	/**
	 * @param args
	 */

	Properties properties;
	Hashtable<String, Table> tables;
	File metadata;
	float loadFactor;
	int numberOfRowsInPage;

	public DBApp() {
		init();
	}

	// Done :D :D
	public void init() {

		try {
			properties = new Properties();
			properties.load(new FileReader(new File(
					"src/config/DBApp.properties")));
			loadFactor = Float.parseFloat(properties.getProperty("LoadFactor"));
			numberOfRowsInPage = Integer.parseInt(properties
					.getProperty("NRows"));
			tables = new Hashtable<String, Table>();

			metadata = new File("src/data/metadata.csv");
			BufferedReader bf = new BufferedReader(new FileReader(metadata));
			while (bf.ready()) {
				StringTokenizer st = new StringTokenizer(bf.readLine(), ",");
				String tableName = st.nextToken();
				File f = new File("src/classes/Datei/" + tableName + ".class");
				FileInputStream fileIn = new FileInputStream(f);
				ObjectInputStream ObjectIn = new ObjectInputStream(fileIn);
				Table table = (Table) ObjectIn.readObject();
				tables.put(tableName, table);
				fileIn.close();
				ObjectIn.close();
			}
			bf.close();
		} catch (NumberFormatException | ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Done !!! :D :D
	public void createTable(String strTableName,
			Hashtable<String, String> htblColNameType,
			Hashtable<String, String> htblColNameRefs, String strKeyColName)
			throws DBAppException {

		try {
			if (tables.contains(strTableName))
				throw new DBAppException();

			ArrayList<String> colName = new ArrayList<String>();

			PrintWriter out = new PrintWriter(new FileWriter(metadata, true));

			Iterator<Entry<String, String>> it = htblColNameType.entrySet()
					.iterator();
			while (it.hasNext()) {
				Entry<String, String> next = it.next();
				colName.add(next.getKey());
				out.println(strTableName
						+ ","
						+ next.getKey()
						+ ","
						+ next.getValue()
						+ ","
						+ ((next.getKey()).equals(strKeyColName))
						+ ","
						+ ((next.getKey()).equals(strKeyColName))
						+ ","
						+ ((htblColNameRefs != null && htblColNameRefs
								.containsKey(next.getKey())) ? htblColNameRefs
								.get(next.getKey()) : null));
			}
			out.flush();
			out.close();

			Table table = new Table(strTableName, htblColNameType,
					htblColNameRefs, strKeyColName);
			table.addPage(numberOfRowsInPage, colName);

			tables.put(strTableName, table);

			CreateIndex(strTableName, strKeyColName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				saveAll();
			} catch (DBEngineException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	// Done !!! :D :D
	public void CreateIndex(String strTableName, String strColName)
			throws DBAppException {

		try {
			tables.get(strTableName).addSingleIndex(strColName, loadFactor,
					numberOfRowsInPage);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				saveAll();
			} catch (DBEngineException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	// Done !!! :D :D
	public void createMultiDimIndex(String strTableName,
			ArrayList<String> arlstColNames) throws DBAppException {

		try {
			String[] columns = new String[arlstColNames.size()];
			for (int i = 0; i < columns.length; i++)
				columns[i] = arlstColNames.get(i);
			tables.get(strTableName).addMultiIndex(columns);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				saveAll();
			} catch (DBEngineException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	// Done !!! :D :D
	public void insertIntoTable(String strTableName,
			Hashtable<String, String> htblColNameValue) throws DBAppException {

		try {
			tables.get(strTableName).insertRecord(htblColNameValue,
					numberOfRowsInPage);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				saveAll();
			} catch (DBEngineException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	// Done !!! :D :D
	public void deleteFromTable(String strTableName,
			Hashtable<String, String> htblColNameValue, String strOperator)
			throws DBEngineException {

		try {
			tables.get(strTableName).delete(htblColNameValue, strOperator);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveAll();
		}

	}

	// Done !!! :D :D
	public Iterator<String> selectFromTable(String strTable,
			Hashtable<String, String> htblColNameValue, String strOperator)
			throws DBEngineException {

		try {
			return tables.get(strTable).select(htblColNameValue, strOperator);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			saveAll();
			return null;
		}

	}

	// Done !!! :D :D
	public void saveAll() throws DBEngineException {

		try {
			PrintWriter out = new PrintWriter(new FileWriter(metadata));
			Iterator<Entry<String, Table>> it = tables.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Table> next = it.next();
				DBApp.writeObj(next.getValue(),
						"src/classes/Datei/" + next.getKey() + ".class");
				Table t = next.getValue();
				Iterator<Entry<String, String>> it2 = t.ColNameType.entrySet()
						.iterator();
				while (it2.hasNext()) {
					Entry<String, String> next2 = it2.next();
					out.println(t.tableName
							+ ","
							+ next2.getKey()
							+ ","
							+ next2.getValue()
							+ ","
							+ ((next2.getKey()).equals(t.KeyColName))
							+ ","
							+ t.indexed(next2.getKey())
							+ ","
							+ ((t.ColNameRefs != null && t.ColNameRefs
									.containsKey(next2.getKey())) ? t.ColNameRefs
									.get(next2.getKey()) : null));
				}
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Done !!! :D :D
	public static void writeObj(Object Obj, String path) throws IOException {
		File f = new File(path);
		FileOutputStream fileOut = new FileOutputStream(f);
		ObjectOutputStream outObject = new ObjectOutputStream(fileOut);
		outObject.writeObject(Obj);
		outObject.close();
		fileOut.close();
	}

	// Done !!! :D :D
	public static Object readObj(String path) throws IOException,
			ClassNotFoundException {
		File f = new File(path);
		FileInputStream fileIn = new FileInputStream(f);
		ObjectInputStream in = new ObjectInputStream(fileIn);
		Object obj = in.readObject();
		fileIn.close();
		in.close();
		return obj;
	}

	// Done !!! :D :D
	public static ArrayList<String> getKeysOfHashtable(
			Hashtable<String, String> htbl) {
		ArrayList<String> keyName = new ArrayList<String>();
		Iterator<Entry<String, String>> it = htbl.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> next = it.next();
			keyName.add(next.getKey());
		}
		return keyName;
	}

	public static ArrayList<String> getKeysOfHashtable1(
			Hashtable<String, Integer> htbl) {
		ArrayList<String> keyName = new ArrayList<String>();
		Iterator<Entry<String, Integer>> it = htbl.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Integer> next = it.next();
			keyName.add(next.getKey());
		}
		return keyName;
	}

	// Done !!! :D :D
	public static ArrayList<String> getValuesOfHashtable(
			Hashtable<String, String> htbl) {
		ArrayList<String> ValueName = new ArrayList<String>();
		Iterator<Entry<String, String>> it = htbl.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> next = it.next();
			ValueName.add(next.getKey());
		}
		return ValueName;
	}

	// Done !!! :D :D
	public static String getKDTreeName(String[] columns) {
		StringBuilder columnNamesBuilder = new StringBuilder("");
		for (int i = 0; i < columns.length - 1; i++) {
			columnNamesBuilder.append(columns[i]);
			columnNamesBuilder.append("#");
		}
		columnNamesBuilder.append(columns[columns.length - 1]);

		return new String(columnNamesBuilder);
	}

}
