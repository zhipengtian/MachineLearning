Problem
This project uses concept learning to determine whether the risk of defaulting on a loan is "high" or "low," based on a set of binary attributes. 

Part A: The 'Risk-9Cat' task and the FIND-S algorithm
In this part, you are provided with a training dataset '9Cat-Train.labeled' including examples with different values for 9 binary attributes and a "high" or "low" label for each example. The task is to learn a hypothesis that best fits the data. For the Risk-9Cat task, the attributes are:
	•	Gender (Male, Female)
	•	Age (Young, Old)
	•	Student? (Yes, No)
	•	PreviouslyDeclined? (Yes, No)
	•	HairLength (Long, Short)
	•	Employed? (Yes , No)
	•	TypeOfColateral (House, Car)
	•	FirstLoan (Yes, No)
	•	LifeInsurance (Yes, No)
In this part, the hypothesis space H should consist of all possible conjunctions over the nine attributes, where each conjunct is of the form ATTR=[value], ATTR='?', or ATTR='null'.  In the program, you can choose to exclude the possibility “ATTR=null”, and find an alternative way to represent the single hypothesis that always returns “False”.

You are also provided with a development dataset '9Cat-Dev.labeled' to develop and test the code.
The command which will be used to run the program on the server is basically:
java partA testFileName

In the file 'partA', the program:
	1.	Prints (to standard output), by itself on the first line, the size of the input space (number of possible unique inputs).
	2.	Let |C| = the size of the concept space (the space of all possible concepts; i.e., Boolean functions of the input). Prints, by itself on the next line, the number of decimal digits in |C|. 
    For example, if the size of the concept space is 128, the value printed should be 3 which is the number of decimal digits in 128. 
	3.	Prints, by itself on the next line, the size of the hypothesis space H (the space of all semantically distinct conjunctions of the type described above).
	4.	Runs the FIND-S algorithm on '9Cat-Train.labeled', using the hypothesis space H described above, and prints to the file "partA4.txt" the current hypothesis after every 30 training instances (namely, after 30, 60, 90,... training instances, counting both positive and negative instances). A hypothesis is displayed as a (tab) delimited list of attribute values enclosed in angle brackets. The current hypothesis after every 30 instances should be printed on a separate line.  
	5.	Applies the final hypothesis to '9Cat-Dev.labeled' then prints a line with the misclassification rate (the fraction of misclassified data points; a number between 0.0 and 1.0). Note that you are provided with the correct labels in '9Cat-Dev.labeled' so you can compare the predicted labels with the correct labels and compute the misclassification rate.
	6.	Applies the final hypothesis to the data in the input file (passed as a command line argument), and prints out the classification of each instance in this set on a separate line.

Part B: Bias-free Learning, the 'Risk-4Cat' task and the LIST-THEN-ELIMINATE algorithm
In this part, I attempt to learn without any bias. The hypotheses space H will be equal to the concept space. Namely, H contains not only conjunctions but also every other possible binary function. The task is to run the LIST-THEN-ELIMINATE algorithm on the training data, keeping track of the version space, and then, for every test instance, hold a vote among the members of the final version space.

To reduce the computational complexity, we reduce the task to involve only the first four attributes. Thus, for the 'Risk-4Cat' task, the attributes are:
	•	Gender (Male, Female)
	•	Age (Young, Old)
	•	Student? (Yes, No)
	•	PreviouslyDeclined? (Yes, No)

The program should have one command line argument which is a test data file that has the same format as the development data file. The command which will be used to run your program on the server is basically:
java partB testFileName
In the file 'partB', the program:
	1.	Prints (to standard output), by itself on the first line, the size of the input space (number of possible unique inputs).
	2.	Prints, by itself on the next line, the size of the concept space (the space of all possible concepts; i.e., Boolean functions of the input).
	3.	Runs the 'List-Then-Eliminate' algorithm, using the entire concept space, on D='4Cat-Train.labeled', settles on a version space VS(H,D), then prints out, on a new line, the size of that version space (number of remaining hypotheses consistent with the training data).
	4.	For each instance in the test data (where the test file is passed as a command line argument), takes a vote among the hypotheses in the version space, and prints a line containing two numbers: #high, #low, separated by a space.
