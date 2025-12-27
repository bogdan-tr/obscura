#!/bin/bash
# Obscura Installation Script
# Author: Bogdan Trigubov
set -e  # Exit on error

echo "=========================================="
echo "  Obscura Installer"
echo "  by Bogdan Trigubov"
echo "=========================================="
echo ""

check_java() {
    echo "🔍 Checking for Java installation..."
    
    # Method 1: Check for javac in PATH
    if command -v javac > /dev/null 2>&1; then
        JAVA_VERSION=$(javac -version 2>&1 | awk '{print $2}' | cut -d'.' -f1)
        echo "✓ Java JDK found via PATH (version $JAVA_VERSION)"
        return 0
    fi
    
    # Method 2: Check for JAVA_HOME environment variable
    if [ -n "$JAVA_HOME" ]; then
        if [ -f "$JAVA_HOME/bin/javac" ]; then
            JAVA_VERSION=$("$JAVA_HOME/bin/javac" -version 2>&1 | awk '{print $2}' | cut -d'.' -f1)
            echo "✓ Java JDK found via JAVA_HOME (version $JAVA_VERSION)"
            # Add JAVA_HOME to PATH for this script
            export PATH="$JAVA_HOME/bin:$PATH"
            return 0
        fi
    fi
    
    # Method 3: Check for java command (might be JRE only)
    if command -v java > /dev/null 2>&1; then
        echo "⚠️  Java Runtime found, but javac (JDK) is not available"
        echo "   JDK is required for compilation"
    fi
    
    return 1
}

# Run Java check
if ! check_java; then
    echo ""
    echo "❌ Error: Java JDK is not properly installed or configured."
    echo ""
    echo "Java JDK installation options:"
    echo ""
    exit 1
fi

# Determine installation directory
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

echo "Installing to: $INSTALL_DIR"
echo ""

# Create directories
mkdir -p "$INSTALL_DIR"
mkdir -p "$BIN_DIR"

# Compile Java files
echo "📦 Compiling Java files..."
javac -d "$INSTALL_DIR" *.java
if [ $? -ne 0 ]; then
    echo "❌ Compilation failed!"
    echo "   Make sure all Java files are in the current directory."
    exit 1
fi
echo "✓ Compilation successful"

# Copy ALL necessary files to installation directory
echo "📦 Copying program files..."

# Copy ALL Java source files (for reference and potential recompilation)
cp *.java "$INSTALL_DIR/" 2>/dev/null || echo "⚠️  No Java source files found to copy"
cp *.json "$INSTALL_DIR/" 2>/dev/null || echo "⚠️  No json source files found to copy"
cp *.txt "$INSTALL_DIR/" 2>/dev/null || echo "⚠️  No txt source files found to copy"
cp *.md "$INSTALL_DIR/" 2>/dev/null || echo "⚠️  No md source files found to copy"

# Copy ALL Python files
if ls *.py > /dev/null 2>&1; then
    cp *.py "$INSTALL_DIR/"
    echo "✓ Copied Python files"
else
    echo "⚠️  No Python files found to copy"
fi

# Create a proper run_gui.sh in the installation directory
echo "Creating run_gui.sh..."
cat > "$INSTALL_DIR/run_gui.sh" << 'EOF'
#!/bin/bash
# Obscura GUI Launcher
# Change to the directory where this script is located
cd "$(dirname "$(readlink -f "$0" 2>/dev/null || realpath "$0")")"
# Run the Java GUI
java -cp . GUI
EOF

chmod +x "$INSTALL_DIR/run_gui.sh"
echo "✓ Created run_gui.sh"

# Copy any other necessary files
if [ -d "resources" ]; then
    cp -r resources "$INSTALL_DIR/"
    echo "✓ Copied resources directory"
fi

# Copy any other files that might be needed
if ls *.txt *.md *.properties *.config *.json *.xml > /dev/null 2>&1; then
    cp *.txt *.md *.properties *.config *.json *.xml "$INSTALL_DIR/" 2>/dev/null || true
    echo "✓ Copied configuration/documentation files"
