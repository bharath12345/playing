import logo from './logo.svg';
import './App.css';
import CustomerList from "./components/CustomerList";
import Filter from "./components/Filter";
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';

function App() {
  return (
    <div className="App">
        <Router>
            <Switch>
                <Route path="/list" component={CustomerList} />
                <Route path="/todo" component={CustomerList} />
                <Route exact path="/" component={CustomerList} />
                <Route default component={CustomerList} />
            </Switch>
        </Router>
    </div>
  );
}

export default App;
