import React, {Component} from 'react';
import {
  BrowserRouter as Router,
  Route,
  Link
} from 'react-router-dom';
import './App.css';
import {TimeTable} from "./TimeTable";
import {ConstraintDesigner} from "./ConstraintDesigner";

class App extends Component {
  render() {
    return (
      <div className="App">
        <Router>
          <div >
            <Link to="/timetable" ><button> View Timetable</button></Link>
            <Link to="/constraint-designer" ><button> Design Constraints </button></Link>

            <br/>
            <br/>

            <Route exact path="/timetable" component={TimeTable}/>
            <Route path="/constraint-designer" component={ConstraintDesigner}/>
          </div>
        </Router>
      </div>
    );
  }
}

export default App;
