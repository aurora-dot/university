import datetime
import json
import time

import requests


def convert_datetime_to_ISO_8601(dt: datetime.datetime) -> str:
    return dt.strftime("%Y-%m-%dT%H:%M:%SZ")


def ISO_8601_to_convert_datetime(datetime_string):
    return datetime.datetime.strptime(datetime_string, "%Y-%m-%dT%H:%M:%SZ")


def twitter_api(
    headers: dict,
    url: str,
    params: dict,
    data_location: str,
) -> tuple:

    raw_request = requests.get(url=url, headers=headers, params=params)
    result_json = json.loads(raw_request.text)

    limit_remaining = int(raw_request.headers["x-rate-limit-remaining"])
    limit_reset = int(raw_request.headers["x-rate-limit-reset"])

    if data_location not in result_json:
        result_json["fyp"] = {"error": True}
        return (
            result_json,
            limit_remaining,
            limit_reset,
        )
    else:
        result_json["fyp"] = {"error": False}
        return (
            result_json,
            limit_remaining,
            limit_reset,
        )


def ratelimit_wait(limit_reset_time, thing, len_concat_data):
    print("---- Start Ratelimit Wait ----")
    print(f"Current {thing} captured: {len_concat_data}")
    print(f"Unix epochs when: {limit_reset_time}")
    time_reset = datetime.datetime.fromtimestamp(limit_reset_time)
    print(f"Completion when: {time_reset}")
    time.sleep(time.mktime(time_reset.timetuple()) - time.time() + 1)
    print(f"Completed, time is: {datetime.datetime.now()}")
    print("---- End Ratelimit Wait ----\n")
