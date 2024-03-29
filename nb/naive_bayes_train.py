import csv
import cPickle
import numpy as np
from sklearn.naive_bayes import MultinomialNB
from sklearn.feature_extraction.text import CountVectorizer

X = []
y = []
delim = ","

def show_most_informative_features(vectorizer, clf, n=20):
    feature_names = vectorizer.get_feature_names()
    coefs_with_fns = sorted(zip(clf.coef_[0], feature_names))
    top = zip(coefs_with_fns[:n], coefs_with_fns[:-(n + 1):-1])
    for (coef_1, fn_1), (coef_2, fn_2) in top:
        print "\t%.4f\t%-15s\t\t%.4f\t%-15s" % (coef_1, fn_1, coef_2, fn_2)

vectorizer = CountVectorizer(min_df=1)
# Note: this class assumes data has been cleaned
with open('nb/naive_bayes.train.inputs') as f:
	scoresAndFeatures = f.readlines()
	partitionedScoresAndFeatures = [entry.partition(delim) for entry in scoresAndFeatures]
	scores = [partitionedEntry[0] for partitionedEntry in partitionedScoresAndFeatures]
	corpus = [partitionedEntry[2] for partitionedEntry in partitionedScoresAndFeatures]
	print "Printing the first entry of the corpus:"
	print "score: ", scores[0], "\n"
	print "comment: ", corpus[0], "\n"
	print "corpus length: ", len(corpus), "\n"
	print "scores length: ", len(scores), "\n"
	# vectorizer = CountVectorizer(min_df=1)
	X = vectorizer.fit_transform(corpus)
	y = np.array(scores)

clf = MultinomialNB(alpha=1.0)
clf.fit(X, y)

# The coefficients
# print "Coefficients: \n", regr.coef_
# The mean square error
predicted_y = clf.predict(X)
errors = predicted_y.astype(int) - y.astype(int)
print("[TRAINING] Residual sum of squares: %.2f"
      % np.mean((errors) ** 2))
# Explained variance score: 1 is perfect prediction
print('[TRAINING] Variance score: %.2f' % clf.score(X, y))

show_most_informative_features(vectorizer, clf)

with open('nb/naive_bayes.model', 'wb') as f:
    cPickle.dump(clf, f)

with open('nb/naive_bayes.vectorizer', 'wb') as f:
    cPickle.dump(vectorizer, f)