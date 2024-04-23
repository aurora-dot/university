import json
import os

from tqdm import tqdm

from fyp.crypto import Crypto
from fyp.db import Database, User

crypto = Crypto()
database = Database(crypto)


def load_data(name):
    base = "/its/home/ep396/Documents/FYP/data/snowball/"
    e = base + f"encrypted_{name}.json"
    d = base + f"decrypted_{name}.json"

    crypto.age_decrypt_file(e, d)

    file = open(d, encoding="utf8")
    data = json.load(file)
    file.close()

    os.remove(d)

    return data


def simple_create_relations(user_id, follower_ids):
    user_orm = User.select(User.id).where(User.twitter_user_id == user_id)
    for follower_id in tqdm(follower_ids):
        follower_orm = User.select(User.id).where(User.twitter_user_id == follower_id)
        database.add_interactor_relation(user_orm, follower_orm)


u = load_data("unique_users")
ur = load_data("unique_user_relations")

print(len(u))

# if database.encrypted():
#     database.encrypt_or_decrypt()

# for user in tqdm(u):
#     database.add_user(user, True, 2)

# for user, interactors in tqdm(ur.items()):
#     simple_create_relations(user, interactors)

# if not database.encrypted():
#     database.encrypt_or_decrypt()
