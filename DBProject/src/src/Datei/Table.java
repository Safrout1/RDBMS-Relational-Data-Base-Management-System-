package src.Datei;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;

public class Table implements Serializable {

	String tableName;
	int numberOfPages = 0;
	ArrayList<String> singleIndexedColumns;
	ArrayList<String[]> multiIndexedColumns;
	Hashtable<String, String> ColNameType;
	Hashtable<String, String> ColNameRefs;
	String KeyColName;

	public Table(String name, Hashtable<String, String> htblColNameType,
			Hashtable<String, String> htblColNameRefs, String strKeyColName) {
		tableName = name;
		singleIndexedColumns = new ArrayList<String>();
		multiIndexedColumns = new ArrayList<String[]>();
		KeyColName = strKeyColName;
		ColNameType = htblColNameType;
		ColNameRefs = htblColNameRefs;

	}

	// Done!!! :D :D
	public Page addPage(int numberOfRows, ArrayList<String> colName)
			throws IOException {
		numberOfPages++;
		File pagesFile = new File("src/classes/Datei/" + tableName
				+ numberOfPages + ".class");
		Page p1 = new Page(numberOfRows, colName, tableName, numberOfPages);
		FileOutputStream fileOut = new FileOutputStream(pagesFile);
		ObjectOutputStream outObject = new ObjectOutputStream(fileOut);
		outObject.writeObject(p1);
		outObject.close();
		fileOut.close();
		return p1;
	}

	// Done !!! :D :D :D
	public void addSingleIndex(String column, float loadFactor, int bucketSize)
			throws IOException, ClassNotFoundException, DBAppException {

		if (!ColNameType.containsKey(column))
			throw new DBAppException();

		singleIndexedColumns.add(column);

		LinearHashTable LHT = new LinearHashTable(tableName, column,
				loadFactor, bucketSize);

		for (int i = 1; i <= numberOfPages; i++) {
			Page p = (Page) DBApp.readObj("src/classes/Datei/" + tableName + i
					+ ".class");
			for (int j = 0; j < p.length(); j++) {
				String val = p.getValue(j, column);
				if (val != null)
					LHT.put(p.getValue(j, column), p.getPageNumber() + "#" + j);
			}
		}

		DBApp.writeObj(LHT, "src/classes/Datei/Hashtable#" + tableName + "#"
				+ column + ".class");

	}

	// Done!!! :D :D
	public void addMultiIndex(String columns[]) throws ClassNotFoundException,
			IOException, DBAppException {

		for (int i = 0; i < columns.length; i++) {
			if (!ColNameType.containsKey(columns[i]))
				throw new DBAppException();
		}

		Arrays.sort(columns);
		multiIndexedColumns.add(columns);

		KDTree kdtree = new KDTree(columns.length);

		for (int i = 1; i <= numberOfPages; i++) {
			Page p = (Page) DBApp.readObj("src/classes/Datei/" + tableName + i
					+ ".class");
			for (int j = 0; j < p.length(); j++) {
				String[] vals = p.getValues(j, columns);
				if (vals != null)
					kdtree.insert(p.getValues(j, columns), p.getPageNumber()
							+ "#" + j);
			}
		}

		String columnNames = DBApp.getKDTreeName(columns);

		DBApp.writeObj(kdtree, "src/classes/Datei/KDTree#" + tableName + "#"
				+ columnNames + ".class");
	}

