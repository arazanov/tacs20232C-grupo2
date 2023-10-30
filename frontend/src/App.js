import "./App.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import UserList from "./users/UserList";
import UserEdit from "./users/UserEdit";
import OrderList from "./orders/OrderList";
import OrderEdit from "./orders/OrderEdit";
import Login from "./users/Login";

export default function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={ <Login/> }/>
                <Route path="/users" element={ <UserEdit/> }/>
                <Route path="/users" element={ <UserList/> }/>
                <Route path="/users/:id" element={ <UserEdit/> }/>
                <Route path="/orders" element={ <OrderList/> }/>
                <Route path="/orders/:id" element={ <OrderEdit/> }/>
            </Routes>
        </BrowserRouter>
    );
}