# 🎨 Digital Art Generator - Java Swing

A creative Java Swing-based application that transforms images into **sketch-style digital art** using edge detection.  
Features two artistic modes: **black stencil** and **colored outline**, rendered gradually in real time like a human drawing on canvas.

---

## ✨ Features

- 🖼️ Load any image (JPG, PNG, etc.)
- 🧠 Applies **Sobel edge detection** to extract outlines
- ✏️ Gradually renders the sketch **pixel-by-pixel** for a hand-drawn effect
- 🖤 **Black-and-white stencil** version
- 🌈 **Colored outline** version (color from original image)
- 💾 Save/export the final artwork as PNG/JPG
- 📂 Simple image picker using `JFileChooser`
- 🎮 Interactive GUI with buttons and dialogs

---

## 📁 Files in This Project

| File                          | Description                                     |
|-------------------------------|-------------------------------------------------|
| `GradualOutlineDrawerblack.java` | Renders a **black stencil-style** outline art  |
| `GradualOutlineDrawercolor.java` | Renders a **colored outline** version of the image |

---

## 🚀 How to Run

### ✅ Prerequisites

- Java Development Kit (JDK 8 or later)
- Any IDE (like IntelliJ, Eclipse) or a terminal

---

### 💡 Steps

#### Option 1: Run via IDE
1. Open either file (`GradualOutlineDrawerblack.java` or `GradualOutlineDrawercolor.java`) in your Java IDE.
2. Run the `main()` method.
3. Use the file chooser to select an image.
4. Watch the artwork render live!
5. Save or restart from the options dialog.

#### Option 2: Run via Terminal
```bash(for black)
javac GradualOutlineDrawerblack.java
java GradualOutlineDrawerblack


