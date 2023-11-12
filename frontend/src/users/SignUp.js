import {UserForm} from "./UserForm";
import {useState} from "react";

export default function SignUp() {
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

    return <UserForm request={request} enableDelete={false} nav={'/orders'} title={'Registrarse'}
                     user={user} setUser={setUser}/>;

}