import java.io.*;
import java.util.*;

public class decisionTree {

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.out.println("Incorrect number of arguments");
			System.exit(0);
		}
		FileReader f = new FileReader(args[0]);
		BufferedReader br = new BufferedReader(f);
		ArrayList<String[]> data = new ArrayList<String[]>();
		int rCount = 0;
		int plus, minus;
		plus = minus = 0;
		String line = br.readLine();
		String[] title = line.split(",");
		while ((line = br.readLine()) != null) {
			String[] row = line.split(",");
			String key = row[row.length-1];
			if (key.equals("yes") || key.equals("A"))
				plus++;
			else
				minus++;
			rCount++;
			data.add(row);
		}
		br.close();
		System.out.println("["+plus+"+/"+minus+"-]");
		boolean result = false;
		if (plus > minus)
			result = true;
		TreeNode classifier = generateTree(data, title, result, -1, 0);
		
		//train
		System.out.print("error (train): ");
		errorRate(data, rCount, classifier);
		
		//test
		FileReader t = new FileReader(args[1]);
		br = new BufferedReader(t);
		ArrayList<String[]> test = new ArrayList<String[]>();
		int rTest = 0;
		line = br.readLine();
		while ((line = br.readLine()) != null) {
			String[] row = line.split(",");
			rTest++;
			test.add(row);
		}
		br.close();
		
		System.out.print("error (test): ");
		errorRate(test, rTest, classifier);
		
	}
	public static boolean predRow(String[] row, TreeNode classifier) {
		if (classifier.val == null)
			return classifier.result;
		if (row[classifier.col].equals(classifier.val))
			return predRow(row, classifier.left);
		else
			return predRow(row, classifier.right);
	}
	public static void errorRate(ArrayList<String[]> data, int n, TreeNode classifier) {
		int wrong = 0;
		for (String[] r : data) {
			if (predRow(r, classifier)) {
				if (!r[r.length-1].equals("yes") && !r[r.length-1].equals("A"))
					wrong++;
			} else {
				if (r[r.length-1].equals("yes") || r[r.length-1].equals("A"))
					wrong++;
			}
		}
		System.out.println((double) wrong/n);
		
	}
	public static TreeNode generateTree(ArrayList<String[]> data, String[] title, boolean prevRes, int done, int level) {
		TreeNode root = new TreeNode(prevRes);
		if (level == 2)
			return root;
		double maxIG = 0;
		int split = 0;
		int colSize = data.get(0).length;
		for (int i = 0; i < colSize - 1; i++) {
			if (i == done)
				continue;
			double IG = infoGain(data, i, colSize-1);
			if (IG > maxIG) {
				maxIG = IG;
				split = i;
			}	
		}
		if (maxIG < 0.1)
			return root;
		String val = null;
		String opp = null;
		int plus1, plus2, minus1, minus2;
		ArrayList<String[]> data1 = new ArrayList<String[]>();
		ArrayList<String[]> data2 = new ArrayList<String[]>();
		plus1 = plus2 = minus1 = minus2 = 0;
		for (String[] r : data) {
			if (val == null) {
				val = r[split];
				root.col = split;
				root.val = val;
			}
			if (opp == null && !r[split].equals(val))
				opp = r[split];
			if (r[split].equals(val)) {
				data1.add(r);
				if (r[r.length-1].equals("yes") || r[r.length-1].equals("A"))
					plus1++;
				else
					minus1++;
			} else {
				data2.add(r);
				if (r[r.length-1].equals("yes") || r[r.length-1].equals("A"))
					plus2++;
				else
					minus2++;
			}
		}
		if (level == 1)
			System.out.print("| ");
		System.out.println(title[split] + " = " + val + ": [" + plus1 + "+/" + minus1 +"-]");
		boolean result = false;
		if (plus1 > minus1)
			result = true;
		root.left = generateTree(data1, title, result, split, level+1);
		
		if (level == 1)
			System.out.print("| ");
		System.out.println(title[split] + " = " + opp + ": [" + plus2 + "+/" + minus2 +"-]");
		result = false;
		if (plus2 > minus2)
			result = true;
		root.right = generateTree(data2, title, result, split, level+1);
		
		return root;
	}
	public static double entropy(double p) {
		if (p == 1 || p == 0)
			return 0.0;
		return 0 - p*(Math.log(p)/Math.log(2.0)) - (1-p)*(Math.log(1-p)/Math.log(2.0));
	}
	public static double infoGain(ArrayList<String[]> data, int x, int y) {
		int[] xCount = new int[2];
		int[] yCount = new int[2];
		int[] yCond = new int[4];
		int rowCount = 0;
		if (data.size() == 0)
			return 0.0;
		String xPlus = data.get(0)[x];
		String yPlus = data.get(0)[y];
		for (String[] row : data) {
			rowCount++;
			if (row[x].equals(xPlus)) {
				xCount[0]++;
				if (row[y].equals(yPlus)) {
					yCount[0]++;
					yCond[0]++;
				}
				else {
					yCount[1]++;
					yCond[1]++;
				}
			}
			else {
				xCount[1]++;
				if (row[y].equals(yPlus)) {
					yCount[0]++;
					yCond[2]++;
				}
				else {
					yCount[1]++;
					yCond[3]++;
				}
			}
		}
		double px = (double) xCount[0]/rowCount;
		double py = (double) yCount[0]/rowCount;
		double py0 = (double) yCond[0]/xCount[0];
		double py1 = (double) yCond[2]/xCount[1];
		return entropy(py) - px*entropy(py0) - (1-px)*entropy(py1);
	}
}
