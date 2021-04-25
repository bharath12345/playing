import React from 'react';
import { render, screen } from '@testing-library/react';
 
import LoadUser from './LoadUser';
 
describe('App', () => {
  test('renders App component', async () => {
    render(<LoadUser />);
 
    expect(screen.queryByText(/Signed in as/i)).toBeNull();
 
    screen.debug();
 
    expect(await screen.findByText(/Signed in as/i)).toBeInTheDocument();
 
    screen.debug();
  });
});