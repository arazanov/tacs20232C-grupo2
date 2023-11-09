import { useState } from 'react';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from '../navbar/AppNavbar';
import { Link, useNavigate } from "react-router-dom";

export default function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [badCredentials, setBadCredentials] = useState(false);
    const navigate = useNavigate();

    localStorage.removeItem('token');

    function handleSubmit(e) {
        e.preventDefault();
        fetch('/', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: username,
                password: password
            }),
        })
            .then(response => {
                if (response.status === 401) {
                    throw new Error(response.statusText);
                }
                return response.json();
            })
            .then(data => {
                setBadCredentials(false);
                localStorage.setItem('token', data.token);
                navigate("/orders");
            })
            .catch((e) => {
                console.log(e);
                setBadCredentials(true);
            });
    }

    function BadCredentialsMessage({ badCredentials }) {
        if (badCredentials)
            return <p style={{color: "red"}}>
                Nombre de usuario o contraseña incorrecto </p>;
    }

    return <div>
        <AppNavbar/>
        <Container>
            <h2 style={{paddingTop: 50, paddingBottom: 50}}>Iniciar sesión</h2>
            <Form onSubmit={handleSubmit}>
                <FormGroup>
                    <Label for="username">Nombre de usuario</Label>
                    <Input type="text" id="username" defaultValue={username} autoComplete={"on"}
                           onChange={e => setUsername(e.target.value)}/>
                </FormGroup>
                <FormGroup>
                    <Label for="password">Constraseña</Label>
                    <Input type="password" id="password" defaultValue={password}
                           onChange={e => setPassword(e.target.value)}/>
                </FormGroup>
                <BadCredentialsMessage badCredentials={badCredentials}/>
                <FormGroup style={{paddingTop: 50}}>
                    <Button color="primary" type="submit">Enviar</Button>{' '}
                </FormGroup>
            </Form>
            <Link to={"/signup"}>
                Registrarse
            </Link>
        </Container>
    </div>;

}