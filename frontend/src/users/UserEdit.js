import React, {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {UserForm} from "./UserForm";

export default function UserEdit() {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();
    let token = localStorage.getItem('token');
    const [invalid, setInvalid] = useState(false);

    useEffect(() => {
        fetch("/user", {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        })
            .then(response => response.json())
            .then(data => {
                setUsername(data.username);
                setEmail(data.email);
            });
    }, [token]);

    function handleSubmit(e) {
        e.preventDefault();

        if (password === '') {
            setInvalid(true);
            return;
        }

        fetch('/users', {
            method: 'PUT',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify({
                username: username,
                email: email,
                password: password
            })
        }).then(response => {
            if(!response.ok) throw new Error(response.statusText);
            return response.json()
        }).then(data => {
            localStorage.setItem('token', data.token);
            navigate(-1);
        }).catch(console.log);
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
        title={"Mi perfil"}
        invalid={invalid}
    />;
}