import { render, screen, fireEvent } from '@testing-library/react';
import App from './App';
test('renders customer application', () => {
  render(<App />);
  // screen.debug(); // DOM in console
  let elem = screen.queryByText(/customer application/i);
  expect(elem).toBeInTheDocument();
});
it("renders customer list", () => {
  // render(<App />);
  // let btns = screen.queryAllByRole("button");
  // expect(btns.length).toBe(6);
  let {container} = render(<App/>); // use container for JS DOM way to access elements
  let btns = container.querySelectorAll('.row');
  expect(btns.length).toBe(6);
});
it("delete a customer", () => {
  render(<App />);
  let btns = screen.queryAllByRole("button");
  fireEvent.click(btns[3]);
  //  fireEvent.click(btns[0]);
  btns = screen.queryAllByRole("button");
  //  screen.debug();
  expect(btns.length).toBe(5);
});
/*it("filter customers", () => {
  render(<App />);
  let txtBox = screen.queryByPlaceholderText('search by name');
  fireEvent.change(txtBox, {"target": {"value":"Geller"}});
  let btns = screen.queryAllByRole("button");
  expect(btns.length).toBe(2);
});*/
