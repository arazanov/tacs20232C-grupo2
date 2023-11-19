import {UserForm} from "./UserForm";
import {useState} from "react";
import {useAuth} from "../AuthContext";
import {useNavigate} from "react-router-dom";

export default function SignUp() {
    const {setToken} = useAuth();
    const [user, setUser] = useState({
        username: '',
        email: '',
        password: ''
    });
    const navigate = useNavigate();

    const handleSubmit = () => fetch('/users', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(user)
    })
        .then(response => {
            if (response.status === 409) {
                throw new Error("Invalid username");
            }
            return response.json();
        })
        .then(data => {
            setToken(data.token);
            navigate('/orders');
        });

    return <UserForm title={'Registrarse'} user={user} setUser={setUser} handleSubmit={handleSubmit}/>;

}