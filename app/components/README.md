# Components Directory

This directory is for reusable UI components.

## Example Structure

```
components/
├── Button.tsx
├── Input.tsx
├── Card.tsx
└── index.ts
```

## Usage

Create reusable components here that can be imported in screens:

```typescript
// components/Button.tsx
import React from 'react';
import { TouchableOpacity, Text, StyleSheet } from 'react-native';

interface ButtonProps {
  title: string;
  onPress: () => void;
}

export const Button: React.FC<ButtonProps> = ({ title, onPress }) => {
  return (
    <TouchableOpacity style={styles.button} onPress={onPress}>
      <Text style={styles.text}>{title}</Text>
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  button: {
    backgroundColor: '#4F46E5',
    padding: 16,
    borderRadius: 8,
  },
  text: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
});
```

Then import in screens:

```typescript
import { Button } from '../components/Button';
```
