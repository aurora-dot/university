import argparse
import os
from getpass import getpass

from cryptography.fernet import Fernet
from ptyprocess import PtyProcessUnicode


class Crypto:
    key = None

    def __init__(self) -> None:
        self.load_key()

    def load_key(self):
        self.key = getpass("Enter key: ")

    # Fernet #

    def fernet_encrypt(self, secret):
        encode_secret = secret.encode()
        fer = Fernet(self.key)
        return fer.encrypt(encode_secret)

    def fernet_decrypt(self, encrypt_secret):
        fer = Fernet(self.key)
        decrypt_secret = fer.decrypt(encrypt_secret.encode("utf-8"))
        return decrypt_secret.decode()

    # Age #

    def age_symmetric(self, infile, outfile, argument):
        cmd = ["/its/home/ep396/programs/age/age", argument, "-o", outfile, infile]
        p = PtyProcessUnicode.spawn(cmd)
        for i in range(2):
            p.write(f"{self.key}\n")
        p.wait()
        p.close()

    def age_encrypt_file(self, infile, outfile):
        self.age_symmetric(infile, outfile, "-p")

    def age_decrypt_file(self, infile, outfile):
        self.age_symmetric(infile, outfile, "-d")


def generate_key():
    def dir_path(string):
        if os.path.isdir(string):
            return string
        else:
            raise NotADirectoryError(string)

    parser = argparse.ArgumentParser()
    parser.add_argument("-p", "--path", type=dir_path)
    args = parser.parse_args()
    if args.path:
        key = Fernet.generate_key()
        with open(args.path + "/db.key", "wb") as key_file:
            key_file.write(key)
        print("Generated key to path.")
    else:
        print("Enter a path to your USB with -p arg.")
