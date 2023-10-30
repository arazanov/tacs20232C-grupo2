import { useState } from 'react';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from '../AppNavbar';
import {useNavigate} from "react-router-dom";

export default function Login() {
    const [user, setUser] = useState({ username: '', password: '' });
    const navigate = useNavigate();

    function handleSubmit(e) {
        e.preventDefault();
        fetch('/', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user),
        }).then(response =>
            response.ok ? response.json() : console.log(response.status)
        ).then(data => localStorage.setItem('currentUser', JSON.stringify(data)));
        navigate("/orders");
    }

    function handleChange(e) {
        let updatedUser = user;
        updatedUser[e.target.name] = e.target.value;
        setUser(updatedUser);
    }

    return (
        <div>
        <AppNavbar/>
        <Container>
            <h2 style={{paddingTop: 50, paddingBottom: 50}}>Login</h2>
            <Form onSubmit={handleSubmit}>
                <FormGroup>
                    <Label for="username">Usuario</Label>
                    <Input type="text" name="username" id="username" defaultValue={user.username}
                           onChange={handleChange} autoComplete="username"/>
                    <Label for="password" style={{ paddingTop: 30 }}>Contraseña</Label>
                    <Input type="password" name="password" id="password" defaultValue={user.password}
                           onChange={handleChange} autoComplete="password"/>
                </FormGroup>
                <FormGroup style={{paddingTop: 50}}>
                    <Button color="primary" type="submit">Iniciar sesión</Button>
                </FormGroup>
            </Form>
        </Container>
        </div>
    );

}