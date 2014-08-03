import sys
import random
from logsum import *

def alpha(input_tokens, hmm_trans, hmm_emit, hmm_prior):
    all_probs = []
    for line in input_tokens:
        probs = []
        probs.append({})
        for p in hmm_prior.keys():
            probs[0][p] = log(hmm_prior[p]) + log(hmm_emit[p][line[0]])

        for t in range(1, len(line)):
            probs.append({})
            for i in probs[t-1].keys():
                probs[t][i] = log(hmm_emit[i][line[t]])
                prior = 0.0
                for j in probs[t-1].keys():
                    if (j == probs[t-1].keys()[0]):
                        prior = probs[t-1][j] + log(hmm_trans[j][i])
                    else:
                        prior = log_sum(prior, probs[t-1][j] + log(hmm_trans[j][i]))
                probs[t][i] += prior

        probability = probs[len(probs)-1].values()[0]
        for p in probs[len(probs)-1].values()[1:]:
            probability = log_sum(probability, p)

        #print probability
        all_probs.append(probs)
    return all_probs

def beta(input_tokens, hmm_trans, hmm_emit, hmm_prior):
    all_probs = []
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

        #print probability
        all_probs.append(probs)
    return all_probs

def get_gamma(m, i, t, states, alpha_p, beta_p, gamma):
    if gamma[m][t][i] != float("inf"):
        return gamma[m][t][i]
    else:
        new_gamma = 0.0
        for j in states:
            new_gamma += exp(alpha_p[m][t][j] + beta_p[m][len(beta_p[m])-1-t][j])
        new_gamma = exp(alpha_p[m][t][i] + beta_p[m][len(beta_p[m])-1-t][i]) / new_gamma
        gamma[m][t][i] = new_gamma
        return new_gamma

def get_xi(m, i, j, t, word, states, alpha_p, beta_p, gamma, trans, emit):
    return get_gamma(m, i, t, states, alpha_p, beta_p, gamma) * trans[i][j] * emit[j][word] * exp(beta_p[m][len(beta_p[m])-2-t][j]) / exp(beta_p[m][len(beta_p[m])-1-t][i])
    
def main():
    """
    Evaluation: the Forward algorithm
    """

    if len(sys.argv) < 2 or len(sys.argv) > 5:
        print "Usage: python baumwelch.py <train> [<hmm-trans> <hmm-emit> <hmm-prior>]"
        return

    input_file = open(sys.argv[1], "r")

    input_tokens = []
    for line in input_file:
        input_tokens += [line.split()]

    tokens = []
    for line in input_tokens:
        for word in line:
            if word not in tokens:
                tokens.append(word)

    hmm_trans = {}
    hmm_emit = {}
    hmm_prior = {}
    gamma = {}
    
    if len(sys.argv) == 5:
        hmm_trans_file = open(sys.argv[2], "r")
        hmm_emit_file = open(sys.argv[3], "r")
        hmm_prior_file = open(sys.argv[4], "r")

        for state1 in hmm_trans_file:
            state1 = state1.split()
            hmm_trans[state1[0]] = {}
            for state2 in state1[1:]:
                state2 = state2.split(":")
                hmm_trans[state1[0]][state2[0]] = float(state2[1])

        for state in hmm_emit_file:
            state = state.split()
            hmm_emit[state[0]] = {}
            for word in state[1:]:
                word = word.split(":")
                hmm_emit[state[0]][word[0]] = float(word[1])
                
        for state in hmm_prior_file:
            state = state.split()
            hmm_prior[state[0]] = float(state[1])

    else:
        states = ["PR", "VB", "RB", "NN", "PC", "JJ", "DT", "OT"]
        for s1 in states:
            hmm_trans[s1] = {}
            total = 0.0
            for s2 in states:
                hmm_trans[s1][s2] = random.uniform(1,100)
                total += hmm_trans[s1][s2]
            for s2 in states:
                hmm_trans[s1][s2] /= total
    
        for s in states:
            total = 0.0
            hmm_emit[s] = {}
            for t in tokens:
                hmm_emit[s][t] = random.uniform(1, 100)
                total += hmm_emit[s][t]
            for t in tokens:
                hmm_emit[s][t] /= total
    
        total = 0.0
        for s in states:
            hmm_prior[s] = random.uniform(1, 100)
            total += hmm_prior[s]
        for s in states:
            hmm_prior[s] /= total
                
    
    times = 0
    prev = 0
    while times < 20:
        times += 1

        try:
            alpha_p = alpha(input_tokens, hmm_trans, hmm_emit, hmm_prior)
            beta_p = beta(input_tokens, hmm_trans, hmm_emit, hmm_prior)
        except:
            return

        log_likelihood = 0.0
        for probs in alpha_p:
            probability = probs[len(probs)-1].values()[0]
            for p in probs[len(probs)-1].values()[1:]:
                probability = log_sum(probability, p)
            log_likelihood += probability
        log_likelihood /= len(alpha_p)
        print log_likelihood

        if log_likelihood - prev < 0.1 and prev != 0:
            break
        else:
            prev = log_likelihood

        gamma = {}
        for m in range(len(input_tokens)):
            gamma[m] = {}
            for t in range(len(input_tokens[m])):
                gamma[m][t] = {}
                for i in hmm_prior:
                    gamma[m][t][i] = float("inf") 
    
        pi = {}
        for l in range(0, len(input_tokens)):
            for i in hmm_prior.keys():
                if i not in pi.keys():
                    pi[i] = 0.0
                cur_pi = get_gamma(l, i, 0, hmm_prior.keys(), alpha_p, beta_p, gamma)
                pi[i] += cur_pi / len(input_tokens)

        trans = {}
        for i in hmm_prior.keys():
            if i not in trans.keys():
                trans[i] = {}
            for j in hmm_prior.keys():
                if j not in trans[i].keys():
                    trans[i][j] = 0.0
                top = 0.0
                bottom = 0.0
                for m in range(0, len(input_tokens)):
                    for t in range(0, len(input_tokens[m])-1):
                        top += get_xi(m, i, j, t, input_tokens[m][t+1], hmm_prior.keys(), alpha_p, beta_p, gamma, hmm_trans, hmm_emit)
                        for jj in hmm_prior.keys():
                            bottom += get_xi(m, i, jj, t, input_tokens[m][t+1], hmm_prior.keys(), alpha_p, beta_p, gamma, hmm_trans, hmm_emit)
                trans[i][j] = top / bottom

        emit = {}
        for i in hmm_prior.keys():
            if i not in emit.keys():
                emit[i] = {}
            for v in hmm_emit[i].keys():
                if v not in tokens:
                    del hmm_emit[i][v]
                    continue
                if v not in emit[i].keys():
                    emit[i][v] = 0.0
                top = 0.0
                bottom = 0.0
                for m in range(0, len(input_tokens)):
                    for t in range(0, len(input_tokens[m])):
                        if input_tokens[m][t] == v:
                            top += get_gamma(m, i, t, hmm_prior.keys(), alpha_p, beta_p, gamma)
                        bottom += get_gamma(m, i, t, hmm_prior.keys(), alpha_p, beta_p, gamma)

                emit[i][v] = top / bottom
                    
        
        hmm_prior = pi
        hmm_trans = trans
        hmm_emit = emit

if __name__ == "__main__": main()





        
