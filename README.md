<div align="center">

<img src="public/logo.png" alt="Copy Cloud Logo" width="96" height="96" />

# Copy Cloud

### Instant, secure clipboard sharing across all your devices

[![Live Demo](https://img.shields.io/badge/Live%20Demo-copycloud.me-4f46e5?style=for-the-badge&logo=vercel&logoColor=white)](https://copycloud.me/)
[![React](https://img.shields.io/badge/React-18-61dafb?style=for-the-badge&logo=react&logoColor=white)](https://react.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5-3178c6?style=for-the-badge&logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![Supabase](https://img.shields.io/badge/Supabase-Backend-3ecf8e?style=for-the-badge&logo=supabase&logoColor=white)](https://supabase.com/)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind%20CSS-4-06b6d4?style=for-the-badge&logo=tailwindcss&logoColor=white)](https://tailwindcss.com/)
[![Deployed on Vercel](https://img.shields.io/badge/Deployed%20on-Vercel-000000?style=for-the-badge&logo=vercel&logoColor=white)](https://vercel.com/)

</div>

---

## Overview

**Copy Cloud** is a modern, privacy-first web application for instantly transferring text and files between devices — no login, no account, no friction. Generate a 6-character code on one device and retrieve your content on any other within 24 hours.

---

## ✨ Features

| Feature | Description |
|---|---|
| 🔐 **No Login Required** | Share content instantly — zero accounts, zero sign-ups |
| ⚡ **Real-time Transfer** | Powered by Supabase PostgreSQL with live subscriptions |
| 📱 **Cross-Device** | Works seamlessly across phones, tablets, and desktops |
| 📝 **Text & Files** | Unlimited text and files up to **40 MB** |
| 🔒 **Auto-Expiry** | All content is permanently deleted after **24 hours** |
| 📷 **QR Code Sharing** | Scan a QR code to jump straight to your content |
| 🎨 **Full Theme System** | Day / Night / Auto modes, 12 accent colors, 11 background textures |
| 📜 **Clip History** | Browser-local history of your 5 most recent clips |
| 📦 **Installable PWA** | Install on any home screen; service worker for offline shell |

---

## 📸 Screenshots

<table>
  <tr>
    <td align="center"><b>Send</b></td>
    <td align="center"><b>Retrieve</b></td>
    <td align="center"><b>About</b></td>
  </tr>
  <tr>
    <td><img src="Screenshots/Send.png" alt="Send tab" /></td>
    <td><img src="Screenshots/Retrieve.png" alt="Retrieve tab" /></td>
    <td><img src="Screenshots/About.png" alt="About tab" /></td>
  </tr>
</table>

---

## 🚀 How It Works

### 1 · Send Content
1. Navigate to the **Send** tab.
2. Choose **Text** or **File** mode.
3. Paste your text, or pick a file (images, documents, audio — up to 40 MB).
4. Click **Generate Secure Code**.

### 2 · Get Your Code
- Receive a random **6-character alphanumeric code** (e.g., `A3K9Z7`).
- Ambiguous characters (`0`, `O`, `1`, `I`) are excluded for accuracy.
- A **QR code** is generated automatically for quick mobile access.

### 3 · Retrieve Anywhere
1. Open **Copy Cloud** on any device.
2. Go to the **Retrieve** tab and enter your code.
3. Copy the text or download the file instantly.

> All data is automatically and permanently purged after **24 hours**.

---

## 🛠️ Technology Stack

| Layer | Technology |
|---|---|
| **Framework** | [React 18](https://react.dev/) + [TypeScript](https://www.typescriptlang.org/) |
| **Build Tool** | [Vite 6](https://vitejs.dev/) |
| **Styling** | [Tailwind CSS 4](https://tailwindcss.com/) |
| **UI Components** | [Radix UI](https://www.radix-ui.com/), [MUI](https://mui.com/), [shadcn/ui](https://ui.shadcn.com/) |
| **Icons** | [Lucide React](https://lucide.dev/) |
| **Backend & DB** | [Supabase](https://supabase.com/) (PostgreSQL + Realtime) |
| **File Storage** | Supabase Storage |
| **Hosting** | [Vercel](https://vercel.com/) |

---

## 📐 Project Structure

```
Copy-Cloud/
├── public/                   # Static assets (favicon, PWA manifest, sw.js, og-image)
├── src/
│   ├── app/
│   │   ├── App.tsx           # Root component & router setup
│   │   └── components/       # Feature-level UI components
│   ├── imports/              # Shared imports & re-exports
│   ├── lib/                  # Supabase client, utilities
│   ├── styles/               # Global CSS / Tailwind base
│   └── main.tsx              # Application entry point
├── Screenshots/              # README screenshot assets
├── vercel.json               # Vercel routing & security headers
├── vite.config.ts            # Vite build configuration
├── postcss.config.mjs        # PostCSS / Tailwind pipeline
└── package.json              # Dependencies & scripts
```

---

## ⚙️ Getting Started

### Prerequisites

- **Node.js** ≥ 18 and **pnpm** (or npm / yarn)
- A [Supabase](https://supabase.com/) project (free tier is sufficient)

### 1 · Clone the Repository

```bash
git clone https://github.com/Ansh200618/Copy-Cloud.git
cd Copy-Cloud
```

### 2 · Install Dependencies

```bash
pnpm install      # or: npm install
```

### 3 · Configure Supabase

Create a `.env` file in the project root:

```env
VITE_SUPABASE_URL=https://your-project-ref.supabase.co
VITE_SUPABASE_ANON_KEY=your-anon-key
```

> **Never commit** `.env` to version control. It is already listed in `.gitignore`.

### 4 · Set Up the Database

Run the following SQL in your Supabase SQL editor:

```sql
-- Clips table
CREATE TABLE clips (
  code        TEXT PRIMARY KEY,
  content     TEXT,
  type        TEXT NOT NULL CHECK (type IN ('text', 'file')),
  file_name   TEXT,
  file_size   BIGINT,
  created_at  TIMESTAMPTZ DEFAULT now() NOT NULL
);

-- Auto-expiry: delete rows older than 24 hours
CREATE OR REPLACE FUNCTION delete_expired_clips() RETURNS void
LANGUAGE sql AS $$
  DELETE FROM clips WHERE created_at < now() - INTERVAL '24 hours';
$$;
```

Create a **Storage bucket** named `clipboard-files` and configure **Row Level Security (RLS)** policies appropriate for your project.

### 5 · Run Locally

```bash
pnpm dev      # or: npm run dev
```

Open [http://localhost:5173](http://localhost:5173) in your browser.

### 6 · Build for Production

```bash
pnpm build    # or: npm run build
```

Output is placed in the `dist/` directory.

---

## 🚢 Deployment

### Vercel (Recommended)

[![Deploy with Vercel](https://vercel.com/button)](https://vercel.com/new/clone?repository-url=https://github.com/Ansh200618/Copy-Cloud)

1. Import your repository in the [Vercel dashboard](https://vercel.com/new).
2. Add `VITE_SUPABASE_URL` and `VITE_SUPABASE_ANON_KEY` as **Environment Variables**.
3. Deploy — Vercel routing rules are already defined in `vercel.json`.

### Other Platforms

Any static host that supports SPA routing (Netlify, Cloudflare Pages, GitHub Pages with a redirect rule) works with the built `dist/` output.

---

## 🔒 Security & Privacy

- **No accounts** — no personal data is ever collected.
- **24-hour auto-purge** — all clips and files are permanently deleted.
- **Supabase RLS** — database-level row security policies restrict access.
- **Secure headers** — `X-Content-Type-Options`, `X-Frame-Options`, and `X-XSS-Protection` are set for all responses via `vercel.json`.
- **Unique codes** — ambiguous characters excluded to prevent look-alike guessing.

---

## 📊 Usage Limits

| Feature | Limit |
|---|---|
| Text length | Unlimited |
| File size | 40 MB per clip |
| Data retention | 24 hours |
| Clip history (local) | 5 most recent clips |
| Authentication | None required |

---

## 🎨 Appearance & Theming

| Option | Details |
|---|---|
| **Theme modes** | Day · Night · Auto (tracks system preference live) |
| **Accent colors** | 12 presets — Indigo, Purple, Blue, Emerald, Rose, Amber, Teal, Pink, Crimson, Cyan, Gold, Violet |
| **Background textures** | 11 SVG patterns — Dots, Grid, Waves, Diagonal, Diamonds, Circles, Crosshatch, Hexagons, Triangles, Stars, Zigzag |
| **Texture density** | Real-time Tight ↔ Loose slider |

All appearance settings are persisted across sessions.

---

## 🐛 Troubleshooting

| Symptom | Solution |
|---|---|
| "Upload failed. Try again." | Verify `VITE_SUPABASE_URL` / `VITE_SUPABASE_ANON_KEY` are correct; confirm RLS allows writes; check file is under 40 MB |
| "Code not found or expired." | The clip may have exceeded 24 hours, or the code was entered incorrectly |
| Blank page / JS errors | Run `pnpm build` and check for TypeScript errors; clear browser cache |
| Service worker stale | Hard-refresh (`Ctrl + Shift + R`) or clear site data in DevTools |

---

## 🔮 Roadmap

- [ ] End-to-end encryption — client-side crypto before upload
- [ ] Password-protected clips — lock content with a passphrase
- [ ] Custom expiration times (1 h, 6 h, 12 h, 7 d, or never)
- [ ] Browser extension for Chrome & Firefox (1-click send/receive)
- [ ] Push notifications — get alerted when your clip is retrieved

---

## 🤝 Contributing

Contributions are welcome!

1. **Fork** the repository
2. **Create** a feature branch: `git checkout -b feature/your-feature`
3. **Commit** your changes with clear messages
4. **Push** and open a **Pull Request**

Please open an issue first for significant changes so we can discuss the approach.

---

## 👨‍💻 Developer

**Anshdeep Singh**

[![Portfolio](https://img.shields.io/badge/Portfolio-Visit-4f46e5?style=flat-square&logo=globe&logoColor=white)](https://anshdeepsingh.dev/)
[![Instagram](https://img.shields.io/badge/Instagram-Follow-e1306c?style=flat-square&logo=instagram&logoColor=white)](https://instagram.com/)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-0a66c2?style=flat-square&logo=linkedin&logoColor=white)](https://linkedin.com/)

---

## 🙏 Acknowledgments

- [Supabase](https://supabase.com/) — open-source Firebase alternative for backend and storage
- [Tailwind CSS](https://tailwindcss.com/) — utility-first CSS framework
- [Radix UI](https://www.radix-ui.com/) — accessible, unstyled UI primitives
- [Lucide](https://lucide.dev/) — beautiful, consistent open-source icons
- [Vite](https://vitejs.dev/) — lightning-fast frontend build tool

---

<div align="center">

⭐ **If you find Copy Cloud useful, please star the repository!** ⭐

Made with ❤️ by [Anshdeep Singh](https://github.com/Ansh200618)

</div>