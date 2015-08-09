package src.Datei;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public interface DBAppInterface {
	public void init();

	public void createTable(String strTableName,
			Hashtable<String, String> htblColNameType,
			Hashtable<String, String> htblColNameRefs, String strKeyColName)
			throws DBAppException;

	public void CreateIndex(String strTableName, String strColName)
			throws DBAppException;

	public void createMultiDimIndex(String strTableName,
			ArrayList<String> htblColNames) throws DBAppException;

	public void insertIntoTable(String strTableName,
			Hashtable<String, String> htblColNameValue) throws DBAppException;

	public void deleteFromTable(String strTableName,
			Hashtable<String, String> htblColNameValue, String strOperator)
			throws DBEngineException;

	public Iterator<String> selectFromTable(String strTable,
			Hashtable<String, String> htblColNameValue, String strOperator)
			throws DBEngineException;

	public void saveAll() throws DBEngineException;
}
