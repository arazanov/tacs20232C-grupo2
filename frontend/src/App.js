import React, { Component } from 'react';
import './App.css';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import UserList from './users/UserList';
import UserEdit from './users/UserEdit';
import OrderList from "./orders/OrderList";
import OrderEdit from "./orders/OrderEdit";
import Login from "./users/Login";

class App extends Component {
    render() {
        return (
            <Router>
                <Switch>
                    <Route path='/' exact={true} component={Login}/>
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