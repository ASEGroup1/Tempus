import React, {Component} from 'react';
import {
  BrowserRouter as Router,
  Route,
  Link
} from 'react-router-dom';

import {TimeTable} from './TimeTable';
import './App.css';
import {InsertDataScreen} from "./InsertDataScreen";

class App extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <Router>
        <div className="App">
          <Router>
            <div >
              <Link to="/timetable" ><button> View Timetable</button></Link>
              <Link to="/insert-data" ><button> Insert data </button></Link>

              <br/>
              <br/>

              <Route exact path="/timetable" component={TimeTable}/>
              <Route path="/insert-data" component={InsertDataScreen}/>
            </div>
          </Router>

        </div>
      </Router>
    );
  }
}
export default App;
