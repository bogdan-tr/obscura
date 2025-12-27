import json

"""
This script reads config.md and saves it as a ready to use python dictionary so that the
config doesn't have to be parsed every time.
"""


def grabconfig():
    f = open("config.md")
    return f


def parseconfig(f):
    dicto = {}
    sep = []
    count = -1
    for line in f:
        line = line.strip()
        # delete comments from line
        if ";;" in line:
            line = line.split(";;")[0]
            line = line.strip()
        count += 1
        if len(line) == 0:
            continue  # skip empty lines
        if count == 0:  # verify line 1 = CONFIG
            if line != "CONFIG":
                print("Not a config file")
                print(line)
                break
        elif count == 1:  # verify line 2 = SEP and collect it
            if line.startswith("SEP:"):
                sep_text = line[4:].strip()
                sepo = sep_text.split(" ")
                if len(sepo) == 2:
                    sep = sepo  # Use the entire separator text as one element
                else:
                    sep = ["", ""]  # Default empty separator
            else:
                print("Missing separator")
                break
        else:  # get the keys and values
            if "->" in line:
                content = line.split("->")
                dicto[content[0].strip()] = content[1].strip()
            else:
                dicto[line.strip()] = ""
    return (dicto, sep)


def updateconfig():
    f = grabconfig()
    dicto, sep = parseconfig(f)
    j = open("config.json", "w")
    json.dump(dicto, j)
    s = open("sep.json", "w")
    json.dump(sep, s)
    return dicto, sep


def process_text(text, dicto, sep):
    """Process text using the configuration rules."""
    for key, value in dicto.items():
        if key and value:  # Only process if both key and value exist
            text = text.replace(key, value)
        elif key and not value:  # If no replacement value, remove the key
            text = text.replace(key, "")
    return text


def test():
    f = grabconfig()
    dicto, sep = parseconfig(f)
    updateconfig()
    print("Sep:", sep)
    print("Dict:", dicto)

    # Process input.txt and write to output.txt
    try:
        with open("input.txt", "r") as input_file:
            input_text = input_file.read()

        processed_text = process_text(input_text, dicto, sep)

        with open("output.txt", "w") as output_file:
            output_file.write(processed_text)

        print(f"Successfully processed input.txt and wrote to output.txt")
        print(f"Original length: {len(input_text)} characters")
        print(f"Processed length: {len(processed_text)} characters")

    except FileNotFoundError:
        print("input.txt not found - creating sample file")
        sample_text = """Contact John Doe at john.doe@example.com or call (555) 123-4567.
His SSN is 123-45-6789 and credit card is 4532-1234-5678-9012.
Visit https://example.com for more information.
Original text should be changed to new text.
I believe this will work.
Sincerely, John"""

        with open("input.txt", "w") as input_file:
            input_file.write(sample_text)

        # Process the sample text
        processed_text = process_text(sample_text, dicto, sep)

        with open("output.txt", "w") as output_file:
            output_file.write(processed_text)

        print("Created sample input.txt and processed it to output.txt")

    except Exception as e:
        print(f"Error processing files: {e}")


if __name__ == "__main__":
    test()