	// Done !!! :D :D
	public void insertRecord(Hashtable<String, String> htblColNameValue,
			int numberOfRows) throws ClassNotFoundException, IOException,
			DBAppException {

		ArrayList<String> colName = DBApp.getKeysOfHashtable(htblColNameValue);

		for (int i = 0; i < colName.size(); i++) {
			if (!ColNameType.containsKey(colName.get(i)))
				throw new DBAppException();
		}

		Page p = (Page) DBApp.readObj("src/classes/Datei/" + tableName
				+ numberOfPages + ".class");

		if (p.isFull()) {
			p = addPage(numberOfRows, colName);
		}

		int row = p.insert(htblColNameValue, colName);

		for (int i = 0; i < singleIndexedColumns.size(); i++)
			if (htblColNameValue.containsKey(singleIndexedColumns.get(i))) {
				LinearHashTable LHT = (LinearHashTable) DBApp
						.readObj("src/classes/Datei/Hashtable#" + tableName
								+ "#" + singleIndexedColumns.get(i) + ".class");
				LHT.put(htblColNameValue.get(singleIndexedColumns.get(i)),
						p.getPageNumber() + "#" + row);
				DBApp.writeObj(LHT, "src/classes/Datei/Hashtable#" + tableName
						+ "#" + singleIndexedColumns.get(i) + ".class");
			}

		for (int i = 0; i < multiIndexedColumns.size(); i++) {
			String[] thisKDTreeColumns = multiIndexedColumns.get(i);
			boolean weNeedIt = true;
			for (int j = 0; j < thisKDTreeColumns.length; j++)
				weNeedIt = weNeedIt
						&& htblColNameValue.containsKey(thisKDTreeColumns[j]);
			String thisKDTreeName = DBApp.getKDTreeName(thisKDTreeColumns);
			KDTree kdtree = (KDTree) DBApp.readObj("src/classes/Datei/KDTree#"
					+ tableName + "#" + thisKDTreeName + ".class");
			kdtree.insert(p.getValues(row, thisKDTreeColumns),
					p.getPageNumber() + "#" + row);
			DBApp.writeObj(kdtree, "src/classes/Datei/KDTree#" + tableName
					+ "#" + thisKDTreeName + ".class");
		}

		DBApp.writeObj(p, "src/classes/Datei/" + tableName + p.getPageNumber()
				+ ".class");
	}

	public Iterator<String> select(Hashtable<String, String> htblColNameValue,
			String strOperator) throws ClassNotFoundException, IOException,
			DBEngineException {

		ArrayList<String> result = new ArrayList<String>();
		ArrayList<Location> locs = search(htblColNameValue, strOperator);

		if (strOperator.equalsIgnoreCase("OR")) {
			for (int i = 0; i < locs.size(); i++) {
				Location loc = locs.get(i);
				Page p = (Page) DBApp.readObj("src/classes/Datei/" + tableName
						+ loc.getPageNumber() + ".class");

				String lol = p.getRowString(loc.getRowNumber());
				if (lol != null)
					result.add(lol);
			}
		} else if (strOperator.equalsIgnoreCase("AND")) {
			if (!(locs.size() == 0)) {
				Location l1 = locs.get(0);
				boolean areSame = true;
				for (int i = 1; i < locs.size(); i++)
					if (l1.compareTo(locs.get(i)) != 0) {
						areSame = false;
						break;
					}
				if (areSame) {
					Page p = (Page) DBApp.readObj("src/classes/Datei/"
							+ tableName + l1.getPageNumber() + ".class");
					String lol = p.getRowString(l1.getRowNumber());
					if (lol != null)
						result.add(lol);
				}
			}
		} else
			throw new DBEngineException();

		return result.iterator();
	}

