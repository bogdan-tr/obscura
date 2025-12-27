# Obscura

A modern GUI application for private data removal with configuration management. Built with Java and Python, Obscura provides an intuitive interface for processing text according to customizable rules.

## Features

- **Live Markdown Editing**: Edit configuration rules with real-time preview
- **Split-View Interface**: Side-by-side input/output panels
- **Case-Sensitive Rules**: Support for exact matching with quoted strings
- **Privacy-Focused**: Automatic file cleanup on application exit, all config private data stored locally.
- **Modern UI**: Dark blue gradient theme with intuitive controls

### Prerequisites

- **Java**: OpenJDK 11 or higher
- **Python 3**: For text processing backend
- **Operating System**: Linux, macOS, or Windows

### Installation

#### Easy installation: just copy and run

```bash
# Clone or download the repository
cd obscura
chmod +x install.sh
./install.sh

# Run from anywhere
obscura
```

### First Run

1. **Launch the application** using the `obscura` command
2. **Input/Output tab** opens automatically (primary workspace)
3. **Configuration tab** loads with your current rules
4. **Type or paste** text in the input panel
5. **Click "Run"** to process according to your configuration
6. **View results** in the output panel
7. **Copy results** using the copy button

### Config.md Format

```markdown
CONFIG
SEP: < >

# Case-insensitive replacement

hello -> REMOVED

# Case-sensitive replacement (exact match)

"teSt" -> REMOVED_EXACT

# Removal (empty replacement)

remove

# Comments

;; This is a comment and won't be processed
```

### Separator Configuration

The `SEP:` line defines how replacements are wrapped:
k

```markdown
SEP: < > ;; Wrap in angle brackets: word → <word>
SEP: [ ] ;; Wrap in square brackets: word → [word]
SEP: | | ;; Wrap with bars word → |word|
SEP: ;; No separation: word → word
```

