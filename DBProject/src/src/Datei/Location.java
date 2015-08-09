package src.Datei;

import java.util.StringTokenizer;

public class Location implements Comparable<Location> {

	private int pageNumber;
	private int rowNumber;

	public Location(int p, int r) {
		pageNumber = p;
		rowNumber = r;
	}

	public Location(String s) {
		StringTokenizer st = new StringTokenizer(s, "#");
		pageNumber = Integer.parseInt(st.nextToken());
		rowNumber = Integer.parseInt(st.nextToken());
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	@Override
	public int compareTo(Location o) {
		// TODO Auto-generated method stub
		return this.pageNumber == o.pageNumber && this.rowNumber == o.rowNumber ? 0 : 2523;
	}

}
