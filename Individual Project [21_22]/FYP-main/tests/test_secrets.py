import unittest

from src.fyp.secrets import SECRETS


class MyTestCase(unittest.TestCase):
    def test_board_setup(self):
        self.assertIsNotNone(SECRETS.TWITTER_API_KEY)
        self.assertIsNotNone(SECRETS.TWITTER_API_KEY_SECRET)

        self.assertIsNotNone(SECRETS.TWITTER_BEARER_TOKEN)
