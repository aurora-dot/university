# %%
from tqdm import tqdm

from fyp.db import UserInteractorRelationships

# %%
UserInteractorRelationships.select().count()


# %%
relationships = []

for relationship in tqdm(UserInteractorRelationships.select()):
    relationships.append(
        (relationship.user, relationship.interactor, relationship.count)
    )

# %%
totals = {}
relationships_multi_connections = {}

for relationship in relationships:
    interactor = relationship[1]
    count = relationship[2]

    if interactor not in totals:
        totals[interactor] = count
    else:
        totals[interactor] += count

    if interactor not in relationships_multi_connections:
        relationships_multi_connections[interactor] = [relationship]
    else:
        relationships_multi_connections[interactor].append(relationship)

# %%
totals_multi_connections = {}
to_remove = []

for interactor, connection_details in relationships_multi_connections.items():
    if len(connection_details) > 1:
        for connection in connection_details:
            if interactor not in totals_multi_connections:
                totals_multi_connections[interactor] = connection[2]
            else:
                totals_multi_connections[interactor] += connection[2]
    else:
        totals_multi_connections[interactor] = connection[2]
        to_remove.append(interactor)

single_connections = []
single_totals = {}

for interactor in to_remove:
    if interactor in relationships_multi_connections:
        con = relationships_multi_connections.pop(interactor)
        single_connections.append(con)
    if interactor in totals_multi_connections:
        total_con = totals_multi_connections.pop(interactor)
        single_totals[interactor] = total_con

# %%
with open("data-norm.csv", "w") as file:
    file.write("source;target;type;weight\n")
    for relationship in tqdm(relationships):
        file.write(f"{relationship[0]};{relationship[1]};Directed;{relationship[2]}\n")

with open("data-norm-totals.csv", "w") as file:
    file.write("id;total\n")
    for interactor, total in tqdm(totals.items()):
        file.write(f"{interactor};{total}\n")

# %%
with open("data-multi.csv", "w") as file:
    file.write("source;target;type;weight\n")
    for interactor, relationships_ in tqdm(relationships_multi_connections.items()):
        for relationship in relationships_:
            print
            file.write(
                f"{relationship[0]};{relationship[1]};Directed;{relationship[2]}\n"
            )

with open("data-multi-totals.csv", "w") as file:
    file.write("id;total\n")
    for interactor, total in tqdm(totals_multi_connections.items()):
        file.write(f"{interactor};{total}\n")

# %%
with open("data-single.csv", "w") as file:
    file.write("source;target;type;weight\n")
    for relationship_ in tqdm(single_connections):
        relationship = relationship_[0]
        file.write(f"{relationship[0]};{relationship[1]};Directed;{relationship[2]}\n")

with open("data-single-totals.csv", "w") as file:
    file.write("id;total\n")
    for interactor, total in tqdm(single_totals.items()):
        file.write(f"{interactor};{total}\n")

# %%
