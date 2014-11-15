import csv
import cPickle
import numpy as np
from sklearn import linear_model
from sklearn.feature_extraction.text import CountVectorizer

X = None
y = None
DELIM = ","
vectorizer = None

# Note: this class assumes data has been cleaned
with open('src/data/lin_reg/linear_regression.train.inputs', 'rb') as f:
    scoresAndFeatures = f.readlines()
    partitionedScoresAndFeatures = [entry.partition(DELIM) for entry in scoresAndFeatures]
    scores = [float(partitionedEntry[0]) for partitionedEntry in partitionedScoresAndFeatures]
    corpus = [partitionedEntry[2] for partitionedEntry in partitionedScoresAndFeatures]
    print "Printing the first entry of the corpus:"
    print "score: ", scores[0], "\n"
    print "comment: ", corpus[0], "\n"
    print "corpus length: ", len(corpus), "\n"
    print "scores length: ", len(scores), "\n"
    vectorizer = CountVectorizer(min_df=1)
    X = vectorizer.fit_transform(corpus)
    y = np.array(scores)

regr = linear_model.LinearRegression()
regr.fit(X, y)

# The coefficients
print "Coefficients: \n", regr.coef_
# The mean square error
print("[TRAINING] Residual sum of squares: %.2f"
      % np.mean((regr.predict(X) - y) ** 2))
# Explained variance score: 1 is perfect prediction
print('[TRAINING] Variance score: %.2f' % regr.score(X, y))

with open('lin_reg/linear_regression.model', 'wb') as f:
    cPickle.dump(regr, f)

with open('lin_reg/linear_regression.vectorizer', 'wb') as f:
    cPickle.dump(vectorizer, f)
