import React, { createContext, useContext, useState, ReactNode } from 'react';

interface ClipboardItem {
  code: string;
  type: 'text' | 'file';
  content?: string;
  fileName?: string;
  fileType?: string;
  fileSize?: number;
  createdAt: string;
  expiresAt: string;
}

interface ClipboardContextType {
  currentItem: ClipboardItem | null;
  setCurrentItem: (item: ClipboardItem | null) => void;
  recentCodes: string[];
  addRecentCode: (code: string) => void;
  clearRecentCodes: () => void;
}

const ClipboardContext = createContext<ClipboardContextType | undefined>(undefined);

export const ClipboardProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [currentItem, setCurrentItem] = useState<ClipboardItem | null>(null);
  const [recentCodes, setRecentCodes] = useState<string[]>([]);

  const addRecentCode = (code: string) => {
    setRecentCodes((prev) => {
      const filtered = prev.filter((c) => c !== code);
      return [code, ...filtered].slice(0, 10); // Keep last 10 codes
    });
  };

  const clearRecentCodes = () => {
    setRecentCodes([]);
  };

  return (
    <ClipboardContext.Provider
      value={{
        currentItem,
        setCurrentItem,
        recentCodes,
        addRecentCode,
        clearRecentCodes,
      }}
    >
      {children}
    </ClipboardContext.Provider>
  );
};

export const useClipboard = () => {
  const context = useContext(ClipboardContext);
  if (context === undefined) {
    throw new Error('useClipboard must be used within a ClipboardProvider');
  }
  return context;
};
