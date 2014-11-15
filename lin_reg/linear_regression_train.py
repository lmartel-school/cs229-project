import csv
import cPickle
import numpy as np
from sklearn import linear_model

X = []
y = []

with open('lin_reg/linear_regression.train.inputs', 'rb') as f:
    read = csv.reader(f)
    for row in read:
        values = [ float(x) for x in row ]
        klass = values.pop(0)
        X.append(values)
        y.append(klass)

regr = linear_model.LinearRegression()
regr.fit(X, y)

# The coefficients
# print "Coefficients: \n", regr.coef_
# The mean square error
print("[TRAINING] Residual sum of squares: %.2f"
      % np.mean((regr.predict(X) - y) ** 2))
# Explained variance score: 1 is perfect prediction
print('[TRAINING] Variance score: %.2f' % regr.score(X, y))

with open('lin_reg/linear_regression.model', 'wb') as f:
    cPickle.dump(regr, f)
