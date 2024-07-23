package Trees;

public class Rope {
	Rope l, r, par;
	int weight;
	final static int LEAF_LIMIT = 4;
	char[] chars;

	public Rope() {
	}

	public static Rope generateRope(String string) {
		return generateRope(null, string.toCharArray(), 0, string.length());
	}

	private static Rope generateRope(Rope par, char[] a, int l, int r) {
		Rope temp = new Rope();
		temp.par = par;

		if (r - l > LEAF_LIMIT) {
			temp.weight = r - l >> 1;
			int m = r + l >> 1;

			temp.l = generateRope(temp, a, l, m);
			temp.r = generateRope(temp, a, m, r);
		} else {
			temp.weight = r - l;
			temp.chars = new char[r - l];

			int j = 0;
			for (int i = l; i < r; i++) {
				temp.chars[j++] = a[i];
			}
		}
		return temp;
	}

	private static Rope concatenate(Rope r1, Rope r2) {
		Rope aux = new Rope();
		aux.l = r1;
		aux.r = r2;
		r1.par = r2.par = aux;
		aux.weight = r1.length();
		return aux;
	}

	private char charAt(int idx) {
		return charAt(this, idx);
	}

	private char charAt(Rope node, int idx) {
		if (node.chars != null) {
			return node.chars[idx];
		}
		if (idx < node.weight) {
			return charAt(node.l, idx);
		}
		return charAt(node.r, idx - node.weight);
	}

	private Rope[] splitAt(int idx) {
		return splitAt(this, idx);
	}

	private Rope[] splitAt(Rope node, int idx) { //TODO:refactor and unfuck
		if (node.chars == null) {
			return idx < node.weight ? splitAt(node.l, idx) : splitAt(node.r, node.weight - idx);
		}
		Rope otherRope = new Rope();
		if (node == node.par.l) {
			otherRope.l = new Rope();
			otherRope.l.chars = new char[node.weight - (idx)];
			otherRope.l.weight = otherRope.weight = node.weight - (idx);
			otherRope.weight = node.weight - (idx);
			otherRope.r = node.par.r;
			node.par.r = null;//ok for splitting at to the right ?? 
			System.arraycopy(node.chars, idx, otherRope.l.chars, 0, node.weight - idx);
			char[] aux = new char[idx];
			System.arraycopy(node.chars, 0, aux, 0, idx);
			node.chars = aux;
			node.weight = idx;
			rebalance(node.par);
		} else {
			otherRope.r = new Rope();
			otherRope.r.chars = new char[idx];
			otherRope.r.weight = idx;
			otherRope.l = node.par.l;
			otherRope.weight = otherRope.l.weight;
			node.par.l = null;
			System.arraycopy(node.chars, 0, otherRope.r.chars, 0, idx);
			char[] aux = new char[idx];
			System.arraycopy(node.chars, 0, aux, 0, idx);
			node.chars = aux;
			node.weight = idx;
			rebalance(node.par);
		}

		return new Rope[] { this, otherRope };
	}

	private void rebalance(Rope node) {	//TODO:refactor and unfuck
		if(node.l == null) {
			node.chars = node.r.chars;
			node.weight = node.r.weight;
			node.r = null;
			rebalance(node.par);
		} else if(node.r == null) {
			node.chars = node.l.chars;
			node.weight = node.l.weight;
			node.l = null;
			rebalance(node.par);
		}
	}

	private int length() {
		return length(this);
	}

	private int length(Rope rope) {
		return rope != null ? rope.weight + length(rope.r) : 0;
	}

	public void print() {
		print(this);
		System.out.println();
	}

	private void print(Rope node) {
		if (node == null) {
			return;
		}
		if (node.chars != null) {
			for (int i = 0; i < node.weight; i++) {
				System.out.print(node.chars[i]);
			}
		}
		print(node.l);
		print(node.r);
	}

	public void visualize() {
		print(this, 0);
		System.out.println();
	}

	private void print(Rope node, int lvl) {
		if (node != null) {
			print(node.r, lvl + 1);
			for (int i = 0; i < lvl; i++) {
				System.out.print("\t");
			}
			System.out.println(node.weight + " / " + (node.chars != null ? new String(node.chars) : ""));
			print(node.l, lvl + 1);
		}
	}

	public static void main(String[] args) {
		Rope rope = generateRope("hello_world!!");
		rope.print();
		rope.visualize();
		System.out.println(rope.length());

		for (Rope r : generateRope("splitting_at5").splitAt(5)) {
			r.print();
			r.visualize();
		}
		rope.visualize();
	}
}