	public ArrayList<Location> search(
			Hashtable<String, String> htblColNameValue, String strOperator)
			throws ClassNotFoundException, IOException, DBEngineException {
		ArrayList<String> cols = DBApp.getKeysOfHashtable(htblColNameValue);

		for (int i = 0; i < cols.size(); i++) {
			if (!ColNameType.containsKey(cols.get(i)))
				throw new DBEngineException();
		}

		ArrayList<Location> locs = new ArrayList<Location>();

		if (strOperator.equalsIgnoreCase("OR") || cols.size() == 1)
			for (int i = 0; i < cols.size(); i++)
				if (singleIndexedColumns.contains(cols.get(i))) {
					LinearHashTable LHT = (LinearHashTable) DBApp
							.readObj("src/classes/Datei/Hashtable#" + tableName
									+ "#" + cols.get(i) + ".class");

					Location l = new Location(LHT.get(htblColNameValue.get(cols
							.get(i))));

					locs.add(l);
				} else {
					for (int j = 1; j <= numberOfPages; j++) {
						Page p = (Page) DBApp.readObj("src/classes/Datei/"
								+ tableName + j + ".class");

						Location lo = p.searchLinearly(cols.get(i),
								htblColNameValue.get(cols.get(i)));
						if (lo != null)
							locs.add(lo);
					}
				}
		else if (strOperator.equalsIgnoreCase("AND")) {
			boolean linear = false;
			for (int i = 0; i < cols.size(); i++) {
				String col = cols.get(i);
				if (!singleIndexedColumns.contains(col)) {
					linear = true;
					break;
				}

				boolean found = false;
				for (int j = 0; j < multiIndexedColumns.size(); j++) {
					String kdtree[] = multiIndexedColumns.get(j);
					for (int k = 0; k < kdtree.length; k++)
						if (kdtree[k].equals(col)) {
							found = true;
							break;
						}
					if (found)
						break;
				}
				if (!found) {
					linear = true;
					break;
				}
			}

			if (linear)
				for (int i = 1; i < numberOfPages; i++) {
					Page p = (Page) DBApp.readObj("src/classes/Datei/"
							+ tableName + i + ".class");
					Location ans = p.searchLinearly(htblColNameValue);
					if (ans != null) {
						locs.add(ans);
						break;
					}
				}
			else {
				Hashtable<String, Integer> colNameIndex = new Hashtable<String, Integer>();
				for (int i = 0; i < cols.size(); i++)
					colNameIndex.put(cols.get(i), i);

				boolean searched[] = new boolean[cols.size()];

				for (int i = 0; i < multiIndexedColumns.size(); i++) {
					boolean hasWeWant = true;
					boolean hasNotSearched = false;
					String[] kd = multiIndexedColumns.get(i);

					for (int j = 0; j < kd.length; j++) {
						hasWeWant = hasWeWant && cols.contains(kd[j]);
						hasNotSearched = hasNotSearched
								|| !searched[colNameIndex.get(kd[j])];
					}

					if (hasWeWant && hasNotSearched) {
						String kdName = DBApp.getKDTreeName(kd);
						KDTree kdtree = (KDTree) DBApp
								.readObj("src/classes/Datei/KDTree#"
										+ tableName + "#" + kdName + ".class");

						String[] values = new String[kd.length];
						for (int j = 0; j < values.length; j++) {
							values[i] = htblColNameValue.get(kd[i]);
							searched[colNameIndex.get(kd[i])] = true;
						}

						Location l = new Location(
								(String) kdtree.search(values));
						locs.add(l);
					}
				}

				for (int i = 0; i < singleIndexedColumns.size(); i++) {
					String lht = singleIndexedColumns.get(i);
					if (cols.contains(lht) && !searched[colNameIndex.get(lht)]) {
						LinearHashTable LHT = (LinearHashTable) DBApp
								.readObj("src/classes/Datei/Hashtable#"
										+ tableName + "#" + lht + ".class");

						Location l = new Location(LHT.get(htblColNameValue
								.get(lht)));
						locs.add(l);
					}
				}

			}
		}
		return locs;
	}

	public void delete(Hashtable<String, String> htblColNameValue,
			String strOperator) throws ClassNotFoundException, IOException,
			DBEngineException {

		ArrayList<Location> locs = search(htblColNameValue, strOperator);

		if (strOperator.equalsIgnoreCase("OR")) {
			for (int i = 0; i < locs.size(); i++) {
				Location loc = locs.get(i);
				Page p = (Page) DBApp.readObj("src/classes/Datei/" + tableName
						+ loc.getPageNumber() + ".class");
				p.deleteRow(loc.getRowNumber());
			}
		} else if (strOperator.equalsIgnoreCase("AND")) {
			if (!(locs.size() == 0)) {
				Location l1 = locs.get(0);
				boolean areSame = true;
				for (int i = 1; i < locs.size(); i++)
					if (l1.compareTo(locs.get(i)) != 0) {
						areSame = false;
						break;
					}
				if (areSame) {
					Page p = (Page) DBApp.readObj("src/classes/Datei/"
							+ tableName + l1.getPageNumber() + ".class");
					p.deleteRow(l1.getRowNumber());
				}
			}
		} else
			throw new DBEngineException();

	}

	public boolean indexed(String key) {
		if (singleIndexedColumns.contains(key))
			return true;

		for (int i = 0; i < multiIndexedColumns.size(); i++) {
			String[] s = multiIndexedColumns.get(i);
			for (int j = 0; j < s.length; j++) {
				if (s[i].equals(key))
					return true;
			}
		}

		return false;
	}
}
