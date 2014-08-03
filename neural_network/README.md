Tasks to Accomplish:
The first task is to predict whether a song was a ‘hit’, meaning it made it onto the Billboard Top 50- each instance has a label hit equal to ‘yes(1)’ or ‘no(0)’. The attributes are: year of release(multi- valued discrete), length of recording (continuous), jazz(yes/no), rock and roll(yes/no).

The second task is to predict the final score for high school students. The attributes are student grades on 2 multiple choice assignments M1 and M2, 2 programming assignments P1 and P2, and the final exam F. The scores of all the components are in the range [0,100]. All the attributes are multi- valued discrete. Again, check the csv files to see the attribute values. The final output also has a range [0,100]. Notice that we are performing regression for this problem instead of classification which was done in the decision tree assignment.


Neural Network Development:
There are two major files 'NN_music.m','NN_education.m'. You have training and development sets for both the domains. You will estimate the weights on your training set, print the squared error after every 10 iterations while training and then use the learned weights to print the output decisions on the development set. The syntax is:
$ octave -q  NN_music.m <training_file> <test_file>
1022.6
1012.7
1005.8
...(do not print these dots; they just signify continuation)
TRAINING COMPLETED! NOW PREDICTING.
no
yes
yes
no
yes
...
$ octave -q  NN_education.m <training_file> <test_file>
632.6
600.7
578.8
...
TRAINING COMPLETED! NOW PREDICTING.
23.0
55.0
65.0
60.0
80.0
50.0
3
...
The output values shown in the examples do not represent actual output.
In the above syntax ‘<training_file>’ is ‘music_train.csv’ and ‘education_train.csv’, and ‘<test_file>’ is ‘music_dev.csv’ or ‘education_dev.csv’ for the music and education domains respectively. You can compare your decisions on development file with the true labels in the development files and calcu- late the error.