import datetime
import json
import os
import time

import requests

from fyp.db import Database, User


class SnowballSampling:
    def __init__(
        self,
        db: Database,
        headers: dict,
        hop: int = 0,
    ) -> None:
        self.db = db
        self.hop = hop
        self.headers = headers

    def main(self):
        data = {}
        print(f"==> Hop {self.hop}\n\n")
        seed_users = self.get_hop_seed_users_from_db(hop_number=self.hop)

        print(f"Hop {self.hop} seed users in db: {len(seed_users)}")

        for idx, user in enumerate(seed_users):
            print(f"\n--> User {idx}")
            result = self.collect_user_followers(user)
            data[user] = result

        print("\n\n==> End hop\n\n")

        e = "encrypted_followers.json"
        d = "decrypted_followers.json"

        with open(d, "w", encoding="utf8") as outfile:
            json.dump(data, outfile, indent=4, ensure_ascii=False)

        self.db.crypto.age_encrypt_file(d, e)

        os.remove(d)

        self.db.encrypt_or_decrypt()
        for user, followers in data.items():
            self.save_result(followers, user, self.hop + 1)
        self.db.encrypt_or_decrypt()

    def save_result(self, result, user, hop):
        self.save_new_users(result, hop, True)
        self.simple_create_relations(user, result)

    def save_new_users(self, users_ids, hop, seed):
        for user_id in users_ids:
            self.db.add_user(user_id, seed, hop + 1)

    def simple_create_relations(self, user_id, follower_ids):
        user_orm = User.select(User.id).where(User.twitter_user_id == user_id)
        for follower_id in follower_ids:
            follower_orm = User.select(User.id).where(
                User.twitter_user_id == follower_id
            )
            self.db.add_relation(user_orm, follower_orm)

    def get_hop_seed_users_from_db(self, hop_number):
        self.db.encrypt_or_decrypt()

        users = [
            user.twitter_user_id
            for user in User.select().where(
                User.hop == hop_number and User.seed == True  # noqa: E712
            )
        ]
        self.db.encrypt_or_decrypt()
        return users

    def api_user_followers(self, user_id, next_token):
        url = f"https://api.twitter.com/1.1/followers/ids.json?user_id={user_id}&count=5000"  # noqa: E501 # here

        if next_token:
            url += f"&cursor={next_token}"

        raw_request = requests.get(
            url=url,
            headers=self.headers,
        )

        result_json = json.loads(raw_request.text)

        if "errors" in result_json and result_json["errors"][0]["code"] == 88:
            return (
                88,
                next_token,
                int(raw_request.headers["x-rate-limit-remaining"]),
                int(raw_request.headers["x-rate-limit-reset"]),
            )
        else:
            return (
                result_json["ids"],
                result_json["next_cursor"] if "next_cursor" in result_json else None,
                int(raw_request.headers["x-rate-limit-remaining"]),
                int(raw_request.headers["x-rate-limit-reset"]),
            )

    def collect_user_followers(self, user):
        concat_data, next_token, cont = [], None, True

        while cont is True:
            (
                data,
                next_token,
                limit_remaining_requests,
                limit_reset_time,
            ) = self.api_user_followers(
                user_id=user,
                next_token=next_token,
            )

            if data != 88:
                concat_data += data
                print(f"Added: {len(data)}")
                print(f"Total: {len(concat_data)}\n")

            if (next_token is None and data != 88) or (
                data != 88 and len(concat_data) >= 25000  # here
            ):
                cont = False

            if limit_remaining_requests <= 0 and cont is True:
                print("\n---- Start Ratelimit Wait ----")
                print(f"Current users captured: {len(concat_data)}")
                print(f"Unix epochs when: {limit_reset_time}")
                time_reset = datetime.datetime.fromtimestamp(limit_reset_time)
                print(f"Completion when: {time_reset}")
                time.sleep(time.mktime(time_reset.timetuple()) - time.time() + 1)
                print(f"Completed, time is: {datetime.datetime.now()}")
                print("---- End Ratelimit Wait ----")

            time.sleep(1.05)

        return concat_data

    def collect_user_following(self, hop, user):
        print()
