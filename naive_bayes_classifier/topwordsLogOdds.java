import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class topwordsLogOdds {
	public static void main(String[] args) throws Exception {
		//training
		File train = new File(args[0]);
		BufferedReader br = new BufferedReader(new FileReader(train));
		String trainFile;
		ArrayList<File> liberalBlogs = new ArrayList<File>();
		ArrayList<File> conservativeBlogs = new ArrayList<File>();
		
		while ((trainFile = br.readLine()) != null) {
			if (trainFile.substring(0, 3).equals("lib")) {
				liberalBlogs.add(new File(trainFile));
			} else {
				conservativeBlogs.add(new File(trainFile));
			}
		}
		
		br.close();
		
		HashSet<String> vocabulary = new HashSet<String>();
		HashMap<String, Integer> libWords = new HashMap<String, Integer>();
		HashMap<String, Integer> conWords = new HashMap<String, Integer>();
		
		int libWordNum = 0;
		int conWordNum = 0;
		
		for (File l : liberalBlogs) {
			
			br = new BufferedReader(new FileReader(l));
			String line;
			while ((line = br.readLine()) != null) {
				libWordNum++;
				String token = line.toLowerCase();
				if (!vocabulary.contains(token)) {
					vocabulary.add(token);
					libWords.put(token, 1);
				} else {
					libWords.put(token, libWords.get(token) + 1);
				}
			}
			br.close();
		}
		
		for (File c : conservativeBlogs) {
			
			br = new BufferedReader(new FileReader(c));
			String line;
			while ((line = br.readLine()) != null) {
				conWordNum++;
				String token = line.toLowerCase();
				if (token.equals("")) {
					System.out.println(line);
				}
				if (!vocabulary.contains(token)) {
					vocabulary.add(token);
					conWords.put(token, 1);
				} else {
					if (!conWords.containsKey(token)) {
						conWords.put(token, 1);
					} else {
						conWords.put(token, conWords.get(token) + 1);
					}
				}
			}
			br.close();
		}
		
		HashMap<String, Double[]> words = new HashMap<String, Double[]>();
		for (String word : vocabulary) {
			Double[] wordP = new Double[2];
			if (libWords.containsKey(word) && conWords.containsKey(word)) {
				wordP[0] = Math.log(((libWords.get(word) + 1.0) / (libWordNum + vocabulary.size())) / ((conWords.get(word) + 1.0) / (conWordNum + vocabulary.size())));
				wordP[1] = Math.log(((conWords.get(word) + 1.0) / (conWordNum + vocabulary.size())) / ((libWords.get(word) + 1.0) / (libWordNum + vocabulary.size())));
			} else if (!libWords.containsKey(word) && !conWords.containsKey(word)) {
				wordP[0] = Math.log((1.0 / (libWordNum + vocabulary.size())) / (1.0 / (conWordNum + vocabulary.size())));
				wordP[1] = Math.log((1.0 / (conWordNum + vocabulary.size())) / (1.0 / (libWordNum + vocabulary.size())));
			} else if (!libWords.containsKey(word)) {
				wordP[0] = Math.log((1.0 / (libWordNum + vocabulary.size())) / ((conWords.get(word) + 1.0) / (conWordNum + vocabulary.size())));
				wordP[1] = Math.log(((conWords.get(word) + 1.0) / (conWordNum + vocabulary.size())) / (1.0 / (libWordNum + vocabulary.size())));
			} else if (!conWords.containsKey(word)) {
				wordP[0] = Math.log(((libWords.get(word) + 1.0) / (libWordNum + vocabulary.size())) / (1.0 / (conWordNum + vocabulary.size())));
				wordP[1] = Math.log((1.0 / (conWordNum + vocabulary.size())) / ((libWords.get(word) + 1.0) / (libWordNum + vocabulary.size())));
			}
			words.put(word, wordP);
		}
		
		List<Entry<String, Double[]>> libList = new ArrayList<Entry<String, Double[]>>(words.entrySet());
		List<Entry<String, Double[]>> conList = new ArrayList<Entry<String, Double[]>>(words.entrySet());
		// Defined Custom Comparator here
		Collections.sort(libList, new Comparator<Map.Entry<String, Double[]>>() {
			public int compare( Map.Entry<String, Double[]> o1, Map.Entry<String, Double[]> o2 ) {
				return (o2.getValue()[0]).compareTo(o1.getValue()[0]);
			}
		});
		Collections.sort(conList, new Comparator<Map.Entry<String, Double[]>>() {
			public int compare( Map.Entry<String, Double[]> o1, Map.Entry<String, Double[]> o2 ) {
				return (o2.getValue()[1]).compareTo(o1.getValue()[1]);
			}
		});

		int count = 0;
		for (Map.Entry<String, Double[]> w : libList) {
			count++;
			System.out.printf("%s %.04f\n", w.getKey(), w.getValue()[0]);
			if (count == 20) {
				break;
			}
		}
		System.out.println();
		count = 0;
		for (Map.Entry<String, Double[]> w : conList) {
			count++;
			System.out.printf("%s %.04f\n", w.getKey(), w.getValue()[1]);
			if (count == 20) {
				break;
			}
		}
	}
}
