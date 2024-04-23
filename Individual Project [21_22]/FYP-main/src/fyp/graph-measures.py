# %%
import json

import networkx as nx

# %%
unique_nodes_init = {}
relations = []

with open(
    "/its/home/ep396/Documents/FYP/data/snowball_second/data-norm.csv", "r"
) as file:
    for idx, line in enumerate(file.readlines()):
        if idx != 0:
            relation_raw = line.replace("Directed;", "").split(";")
            relation_raw = [int(num) for num in relation_raw]

            if relation_raw[0] not in unique_nodes_init:
                unique_nodes_init[relation_raw[0]] = {"total": 0}
            if relation_raw[1] not in unique_nodes_init:
                unique_nodes_init[relation_raw[1]] = {"total": 0}

            relation = (
                relation_raw[1],
                relation_raw[0],
                {"weight": -abs(relation_raw[2])},
            )
            relations.append(relation)

# %%
with open(
    "/its/home/ep396/Documents/FYP/data/snowball_second/data-norm-totals.csv", "r"
) as file:
    for idx, line in enumerate(file.readlines()):
        if idx != 0:
            raw_total = line.split(";")
            raw_total = [int(num) for num in raw_total]
            unique_nodes_init[raw_total[0]]["total"] = raw_total[1]

# %%
DiG = nx.DiGraph()
G = nx.Graph()


# %%
G.add_nodes_from(unique_nodes_init)
G.add_edges_from(relations)

DiG.add_nodes_from(unique_nodes_init)
DiG.add_edges_from(relations)

G.remove_edges_from(nx.selfloop_edges(G))
DiG.remove_edges_from(nx.selfloop_edges(G))

print("loaded graphs")

# %%
print("start rc")
rc = nx.rich_club_coefficient(G)
with open("rc.json", "w", encoding="utf8") as outfile:
    json.dump(rc, outfile, indent=4, ensure_ascii=False)
print("end rc")

# %%
print("start bc")
bc = nx.betweenness_centrality(G)
with open("bc.json", "w", encoding="utf8") as outfile:
    json.dump(bc, outfile, indent=4, ensure_ascii=False)
print("end bc")

# %%
print("start ec")
ec = nx.eigenvector_centrality(G, max_iter=80000)
with open("ec.json", "w", encoding="utf8") as outfile:
    json.dump(ec, outfile, indent=4, ensure_ascii=False)
print("end ec")

# %%
print("start bc dir")
bc_d = nx.betweenness_centrality(DiG)
with open("bc_d.json", "w", encoding="utf8") as outfile:
    json.dump(bc_d, outfile, indent=4, ensure_ascii=False)
print("end bc")

# %%
print("start ec dir")
ec_d = nx.eigenvector_centrality(DiG, max_iter=80000)
with open("ec_d.json", "w", encoding="utf8") as outfile:
    json.dump(ec_d, outfile, indent=4, ensure_ascii=False)
print("end ec")
