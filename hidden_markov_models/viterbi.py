import sys
from logsum import *

def get_val(dic, key):
    try:
        return dic[key]
    except KeyError:
        return 0

def main():
    """
    Decoding: the Viterbi algorithm
    """

    if len(sys.argv) != 5:
        print "Usage: python viterbi.py <dev> <hmm-trans> <hmm-emit> <hmm-prior>"
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
        path = []
        probs.append({})
        path.append({})
        for p in hmm_prior.keys():
            probs[0][p] = log(hmm_prior[p]) + log(hmm_emit[p][line[0]])
            path[0][p] = [line[0] + '_' + p]

        for t in range(1, len(line)):
            probs.append({})
            path.append({})
            for i in probs[t-1].keys():
                probs[t][i] = -float("inf")
                parent = ""
                for j in probs[t-1].keys():
                    cur = log(hmm_emit[i][line[t]]) + probs[t-1][j] + log(hmm_trans[j][i])
                    if cur > probs[t][i]:
                        probs[t][i] = cur
                        parent = path[t-1][j]
                path[t][i] = parent + [line[t] + '_' + i] 

        max_prob = -float("inf")
        end = ""
        for state in probs[len(probs)-1].keys():
            if probs[len(probs)-1][state] > max_prob:
                max_prob = probs[len(probs)-1][state]
                end = state
        #print path

        print ' '.join(path[len(path)-1][end])
        
if __name__ == "__main__": main()
