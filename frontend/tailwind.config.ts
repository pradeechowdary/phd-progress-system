import type { Config } from "tailwindcss";

export default {
  content: ["./index.html", "./src/**/*.{ts,tsx}"],
  theme: {
    extend: {
      colors: {
        canvas: "#f6f3ee",
        ink: "#15212c",
        slate: "#516173",
        accent: "#a8542a",
        moss: "#315d4d",
        gold: "#bf8f31",
        panel: "#fffdf9",
        border: "#ddd3c7",
      },
      fontFamily: {
        sans: ["Manrope", "IBM Plex Sans", "Segoe UI", "sans-serif"],
      },
      boxShadow: {
        panel: "0 14px 42px rgba(21, 33, 44, 0.08)",
      },
      backgroundImage: {
        "hero-grid":
          "radial-gradient(circle at top left, rgba(168,84,42,0.08), transparent 32%), linear-gradient(135deg, rgba(49,93,77,0.08), transparent 45%)",
      },
    },
  },
  plugins: [],
} satisfies Config;
