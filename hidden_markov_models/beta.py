import sys
from logsum import *

def main():
    """
    Evaluation: the Backward algorithm
    """

    if len(sys.argv) != 5:
        print "Usage: python beta.py <dev> <hmm-trans> <hmm-emit> <hmm-prior>"
        return

    input_file = open(sys.argv[1], "r")
    hmm_trans_file = open(sys.argv[2], "r")
    hmm_emit_file = open(sys.argv[3], "r")
    hmm_prior_file = open(sys.argv[4], "r")

    input_tokens = []
    for line in input_file:
        input_tokens += [line.split()]

    hmm_trans = {}
    for state1 in hmm_trans_file:
        state1 = state1.split()
        hmm_trans[state1[0]] = {}
        for state2 in state1[1:]:
            state2 = state2.split(":")
            hmm_trans[state1[0]][state2[0]] = float(state2[1])

    hmm_emit = {}
    for state in hmm_emit_file:
        state = state.split()
        hmm_emit[state[0]] = {}
        for word in state[1:]:
            word = word.split(":")
            hmm_emit[state[0]][word[0]] = float(word[1])
            
    hmm_prior = {}
    for state in hmm_prior_file:
        state = state.split()
        hmm_prior[state[0]] = float(state[1])
    
    for line in input_tokens:
        probs = []
        probs.append({})
        for p in hmm_prior.keys():
            probs[0][p] = 0

        for t in range(1, len(line)):
            probs.append({})
            for i in probs[t-1].keys():
                for j in probs[t-1].keys():
                    if (j == probs[t-1].keys()[0]):
                        probs[t][i] = probs[t-1][j] + log(hmm_trans[i][j]) + log(hmm_emit[j][line[len(line)-t]])
                    else:
                        probs[t][i] = log_sum(probs[t][i], probs[t-1][j] + log(hmm_trans[i][j]) + log(hmm_emit[j][line[len(line)-t]]))
                        
        probability = log(hmm_prior.values()[0]) + log(hmm_emit.values()[0][line[0]]) + probs[len(probs)-1].values()[0]
        for p in probs[len(probs)-1].keys()[1:]:
            probability = log_sum(probability, log(hmm_prior[p]) + log(hmm_emit[p][line[0]]) + probs[len(probs)-1][p])

        print probability
    

if __name__ == "__main__": main()
