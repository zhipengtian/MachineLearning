1 Naive Bayes Classifier
This is a Naive Bayes classifier that classifies a political blog as being “liberal” or “conservative”. The data set included is a set of self-identified liberal and conservative blogs1. The dataset contains a total of 120 blogs, among which 56 were identified by their author as being “liberal”, with the remaining 64 considered by their author to be “conservative”. Each blog is stored in a separate file, within which each line is a separate word. Files with a name of the form “lib*.txt” are liberal blogs, and files with a name of the form “con*.txt” are conservative.

Implemented the Naive Bayes algorithm, using smoothing, as shown in Table 6.2 on page 183 of Tom Mitchell’s textbook. The program will take a set of labeled training examples in split.train and a set of test examples split.test, and classify them using a Naive Bayes classifier. The program outputs predicted labels for the test data, one per line, in the order they are listed in split.test, and calculate the accuracy on the test dataset.
$ java nb split.train split.test
L
L
...
L
C
C
L
Accuracy: 0.8056


2 Interpreting the output
topwords.java takes a training dataset as input and print out the top 20 words with the highest word probabilities in the liberal category as well as in the conservative category (i.e., pˆ(w|Clib) and pˆ(w|Ccons), where Clib is the liberal class and Ccons is the conservative class). The format should be one word per line, sorted with the highest probability first. Use the same smoothing as above. Print the probabilities with 4 digits after the decimal place (i.e. use “%.04f”). Output the top 20 liberal words and probabilities first, then print a blank line, then print the top 20 conservative words and probabilities:
$ java topwords split.train
liberalword1 .0911
liberalword2 .0505
...
liberalword20 .0011
conservativeword1 .1013
conservativeword2 .0905
...
conservativeword20 .0021


3 Stop words
It is general practice to preprocess datasets and remove words like “the”, “a”, “of”, etc. before training a classifier. Rather than prespecifying a list of stop words, we can simply exclude the N most frequent words. A new classifier nbStopWords.java based on nb.java which additionally takes a parameter N and excludes the N most frequent words from its vocabulary before training the classifier. Here is the syntax for N = 10; the output should look like the output from nb.java:
java nbStopWords split.train split.test 10

4 Smoothing
As discussed in section 6.9.1.1 on page 179 of Mitchell, estimating probabilities based on observed fractions can cause problems when observed counts are small or 0. We will investigate various approaches to this.

smoothing.java is based on nb.java which additionally takes a single parameter q.
Here is the syntax for q = 1; the output should be identical to the output from nb.java: java smoothing split.train split.test 1


5 Log Odds
topwordsLogOdds.java is based on topwords.java to print out the top 20 words with the highest log-odds ratio for each class. Assume the same input and output format as in topwords.py. Print the log-odds with 4 digits after the
decimal place (i.e. use “%.04f”). Use natural log (log base e): 
java topwordsLogOdds split.train

