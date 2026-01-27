# 📋 Online Clipboard

A modern, secure, and user-friendly web application for transferring text and files between devices without requiring login or registration. Built with vanilla JavaScript, Firebase, and Tailwind CSS.

![HTML](https://img.shields.io/badge/HTML-100%25-orange?style=flat-square)
![Firebase](https://img.shields.io/badge/Backend-Firebase-yellow?style=flat-square)
![Status](https://img.shields.io/badge/Status-Active-success?style=flat-square)

---

## 🌟 Features

- **🔐 No Login Required** - Start sharing content instantly without creating an account
- **⚡ Real-time Transfer** - Powered by Firebase Firestore for instant synchronization
- **📱 Cross-Device Sharing** - Seamlessly move content between phones, tablets, and computers
- **📝 Text & Files** - Support for unlimited text and files up to 1MB
- **🔒 Auto-Expiry** - All content automatically deletes after 24 hours for privacy
- **🎨 Modern UI** - Beautiful glassmorphic design with smooth animations
- **🌐 Offline-Capable** - Uses Firebase's offline persistence

---

## 🚀 How It Works

### 1. **Upload Content**
- Navigate to the **Send** tab
- Choose between **Text** or **File** input
- Paste your text or select a file (images, documents, audio up to 1MB)

### 2. **Get Unique Code**
- Click "Generate Secure Code"
- Receive a random 6-character code (e.g., `A3K9Z7`)
- This code is your key to retrieve the content

### 3. **Retrieve Anywhere**
- Go to the **Retrieve** tab on any device
- Enter the 6-character code
- Access your content instantly
- Copy text or download files

---

## 🛠️ Technology Stack

- **Frontend**: HTML5, Vanilla JavaScript (ES6 Modules)
- **Styling**: Tailwind CSS (CDN)
- **Icons**: Lucide Icons
- **Backend**: Firebase Firestore (NoSQL Database)
- **Authentication**: Firebase Anonymous Auth
- **Hosting**: Can be deployed on GitHub Pages, Vercel, Netlify, or Firebase Hosting

---

## 📦 Installation & Setup

### Prerequisites
- A modern web browser (Chrome, Firefox, Safari, Edge)
- A Firebase project (Free tier is sufficient)

### Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/Ansh200618/Online-Clipboard.git
   cd Online-Clipboard
   ```

2. **Set Up Firebase**
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Create a new project (or use existing)
   - Enable **Firestore Database** (Start in test mode for development)
   - Enable **Anonymous Authentication** in Authentication settings
   - Copy your Firebase configuration

3. **Configure Firebase Keys**
   - Open `index.html`
   - Locate the Firebase configuration section (around line 373):
   ```javascript
   const firebaseConfig = {
       apiKey: "YOUR_API_KEY",
       authDomain: "YOUR_AUTH_DOMAIN",
       projectId: "YOUR_PROJECT_ID",
       storageBucket: "YOUR_STORAGE_BUCKET",
       messagingSenderId: "YOUR_MESSAGING_SENDER_ID",
       appId: "YOUR_APP_ID"
   };
   ```
   - Replace with your Firebase project credentials

4. **Run Locally**
   - Simply open `index.html` in a web browser, or
   - Use a local server:
   ```bash
   # Python 3
   python -m http.server 8000
   
   # Node.js (with http-server installed)
   npx http-server
   ```
   - Navigate to `http://localhost:8000`

5. **Deploy (Optional)**
   - **GitHub Pages**: Push to GitHub and enable Pages in repository settings
   - **Vercel**: `vercel deploy`
   - **Netlify**: Drag and drop the folder to Netlify
   - **Firebase Hosting**:
     ```bash
     firebase init hosting
     firebase deploy
     ```

---

## 📐 Project Structure

```
Online-Clipboard/
│
├── index.html          # Main application file (HTML, CSS, JS all-in-one)
└── README.md           # Project documentation
```

---

## 🔒 Security & Privacy

- **Anonymous Authentication**: Users are authenticated anonymously through Firebase, no personal data collected
- **Temporary Storage**: All data expires and is deleted after 24 hours
- **No External APIs**: All functionality is self-contained within Firebase services
- **Base64 Encoding**: Files are stored as Base64 strings directly in Firestore (no separate storage bucket needed)
- **Unique Codes**: 6-character random codes using alphanumeric characters (excluding confusing characters like 0, O, 1, I)

---

## 📊 Usage Limits

| Feature | Limit |
|---------|-------|
| Text Length | Unlimited characters |
| File Size | Max 1MB |
| Data Expiration | 24 hours |
| Authentication | Anonymous (no login) |

---

## 🎨 UI Highlights

- **Glassmorphic Design**: Modern frosted glass effect panels
- **Responsive Layout**: Works perfectly on mobile, tablet, and desktop
- **Smooth Animations**: Fade-in effects and loading states
- **Custom Scrollbars**: Sleek custom-styled scrollbars
- **Notification System**: Toast notifications for user feedback
- **Loading States**: Visual feedback during data transfer

---

## 🐛 Troubleshooting

### Issue: "Authentication failed. Please refresh."
- **Solution**: Check Firebase configuration keys and ensure Anonymous Auth is enabled

### Issue: "Upload failed. Try again."
- **Solution**: Ensure Firestore rules allow writes, check file size is under 1MB

### Issue: "Code not found or expired."
- **Solution**: Verify the code is correct and was created within the last 24 hours

### Issue: Page doesn't load properly
- **Solution**: Clear browser cache, check console for JavaScript errors, ensure CDN resources (Tailwind, Lucide) are loading

---

## 📝 Firestore Rules (Development)

For development, use these Firestore rules:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /artifacts/{appId}/public/data/clipboard_items/{itemId} {
      allow read, write: if request.auth != null;
    }
  }
}
```

---

## 🤝 Contributing

Contributions are welcome! Feel free to:
- Report bugs
- Suggest new features
- Submit pull requests

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

---

## 👨‍💻 Developer

**Anshdeep Singh**

- 🌐 [Portfolio](http://ansh200618.github.io/Portfolio/)
- 🎨 [PixelForge](https://pixelclouds.vercel.app/)
- 📸 [Instagram](https://www.instagram.com/anshdeep_officiall/)
- 💼 [LinkedIn](https://www.linkedin.com/in/anshdeep-singh-editor)

---

## 🌟 Acknowledgments

- **Firebase** - For backend infrastructure
- **Tailwind CSS** - For styling framework
- **Lucide Icons** - For beautiful icons
- **Google Fonts** - For Inter font family

---

## 📸 Screenshots

### Send Tab
Upload your text or file and receive a unique 6-character code.

### Retrieve Tab
Enter a code to instantly access shared content from any device.

### About Tab
Learn about the platform's features and usage limits.

---

## 🔮 Future Enhancements

- [ ] End-to-end encryption option
- [ ] Custom expiration times (1h, 12h, 7d)
- [ ] Password protection for sensitive content
- [ ] QR code generation for easy code sharing
- [ ] Usage statistics dashboard
- [ ] Multi-file upload support
- [ ] Rich text editor
- [ ] Dark/Light theme toggle

---

<div align="center">

**⭐ Star this repository if you find it helpful!**

Made with ❤️ by Anshdeep Singh

</div>
