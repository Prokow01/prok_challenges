"""
Problem 2 - Solution

Given a pattern and an input string, find if the string follows the same pattern. Return 1 if the pattern matches, otherwise return 0.
Author: Peter Rokowski (peterrokowski@gmail.com)
"""

s1 = "aabc"
s2 = "fivefivesixsix"

patternDict = {}

def cutoff(string1, string2):
    return len(string1)

if __name__=="__main__":
    print "%s" % cutoff(s1, s2) 

