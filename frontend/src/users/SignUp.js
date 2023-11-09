import {useState} from "react";
import {useNavigate} from "react-router-dom";
import {UserForm} from "./UserForm";

export default function SignUp() {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [invalid, setInvalid] = useState(false);
    const navigate = useNavigate();

    function handleSubmit(e) {
        e.preventDefault();

        if (password === '') {
            setInvalid(true);
            return;
        }

        fetch('/users', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: username,
                email: email,
                password: password
            }),
        })
            .then(response => {
                if (!response.ok) throw new Error(response.statusText);
                return response.json();
            })
            .then(data => {
                localStorage.setItem('token', data.token);
                navigate("/orders");
            })
            .catch(console.log);
    }

    return <UserForm
        handleSubmit={handleSubmit}
        user={{
            username: username,
            email: email,
            password: password
        }}
        setUsername={setUsername}
        setEmail={setEmail}
        setPassword={setPassword}
        title={"Crear usuario"}
        invalid={invalid}
    />

}