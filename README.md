# 7Letters

Author: Ramiro Deo-Campo Vuong


# Contributors:

Mr. Brian King

He is former Microsoft employee	that mentored my high school robotics team.
The LetterNodeTree data structure was his idea. I had come close to something resembling it,
but nothing quite as fast. He also showed me the methods found in the tricks section.
Note: Mr. King explained to me how his various ideas worked. All of my code is original,
outside of handling threads and finding combinations.

https://www.baeldung.com/java-combinations-algorithm

I use code from this website to find combinations of letters



# Purpose:

This program reads a .txt file and finds the combination of n letters
to spell the most words in the file.
example: the letters "catle" (n = 5) can spell "cattle", "cat", "teal", and more
The goal is to run as fast as possible.
Main.java will run 50 tests and check for correctness



# Usage:

Feel free to add a .txt file into under the TextFiles directory. Then add the necessary information to the 
Constants.TXT_FILES enum.



# How it works:

Words are converted into bits. If a word contains 'e', then the first bit is active. It it contains 'a',
the second bit is active and so on (the order is based on Constants.LETTERS). This makes comparing extremely 
fast. 

Next, these words are sorted into a modified binary tree. Instead of two children, a node will have children
representing each letter (no letters already represented by parents). The sorting uses bit shifting to determine which path to take
through the tree and adds to the end node. To count words, a method is called recursively to go down appropriate paths.



# Neat Tricks:

Parent nodes keep track of words that pass through them, so when scoring combos, we can pre determine with a single
if statement whether the given combo has the potential to be the best.

Multiple threads are run in parrallel when searching the tree. This technique makes use of all available computing power.



# Todos for more speed:

I defintely need to revamp how combinations are calculated. I have considered looking into bit based combinatorics and
somehow making the combinations an inherent part of the letter tree. However, I have tried looked at these solutions to deeply.
