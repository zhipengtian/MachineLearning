import java.io.*;
import java.util.*;

public class inspect {

	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.out.println("Incorrect number of arguments");
			System.exit(0);
		}
		FileReader f = new FileReader(args[0]);
		BufferedReader br = new BufferedReader(f);
		String line = br.readLine();
		ArrayList<String[]> data = new ArrayList<String[]>();
		HashMap<String, Integer> output = new HashMap<String, Integer>();
		int rCount = 0;
		while ((line = br.readLine()) != null) {
			String[] row = line.split(",");
			String key = row[row.length-1];
			if (output.containsKey(key))
				output.put(key, output.get(key)+1);
			else
				output.put(key, 1);
			rCount++;
			data.add(row);
		}
		double entropy = 0;
		int major = 0;
		for (String k : output.keySet()) {
			int num = output.get(k);
			if (num > major)
				major = num;
			double px = (double) num/rCount;
			entropy -= px*(Math.log(px)/Math.log(2.0));
		}
		
		double error = (double) (rCount-major)/rCount;
		System.out.println("entropy: "+ (double) Math.round(entropy*1000)/1000);
		System.out.println("error: " + (double) Math.round(error*100)/100);
		br.close();
	}

}
