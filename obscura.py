import json
import re

import config

"""
Read the communicatio file between front end and back end (that file can hold specific settings) and is the
main way the front-end (java) and back-end (python) communicate and return the config dictionary
"""


def read_comms():
    # f = open("frontbackcomms.txt")
    # settings = {}
    # count = -1
    # for line in f:
    #     count += 1
    #     # delete comments from line
    #     if ";;" in line:
    #         line = line.split(";;")[0]
    #     if count == 0:  # verify line 1 = CONFIG
    #         if line != "FRONT END - BACK END COMMUNICATION FILE\n":
    #             print("Not a comms file")
    #             break
    #     else:  # get the keys and values
    #         if ":" in line:
    #             content = line.split(":")
    #             settings[content[0].strip()] = content[1].strip()
    f = open("frontbackcomms.json", "r")
    settings = json.load(f)
    return settings


"""
efficiently_load_dict() returns config and sep dicitonaries in an efficient way, where if the config has 
been updated it reparses the config, else it loads the saved json
"""


def efficiently_load_dictsep():
    settings = read_comms()
    dicto = {}
    sep = []
    if settings["update-config"] == True:  # settings must be updated
        dicto, sep = config.updateconfig()
    else:  # load current settings
        f = open("config.json", "r")
        dicto = json.load(f)
        s = open("sep.json", "r")
        sep = json.load(s)
    if len(sep) != 2:  # if sep empty or incorrect use empty separator
        sep = ["", ""]
    return (dicto, sep)


# def main():
#     input = open("input.txt", "r")
#     output = open("output.txt", "w")
#     dicto, sep = efficiently_load_dictsep()
#     replacewords = list(dicto.keys())
#     print(replacewords)
#     for line in input:
#         for word in replacewords:
#             if word.startswith('"') and word.endswith(
#                 '"'
#             ):  # replace only specific word (do not ignore case)
#                 wrapped = sep[0] + dicto[word] + sep[1]
#                 pattern = re.compile(re.escape(word))
#                 line = pattern.sub(wrapped, line)
#             else:
#                 wrapped = sep[0] + dicto[word] + sep[1]
#                 # line = line.replace(word, (wrapped))
#                 pattern = re.compile(re.escape(word), re.IGNORECASE)
#                 line = pattern.sub(wrapped, line)
#         output.write(line)
def is_quoted_string(word):
    """Check if a dictionary key represents a quoted string for case-sensitive matching"""
    return (
        word.startswith('"') and word.endswith('"') and len(word) >= 3
    )  # At least "a" (quotes + one character)


def clean_quoted_word(word):
    """Remove quotes from a quoted word for pattern matching"""
    if is_quoted_string(word):
        return word[1:-1]  # Remove first and last characters (quotes)
    return word


def main():
    with open("input.txt", "r") as infile, open("output.txt", "w") as output:
        dicto, sep = efficiently_load_dictsep()
        replacewords = list(dicto.keys())

        for line in infile:
            original_line = line  # Keep for debugging
            for word in replacewords:
                if is_quoted_string(word):
                    # Case-sensitive exact match for quoted strings
                    clean_word = clean_quoted_word(word)
                    pattern = re.compile(r"\b" + re.escape(clean_word) + r"\b")
                else:
                    # Case-insensitive match for unquoted strings
                    clean_word = word
                    pattern = re.compile(
                        r"\b" + re.escape(clean_word) + r"\b", re.IGNORECASE
                    )

                wrapped = sep[0] + dicto[word] + sep[1]

                # Count matches for debugging
                matches_before = len(pattern.findall(line))
                line = pattern.sub(wrapped, line)
                matches_after = len(pattern.findall(line))

            output.write(line)


if __name__ == "__main__":
    main()
