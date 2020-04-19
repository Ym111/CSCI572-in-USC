import networkx as nx

G = nx.read_edgelist("/Users/Bruce/Desktop/extraLinks.txt", create_using=nx.DiGraph())
pr = nx.pagerank(G, alpha=0.85, personalization=None, max_iter=30, tol=1e-06, nstart=None, weight='weight',dangling=None)
with open("pageRank.txt","w") as f:
        for node, value in pr.items():
            f.write("/Users/Bruce/Desktop/20spring/572/CSCI572-in-USC/hw4/solr-7.7.2/crawl_data/latimes/"+ node + "=" + str(value) + "\n")

