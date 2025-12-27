# Obscura

A modern GUI application for private data removal with configuration management. Built with Java and Python, Obscura provides an intuitive interface for processing text according to customizable rules.

<img width="1197" height="817" alt="image" src="https://github.com/user-attachments/assets/54a2fc85-4fcb-4258-8e1c-447e7f320c69" />

## Purpose

Let's say you want to ask ChatGPT to review your resume, but don't want to leak your personal information. Instead of manually deleting all of it, you can use Obscura. Obscura will delete/reformat all your private information according to your configuration. This can be done for any document with private information in it.

## Features

- **Live Markdown Editing**: Edit configuration rules with real-time preview
- **Split-View Interface**: Side-by-side input/output panels
- **Case-Sensitive Rules**: Support for exact matching with quoted strings
- **Privacy-Focused**: Automatic file cleanup on application exit, all config private data stored locally.
- **Modern UI**: Dark blue gradient theme with intuitive controls

## Installation

### Prerequisites

- **Java**: OpenJDK 11 or higher
- **Python 3**: For text processing backend
- **Operating System**: Linux, macOS, or Windows

### Easy installation: just copy and run

```bash
# Clone the repo
git clone https://github.com/bogdan-tr/obscura.git
# cd into the repo
cd obscura
# make install script executable
chmod +x install.sh
# install
./install.sh
```

That's it! Now run it with `obscura` in the terminal.

## First Run

1. **Launch the application** using the `obscura` command
2. **Input/Output tab** opens automatically (primary workspace)
3. **Configuration tab** loads with your current rules
4. **Type or paste** text in the input panel
5. **Click "Run"** to process according to your configuration
6. **View results** in the output panel
7. **Copy results** using the copy button

### Config.md Format

- The top of the file says `CONFIG`, this tells the program that the file is a config file.
- `SEP: < >` specifies which separator to surround replaced words with, in this case a replaced word will be surrounded by angle brackets. For example: `John -> <name>`. Separators must be separated with a space. No separator is also valid.
- `john -> name` specifies that any case insensitive `john` should be replaced with `name`. (name will be surrounded by the configured separator)
- `"Li" -> lastname` specifies that any exact match of `Li` should be replaced with `lastname`. (lastname will be surrounded by the configured separator)
- `Jacob` with no replacement will simply be deleted.
- `;; this is a comment` any `;;` specify the start of a comment.

<img width="1194" height="804" alt="image" src="https://github.com/user-attachments/assets/9d51e8ca-8f82-4a44-bae1-a155e49ff912" />

## Uninstall

```bash
# go to installation directory
cd obscura
chmod +x uninstall.sh
./uninstall.sh
```

## Update Ideas

- NLTK integration for smarter private data detection
- Phone number and link detection with regular expressions

## Contact

[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-blue?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/bogdan-trigubov-bt3g)
