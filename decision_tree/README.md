The first task is to predict whether a song was a “hit” meaning it made it onto the Billboard Top 50—each instance has a label hit equal to “yes” or “no”. Attribute names are listed in bold; check the csv file to see their possible values: year of release, solo recording or band, vocal or instrumental, length of recording (< 3 minutes or > 3 minutes), original composition or a “cover”, tempo, folk song, classical piece, rhythm and blues, jazz, rock and roll.

The second task is to predict the final grade (A, not A) for high school students. The attributes (co- variates, predictors) are student grades on 5 multiple choice assignments M1 through M5, 4 program- ming assignments P1 through P4, and the final exam F. Again, check the csv files to see the attribute values.

"inspect.java" calculates the label entropy at the root (i.e. the entropy of the labels before any splits) and the error rate (the percent of incorrectly classified instances) of classifying using a majority vote.
$ java inspect example1.csv
entropy: 0.981
error: 0.42

Implemented a decision tree learner with the following guidelines.
• Use mutual information to determine which attribute to spliton.
• For a split on attributeX, I(Y;X)=H(Y)−H(Y|X)=H(Y)−P(X =0)H(Y|X =0)−P(X =1)H(Y|X =1). Equivalently, you can calculate I (Y ; X ) = H (Y ) + H (X ) − H (Y , X ).
• As a stopping rule, only split on an attribute if the mutual information is ≥ .1.
• Do not grow the tree beyond depth 2. Namely, split a node only if the mutual information is ≥ .1 and the node is the root or a direct child of the root.
• Use a majority vote of the labels at each leaf to make classification decisions.

$ java decisionTree example2.csv example1.csv
[28+/72-]
love = yes: [27+/25-]
| debut = yes: [26+/0-]
| debut = no: [1+/25-]
love = no: [1+/47-]
error (train): 0.02
error (test): 0.29

The numbers in brackets give the number of positive and negative labels in that part of the tree. The last two numbers are the error rate on the training data and the error rate on the testing data.
