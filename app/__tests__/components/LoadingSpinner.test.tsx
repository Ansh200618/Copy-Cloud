import React from 'react';
import { render } from '@testing-library/react-native';
import { LoadingSpinner } from '../../components/LoadingSpinner';

describe('LoadingSpinner Component', () => {
  it('renders correctly', () => {
    const { UNSAFE_root } = render(<LoadingSpinner />);
    expect(UNSAFE_root).toBeTruthy();
  });

  it('displays text when provided', () => {
    const { getByText } = render(<LoadingSpinner text="Loading..." />);
    expect(getByText('Loading...')).toBeTruthy();
  });

  it('renders in full screen mode', () => {
    const { UNSAFE_root } = render(<LoadingSpinner fullScreen />);
    expect(UNSAFE_root).toBeTruthy();
  });
});
