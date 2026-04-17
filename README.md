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
- **🔥 Burn After Reading** - Optional one-time retrieval mode deletes clips after first successful fetch
- **🛡️ Optional E2EE (AES-256)** - Client-side password encryption for text clips so plaintext never reaches the server
- **🔐 Secure Lock (IP Whitelist)** - Optional retrieval lock to a specific recipient IP
- **📝 Markdown + Syntax Preview** - Live rendered preview with code highlighting for text snippets
- **🖼️ OCR Image-to-Text** - Extract text from screenshots directly in the Send flow
- **📲 Direct Device Push (Live Channel)** - Push newly generated clips to connected devices instantly
- **🕘 Local Recent History** - Last 5 send/retrieve entries stored only in the browser
- **🎨 Appearance System** - Day/Night/Auto theme modes, 12 accent color presets, 11 background textures — all persisted across sessions
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
- **Edge API**: Vercel Edge Functions (`/api/create-clip`, `/api/retrieve-clip`)
- **Storage**: Supabase Storage for file uploads
- **Hosting**: Deployed on Vercel at https://copycloud.me/

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

4. **Configure Edge Function Environment Variables (Required for secure create/retrieve)**
   - In Vercel project settings, set:
     - `SUPABASE_URL`
     - `SUPABASE_SERVICE_ROLE_KEY`
   - These are used only by Edge endpoints to enforce expiry, secure lock IP checks, and burn-after-reading.

5. **Run Locally**
   - Simply open `index.html` in a web browser, or
   - Use a local server:
   ```bash
   # Python 3
   python -m http.server 8000
   
   # Node.js (with http-server installed)
   npx http-server
   ```
   - Navigate to `http://localhost:8000`

6. **Deploy (Optional)**
   - **Vercel** (Recommended): `vercel deploy` or connect GitHub repo
   - **GitHub Pages**: Push to GitHub and enable Pages in repository settings
   - **Netlify**: Drag and drop the folder to Netlify

---

## 📐 Project Structure

```
Online-Clipboard/
│
├── index.html          # Main application file (HTML, CSS, JS all-in-one)
├── api/
│   ├── create-clip.js  # Edge function for secure clip creation
│   └── retrieve-clip.js# Edge function for secure retrieval/burn/IP validation
├── favicon.svg         # Site favicon
├── sitemap.xml         # SEO sitemap
└── README.md           # Project documentation
```

---

## 🔒 Security & Privacy

- **No Authentication Required**: No user accounts or personal data collection
- **Temporary Storage**: All data expires and is deleted after 24 hours
- **Burn After Reading**: Optional immediate deletion after first successful retrieval
- **Optional E2EE**: AES-256 encryption for text clips via browser crypto, password is never sent to backend
- **IP Secure Lock**: Optional IP match enforcement during retrieval through edge boundary
- **Secure Storage**: Files are stored securely in Supabase Storage
- **Database Security**: PostgreSQL database with Row Level Security (RLS)
- **Unique Codes**: 6-character random codes using alphanumeric characters (excluding confusing characters like 0, O, 1, I)

---

## 📊 Usage Limits

| Feature | Limit |
|---------|-------|
| Text Length | Unlimited characters |
| File Size | Max 40MB |
| Data Expiration | 24 hours (or first retrieval when Burn After Reading is enabled) |
| Authentication | None required (no login) |

---

## 🎨 UI Highlights

- **Glassmorphic Design**: Modern frosted glass effect panels
- **Responsive Layout**: Works perfectly on mobile, tablet, and desktop
- **Smooth Animations**: Fade-in effects and loading states
- **Custom Scrollbars**: Sleek custom-styled scrollbars
- **Notification System**: Toast notifications for user feedback
- **Loading States**: Visual feedback during data transfer
- **Day / Night / Auto Theme**: Full light and dark modes; Auto tracks system preference live
- **12 Accent Color Presets**: Indigo, Purple, Blue, Emerald, Rose, Amber, Teal, Pink, Crimson, Cyan, Gold, Violet — all apply to buttons, icons, and background gradient
- **11 Background Textures**: None, Dots, Grid, Waves, Diagonal, Diamonds, Circles, Crosshatch, Hexagons, Triangles, Stars, Zigzag — pure SVG, no external assets
- **Accent-Tinted Backgrounds**: Body and overlay colors shift with the chosen accent in both light and dark modes
- **Texture Density Control**: Tight ↔ Loose slider scales any background texture pattern in real-time

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

### Send Tab
![Send Tab](https://github.com/Ansh200618/Online-Clipboard/blob/main/Screenshots/Send.png)
- Upload your text or file and receive a unique 6-character code.

### Retrieve Tab
![Retrieve Tab](https://github.com/Ansh200618/Online-Clipboard/blob/main/Screenshots/Retrieve.png)
- Enter a code to instantly access shared content from any device.

### About Tab
![About Tab](https://github.com/Ansh200618/Online-Clipboard/blob/main/Screenshots/About.png)
- Learn about the platform's features and usage limits.

---

## 🔮 Future Enhancements

- [ ] Custom expiration times (1h, 6h, 12h, 7d, or never)
- [ ] Browser extension for Chrome & Firefox (1-click send/receive)
- [ ] Native Web Push delivery for offline devices

---

<div align="center">

**⭐ Star this repository if you find it helpful!**

Made with ❤️ by Anshdeep Singh

</div>
