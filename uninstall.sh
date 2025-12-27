#!/bin/bash
# Obscura Uninstall Script
# Author: Bogdan Trigubov
set -e  # Exit on error

echo "=========================================="
echo "  Obscura Uninstaller"
echo "  by Bogdan Trigubov"
echo "=========================================="
echo ""

# Determine installation directory (same logic as install script)
case "$(uname -s)" in
    CYGWIN*|MINGW*|MSYS*)
        # Windows
        INSTALL_DIR="$HOME/bin/obscura"
        BIN_DIR="$HOME/bin"
        OS="windows"
        ;;
    Darwin*)
        # macOS
        INSTALL_DIR="$HOME/.local/share/obscura"
        BIN_DIR="$HOME/.local/bin"
        OS="macos"
        ;;
    *)
        # Linux/Unix
        INSTALL_DIR="$HOME/.local/share/obscura"
        BIN_DIR="$HOME/.local/bin"
        OS="linux"
        ;;
esac

echo "This will uninstall Obscura from your system."
echo ""
echo "Installation directory: $INSTALL_DIR"
echo "Launcher directory: $BIN_DIR"
echo ""

# Check if Obscura is installed
if [ ! -d "$INSTALL_DIR" ]; then
    echo "⚠️  Obscura installation directory not found at: $INSTALL_DIR"
    echo "   It may already be uninstalled or was installed in a different location."
    echo ""
    read -p "Do you want to check for and remove launcher scripts anyway? (y/n): " -n 1 -r
    echo ""
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        echo "Uninstall cancelled."
        exit 0
    fi
else
    echo "Found Obscura installation."
    echo ""
    
    # Show what will be removed
    echo "The following will be removed:"
    echo "  • Installation directory: $INSTALL_DIR"
    
    if [ "$OS" != "windows" ]; then
        [ -f "$BIN_DIR/obscura" ] && echo "  • Launcher script: $BIN_DIR/obscura"
    else
        [ -f "$BIN_DIR/obscura.bat" ] && echo "  • Launcher script: $BIN_DIR/obscura.bat"
        [ -f "$BIN_DIR/obscura" ] && echo "  • Launcher script: $BIN_DIR/obscura"
    fi
    
    echo ""
    
    # Confirm uninstall
    read -p "Are you sure you want to uninstall Obscura? (y/n): " -n 1 -r
    echo ""
    
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        echo "Uninstall cancelled."
        exit 0
    fi
fi

echo ""
echo "🗑️  Uninstalling Obscura..."
echo ""

# Remove installation directory
if [ -d "$INSTALL_DIR" ]; then
    echo "Removing installation directory..."
    rm -rf "$INSTALL_DIR"
    if [ $? -eq 0 ]; then
        echo "✓ Removed: $INSTALL_DIR"
    else
        echo "❌ Failed to remove: $INSTALL_DIR"
        echo "   You may need to remove it manually with elevated permissions."
    fi
else
    echo "⚠️  Installation directory not found (may already be removed)"
fi

# Remove launcher scripts
REMOVED_LAUNCHERS=0

if [ "$OS" != "windows" ]; then
    # Unix-like systems
    if [ -f "$BIN_DIR/obscura" ]; then
        echo "Removing launcher script..."
        rm -f "$BIN_DIR/obscura"
        if [ $? -eq 0 ]; then
            echo "✓ Removed: $BIN_DIR/obscura"
            REMOVED_LAUNCHERS=1
        else
            echo "❌ Failed to remove: $BIN_DIR/obscura"
        fi
    fi
else
    # Windows
    if [ -f "$BIN_DIR/obscura.bat" ]; then
        echo "Removing Windows launcher (cmd)..."
        rm -f "$BIN_DIR/obscura.bat"
        if [ $? -eq 0 ]; then
            echo "✓ Removed: $BIN_DIR/obscura.bat"
            REMOVED_LAUNCHERS=1
        else
            echo "❌ Failed to remove: $BIN_DIR/obscura.bat"
        fi
    fi
    
    if [ -f "$BIN_DIR/obscura" ]; then
        echo "Removing Windows launcher (Git Bash/WSL)..."
        rm -f "$BIN_DIR/obscura"
        if [ $? -eq 0 ]; then
            echo "✓ Removed: $BIN_DIR/obscura"
            REMOVED_LAUNCHERS=1
        else
            echo "❌ Failed to remove: $BIN_DIR/obscura"
        fi
    fi
fi

if [ $REMOVED_LAUNCHERS -eq 0 ]; then
    echo "⚠️  No launcher scripts found (may already be removed)"
fi

echo ""
echo "=========================================="
echo "✓ Uninstall complete!"
echo "=========================================="
echo ""
echo "Obscura has been removed from your system."
echo ""
echo "Note: The PATH modifications in your shell configuration"
echo "      files (.bashrc, .zshrc, .bash_profile, etc.) were NOT"
echo "      automatically removed. If you added $BIN_DIR to your"
echo "      PATH during installation, you may want to remove that"
echo "      line manually if you're not using that directory for"
echo "      other programs."
echo ""
echo "To manually verify removal:"
echo "  ls -la $INSTALL_DIR  (should show 'No such file or directory')"
echo "  ls -la $BIN_DIR/obscura  (should show 'No such file or directory')"
echo ""
