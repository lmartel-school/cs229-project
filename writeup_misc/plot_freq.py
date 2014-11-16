import matplotlib.pyplot as plt

scores = None
counts = None

with open('frequencies.txt', 'rb') as f:
    partition = [line.partition('|') for line in f.readlines()]
    scores = [line[2] for line in partition]
    counts = [line[0] for line in partition]

plt.hist(scores, weights=counts)
plt.show()
