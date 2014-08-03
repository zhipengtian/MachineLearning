import java.io.*;
import java.util.*;

public class partA {
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("Incorrect number of arguments");
			System.exit(0);
		}
		
		final int SIZE = 9;
		final String[] names = {"Gender", "Age", "Student?", "PreviouslyDeclined?", "HairLength", "Employed?", "TypeOfColateral", "FirstLoan", "LifeInsurance", "Risk"};
		String[] values = new String[SIZE];
		
		long inputSize = (long) Math.pow(2, SIZE);
		System.out.println(inputSize);
		long conceptSize = (long) Math.ceil(512 / (Math.log(10) / Math.log(2)));
		System.out.println(conceptSize);
		System.out.println((long) Math.pow(3, SIZE) + 1);
		
		// Training
		Scanner train = new Scanner(new File("9Cat-Train.labeled"));
		PrintWriter output = new PrintWriter("partA4.txt");
		String[] newVals = new String[SIZE];
		String newVal;
		int rows = 0;
		int i = 0;
		while(train.hasNext()) {
			if(train.next().equals(names[i])) {
				newVal = train.next();
				if (names[i].equals("Risk")) {
					if (newVal.equals("high"))
						values = newVals.clone();
					else
						newVals = values.clone();
					i = 0;
					rows++;
					if (rows >= 30 && rows % 30 == 0) {
						for (int x=0; x<SIZE-1; x++) {
							output.print(names[x]+" "+values[x]+"\t");
						}
						output.println(names[SIZE-1]+" "+values[SIZE-1]);
					}
					continue;
				}
				if (values[i] == null)
					newVals[i] = newVal;
				else if (!values[i].equals("?") && !values[i].equals(newVal)) {
					newVals[i] = "?";
				}
				else
					newVals[i] = values[i];
			} else {
				System.out.println("Invalid data set format!");
				System.exit(0);
			}
			i++;
		}
		output.close();
		
		// Test
		Scanner test = new Scanner(new File("9Cat-Dev.labeled"));
		int correct = 0;
		int total = 0;
		String pred = "high";
		i = 0;
		while (test.hasNext()) {
			if (pred.equals("low")) {
				total += 1;
				while (test.hasNext() && !test.next().equals("Risk"));
				if (test.next().equals(pred))
					correct += 1;
				pred = "high";
				i = 0;
			}	else if (test.next().equals(names[i])) {
				newVal = test.next();
				if (names[i].equals("Risk")) {
					total += 1;
					if (pred.equals(newVal))
						correct += 1;
					pred = "high";
					i = 0;
				}	else {
					if (values[i] == null)
						pred = "low";
					if (!values[i].equals("?")) {
						if (!values[i].equals(newVal))
							pred = "low";
					}
					i++;
				}
			} else {
				System.out.println("Invalid data set format!");
				System.exit(0);
			}
		}
		System.out.println((double)(total-correct)/total);
		
		// Prediction
		Scanner predict = new Scanner(new File(args[0]));
		pred = "high";
		i = 0;
		while (predict.hasNext()) {
			if (pred.equals("low")) {
				while (predict.hasNext() && !predict.next().equals("Risk"));
				predict.next();
				System.out.println(pred);
				pred = "high";
				i = 0;
			}	else if (predict.next().equals(names[i])) {
				newVal = predict.next();
				if (names[i].equals("Risk")) {
					System.out.println(pred);
					pred = "high";
					i = 0;
				}	else {
					if (values[i] == null)
						pred = "low";
					if (!values[i].equals("?")) {
						if (!values[i].equals(newVal))
							pred = "low";
					}
					i++;
				}
			} else {
				System.out.println("Invalid data set format!");
				System.exit(0);
			}
		}
	}
}
