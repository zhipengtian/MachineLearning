import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class nbStopWords {
	public static void main(String[] args) throws Exception {
		int num = Integer.parseInt(args[2]);
		//int num = 10;
		//training
		File train = new File(args[0]);
		//File train = new File("split.train");
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
		
		
		//liberal blogs
		int libNum = 0;
		int conNum = 0;
		int libWordNum = 0;
		int conWordNum = 0;
		int total = 0;
		
		HashMap<String, Integer> vocabulary = new HashMap<String, Integer>();
		HashMap<String, Integer> libWords = new HashMap<String, Integer>();
		HashMap<String, Integer> conWords = new HashMap<String, Integer>();
		
		for (File l : liberalBlogs) {
			libNum++;
			total++;
			
			br = new BufferedReader(new FileReader(l));
			String line;
			while ((line = br.readLine()) != null) {
				libWordNum++;
				String token = line.toLowerCase();
				if (!vocabulary.containsKey(token)) {
					vocabulary.put(token, 1);
					libWords.put(token, 1);
				} else {
					vocabulary.put(token, vocabulary.get(token) + 1);
					libWords.put(token, libWords.get(token) + 1);
				}
			}
			br.close();
		}
		
		for (File c : conservativeBlogs) {
			conNum++;
			total++;
			
			br = new BufferedReader(new FileReader(c));
			String line;
			while ((line = br.readLine()) != null) {
				conWordNum++;
				String token = line.toLowerCase();
				if (!vocabulary.containsKey(token)) {
					vocabulary.put(token, 1);
					conWords.put(token, 1);
				} else {
					vocabulary.put(token, vocabulary.get(token) + 1);
					if (!conWords.containsKey(token)) {
						conWords.put(token, 1);
					} else {
						conWords.put(token, conWords.get(token) + 1);
					}
				}
			}
			br.close();
		}
		
		List<Entry<String, Integer>> newVocabulary = new ArrayList<Entry<String, Integer>>(vocabulary.entrySet());
		Collections.sort(newVocabulary, new Comparator<Map.Entry<String, Integer>>() {
			public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 ) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		
		List<Entry<String, Integer>> stopwords = newVocabulary.subList(0,num);
		
		for (Entry<String, Integer> word : stopwords) {
			vocabulary.remove(word.getKey());
			if (libWords.containsKey(word.getKey())) {
				libWordNum -= libWords.get(word.getKey());
				libWords.remove(word.getKey());
			}
			if (conWords.containsKey(word.getKey())) {
				conWordNum -= conWords.get(word.getKey());
				conWords.remove(word.getKey());
			}
		}
		
		
		Double libP = (libNum + 0.0) / total;
		Double conP = (conNum + 0.0) / total;

		HashMap<String, Double[]> words = new HashMap<String, Double[]>();
		for (String word : vocabulary.keySet()) {
			Double[] wordP = new Double[2];
			try {
				wordP[0] = (libWords.get(word) + 1.0) / (libWordNum + vocabulary.size());
			} catch (Exception e) {
				wordP[0] = 1.0 / (libWordNum + vocabulary.size());
			}
			try {
				wordP[1] = (conWords.get(word) + 1.0) / (conWordNum + vocabulary.size());
			} catch (Exception e) {
				wordP[1] = 1.0 / (conWordNum + vocabulary.size());
			}
			words.put(word, wordP);
		}
		
		//test
		File test = new File(args[1]);
		//File test = new File("split.test");
		br = new BufferedReader(new FileReader(test));
		String testFile;
		ArrayList<File> testFiles = new ArrayList<File>();
		ArrayList<String> result = new ArrayList<String>();
		
		while ((testFile = br.readLine()) != null) {
			testFiles.add(new File(testFile));
			if (testFile.substring(0, 3).equals("lib")) {
				result.add("L");
			} else {
				result.add("C");
			}
		}
		br.close();
		
		int i = 0;
		int correct = 0;
		String output;
		for (File tf : testFiles) {
			String line;
			Double libVal = Math.log(libP); 
			Double conVal = Math.log(conP);
			br = new BufferedReader(new FileReader(tf));
			
			while ((line = br.readLine()) != null) {
				String token = line.toLowerCase();
				if (words.containsKey(token)) {
					libVal += Math.log(words.get(token)[0]);
					conVal += Math.log(words.get(token)[1]);
				}
			}
			br.close();
			
			if (libVal > conVal) {
				output = "L";
			} else {
				output = "C";
			}
			
			if (result.get(i).equals(output)) {
				correct++;
			}
			
			i++;
			System.out.println(output);
		}
		System.out.printf("Accuracy: %.04f\n",(correct+0.0)/i);
	}
}
