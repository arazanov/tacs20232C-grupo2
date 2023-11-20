import "./App.css";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import UserEdit from "./users/UserEdit";
import OrderList from "./orders/OrderList";
import OrderEdit from "./orders/OrderEdit";
import Login from "./users/Login";
import SignUp from "./users/SignUp";
import ItemEdit from "./items/ItemEdit";
import UserFind from "./share-users/UserFind";
import AuthProvider from "./AuthContext";

export default function App() {
    return <BrowserRouter>
        <AuthProvider>
            <Routes>
                <Route path="/profile" element={<UserEdit/>}/>
                <Route path="/orders" element={<OrderList/>}/>
                <Route path="/orders/:id" element={<OrderEdit/>}/>
                <Route path="/orders/:id/users" element={<UserFind/>}/>
                <Route path="/orders/:orderId/items/:id" element={<ItemEdit/>}/>
                <Route path="/" element={<Login/>}/>
                <Route path="/signup" element={<SignUp/>}/>
            </Routes>
        </AuthProvider>
    </BrowserRouter>
}