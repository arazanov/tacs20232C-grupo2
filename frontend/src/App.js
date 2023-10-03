import React, { Component } from 'react';
import './App.css';
import Home from './Home';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import UserList from './UserList';
import UserEdit from './UserEdit';
import OrderList from "./OrderList";
import OrderEdit from "./OrderEdit";

class App extends Component {
    render() {
        return (
            <Router>
                <Switch>
                    <Route path='/' exact={true} component={Home}/>
                    <Route path='/users' exact={true} component={UserList}/>
                    <Route path='/users/:id' component={UserEdit}/>
                    <Route path='/orders' exact={true} component={OrderList}/>
                    <Route path='/orders/:id' component={OrderEdit}/>
                </Switch>
            </Router>
        )
    }
}

export default App;