import java.io.*;
import java.util.*;

public class partB {
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("Incorrect number of arguments");
			System.exit(0);
		}
		
		final int SIZE = 4;
		final String[] names = {"Gender", "Age", "Student?", "PreviouslyDeclined?", "Risk"};
		final String[] standard = {"Male", "Young", "Yes", "Yes"};
		
		int inputSize = (int) Math.pow(2, SIZE);
		System.out.println(inputSize);
		int conceptSize = (int) Math.pow(2, inputSize);
		System.out.println(conceptSize);
		
		int[] vs = new int[conceptSize];

		// Training
		Scanner train = new Scanner(new File("4Cat-Train.labeled"));
		int rows = 0;
		int i = 0;
		int x = 0;
		String newVal;
		while(train.hasNext()) {
			if(train.next().equals(names[i])) {
				newVal = train.next();
				if (names[i].equals("Risk")) {
					for (int j = 0; j < conceptSize; j++) {
						if (newVal.equals("high") && (((j >> x) & 1) == 0)) {
							vs[j] = 1;
						} 
						else if (newVal.equals("low") && (((j >> x) & 1) == 1)) {
							vs[j] = 1;
						}
					}
					i = 0;
					x = 0;
					rows++;
					continue;
				}
				if (newVal.equals(standard[i]))
					x = (int) Math.pow(2, (SIZE-i-1)) + x;
			} else {
				System.out.println("Invalid data set format!");
				System.exit(0);
			}
			i++;
		}
		int vsSize = 0;
		for (int v : vs) {
			if (v == 0)
				vsSize += 1;
		}
		
		System.out.println(vsSize);
		
		int[] vsTemp = new int[vsSize];
		int vt = 0;
		for (int v = 0; v < conceptSize; v++) {
			if (vs[v] == 0) {
				vsTemp[vt] = v;
				vt++;
			}
		}
		
		vs = vsTemp.clone();

		Scanner test = new Scanner(new File(args[0]));
		i = 0;
		while (test.hasNext()) {
			if (test.next().equals(names[i])) {
				newVal = test.next();
				if (names[i].equals("Risk")) {
					int high = 0;
					int low = 0;
					for (int v : vs) {
						if (((v >> x) & 1) == 1) {
							high += 1;
						} else {
							low += 1;
						}
					}
					System.out.println(high+" "+low);
					i = 0;
					x = 0;
					continue;
				} 
				if (newVal.equals(standard[i]))
					x = (int) Math.pow(2, (SIZE-i-1)) + x;
			} else {
				System.out.println("Invalid data set format!");
				System.exit(0);
			}
			i++;
		}
	}
}