import React, {useState} from 'react';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from '../AppNavbar';
import {useNavigate} from "react-router-dom";

export default function Login() {
    const [user, setUser] = useState({ username: '', password: '' });
    const navigate = useNavigate();

    function handleChange(event) {
        const value = event.target.value;
        const name = event.target.name;
        let login = user;
        login[name] = value;
        setUser(login);
    }

    function handleSubmit(event) {
        event.preventDefault();

        fetch('/', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user),
        }).then((response) => {
            if(response.ok) navigate('/orders');
            else throw new Error(response.status.toString());
        });
    }

    return (
        <div>
        <AppNavbar/>
        <Container>
            <h2 style={{paddingTop: 50, paddingBottom: 50}}>Login</h2>
            <Form onSubmit={handleSubmit}>
                <FormGroup>
                    <Label for="username">Username</Label>
                    <Input type="text" name="username" id="username" defaultValue={user.username || ''}
                           onChange={handleChange} autoComplete="username"/>
                    <Label for="password">Password</Label>
                    <Input type="password" name="password" id="password" defaultValue={user.password || ''}
                           onChange={handleChange} autoComplete="password"/>
                </FormGroup>
                <FormGroup style={{paddingTop: 50}}>
                    <Button color="primary" type="submit">Login</Button>{' '}
                </FormGroup>
            </Form>
        </Container>
        </div>
    );

}