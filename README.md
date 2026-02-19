# 📋 Copy Cloud - Online Clipboard

A modern, secure, and user-friendly web application for transferring text and files between devices without requiring login or registration. Built with vanilla JavaScript, Supabase, and Tailwind CSS.

![HTML](https://img.shields.io/badge/HTML-100%25-orange?style=flat-square)
![Supabase](https://img.shields.io/badge/Backend-Supabase-green?style=flat-square)
![Status](https://img.shields.io/badge/Status-Active-success?style=flat-square)

---

## 🌟 Features

- **🔐 No Login Required** - Start sharing content instantly without creating an account
- **⚡ Real-time Transfer** - Powered by Supabase PostgreSQL for instant synchronization
- **📱 Cross-Device Sharing** - Seamlessly move content between phones, tablets, and computers
- **📝 Text & Files** - Support for unlimited text and files up to 40MB
- **🔒 Auto-Expiry** - All content automatically deletes after 24 hours for privacy
- **📷 QR Code Generation** - Scan QR codes to quickly access shared content
- **💜 Purple Gradient UI** - Modern design with indigo-to-purple gradient theme
- **🌐 Real-time Database** - PostgreSQL database with real-time subscriptions

---

## 🚀 How It Works

### 1. **Upload Content**
- Navigate to the **Send** tab
- Choose between **Text** or **File** input
- Paste your text or select a file (images, documents, audio up to 40MB)

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
- **Backend**: Supabase PostgreSQL Database
- **Storage**: Supabase Storage for file uploads
- **Hosting**: Deployed on Vercel at https://copycloud.vercel.app/

---

## 📦 Installation & Setup

### Prerequisites
- A modern web browser (Chrome, Firefox, Safari, Edge)
- A Supabase project (Free tier is sufficient)

### Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/Ansh200618/Online-Clipboard.git
   cd Online-Clipboard
   ```

2. **Set Up Supabase**
   - Go to [Supabase Console](https://supabase.com/)
   - Create a new project (or use existing)
   - Create a **clipboard_items** table with required columns
   - Create a **Storage** bucket for file uploads
   - Set up Row Level Security (RLS) policies as needed
   - Copy your project URL and anon key

3. **Configure Supabase Keys**
   - Open `index.html`
   - Locate the Supabase configuration section (search for `SUPABASE CONFIGURATION`):
   ```javascript
   const SB_URL = 'YOUR_SUPABASE_URL';
   const SB_KEY = 'YOUR_SUPABASE_ANON_KEY';
   ```
   - Replace with your Supabase project credentials

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
   - **Vercel** (Recommended): `vercel deploy` or connect GitHub repo
   - **GitHub Pages**: Push to GitHub and enable Pages in repository settings
   - **Netlify**: Drag and drop the folder to Netlify

---

## 📐 Project Structure

```
Online-Clipboard/
│
├── index.html          # Main application file (HTML, CSS, JS all-in-one)
├── favicon.svg         # Site favicon
├── sitemap.xml         # SEO sitemap
└── README.md           # Project documentation
```

---

## 🔒 Security & Privacy

- **No Authentication Required**: No user accounts or personal data collection
- **Temporary Storage**: All data expires and is deleted after 24 hours
- **Secure Storage**: Files are stored securely in Supabase Storage
- **Database Security**: PostgreSQL database with Row Level Security (RLS)
- **Unique Codes**: 6-character random codes using alphanumeric characters (excluding confusing characters like 0, O, 1, I)

---

## 📊 Usage Limits

| Feature | Limit |
|---------|-------|
| Text Length | Unlimited characters |
| File Size | Max 40MB |
| Data Expiration | 24 hours |
| Authentication | None required (no login) |

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

### Issue: "Upload failed. Try again."
- **Solution**: Check Supabase configuration keys and ensure database is accessible. Ensure Supabase RLS policies allow writes, check file size is under 40MB

### Issue: "Code not found or expired."
- **Solution**: Verify the code is correct and was created within the last 24 hours

### Issue: Page doesn't load properly
- **Solution**: Clear browser cache, check console for JavaScript errors, ensure CDN resources (Tailwind, Lucide) are loading

---

## 📝 Supabase Configuration

### Database Schema

Create a `clips` table with the following structure:
- `code` (text, primary key) - The 6-character code
- `content` (text, nullable) - For text content
- `type` (text) - Either "text" or "file"
- `created_at` (timestamp with time zone) - Creation timestamp

### Storage Setup

Create a storage bucket named `clipboard-files` for file uploads.

### Row Level Security (RLS)

Enable RLS and create policies as needed for your security requirements.

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

- **Supabase** - For backend infrastructure and database
- **Tailwind CSS** - For styling framework
- **Lucide Icons** - For beautiful icons
- **Google Fonts** - For Inter font family

---

## 📸 Screenshots

### **Send Tab**
![Send Tab](https://github.com/Ansh200618/Online-Clipboard/blob/main/Screenshots/Send.png)
Upload your text or file and receive a unique 6-character code.

### **Retrieve Tab**
![Retrieve Tab](https://github.com/Ansh200618/Online-Clipboard/blob/main/Screenshots/Retrieve.png)
Enter a code to instantly access shared content from any device.

### **About Tab**
![About Tab](https://github.com/Ansh200618/Online-Clipboard/blob/main/Screenshots/About.png)
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
