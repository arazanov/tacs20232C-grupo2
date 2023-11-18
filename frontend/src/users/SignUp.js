import {UserForm} from "./UserForm";
import {useState} from "react";
import {useAuth} from "../AuthContext";

export default function SignUp() {
    const { setToken } = useAuth();
    const [user, setUser] = useState({
        username: '',
        email: '',
        password: ''
    });

    const request = () => {
        return {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user)
        };
    };

    return <UserForm request={request} nav={'/orders'} title={'Registrarse'}
                     user={user} setUser={setUser} setToken={setToken}/>;

}