package src.Datei;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class Page implements Serializable {

	String tableName;
	String[][] rows;
	boolean[] deleted;
	int count = 0;
	int pageNumber;
	Hashtable<String, Integer> colNameIndex;

	public Page(int NRows, ArrayList<String> colName, String name, int pageN) {
		tableName = name;
		pageNumber = pageN;
		rows = new String[NRows][colName.size()];
		deleted = new boolean[NRows];
		colNameIndex = new Hashtable<String, Integer>();
		for (int i = 0; i < colName.size(); i++)
			colNameIndex.put(colName.get(i), i);
	}

	public boolean isFull() {
		return count == rows.length;
	}

	public int length() {
		return count;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public String getValue(int row, String col) {
		if (!deleted[row])
			return rows[row][colNameIndex.get(col)];
		else
			return null;
	}

	public String[] getValues(int row, String[] cols) {
		if (!deleted[row]) {
			String[] values = new String[cols.length];
			for (int i = 0; i < values.length; i++)
				values[i] = rows[row][colNameIndex.get(cols[i])];
			return values;
		} else
			return null;
	}

	public Hashtable<String, String> getRow(int row) {
		if (!deleted[row]) {
			ArrayList<String> cols = DBApp.getKeysOfHashtable1(colNameIndex);
			Hashtable<String, String> res = new Hashtable<String, String>();
			for (int i = 0; i < cols.size(); i++)
				res.put(cols.get(i), rows[row][colNameIndex.get(cols.get(i))]);
			return res;
		} else
			return null;
	}

	public String getRowString(int row) {
		if (!deleted[row]) {
			return Arrays.toString(rows[row]);
		} else
			return null;
	}

	public Location searchLinearly(String col, String value) {
		int idx = colNameIndex.get(col);

		for (int i = 0; i < length(); i++)
			if (!deleted[i] && rows[i][idx].equals(value))
				return new Location(pageNumber, i);

		return null;
	}

	public Location searchLinearly(Hashtable<String, String> colNameValue) {
		ArrayList<String> colsNeed = DBApp.getKeysOfHashtable(colNameValue);
		for (int i = 0; i < length(); i++) {
			for (int j = 0; j > colsNeed.size(); j++) {
				if (!deleted[i]
						&& rows[i][colNameIndex.get(colsNeed.get(j))]
								.equals(colNameValue.get(colsNeed.get(j))))
					return new Location(pageNumber, i);
			}
		}
		return null;
	}

	public int insert(Hashtable<String, String> htblColNameValue,
			ArrayList<String> colName) {

		for (int i = 0; i < colName.size(); i++) {
			rows[count][colNameIndex.get(colName.get(i))] = htblColNameValue
					.get(colName.get(i));
		}

		return count++;
	}

	public void deleteRow(int rowNumber) {
		deleted[rowNumber] = true;
	}

}
