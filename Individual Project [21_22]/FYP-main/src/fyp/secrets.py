import os

from dotenv import load_dotenv


class secrets:
    TWITTER_API_KEY = None
    TWITTER_API_KEY_SECRET = None

    TWITTER_BEARER_TOKEN = None

    TWITTER_ACCESS_TOKEN = None
    TWITTER_ACCESS_TOKEN_SECRET = None

    def __init__(self) -> None:
        load_dotenv()

        self.TWITTER_API_KEY = os.environ.get("TWITTER_API_KEY")
        self.TWITTER_API_KEY_SECRET = os.environ.get("TWITTER_API_KEY_SECRET")

        assert self.TWITTER_API_KEY is not None
        assert self.TWITTER_API_KEY_SECRET is not None

        self.TWITTER_BEARER_TOKEN = os.environ.get("TWITTER_BEARER_TOKEN")

        assert self.TWITTER_BEARER_TOKEN is not None


SECRETS = secrets()
