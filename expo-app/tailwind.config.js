module.exports = {
  content: [
    "./App.js",
    "./screens/**/*.{js,jsx,ts,tsx}",
    "./components/**/*.{js,jsx,ts,tsx}"
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: '#6366F1',
          dark: '#4F46E5',
          light: '#818CF8'
        },
        accent: {
          DEFAULT: '#A855F7',
          light: '#C084FC'
        }
      }
    },
  },
  plugins: [],
}
