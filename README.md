# 🪑 Furniture Matrix – Interactive 2D & 3D Room Designer

**Furniture Matrix** is a Java-based desktop application that empowers furniture designers and customers to collaboratively create, configure, and visualize room layouts in both 2D and 3D views.

---

## 📋 Features

### 🔐 User Authentication
- Secure login for two roles:
  - **Designer**: `designer / designer123`
  - **Admin**: `admin / admin123`

### 📐 Room Setup
- Choose room shapes: Rectangle, L-shape, U-shape, T-shape, Square, Circular
- Set custom room dimensions (width & depth)
- Apply color themes to walls and floors
- Right click and drag to rotate the 3D room and check lighting/shading.

### 🛋️ Furniture Management
- Add furniture like chairs, tables, wardrobes, and desks
- Scale, rotate, move items freely
- Change **individual furniture colors**
- Select furniture items for editing/removal

### 🖼️ Visualization
- **2D top-down** layout with zoom
- **3D real-time** rendering using OpenGL (via JOGL)
- Intuitive drag-and-drop and mouse-based manipulation

### 📁 Design Management
- Save and load design layouts
- Delete or modify existing projects
- Session-based item synchronization between 2D and 3D views

---

## 🧰 Technologies Used

- **Java Swing** – UI components and layouts
- **JOGL** – Real-time 3D rendering via OpenGL
- **AWT** – Mouse & keyboard event handling
- **MVC Architecture** – Clean separation of logic, UI, and interaction

---

## 🧠 System Overview

This application simulates an in-store design system that helps users visualize how selected furniture items will look in their room before purchasing.

### Key Components
- `Renderer2D.java` / `Renderer3D.java`: Dual rendering logic
- `FurnitureFactory.java`: Object factory pattern for managing furniture types
- `ColorManager.java`: Handles dynamic color selection
- `ShowRoomPanel.java`: Core interface for the design canvas
- `Menu.java` and `Login.java`: Entry points and access control

---

## 🚀 Setup & Usage

### Requirements
- Java 11 or higher
- JOGL library dependencies included in `lib/`
- Windows OS recommended for compatibility

### Launching
Run the application via:

```bash
java -cp .;lib/* furniture.management.system.controller.Main
