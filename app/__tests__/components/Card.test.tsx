import React from 'react';
import { render } from '@testing-library/react-native';
import { Text } from 'react-native';
import { Card } from '../../components/Card';

describe('Card Component', () => {
  it('renders children correctly', () => {
    const { getByText } = render(
      <Card>
        <Text>Card Content</Text>
      </Card>
    );
    expect(getByText('Card Content')).toBeTruthy();
  });

  it('applies elevated style by default', () => {
    const { getByText } = render(
      <Card>
        <Text>Elevated Card</Text>
      </Card>
    );
    expect(getByText('Elevated Card')).toBeTruthy();
  });

  it('renders without elevation when elevated is false', () => {
    const { getByText } = render(
      <Card elevated={false}>
        <Text>Flat Card</Text>
      </Card>
    );
    expect(getByText('Flat Card')).toBeTruthy();
  });
});
