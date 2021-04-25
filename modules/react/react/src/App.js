import logo from './logo.svg';
import './App.css';
import CustomerList from "./components/CustomerList";
import Filter from "./components/Filter";

function App() {
  return (
    <div className="App">
      <h1>Customer Application</h1>
        <CustomerList/>
    </div>
  );
}

export default App;
