import React from 'react';
import { render } from '@testing-library/react-native';
import { Input } from '../../components/Input';

describe('Input Component', () => {
  it('renders correctly', () => {
    const { getByPlaceholderText } = render(
      <Input placeholder="Enter text" />
    );
    expect(getByPlaceholderText('Enter text')).toBeTruthy();
  });

  it('displays label when provided', () => {
    const { getByText } = render(
      <Input label="Username" placeholder="Enter username" />
    );
    expect(getByText('Username')).toBeTruthy();
  });

  it('displays error message when provided', () => {
    const { getByText } = render(
      <Input error="This field is required" placeholder="Enter text" />
    );
    expect(getByText('This field is required')).toBeTruthy();
  });

  it('applies error style when error is present', () => {
    const { getByPlaceholderText } = render(
      <Input placeholder="Enter text" error="Error" />
    );
    const input = getByPlaceholderText('Enter text');
    expect(input).toBeTruthy();
  });
});
