import html
import re
import string
from collections import Counter

import contractions
import emoji
import nltk


class aa_feature_extractor:
    happy_emoticons = [
        ":)",
        ":-)",
        ":]",
        ":-]",
        ":3",
        ":-3",
        ":>",
        ":->",
        "8)",
        "8-)",
        ":}",
        ":-}",
        ":o)",
        ":o]",
        ":o3",
        ":o>",
        ":o}",
        ":c)",
        ":c]",
        ":c3",
        ":c>",
        ":c}",
        ":^)",
        ":^]",
        ":^3",
        ":^>",
        ":^}",
        "=^)",
        "=^]",
        "=^3",
        "=^>",
        "=^}",
        "=)",
        "=-)",
        "=]",
        "=-]",
        "=3",
        "=-3",
        "=>",
        "=->",
        "=}",
        "=-}",
        "=o)",
        "=o]",
        "=o3",
        "=o>",
        "=o}",
        "=c)",
        "=c]",
        "=c3",
        "=c>",
        "=c}",
        "(:",
        "(-:",
        "[:",
        "[-:",
        "Ɛ:",
        "Ɛ-:",
        "<:",
        "<-:",
        "(8",
        "(-8",
        "{:",
        "{-:",
        "(o:",
        "[o:",
        "Ɛo:",
        "<o:",
        "{o:",
        "(ɔ:",
        "[ɔ:",
        "Ɛɔ:",
        "<ɔ:",
        "{ɔ:",
        "(^:",
        "[^:",
        "Ɛ^:",
        "<^:",
        "{^:",
        "(^=",
        "[^=",
        "Ɛ^=",
        "<^=",
        "{^=",
        "(=",
        "(-=",
        "[=",
        "[-=",
        "Ɛ=",
        "Ɛ-=",
        "<=",
        "<-=",
        "{=",
        "{-=",
        "(o=",
        "[o=",
        "Ɛo=",
        "<o=",
        "{o=",
        "(ɔ=",
        "[ɔ=",
        "Ɛɔ=",
        "<ɔ=",
        "{ɔ=",
        ":))",
        ":)))",
        ":))))",
        ":)))))",
        ":))))))",
        ":)))))))",
        ":))))))))",
        ":)))))))))",
        "¦)",
        "¦¬)",
        "¦-)",
        "¦^)",
        "¦o)",
        "¦c)",
        "(¦",
        "(-¦",
        "(^¦",
        "(o¦",
        "(c¦",
        "〓)",
        "〓¬)",
        "〓-)",
        "〓^)",
        "〓o)",
        "〓c)",
        "(〓",
        "(-〓",
        "(^〓",
        "(o〓",
        "(c〓",
        "C:",
        "C-:",
        "C^:",
        "c:",
        "c-:",
        "c^:",
        ":Ↄ",
        ":-Ↄ",
        ":^Ↄ",
        ":ɔ",
        ":-ɔ",
        ":^ɔ",
        ":D",
        ":-D",
        "8D",
        "8-D",
        "=D",
        "=-D",
        ":^D",
        "8^D",
        "=^D",
        "BD",
        "B-D",
        "B^D",
        ";D",
        ";-D",
        ";^D",
        ":ↁ",
        "〓D",
        "¦D",
        "^^",
        "^ ^",
        "^-^",
        "^_^",
        "^.^",
        "^,^",
        "^u^",
        "^w^",
        "^ㅂ^",
        "^o^",
        "^U^",
        "^O^",
        "^0^",
        "^_________^",
        "^오^",
        "n.n",
        "n_n",
        "•ᴗ•",
        ".^◡^.",
        "´͈ ᵕ `͈",
        "¢‿¢",
        "°ﺑ°",
        "ó‿ó",
        "ôヮô",
        "ʘ‿ʘ",
        "◕ω◕",
        "◕‿◕",
        "◕◡◕",
        "◕ ◡ ◕",
        "◘‿◘",
        "◙‿◙",
        "☻_☻",
        "☻‿☻",
        "Ꙩ⌵Ꙩ",
        ".⌵.",
        "-⌵-",
        "'⌵'",
        ">⌵<",
        "(^_^)",
        "(^ ^)",
        "(^O^)",
        "(^o^)",
        "(^^)",
        "(≧∇≦)",
        "(◕ヮ◕)",
        "(^.^)",
        "(^·^)",
        "(*^0^*)",
        "!(^^)!",
        "(●^o^●)",
        "(^v^)",
        "(^u^)",
        "(＾◇＾)",
        "( ^)o(^ )",
        "(^○^)",
        "\\(^o^)/",
        "\\(^o^)/",
        "(*^▽^*)",
        "(✿◠‿◠)",
        "ヽ(´ー｀)ﾉ",
        "（´∀｀）",
        "( ﾟヮﾟ)",
        "d(*⌒▽⌒*)b",
        "(◕‿◕✿)",
        "(◡‿◡✿)",
        "(≧ ᗜ ≦)",
        "(⌃·̫⌃)",
        "( ⁼̴̤̆◡̶͂⁼̴̤̆ )",
        "Σ ◕ ◡ ◕",
        "٩(•̮̮̃•̃)۶",
        "٩(●̮̮̃●̃)۶",
        "٩(｡͡•‿•｡)۶",
        "۹⌤_⌤۹",
        "ლ(╹◡╹ლ)",
        "uwu",
        "UwU",
        "UuU",
        "UvU",
        "UvvU",
        "◕◡◕",
        "◕v◕",
        "◕u◕",
        "◕w◕",
        "Ü Ü",
        "ü ü",
        "v̈",
        "V̈",
        "Ѷ",
        "ẅ",
        "Ẅ",
        "(Ü)",
        "⏝̈",
        "⏝̎",
        "◟̆◞̆",
        "◟̊◞̊",
        "◡̈",
        "ᵔ.ᵔ",
        "ᵔ◡ᵔ",
        "'◡'",
        "ツ",
        "シ",
        "ッ",
        "ヅ",
        "ツ゚",
        "ϡ",
        "ジ",
        "ｼ",
        "ﾂ",
        "㋡",
        "㋛",
        "☺",
        "☻",
        "〠",
        "(ツ)",
        "⍢",
        "⍢⃝",
        "ت",
        "ﭢ",
        "ت",
    ]

    happy_emojis = [
        "😺",
        "🤗",
        "😀",
        "🙂",
        "😃",
        "☺️",
        "😊",
        "😸",
        "😹",
        "😂",
    ]

    vowels = ["a", "e", "i", "o", "u"]

    @staticmethod
    def download_nltk():
        nltk.download("averaged_perceptron_tagger")
        nltk.download("stopwords")
        nltk.download("punkt")
        nltk.download("wordnet")
        nltk.download("omw-1.4")
        nltk.download("tagsets")

    @staticmethod
    def clean_text(text):
        html.unescape(text)
        removed_links = re.sub(r"https?://\S+", "", text)
        removed_mentions = re.sub(r"(^|[^@\w])@(\w{1,15})\b", "", removed_links)
        return removed_mentions.strip()

    def extract_num_features_from_document(self, text):
        return [
            len(text),
            self.get_sentence_count(text),
            self.get_token_count(text),
            self.get_no_vowels_count(text),
            self.get_alphabet_count(text),
            self.get_punctuation_count(text),
            self.get_two_three_continuous_punctuation(text),
            self.get_contraction_count(text),
            self.get_parenthesis_count(text),
            self.get_all_caps_letter_word_count(text),
            self.get_emoticons_count(text),
            self.get_happy_emoticons_count(text),
            self.get_sentence_without_capital_at_beginning(text),
            self.get_quotation(text),
        ]

    @staticmethod
    def get_sentence_count(text):
        return len(nltk.sent_tokenize(text))

    @staticmethod
    def get_token_count(text):
        return len(nltk.word_tokenize(text))

    def get_no_vowels_count(self, text):
        count = 0
        tokenized = nltk.word_tokenize(text.lower())

        for word in tokenized:
            if not any(v in word for v in self.vowels):
                count += 1
        return count

    @staticmethod
    def get_alphabet_count(text):
        return sum([1 for letter in text if letter.isalpha()])

    @staticmethod
    def get_punctuation_count(text):
        return sum([1 for letter in text if letter in string.punctuation])

    @staticmethod
    def get_two_three_continuous_punctuation(text):
        return sum(
            [
                1
                for group in re.findall(r"([^\s\w]+)", text)
                if len(group) > 1 and len(group) < 4
            ]
        )

    @staticmethod
    def get_contraction_count(text):
        return sum(
            [
                1
                for word in re.findall(r"[\w']+", text)
                if word != contractions.fix(word)
            ]
        )

    @staticmethod
    def get_parenthesis_count(text):
        return len(re.findall(r"(,.*?,)|(\(.*?\))|(-.*?-)", text))

    @staticmethod
    def get_all_caps_letter_word_count(text):
        return sum(
            [
                1
                for word in nltk.word_tokenize(text)
                if word.upper() == word and word not in string.punctuation
            ]
        )

    @staticmethod
    def get_emoticons_count(text):
        tokenized = nltk.casual_tokenize(text)
        count = 0

        for token in tokenized:
            if emoji.emojize(token) in emoji.UNICODE_EMOJI["en"]:
                count += 1
            elif nltk.tokenize.casual.EMOTICON_RE.search(token):
                count += 1

        return count

    def get_happy_emoticons_count(self, text):
        tokenized = nltk.casual_tokenize(text)
        count = 0

        for token in tokenized:
            emojized = emoji.emojize(token)
            if emojized in emoji.UNICODE_EMOJI["en"] and emojized in self.happy_emojis:
                count += 1
            elif token in self.happy_emoticons:
                count += 1

        return count

    @staticmethod
    def get_sentence_without_capital_at_beginning(text):
        return sum(
            [
                1
                for sentence in nltk.sent_tokenize(text)
                if (sentence[0].lower() == sentence[0]) or (sentence[0].isnumeric())
            ]
        )

    @staticmethod
    def get_quotation(text):
        return len(re.findall(r'"[^"]*"', text))

    def extract_freq_features_from_document(self, text):
        return [
            self.get_freq_pos_tags(text),
            self.get_freq_of_letters(text),
            self.get_freq_function_words(text),
            self.get_freq_lower_i(text),
            self.get_freq_stop_without_white_space(text),
            self.get_freq_question(text),
            self.get_freq_sentence_with_small_letter(text),
            self.get_freq_alpha_digit_uppercase_whitespace_tab(text),
            self.get_freq_a_and_an_error(text),
            self.get_freq_he_she_they(text),
        ]

    @staticmethod
    def get_freq_pos_tags(text):
        tags_frequencies = {
            "CC": 0,
            "CD": 0,
            "DT": 0,
            "EX": 0,
            "FW": 0,
            "IN": 0,
            "JJ": 0,
            "JJR": 0,
            "JJS": 0,
            "LS": 0,
            "MD": 0,
            "NN": 0,
            "NNP": 0,
            "NNPS": 0,
            "NNS": 0,
            "PDT": 0,
            "POS": 0,
            "PRP": 0,
            "PRP$": 0,
            "RB": 0,
            "RBR": 0,
            "RBS": 0,
            "RP": 0,
            "SYM": 0,
            "TO": 0,
            "UH": 0,
            "VB": 0,
            "VBD": 0,
            "VBG": 0,
            "VBN": 0,
            "VBP": 0,
            "VBZ": 0,
            "WDT": 0,
            "WP": 0,
            "WP$": 0,
            "WRB": 0,
        }
        lower_case = text.lower()
        tokens = nltk.word_tokenize(lower_case)
        tags = nltk.pos_tag(tokens)

        for tag_type, count in Counter(tag for word, tag in tags).items():
            if tag_type in tags_frequencies:
                tags_frequencies[tag_type] = count

        return tags_frequencies

    @staticmethod
    def get_freq_of_letters(text):
        frequencies = {char: 0 for char in string.ascii_lowercase}
        total = 0

        for char in text.lower():
            if char in frequencies:
                frequencies[char] += 1
                total += 1

        for char, freq in frequencies.items():
            frequencies[char] = freq / total

        return frequencies

    @staticmethod
    def get_freq_function_words(text):
        # 10.1002/asi.20316
        functional_words_freq = {
            "a": 0,
            "between": 0,
            "in": 0,
            "nor": 0,
            "some": 0,
            "upon": 0,
            "about": 0,
            "both": 0,
            "including": 0,
            "nothing": 0,
            "somebody": 0,
            "us": 0,
            "above": 0,
            "but": 0,
            "inside": 0,
            "of": 0,
            "someone": 0,
            "used": 0,
            "after": 0,
            "by": 0,
            "into": 0,
            "off": 0,
            "something": 0,
            "via": 0,
            "all": 0,
            "can": 0,
            "is": 0,
            "on": 0,
            "such": 0,
            "we": 0,
            "although": 0,
            "cos": 0,
            "it": 0,
            "once": 0,
            "than": 0,
            "what": 0,
            "am": 0,
            "do": 0,
            "its": 0,
            "one": 0,
            "that": 0,
            "whatever": 0,
            "among": 0,
            "down": 0,
            "latter": 0,
            "onto": 0,
            "the": 0,
            "when": 0,
            "an": 0,
            "each": 0,
            "less": 0,
            "opposite": 0,
            "their": 0,
            "where": 0,
            "and": 0,
            "either": 0,
            "like": 0,
            "or": 0,
            "them": 0,
            "whether": 0,
            "another": 0,
            "enough": 0,
            "little": 0,
            "our": 0,
            "these": 0,
            "which": 0,
            "any": 0,
            "every": 0,
            "lots": 0,
            "outside": 0,
            "they": 0,
            "while": 0,
            "anybody": 0,
            "everybody": 0,
            "many": 0,
            "over": 0,
            "this": 0,
            "who": 0,
            "anyone": 0,
            "everyone": 0,
            "me": 0,
            "own": 0,
            "those": 0,
            "whoever": 0,
            "anything": 0,
            "everything": 0,
            "more": 0,
            "past": 0,
            "though": 0,
            "whom": 0,
            "are": 0,
            "few": 0,
            "most": 0,
            "per": 0,
            "through": 0,
            "whose": 0,
            "around": 0,
            "following": 0,
            "much": 0,
            "plenty": 0,
            "till": 0,
            "will": 0,
            "as": 0,
            "for": 0,
            "must": 0,
            "plus": 0,
            "to": 0,
            "with": 0,
            "at": 0,
            "from": 0,
            "my": 0,
            "regarding": 0,
            "toward": 0,
            "within": 0,
            "be": 0,
            "have": 0,
            "near": 0,
            "same": 0,
            "towards": 0,
            "without": 0,
            "because": 0,
            "he": 0,
            "need": 0,
            "several": 0,
            "under": 0,
            "worth": 0,
            "before": 0,
            "her": 0,
            "neither": 0,
            "she": 0,
            "unless": 0,
            "would": 0,
            "behind": 0,
            "him": 0,
            "no": 0,
            "should": 0,
            "unlike": 0,
            "yes": 0,
            "below": 0,
            "i": 0,
            "nobody": 0,
            "since": 0,
            "until": 0,
            "you": 0,
            "beside": 0,
            "if": 0,
            "none": 0,
            "so": 0,
            "up": 0,
            "your": 0,
        }
        for word in nltk.casual_tokenize(text.lower()):
            if word in functional_words_freq:
                functional_words_freq[word] += 1
        return functional_words_freq

    @staticmethod
    def get_freq_lower_i(text):
        return sum([1 for word in nltk.casual_tokenize(text) if word == "i"])

    @staticmethod
    def get_freq_stop_without_white_space(text):
        return len(re.findall(r"\.\w", text))

    @staticmethod
    def get_freq_question(text):
        return text.count("?")

    @staticmethod
    def get_freq_sentence_with_small_letter(text):
        return sum(
            [
                1
                for sentence in nltk.sent_tokenize(text)
                if sentence[0].lower() == sentence[0] and sentence[0].isalpha()
            ]
        )

    @staticmethod
    def get_freq_alpha_digit_uppercase_whitespace_tab(text):
        freq = {
            "alpha": 0,
            "digit": 0,
            "uppercase": 0,
            "whitespace": text.count(" "),
            "tab": text.count("\t"),
        }
        for char in text:
            if char.isalpha():
                freq["alpha"] += 1
                if char.upper() == char:
                    freq["uppercase"] += 1
            elif char.isnumeric():
                freq["digit"] += 1

        return freq

    def get_freq_a_and_an_error(self, text):
        freq = {"a": 0, "an": 0}
        bigrams = nltk.bigrams(nltk.casual_tokenize(text.lower()))

        for bigram in bigrams:
            if bigram[0] in freq and bigram[1].isalpha():
                if bigram[1][0] in self.vowels and bigram[0] == "a":
                    freq["a"] += 1
                elif bigram[1][0] not in self.vowels and bigram[0] == "an":
                    freq["an"] += 1

        return freq

    @staticmethod
    def get_freq_he_she_they(text):
        frequency = {"he": 0, "she": 0, "they": 0}
        for token in nltk.casual_tokenize(text.lower()):
            if token in frequency:
                frequency[token] += 1

        return frequency

    @staticmethod
    def flatten_data(data):
        flatten_data = []
        for item in data:
            if type(item) is dict:
                for val in item.values():
                    flatten_data.append(val)
            else:
                flatten_data.append(val)

        return flatten_data

    def extract_features_from_document(self, text):
        cleaned_text = self.clean_text(text)
        return self.extract_num_features_from_document(
            cleaned_text
        ) + self.flatten_data(self.extract_freq_features_from_document(cleaned_text))
