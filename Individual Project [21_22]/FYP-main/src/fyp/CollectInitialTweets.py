import datetime
import json
import os
import time

from fyp.crypto import Crypto
from fyp.twitter_api import convert_datetime_to_ISO_8601, twitter_api


class CollectInitialTweets:
    def __init__(
        self,
        headers: dict,
        query: str,
        start_time: datetime.datetime,
        end_time: datetime.datetime,
        crypto: Crypto,
        path_beginning: str,
    ) -> None:
        self.HEADERS = headers
        self.QUERY = query
        self.START_TIME = start_time
        self.END_TIME = end_time
        self.CRYPTO = crypto
        self.PATH_PREFIX = path_beginning

    # === Public === #

    def get_seed_users(self) -> list:
        TWEET_DATA = self.__search_tweets(
            self.QUERY, self.HEADERS, self.START_TIME, self.END_TIME
        )

        d = f"{self.PATH_PREFIX}_decrypted_initial_tweet_data.json"
        e = f"{self.PATH_PREFIX}_encrypted_initial_tweet_data.json"

        with open(d, "w", encoding="utf8") as outfile:
            json.dump(TWEET_DATA, outfile, indent=4, ensure_ascii=False)

        self.CRYPTO.age_encrypt_file(d, e)

        os.remove(d)

        print(
            f"\n`{self.QUERY}` : Collected {len(TWEET_DATA)} tweets, "
            + f"between {self.START_TIME} and {self.END_TIME}"
        )

    # === ??? === #

    def __search_tweets(
        self, query: str, headers: str, start: datetime.datetime, end: datetime.datetime
    ) -> dict:
        concat_data, next_token, cont = [], None, True

        start, end = (
            convert_datetime_to_ISO_8601(start),
            convert_datetime_to_ISO_8601(end),
        )

        while cont:
            params = {
                "query": query,
                "next_token": next_token,
                "start_time": start,
                "end_time": end,
                "tweet.fields": "public_metrics",
                "expansions": "author_id",
                "max_results": 500,
            }

            data, limit_remaining_requests, limit_reset_time = twitter_api(
                url="https://api.twitter.com/2/tweets/search/all",
                headers=headers,
                params=params,
                data_location="data",
            )

            if data["fyp"]["error"] is True:
                if limit_remaining_requests <= 0 and cont is True:
                    print("\n---- Start Ratelimit Wait ----")
                    print(f"Current tweets captured: {len(concat_data)}")
                    print(f"Unix epochs when: {limit_reset_time}")
                    time_reset = datetime.datetime.fromtimestamp(limit_reset_time)
                    print(f"Completion when: {time_reset}")
                    time.sleep(time.mktime(time_reset.timetuple()) - time.time() + 1)
                    print(f"Completed, time is: {datetime.datetime.now()}")
                    print("---- End Ratelimit Wait ----")
                else:
                    raise Exception(data)
            else:
                if data["fyp"]["error"] is False:
                    concat_data += data["data"]
                    print(f"\nAdded: {len(data['data'])}")
                    print(f"Total: {len(concat_data)}")
                    next_token = (
                        data["meta"]["next_token"]
                        if "next_token" in data["meta"]
                        else None
                    )

                if next_token is None and data["fyp"]["error"] is False:
                    cont = False

            time.sleep(1.05)

        return concat_data