fi

# Create launcher script for Unix-like systems
if [ "$OS" != "windows" ]; then
    LAUNCHER="$BIN_DIR/obscura"
    
    cat > "$LAUNCHER" << EOF
#!/bin/bash
# Obscura launcher script
# Run the run_gui.sh script from the installation directory
"$INSTALL_DIR/run_gui.sh"
EOF
    
    chmod +x "$LAUNCHER"
    echo "✓ Created launcher script: $LAUNCHER"
    echo "  Command: obscura"
fi

# Create Windows launcher
if [ "$OS" = "windows" ]; then
    # Create batch file for cmd
    LAUNCHER_BAT="$BIN_DIR/obscura.bat"
    
    cat > "$LAUNCHER_BAT" << EOF
@echo off
set INSTALL_DIR=%USERPROFILE%\\bin\\obscura
cd "%INSTALL_DIR%" && run_gui.sh
EOF
    
    echo "✓ Created launcher script: $LAUNCHER_BAT"
    echo "  Command: obscura (from cmd.exe)"
    
    # Also create a shell script for Git Bash/WSL
    LAUNCHER_SH="$BIN_DIR/obscura"
    
    cat > "$LAUNCHER_SH" << EOF
#!/bin/bash
# Obscura launcher script for Git Bash
"$INSTALL_DIR/run_gui.sh"
EOF
    
    chmod +x "$LAUNCHER_SH"
    echo "✓ Created launcher script: $LAUNCHER_SH"
    echo "  Command: obscura (from Git Bash/WSL)"
fi

# Check if BIN_DIR is in PATH using grep for POSIX compatibility
if echo ":$PATH:" | grep -q ":$BIN_DIR:"; then
    echo "✓ $BIN_DIR is already in PATH"
else
    echo ""
    echo "⚠️  Warning: $BIN_DIR is not in your PATH"
    echo ""
    echo "Add this line to your shell configuration file:"
    
    # Determine shell
    if [ -n "$ZSH_VERSION" ]; then
        SHELL_RC="$HOME/.zshrc"
        echo "  echo 'export PATH=\"$BIN_DIR:\$PATH\"' >> ~/.zshrc"
        echo "  Then run: source ~/.zshrc"
    elif [ -n "$BASH_VERSION" ]; then
        if [ "$OS" = "macos" ]; then
            SHELL_RC="$HOME/.bash_profile"
            echo "  echo 'export PATH=\"$BIN_DIR:\$PATH\"' >> ~/.bash_profile"
            echo "  Then run: source ~/.bash_profile"
        else
            SHELL_RC="$HOME/.bashrc"
            echo "  echo 'export PATH=\"$BIN_DIR:\$PATH\"' >> ~/.bashrc"
            echo "  Then run: source ~/.bashrc"
        fi
    else
        echo "  export PATH=\"$BIN_DIR:\$PATH\""
    fi
    
    if [ "$OS" = "windows" ]; then
        echo ""
        echo "For Windows, add to PATH:"
        echo "  setx PATH \"%PATH%;$BIN_DIR\""
    fi
fi

echo ""
echo "=========================================="
echo "✓ Installation complete!"
echo "=========================================="
echo ""
echo "To run Obscura:"
echo "  obscura"
echo ""
echo "If 'obscura' command is not found:"
echo "  1. Make sure $BIN_DIR is in your PATH"
echo "  2. Restart your terminal"
echo "  3. Or run the appropriate source command above"
echo ""
echo "You can also run Obscura directly:"
echo "  $INSTALL_DIR/run_gui.sh"
echo ""
echo "Debugging:"
echo "  Check if files were copied: ls -la $INSTALL_DIR/"
echo "  Check if launcher exists: ls -la $BIN_DIR/obscura"
echo "  Run directly to test: $INSTALL_DIR/run_gui.sh"
echo ""
