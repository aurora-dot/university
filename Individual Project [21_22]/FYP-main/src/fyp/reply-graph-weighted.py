# %%
import json
import os

from tqdm import tqdm

from fyp.crypto import Crypto
from fyp.db import Database, User

# %%
crypto = Crypto()
database = Database(crypto)


# %%
def load_data(name):
    base = "/its/home/ep396/Documents/FYP/data/snowball_second/"
    e = base + f"encrypted_{name}.json"
    d = base + f"decrypted_{name}.json"

    crypto.age_decrypt_file(e, d)

    file = open(d, encoding="utf8")
    data = json.load(file)
    file.close()

    os.remove(d)

    return data


# %%
def simple_create_relations(user_id, follower_ids, counts):
    user_orm = User.select(User.id).where(User.twitter_user_id == user_id)
    for follower_id in follower_ids:
        follower_orm = User.select(User.id).where(User.twitter_user_id == follower_id)
        count = counts[(user_id, follower_id)]
        database.add_interactor_relation(user_orm, follower_orm, count)


# %%
def get_unique_repliers(users, repliers):
    unique_users = []
    relations = {}
    counts = {}

    for user in tqdm(users):
        captured_users = repliers[str(user)]
        relations[user] = []
        for captured_user in captured_users:
            if captured_user not in unique_users:
                unique_users.append(captured_user)
            if captured_user not in relations[user]:
                relations[user].append(captured_user)
            if (user, captured_user) not in counts:
                counts[(user, captured_user)] = 0
            counts[(user, captured_user)] += 1

    return unique_users, relations, counts


# %%
users = {
    int(crypto.fernet_decrypt(user.twitter_user_id)): user.id
    for user in User.select(User.id, User.twitter_user_id).where(User.hop == 1)
}

user_reverse = {value: key for key, value in users.items()}
user_twitter_ids = [user for user in users.keys()]


# %%
repliers = load_data("repliers")


# %%
reply_users, relations, counts = get_unique_repliers(user_twitter_ids, repliers)


# %%
if database.encrypted():
    database.encrypt_or_decrypt()


# %%
for user in tqdm(reply_users):
    database.add_user(user, False, 2)


# %%
for user, followers in tqdm(relations.items()):
    simple_create_relations(user, followers, counts)


# %%
if not database.encrypted():
    database.encrypt_or_decrypt()


# %% [markdown]
#
