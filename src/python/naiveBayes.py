import numpy as np
from sklearn.naive_bayes import MultinomialNB
from sklearn.feature_extraction.text import CountVectorizer

trainDataFilename = "trainData.csv"
trainDataPathPrefix = "../data/"
trainDataPath = trainDataPathPrefix + trainDataFilename
delim = ","

def getTrainingData(filename):
	with open(filename) as f:
		scoresAndFeatures = f.readlines()
		partitionedScoresAndFeatures = [entry.partition(delim) for entry in scoresAndFeatures]
		scores = [partitionedEntry[0] for partitionedEntry in partitionedScoresAndFeatures]
		comments = [partitionedEntry[2] for partitionedEntry in partitionedScoresAndFeatures]
		return (scores, comments)

# Note: this class assumes data has been cleaned

vectorizer = CountVectorizer(min_df=1)
(scores, corpus) = getTrainingData(trainDataPath)
# print "Printing the first entry of the corpus:"
# print scores[0]
# print corpus[0]
# print '\n'
# print len(corpus)
X = vectorizer.fit_transform(corpus)
y = np.array(scores)
clf = MultinomialNB()
clf.fit(X, y)
print(clf.predict(X[2])) # predict on second example