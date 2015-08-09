package src.Datei;

import java.io.Serializable;

class HRect implements Serializable {

	/**
	 * @uml.property name="min"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	protected HPoint min;
	/**
	 * @uml.property name="max"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	protected HPoint max;

	protected HRect(int ndims) {
		min = new HPoint(ndims);
		max = new HPoint(ndims);
	}

	protected HRect(HPoint vmin, HPoint vmax) {

		min = (HPoint) vmin.clone();
		max = (HPoint) vmax.clone();
	}

	protected Object clone() {

		return new HRect(min, max);
	}

	// from Moore's eqn. 6.6
	protected HPoint closest(HPoint t) {

		HPoint p = new HPoint(t.coord.length);

		for (int i = 0; i < t.coord.length; ++i) {

			if (t.coord[i].compareTo(min.coord[i]) <= 0) {
				p.coord[i] = min.coord[i];
			} else if (t.coord[i].compareTo(min.coord[i]) >= 0) {
				p.coord[i] = max.coord[i];
			} else {
				p.coord[i] = t.coord[i];
			}
		}

		return p;
	}

	// used in initial conditions of kDTree.nearest()
	protected static HRect infiniteHRect(int d) {

		HPoint vmin = new HPoint(d);
		HPoint vmax = new HPoint(d);

		for (int i = 0; i < d; ++i) {
			vmin.coord[i] = Integer.MIN_VALUE;
			vmax.coord[i] = Integer.MAX_VALUE;
		}

		return new HRect(vmin, vmax);
	}

	// currently unused
	protected HRect intersection(HRect r) {

		HPoint newmin = new HPoint(min.coord.length);
		HPoint newmax = new HPoint(min.coord.length);

		for (int i = 0; i < min.coord.length; ++i) {
			newmin.coord[i] = (min.coord[i].compareTo(r.min.coord[i])) > 0 ? min.coord[i]
					: r.min.coord[i];
			newmax.coord[i] = (max.coord[i].compareTo(r.max.coord[i])) > 0 ? max.coord[i]
					: r.max.coord[i];

			if (newmin.coord[i].compareTo(newmax.coord[i]) >= 0)
				return null;
		}

		return new HRect(newmin, newmax);
	}

	// currently unused
	protected double area() {

		double a = 1;

		for (int i = 0; i < min.coord.length; ++i) {
			a *= (max.coord[i].compareTo(min.coord[i]));
		}

		return a;
	}

	public String toString() {
		return min + "\n" + max + "\n";
	}
}